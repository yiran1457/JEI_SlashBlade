package net.yiran.jsb;

import com.mojang.logging.LogUtils;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.yiran.jsb.data.ShowEntityListener;
import net.yiran.jsb.data.SpecialBladeDescListener;
import org.slf4j.Logger;

@SuppressWarnings("removal")
@Mod(JEISlashBlade.MODID)
public class JEISlashBlade {
    public static final String MODID = "jei_slashblade";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JEISlashBlade() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(JEISlashBlade::registerClientReloadListener);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    public static void registerClientReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SpecialBladeDescListener());
        event.registerReloadListener(new ShowEntityListener());
    }
}
