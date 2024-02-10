package dev.caoimhe.enchanttooltips;

import dev.caoimhe.enchanttooltips.config.EnchantTooltipsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class EnchantTooltips implements ModInitializer {
    public static boolean IS_TOOLTIP_BEING_ADDED_TO_BOOK = false;

    @Override
    public void onInitialize() {
        AutoConfig.register(EnchantTooltipsConfig.class, GsonConfigSerializer::new);
    }
}
