package net.yiran.jsb;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.yiran.jsb.data.ShowEntityListener;
import net.yiran.jsb.data.SpecialBladeDescListener;
import org.slf4j.Logger;

@SuppressWarnings("removal")
@Mod(JEISlashBlade.MODID)
public class JEISlashBlade {
    public static final String MODID = "jei_slashblade";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JEISlashBlade(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(JEISlashBlade::registerClientReloadListener);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    public static void registerClientReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SpecialBladeDescListener());
        event.registerReloadListener(new ShowEntityListener());
    }
}
