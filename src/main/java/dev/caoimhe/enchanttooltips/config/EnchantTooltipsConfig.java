package dev.caoimhe.enchanttooltips.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.jetbrains.annotations.NotNull;

@Config(name = "enchant-tooltips")
public class EnchantTooltipsConfig implements ConfigData {
    /**
     * Whether to replace roman numerals with their actual numeric values
     */
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useRomanNumerals = true;

    /**
     * If the maximum level should only be shown on enchanted books
     */
    @ConfigEntry.Gui.PrefixText()
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean showMaxOnEnchantedBooksOnly = true;

    /**
     * If the maximum level should be hidden when the item has an enchantment of the maximum level
     */
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideMaxOnMaximumLevel = true;

    /**
     * If the maximum level should be hidden when the item has multiple enchantments
     */
    public boolean hideOnItemsWithMultipleEnchantments = false;

    public static @NotNull EnchantTooltipsConfig getInstance() {
        return AutoConfig.getConfigHolder(EnchantTooltipsConfig.class).get();
    }
}
