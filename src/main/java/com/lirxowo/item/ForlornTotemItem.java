package com.lirxowo.item;

import com.lirxowo.compat.WPCompat;
import com.lirxowo.item.reg.AllCurios;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForlornTotemItem extends AccessoryItem {

    public static final int ABILITY_COOLDOWN_TICKS = 3 * 60 * 20;
    private static final int DARKNESS_DURATION_TICKS = 200;

    public ForlornTotemItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    public static void activateAbility(ServerPlayer player) {
        if (!AccessoryHelper.isWorn(player, AllCurios.FORLORN_TOTEM)) {
            return;
        }
        if (player.getCooldowns().isOnCooldown(AllCurios.FORLORN_TOTEM)) {
            return;
        }
        WPCompat.applyEffect(player, WPCompat.EFFECT_DARKNESS_INCARNATE, DARKNESS_DURATION_TICKS, 0);
        player.getCooldowns().addCooldown(AllCurios.FORLORN_TOTEM, ABILITY_COOLDOWN_TICKS);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.forlorn_totem.tooltip.drop"));
        tooltip.add(Component.translatable("item.whispering.forlorn_totem.tooltip.ability"));
        tooltip.add(Component.translatable("item.whispering.forlorn_totem.tooltip.cooldown"));
    }
}
