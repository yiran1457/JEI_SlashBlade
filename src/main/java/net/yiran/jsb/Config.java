package net.yiran.jsb;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.ConfigValue<Integer> SA_RECIPE_WIDTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> SA_RECIPE_HEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> SE_RECIPE_WIDTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> SE_RECIPE_HEIGHT;

    static {
        SA_RECIPE_WIDTH = BUILDER
                .define("sa recipe width", 128);
        SA_RECIPE_HEIGHT = BUILDER
                .define("sa recipe height", 100);
        SE_RECIPE_WIDTH = BUILDER
                .define("se recipe width", 176);
        SE_RECIPE_HEIGHT = BUILDER
                .define("se recipe height", 120);
        SPEC = BUILDER.build();
    }
}
