package com.lirxowo.mixin;

import com.lirxowo.item.AccessoryHelper;
import com.lirxowo.item.LavaHeartShieldItem;
import com.lirxowo.item.reg.AllCurios;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityTotemMixin {

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void whispering$lavaHeartShield(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide() || !(self instanceof ServerPlayer player)) {
            return;
        }
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }
        if (!AccessoryHelper.isWorn(player, AllCurios.LAVA_HEART_SHIELD)) {
            return;
        }
        if (player.getCooldowns().isOnCooldown(AllCurios.LAVA_HEART_SHIELD)) {
            return;
        }
        LavaHeartShieldItem.activate(player);
        cir.setReturnValue(true);
    }
}
