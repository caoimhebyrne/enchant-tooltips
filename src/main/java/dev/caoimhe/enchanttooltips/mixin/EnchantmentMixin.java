package dev.caoimhe.enchanttooltips.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.caoimhe.enchanttooltips.config.EnchantTooltipsConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @WrapOperation(
        method = "getName",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private static MutableText enchantTooltips$dontUseRomanNumerals(String key, Operation<MutableText> original) {
        if (!EnchantTooltipsConfig.getInstance().useRomanNumerals && key.startsWith("enchantment.level.")) {
            String level = key.replace("enchantment.level.", "");
            return Text.literal(level);
        }

        return original.call(key);
    }
}
