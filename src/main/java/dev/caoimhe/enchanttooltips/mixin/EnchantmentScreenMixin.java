package dev.caoimhe.enchanttooltips.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.caoimhe.enchanttooltips.config.EnchantTooltipsConfig;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {

    @WrapOperation(
        method = "render",
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

        boolean isSingleLevelEnchantment = enchantment.getMaxLevel() == 1;
        boolean isMaximumLevel = level == enchantment.getMaxLevel();

        Text originalText = original.call(enchantmentRegistryEntry, level);

        if (isSingleLevelEnchantment || (EnchantTooltipsConfig.getInstance().hideMaxOnMaximumLevel && isMaximumLevel)) {
            return originalText;
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
