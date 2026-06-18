package com.lirxowo.event;

import com.lirxowo.compat.WPCompat;
import com.lirxowo.item.AccessoryHelper;
import com.lirxowo.item.reg.AllCurios;
import com.lirxowo.item.PharaohNecklaceItem;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public final class WPEvents {

    private WPEvents() {
        throw new AssertionError("No instances");
    }

    private static final float APPLE_DROP_CHANCE = 0.30F;
    private static final float STICK_DROP_CHANCE = 0.10F;

    private record DimensionDrop(Item badge, ResourceLocation dimension) {
    }

    private static final List<DimensionDrop> DIMENSION_DROPS = List.of(
            new DimensionDrop(AllCurios.FORLORN_TOTEM, WPCompat.DIM_FORLORN_HOLLOWS),
            new DimensionDrop(AllCurios.CANDY_HEART, WPCompat.DIM_CANDY_CAVITY),
            new DimensionDrop(AllCurios.ABYSSAL_SCALE, WPCompat.DIM_ABYSSAL_CHASM),
            new DimensionDrop(AllCurios.EMBER_CORE, WPCompat.DIM_PRIMORDIAL_CAVES),
            new DimensionDrop(AllCurios.MAGNETIC_BADGE, WPCompat.DIM_MAGNETIC_CAVES));

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register(WPEvents::onLivingDeath);
    }

    private static void onLivingDeath(LivingEntity entity, DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }
        handlePharaohDrop(entity, player);
        handleEnvironmentDrops(entity, player);
    }

    private static void handlePharaohDrop(LivingEntity entity, Player player) {
        if (entity.getMobType() != MobType.UNDEAD) {
            return;
        }
        if (!AccessoryHelper.isWorn(player, AllCurios.PHARAOH_NECKLACE)) {
            return;
        }
        if (player.getRandom().nextFloat() < PharaohNecklaceItem.DIAMOND_DROP_CHANCE) {
            entity.spawnAtLocation(Items.DIAMOND);
        }
    }

    private static void handleEnvironmentDrops(LivingEntity entity, Player player) {
        boolean triggered = false;
        for (DimensionDrop drop : DIMENSION_DROPS) {
            if (WPCompat.isInDimension(player.level(), drop.dimension()) && AccessoryHelper.isWorn(player, drop.badge())) {
                triggered = true;
                break;
            }
        }
        if (!triggered
                && AccessoryHelper.isWorn(player, AllCurios.EARTHSHAKER_CRYSTAL)
                && WPCompat.hasEffect(entity, WPCompat.EFFECT_IRRADIATED)) {
            triggered = true;
        }
        if (!triggered) {
            return;
        }
        rollEnvironmentDrops(entity, player);
    }

    private static void rollEnvironmentDrops(LivingEntity entity, Player player) {
        if (player.getRandom().nextFloat() < APPLE_DROP_CHANCE) {
            entity.spawnAtLocation(Items.APPLE);
        }
        if (player.getRandom().nextFloat() < STICK_DROP_CHANCE) {
            entity.spawnAtLocation(Items.STICK);
        }
    }
}
