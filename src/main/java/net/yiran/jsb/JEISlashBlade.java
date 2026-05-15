package net.yiran.jsb;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@SuppressWarnings("removal")
@Mod(JEISlashBlade.MODID)
public class JEISlashBlade {
    public static final String MODID = "jei_slashblade";
    private static final Logger LOGGER = LogUtils.getLogger();
    public JEISlashBlade() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}
