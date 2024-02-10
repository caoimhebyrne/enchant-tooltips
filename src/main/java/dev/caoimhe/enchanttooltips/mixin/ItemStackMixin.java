package dev.caoimhe.enchanttooltips.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.caoimhe.enchanttooltips.EnchantTooltips;
import dev.caoimhe.enchanttooltips.config.EnchantTooltipsConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    private static @Nullable NbtList enchantTooltips$enchantmentsList = null;

    @ModifyVariable(
        method = "appendEnchantments",
        at = @At("HEAD"),
        argsOnly = true
    )
    private static NbtList enchantTooltips$storeEnchantmentsList(NbtList list) {
        ItemStackMixin.enchantTooltips$enchantmentsList = list;
        return list;
    }

    @WrapOperation(
        method = "method_17869", // Lambda of `ifPresent` in `appendEnchantments`.
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
        )
    )
    private static boolean enchantTooltips$addMaximumEnchantmentLevel(
        List<Object> tooltip,
        Object text,
        Operation<Boolean> original,
        @Local(argsOnly = true) Enchantment enchantment,
        @Local(argsOnly = true) NbtCompound compound
    ) {
        boolean isNotBook = !EnchantTooltips.IS_TOOLTIP_BEING_ADDED_TO_BOOK;
        boolean isSingleLevelEnchantment = enchantment.getMaxLevel() == 1;
        boolean stackHasMaximumEnchantmentLevel = EnchantmentHelper.getLevelFromNbt(compound) == enchantment.getMaxLevel();

        if (isSingleLevelEnchantment
            || (EnchantTooltipsConfig.getInstance().showMaxOnEnchantedBooksOnly && isNotBook)
            || (EnchantTooltipsConfig.getInstance().hideMaxOnMaximumLevel && stackHasMaximumEnchantmentLevel)
        ) {
            return original.call(tooltip, text);
        }

        if (EnchantTooltipsConfig.getInstance().hideOnItemsWithMultipleEnchantments) {
            @Nullable NbtList list = ItemStackMixin.enchantTooltips$enchantmentsList;
            if (list != null && list.size() > 1) {
                return original.call(tooltip, text);
            }
        }

        Text newTooltip = ((Text) text)
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

        return tooltip.add(newTooltip);
    }
}
