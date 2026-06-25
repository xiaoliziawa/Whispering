package com.lirxowo.item;

import com.lirxowo.effect.ModEffects;
import com.lirxowo.item.reg.AllCurios;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbyssalHeartItem extends AccessoryItem {

    public static final float DAMAGE_IMMUNE_CHANCE = 0.08F;
    public static final float PROJECTILE_DAMAGE_MULTIPLIER = 0.80F;

    private static final int MAX_AMPLIFIER = 9;
    private static final int STACK_DURATION_TICKS = 200;

    public AbyssalHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference reference) {
        if (reference.entity() instanceof Player player && !player.level().isClientSide()) {
            player.removeEffect(ModEffects.KILLING_FRENZY);
        }
    }

    public static void onKill(Player player) {
        if (!AccessoryHelper.isWorn(player, AllCurios.ABYSSAL_HEART)) {
            return;
        }
        ModEffects.KILLING_FRENZY.ensureAttributeModifiers();
        MobEffectInstance current = player.getEffect(ModEffects.KILLING_FRENZY);
        int amplifier = current == null ? 0 : Math.min(current.getAmplifier() + 1, MAX_AMPLIFIER);
        player.addEffect(new MobEffectInstance(ModEffects.KILLING_FRENZY, STACK_DURATION_TICKS, amplifier, false, true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.abyssal_heart.tooltip.immune"));
        tooltip.add(Component.translatable("item.whispering.abyssal_heart.tooltip.projectile"));
        tooltip.add(Component.translatable("item.whispering.abyssal_heart.tooltip.frenzy"));
    }
}
