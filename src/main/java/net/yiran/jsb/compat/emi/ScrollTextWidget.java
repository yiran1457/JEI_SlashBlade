package net.yiran.jsb.compat.emi;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.*;

/**
 * 专为 EMI 配方设计的可滚动文本框组件
 * 模拟 JEI ScrollBoxWidget 功能，包含边框裁剪、滚动条轨道/滑块、鼠标滚轮平滑滚动与拖拽支持
 * 试了很久没有办法让描述兼容EMI的的翻页，于是转向JEI
 * 设计思路来自EMI和JEI的GUI源码，感谢开源
 */
public class ScrollTextWidget extends Widget {
    private static final int LINE_HEIGHT = 10;
    private static final Set<ScrollTextWidget> ACTIVE_WIDGETS = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
    private static boolean registered = false;

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final List<FormattedCharSequence> lines = new ArrayList<>();

    private float scrollOffset = 0.0f;
    private int totalHeight = 0;
    private int maxScroll = 0;
    private boolean needsScrollbar = false;
    private boolean isDragging = false;

    // 屏幕绝对坐标（供 enableScissor 与 ScreenEvent 滚轮检测使用）
    private int screenMinX;
    private int screenMinY;
    private int screenMaxX;
    private int screenMaxY;
    private long lastRenderTime = 0;

    public ScrollTextWidget(int x, int y, int width, int height, Component content) {
        this(x, y, width, height, List.of(content));
    }

    public ScrollTextWidget(int x, int y, int width, int height, List<Component> contents) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setContents(contents);
    }

    /**
     * 注册 Forge 客户端滚轮事件，拦截 EMI 翻页并转为滚动条滚动
     */
    public static synchronized void registerEvents() {
        if (!registered) {
            MinecraftForge.EVENT_BUS.register(ScrollTextWidget.class);
            registered = true;
        }
    }

    @SubscribeEvent
    public static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
        if (event.getScreen() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();

        for (ScrollTextWidget widget : ACTIVE_WIDGETS) {
            // 确保只响应最近一帧（200ms 内）正在渲染且鼠标悬停在区域内的文本框
            if (now - widget.lastRenderTime < 200) {
                if (mouseX >= widget.screenMinX && mouseX <= widget.screenMaxX &&
                        mouseY >= widget.screenMinY && mouseY <= widget.screenMaxY) {
                    if (widget.canScroll()) {
                        widget.onScroll(event.getScrollDelta());
                        // 阻止 EMI 默认的配方翻页行为
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 设置并格式化显示的文本内容
     */
    public void setContents(List<Component> contents) {
        Font font = Minecraft.getInstance().font;
        this.lines.clear();
        int fullWidth = this.width - 6;

        List<FormattedCharSequence> temp = new ArrayList<>();
        for (Component c : contents) {
            temp.addAll(font.split(c, fullWidth));
        }

        int visibleH = this.height - 4;
        int totalH = temp.size() * LINE_HEIGHT;

        if (totalH > visibleH) {
            // 超出可见高度，需要预留右侧滚动条宽度（5px + 边距）
            int scrollbarTextWidth = this.width - 12;
            this.lines.clear();
            for (Component c : contents) {
                this.lines.addAll(font.split(c, scrollbarTextWidth));
            }
            this.needsScrollbar = true;
        } else {
            this.lines.addAll(temp);
            this.needsScrollbar = false;
        }

        this.totalHeight = this.lines.size() * LINE_HEIGHT;
        this.maxScroll = Math.max(0, this.totalHeight - visibleH);
        this.scrollOffset = Math.max(0, Math.min(this.scrollOffset, this.maxScroll));
    }

    public boolean canScroll() {
        return needsScrollbar && maxScroll > 0;
    }

    public void onScroll(double delta) {
        if (canScroll()) {
            this.scrollOffset = (float) Math.max(0, Math.min(maxScroll, this.scrollOffset - delta * 12.0f));
        }
    }

    private void updateScrollFromMouse(int mouseY) {
        int trackY = this.y + 1;
        int trackHeight = this.height - 2;
        int visibleH = this.height - 4;
        int thumbHeight = Math.max(8, (int) ((float) visibleH / this.totalHeight * trackHeight));
        int maxTravel = trackHeight - thumbHeight;

        if (maxTravel > 0 && this.maxScroll > 0) {
            float ratio = (float) (mouseY - trackY - thumbHeight / 2) / maxTravel;
            this.scrollOffset = Math.max(0, Math.min(this.maxScroll, ratio * this.maxScroll));
        }
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(this.x, this.y, this.width, this.height);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && needsScrollbar) {
            int trackX = this.x + this.width - 7;
            int trackY = this.y + 1;
            int trackWidth = 7;
            int trackHeight = this.height - 2;

            if (mouseX >= trackX && mouseX <= trackX + trackWidth && mouseY >= trackY && mouseY <= trackY + trackHeight) {
                this.isDragging = true;
                updateScrollFromMouse(mouseY);
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        // 计算当前在屏幕上的绝对坐标
        Matrix4f matrix = draw.pose().last().pose();
        Vector3f p1 = new Vector3f(this.x, this.y, 1.0f);
        Vector3f p2 = new Vector3f(this.x + this.width, this.y + this.height, 1.0f);
        matrix.transformPosition(p1);
        matrix.transformPosition(p2);

        this.screenMinX = Math.round(p1.x);
        this.screenMinY = Math.round(p1.y);
        this.screenMaxX = Math.round(p2.x);
        this.screenMaxY = Math.round(p2.y);
        this.lastRenderTime = System.currentTimeMillis();
        ACTIVE_WIDGETS.add(this);

        // 鼠标按住拖拽检测
        if (this.isDragging) {
            if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) != GLFW.GLFW_PRESS) {
                this.isDragging = false;
            } else {
                updateScrollFromMouse(mouseY);
            }
        }

        // 绘制半透明背景与质感边框
        draw.fill(this.x, this.y, this.x + this.width, this.y + this.height, 0x30000000);
        draw.fill(this.x, this.y, this.x + this.width, this.y + 1, 0x50000000);
        draw.fill(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0x40FFFFFF);
        draw.fill(this.x, this.y, this.x + 1, this.y + this.height, 0x50000000);
        draw.fill(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0x40FFFFFF);

        // 启用剪裁区域进行文本绘制
        Vector3f sc1 = new Vector3f(this.x + 1, this.y + 1, 1.0f);
        Vector3f sc2 = new Vector3f(this.x + this.width - (needsScrollbar ? 7 : 1), this.y + this.height - 1, 1.0f);
        matrix.transformPosition(sc1);
        matrix.transformPosition(sc2);
        int sMinX = Math.round(sc1.x);
        int sMinY = Math.round(sc1.y);
        int sMaxX = Math.round(sc2.x);
        int sMaxY = Math.round(sc2.y);

        if (sMaxX > sMinX && sMaxY > sMinY) {
            draw.enableScissor(sMinX, sMinY, sMaxX, sMaxY);
            try {
                Font font = Minecraft.getInstance().font;
                for (int i = 0; i < lines.size(); i++) {
                    int lineY = this.y + 2 + i * LINE_HEIGHT - Math.round(this.scrollOffset);
                    if (lineY + 9 >= this.y && lineY <= this.y + this.height) {
                        draw.drawString(font, lines.get(i), this.x + 3, lineY, 0xFFE0E0E0, false);
                    }
                }
            } finally {
                draw.disableScissor();
            }
        }

        // 绘制滚动条
        if (needsScrollbar) {
            int trackX = this.x + this.width - 6;
            int trackY = this.y + 1;
            int trackWidth = 5;
            int trackHeight = this.height - 2;
            int visibleH = this.height - 4;

            // 滚动条滑轨
            draw.fill(trackX, trackY, trackX + trackWidth, trackY + trackHeight, 0x30000000);

            // 滑块计算
            int thumbHeight = Math.max(8, (int) ((float) visibleH / this.totalHeight * trackHeight));
            int maxTravel = trackHeight - thumbHeight;
            int thumbY = trackY + (this.maxScroll > 0 ? (int) ((this.scrollOffset / this.maxScroll) * maxTravel) : 0);

            boolean hoverThumb = mouseX >= trackX && mouseX <= trackX + trackWidth && mouseY >= thumbY && mouseY <= thumbY + thumbHeight;
            int thumbColor = this.isDragging ? 0xFFFFFFFF : (hoverThumb ? 0xFFCCCCCC : 0xFF888888);

            // 滑块本体与立体高光
            draw.fill(trackX, thumbY, trackX + trackWidth, thumbY + thumbHeight, thumbColor);
            draw.fill(trackX, thumbY, trackX + trackWidth, thumbY + 1, 0xFFFFFFFF);
            draw.fill(trackX, thumbY + thumbHeight - 1, trackX + trackWidth, thumbY + thumbHeight, 0xFF444444);
        }
    }
}
