package net.yiran.jsb;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.ConfigValue<Boolean> FILTER_DROP;
    public static final ForgeConfigSpec.ConfigValue<Double> ENTITY_ROTATION_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> SA_RECIPE_WIDTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> SA_RECIPE_HEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> SE_RECIPE_WIDTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> SE_RECIPE_HEIGHT;

    static {
        FILTER_DROP = BUILDER
                .comment("仅显示可获取到拔刀的 生物掉落配方")
                .define("filter_drop", true);
        ENTITY_ROTATION_SPEED = BUILDER
                .comment("设置旋转速度，等于0的时候关闭旋转")
                .define("entity_rotation_speed", 0.0125);
        SA_RECIPE_WIDTH = BUILDER
                .define("sa_recipe_width", 128);
        SA_RECIPE_HEIGHT = BUILDER
                .define("sa_recipe_height", 100);
        SE_RECIPE_WIDTH = BUILDER
                .define("se_recipe_width", 176);
        SE_RECIPE_HEIGHT = BUILDER
                .define("se_recipe_height", 120);
        SPEC = BUILDER.build();
    }
}
