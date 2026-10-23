package net.yiran.jsb.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.drop.EntityDropEntry;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import mods.flammpfeil.slashblade.registry.SpecialEffectsRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.yiran.jsb.Config;
import net.yiran.jsb.Util;
import net.yiran.jsb.data.SpecialBladeDescListener;
import net.yiran.jsb.recipe.BladeRegisterManager;

import java.util.stream.Stream;

@EmiEntrypoint
public class SlashBladeEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory SA_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse("jei_slashblade:sa"),
            EmiStack.of(SlashBladeItems.PROUDSOUL_SPHERE.get())
    );

    public static final EmiRecipeCategory SE_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse("jei_slashblade:se"),
            EmiStack.of(SlashBladeItems.PROUDSOUL_CRYSTAL.get())
    );

    public static final EmiRecipeCategory DROP_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse("jei_slashblade:drop"),
            EmiStack.of(SlashBladeItems.SLASHBLADE.get())
    );

    public static final EmiRecipeCategory DESC_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse("jei_slashblade:desc"),
            EmiStack.of(SlashBladeItems.SLASHBLADE.get())
    );

    @Override
    public void register(EmiRegistry registry) {
        // 注册滚动条滚轮交互与事件监听
        ScrollTextWidget.registerEvents();

        // 构建拔刀剑缓存
        BladeRegisterManager.build();

        // 注册拔刀剑的 EMI Subtype 比较器，让杀敌/耀魂/掉耐久的拔刀剑依然能匹配对应的拔刀配方
        registry.setDefaultComparison(SlashBladeItems.SLASHBLADE.get(), Comparison.compareData(stack -> {
            ItemStack itemStack = stack.getItemStack();
            itemStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(cap -> {
                if (itemStack.hasTag() && itemStack.getTag().contains("bladeState")) {
                    cap.deserializeNBT(itemStack.getTag().getCompound("bladeState"));
                }
            });
            return itemStack.getCapability(ItemSlashBlade.BLADESTATE)
                    .map(ISlashBladeState::getTranslationKey)
                    .orElse("");
        }));

        // 注册分类
        registry.addCategory(SA_CATEGORY);
        registry.addCategory(SE_CATEGORY);
        registry.addCategory(DROP_CATEGORY);
        registry.addCategory(DESC_CATEGORY);

        // 注册 SA 配方
        for (SlashArts slashArts : SlashArtsRegistry.REGISTRY.get().getValues()) {
            registry.addRecipe(new SlashBladeSAEmiRecipe(slashArts));
        }

        // 注册 SE 配方
        SpecialEffectsRegistry.REGISTRY.get().getValues().forEach(specialEffect -> {
            registry.addRecipe(new SlashBladeSEEmiRecipe(specialEffect));
        });

        // 注册掉落配方
        Stream<EntityDropEntry> drops = Util.getEntityDropEntryRegistry().stream();
        if (Config.FILTER_DROP.get()) {
            drops = drops.filter(entityDropEntry -> {
                if (!ForgeRegistries.ENTITY_TYPES.containsKey(entityDropEntry.entityType())) return false;
                return Util.getSlashBladeDefinitionRegistry().containsKey(entityDropEntry.bladeName());
            });
        }
        drops.forEach(drop -> registry.addRecipe(new SlashBladeDropEmiRecipe(drop)));

        // 注册拔刀特殊描述配方
        SpecialBladeDescListener.DESC_DATA.stream()
                .filter(desc -> Util.getSlashBladeDefinitionRegistry().containsKey(desc.bladeName()))
                .forEach(desc -> registry.addRecipe(new SlashBladeDescEmiRecipe(desc)));
    }
}
