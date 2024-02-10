package dev.caoimhe.enchanttooltips.mixin;

import dev.caoimhe.enchanttooltips.EnchantTooltips;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void enchantTooltips$setIsEnchantedBook(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        EnchantTooltips.IS_TOOLTIP_BEING_ADDED_TO_BOOK = true;
    }

    @Inject(method = "appendTooltip", at = @At("RETURN"))
    private void enchantTooltips$unsetIsEnchantedBook(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        EnchantTooltips.IS_TOOLTIP_BEING_ADDED_TO_BOOK = false;
    }
}
