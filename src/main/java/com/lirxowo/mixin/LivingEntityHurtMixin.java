package com.lirxowo.mixin;

import com.lirxowo.WPTags;
import com.lirxowo.compat.WPCompat;
import com.lirxowo.item.AbyssalScaleItem;
import com.lirxowo.item.AccessoryHelper;
import com.lirxowo.item.reg.AllCurios;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityHurtMixin {

    private static final float PHARAOH_UNDEAD_MULTIPLIER = 1.10F;
    private static final float MOLTEN_NETHER_MULTIPLIER = 0.90F;

    private static final float THUNDER_PARALYSIS_CHANCE = 0.05F;
    private static final int THUNDER_PARALYSIS_DURATION_TICKS = 60;

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void whispering$cancelImmuneDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (source.is(WPCompat.DAMAGE_REFLECTION) && AccessoryHelper.isWorn(self, AllCurios.THUNDER_CORE)) {
            cir.setReturnValue(false);
            return;
        }
        if (source.is(DamageTypeTags.IS_FIRE) && AccessoryHelper.isWorn(self, AllCurios.MOLTEN_EMBLEM)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void whispering$spellParalysis(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            return;
        }
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide() || !WPCompat.isSpellDamage(source)) {
            return;
        }
        if (source.getEntity() instanceof LivingEntity attacker
                && AccessoryHelper.isWorn(attacker, AllCurios.THUNDER_CORE)
                && self.getRandom().nextFloat() < THUNDER_PARALYSIS_CHANCE) {
            WPCompat.applyEffect(self, WPCompat.EFFECT_PARALYSIS, THUNDER_PARALYSIS_DURATION_TICKS, 0);
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float whispering$scaleDamage(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;
        float result = amount;

        if (self.getMobType() == MobType.UNDEAD
                && source.getEntity() instanceof LivingEntity attacker
                && AccessoryHelper.isWorn(attacker, AllCurios.PHARAOH_NECKLACE)) {
            result *= PHARAOH_UNDEAD_MULTIPLIER;
        }

        if (AccessoryHelper.isWorn(self, AllCurios.MOLTEN_EMBLEM)
                && source.getEntity() instanceof LivingEntity attacker
                && attacker.getType().builtInRegistryHolder().is(WPTags.NETHER_CREATURES)) {
            result *= MOLTEN_NETHER_MULTIPLIER;
        }

        if (self.getMobType() == MobType.WATER
                && source.getEntity() instanceof LivingEntity attacker
                && AccessoryHelper.isWorn(attacker, AllCurios.ABYSSAL_SCALE)) {
            result *= AbyssalScaleItem.AQUATIC_DAMAGE_MULTIPLIER;
        }

        return result;
    }
}
