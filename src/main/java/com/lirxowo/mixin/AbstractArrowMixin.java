package com.lirxowo.mixin;

import com.lirxowo.item.AccessoryHelper;
import com.lirxowo.item.reg.AllCurios;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    private static final float NO_WATER_DRAG = 0.99F;

    @Inject(method = "getWaterInertia", at = @At("HEAD"), cancellable = true)
    private void whispering$noUnderwaterPenalty(CallbackInfoReturnable<Float> cir) {
        Entity owner = ((AbstractArrow) (Object) this).getOwner();
        if (owner instanceof LivingEntity shooter && AccessoryHelper.isWorn(shooter, AllCurios.ABYSSAL_SCALE)) {
            cir.setReturnValue(NO_WATER_DRAG);
        }
    }
}
