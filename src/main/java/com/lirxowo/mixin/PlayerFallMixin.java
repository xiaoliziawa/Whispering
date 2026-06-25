package com.lirxowo.mixin;

import com.lirxowo.item.PioneerCoreItem;
import com.lirxowo.item.WaterRuneItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerFallMixin {

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void whispering$noJetpackFallDamage(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        Player self = (Player) (Object) this;
        if (PioneerCoreItem.isWorn(self) || WaterRuneItem.isWorn(self)) {
            cir.setReturnValue(false);
        }
    }
}
