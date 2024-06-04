package dev.caoimhe.enchanttooltips.mixin;

import dev.caoimhe.enchanttooltips.EnchantTooltips;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
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

    @Inject(
        method = "getTooltip",
        at = @At(value = "HEAD")
    )
    private void enchantTooltips$setIsEnchantedBook(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        EnchantTooltips.IS_TOOLTIP_BEING_ADDED_TO_BOOK = getItem() instanceof EnchantedBookItem;
    }

}
