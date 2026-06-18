package com.lirxowo.mixin;

import com.lirxowo.compat.WPCompat;
import com.lirxowo.item.reg.AllCurios;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityParalysisMixin {

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void whispering$immuneToEffects(MobEffectInstance effectInstance, CallbackInfoReturnable<Boolean> cir) {
        MobEffect effect = effectInstance.getEffect();
        AccessoriesCapability capability = AccessoriesCapability.get((LivingEntity) (Object) this);
        if (capability == null) {
            return;
        }

        MobEffect paralysis = WPCompat.effect(WPCompat.EFFECT_PARALYSIS);
        if (paralysis != null && effect == paralysis && capability.isEquipped(AllCurios.PIONEER_CORE)) {
            cir.setReturnValue(false);
            return;
        }

        MobEffect irradiated = WPCompat.effect(WPCompat.EFFECT_IRRADIATED);
        if (irradiated != null && effect == irradiated && capability.isEquipped(AllCurios.EARTHSHAKER_CRYSTAL)) {
            cir.setReturnValue(false);
        }
    }
}
