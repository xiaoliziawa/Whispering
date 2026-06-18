package com.lirxowo.item;

import com.lirxowo.Whispering;
import com.lirxowo.compat.WPCompat;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PharaohNecklaceItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "pharaoh_necklace");
    private static final double DAMAGE_BONUS = 0.10D;

    public static final float DIAMOND_DROP_CHANCE = 0.20F;

    private static final double STUN_RADIUS = 10.0D;
    private static final int STUN_DURATION_TICKS = 100;
    private static final int STUN_INTERVAL_TICKS = 20;
    private static final float LOW_HEALTH_FRACTION = 0.30F;

    public PharaohNecklaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.NECKLACE_SLOT.equals(reference.slotName());
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (AccessoryHelper.NECKLACE_SLOT.equals(reference.slotName())) {
            AccessoryHelper.applyAllDamageBonus(builder, DAMAGE_MODIFIER_ID, DAMAGE_BONUS);
        }
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        if (player.tickCount % STUN_INTERVAL_TICKS != 0) {
            return;
        }
        if (player.getHealth() >= player.getMaxHealth() * LOW_HEALTH_FRACTION) {
            return;
        }
        stunNearbyEnemies(player);
    }

    private void stunNearbyEnemies(Player player) {
        Level level = player.level();
        AABB area = player.getBoundingBox().inflate(STUN_RADIUS);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity instanceof Enemy && entity != player && entity.isAlive())) {
            WPCompat.applyEffect(target, WPCompat.EFFECT_STUN, STUN_DURATION_TICKS, 0);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.pharaoh_necklace.tooltip.undead"));
        tooltip.add(Component.translatable("item.whispering.pharaoh_necklace.tooltip.stun"));
        tooltip.add(Component.translatable("item.whispering.pharaoh_necklace.tooltip.damage"));
    }
}
