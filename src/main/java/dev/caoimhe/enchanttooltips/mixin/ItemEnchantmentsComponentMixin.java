package dev.caoimhe.enchanttooltips.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.caoimhe.enchanttooltips.EnchantTooltips;
import dev.caoimhe.enchanttooltips.config.EnchantTooltipsConfig;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin {
    @Shadow
    @Final
    Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchantments;

    @WrapOperation(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/Enchantment;getName(Lnet/minecraft/registry/entry/RegistryEntry;I)Lnet/minecraft/text/Text;"
        )
    )
    private Text enchantTooltips$addMaximumEnchantmentLevel(
        RegistryEntry<Enchantment> enchantmentRegistryEntry,
        int level,
        Operation<Text> original
    ) {
        Enchantment enchantment = enchantmentRegistryEntry.value();

        boolean isNotBook = !EnchantTooltips.IS_TOOLTIP_BEING_ADDED_TO_BOOK;
        boolean isSingleLevelEnchantment = enchantment.getMaxLevel() == 1;
        boolean stackHasMaximumEnchantmentLevel = level == enchantment.getMaxLevel();

        Text originalText = original.call(enchantmentRegistryEntry, level);

        if (isSingleLevelEnchantment
            || (EnchantTooltipsConfig.getInstance().showMaxOnEnchantedBooksOnly && isNotBook)
            || (EnchantTooltipsConfig.getInstance().hideMaxOnMaximumLevel && stackHasMaximumEnchantmentLevel)
        ) {
            return originalText;
        }

        if (EnchantTooltipsConfig.getInstance().hideOnItemsWithMultipleEnchantments) {
            if (this.enchantments.size() > 1) {
                return originalText;
            }
        }

        return originalText
            .copy()
            .append(ScreenTexts.SPACE)
            .append("(")
            .append(Text.translatable("enchant-tooltips.enchantment.maximum_abbreviation"))
            .append(":")
            .append(ScreenTexts.SPACE)
            .append(
                EnchantTooltipsConfig.getInstance().useRomanNumerals
                    ? Text.translatable("enchantment.level." + enchantment.getMaxLevel())
                    : Text.literal(String.valueOf(enchantment.getMaxLevel()))
            )
            .append(")");
    }
}
