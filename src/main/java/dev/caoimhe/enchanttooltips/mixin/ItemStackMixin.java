package dev.caoimhe.enchanttooltips.mixin;

import dev.caoimhe.enchanttooltips.EnchantTooltips;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(method = "getTooltip", at = @At("HEAD"))
    private void enchantTooltips$setIsEnchantedBook(
        Item.TooltipContext context,
        @Nullable PlayerEntity player,
        TooltipType type,
        CallbackInfoReturnable<List<Text>> cir
    ) {
        // There's no need to reset this at the end of `getTooltips`, the next call will set it to
        // false before the code that depends on it gets to run anyway.
        EnchantTooltips.IS_TOOLTIP_BEING_ADDED_TO_BOOK = this.getItem() instanceof EnchantedBookItem;
    }
}
