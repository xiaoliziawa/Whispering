package com.lirxowo.item;

import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagneticBadgeItem extends AccessoryItem {

    private static final double PULL_RADIUS = 8.0D;
    private static final double PULL_SPEED = 0.35D;
    private static final double STOP_DISTANCE = 1.0D;

    public MagneticBadgeItem(Properties properties) {
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
        Level level = player.level();
        AABB area = player.getBoundingBox().inflate(PULL_RADIUS);
        Vec3 center = player.position().add(0.0D, player.getBbHeight() * 0.5D, 0.0D);

        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, area, ItemEntity::isAlive)) {
            if (item.hasPickUpDelay()) {
                continue;
            }
            pullToward(item, center);
        }
        for (ExperienceOrb orb : level.getEntitiesOfClass(ExperienceOrb.class, area, ExperienceOrb::isAlive)) {
            pullToward(orb, center);
        }
    }

    private static void pullToward(net.minecraft.world.entity.Entity entity, Vec3 center) {
        Vec3 delta = center.subtract(entity.position());
        if (delta.length() < STOP_DISTANCE) {
            return;
        }
        entity.setDeltaMovement(delta.normalize().scale(PULL_SPEED));
        entity.hasImpulse = true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.magnetic_badge.tooltip.magnet"));
        tooltip.add(Component.translatable("item.whispering.magnetic_badge.tooltip.rune"));
        tooltip.add(Component.translatable("item.whispering.magnetic_badge.tooltip.drop"));
    }
}
