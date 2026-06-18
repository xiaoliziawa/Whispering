package com.lirxowo.item;

import com.lirxowo.compat.WPCompat;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EarthshakerCrystalItem extends AccessoryItem {

    public EarthshakerCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        LivingEntity entity = reference.entity();
        if (entity == null || entity.level().isClientSide()) {
            return;
        }
        WPCompat.removeEffect(entity, WPCompat.EFFECT_IRRADIATED);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.earthshaker_crystal.tooltip.immune"));
        tooltip.add(Component.translatable("item.whispering.earthshaker_crystal.tooltip.drop"));
    }
}
