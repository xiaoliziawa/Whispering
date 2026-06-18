package com.lirxowo.item;

import com.lirxowo.compat.WPCompat;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbyssalScaleItem extends AccessoryItem {

    public static final float AQUATIC_DAMAGE_MULTIPLIER = 1.05F;

    private static final int INTERVAL_TICKS = 20;
    private static final int EFFECT_DURATION_TICKS = 200;
    private static final int INFINITE_DURATION = -1;

    public AbyssalScaleItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        if (player.tickCount % INTERVAL_TICKS != 0) {
            return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, EFFECT_DURATION_TICKS, 0, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, EFFECT_DURATION_TICKS, 0, false, false, true));

        MobEffect deepsight = WPCompat.effect(WPCompat.EFFECT_DEEPSIGHT);
        if (deepsight != null && !player.hasEffect(deepsight)) {
            player.addEffect(new MobEffectInstance(deepsight, INFINITE_DURATION, 0, false, false, true));
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference reference) {
        if (reference.entity() instanceof Player player && !player.level().isClientSide()) {
            WPCompat.removeEffect(player, WPCompat.EFFECT_DEEPSIGHT);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.abyssal_scale.tooltip.aquatic"));
        tooltip.add(Component.translatable("item.whispering.abyssal_scale.tooltip.combat"));
        tooltip.add(Component.translatable("item.whispering.abyssal_scale.tooltip.archery"));
        tooltip.add(Component.translatable("item.whispering.abyssal_scale.tooltip.drop"));
    }
}
