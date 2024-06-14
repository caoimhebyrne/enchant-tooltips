package dev.caoimhe.enchanttooltips.util;

import dev.caoimhe.enchanttooltips.config.EnchantTooltipsConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class TextUtil {
    public static Text appendMaximumLevel(Text text, Enchantment enchantment) {
        return text
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
