package com.lirxowo.event;

import com.lirxowo.compat.WPCompat;
import com.lirxowo.item.AbyssalHeartItem;
import com.lirxowo.item.AccessoryHelper;
import com.lirxowo.item.reg.AllCurios;
import com.lirxowo.item.PharaohNecklaceItem;
import com.lirxowo.item.SculkBladeItem;
import com.lirxowo.item.SoulGraspRingItem;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;

public final class WPEvents {

    private WPEvents() {
        throw new AssertionError("No instances");
    }

    private static final float APPLE_DROP_CHANCE = 0.30F;
    private static final float STICK_DROP_CHANCE = 0.10F;

    private static final TagKey<Item> RUNES_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("runes", "runes"));

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
        handleRuneDrop(entity, player);
        handleSculkBladeHeal(player);
        handleSoulGraspDrop(entity, player);
        AbyssalHeartItem.onKill(player);
    }

    private static void handleSoulGraspDrop(LivingEntity entity, Player player) {
        if (entity.getMobType() != MobType.UNDEAD) {
            return;
        }
        if (AccessoryHelper.isWorn(player, AllCurios.SOUL_GRASP_RING)
                && player.getRandom().nextFloat() < SoulGraspRingItem.STICK_DROP_CHANCE) {
            entity.spawnAtLocation(Items.STICK);
        }
    }

    private static void handleSculkBladeHeal(Player player) {
        if (AccessoryHelper.isWorn(player, AllCurios.SCULK_BLADE)) {
            player.heal(player.getMaxHealth() * SculkBladeItem.KILL_HEAL_FRACTION);
        }
    }

    private static void handleRuneDrop(LivingEntity entity, Player player) {
        if (!AccessoryHelper.isWorn(player, AllCurios.MAGNETIC_BADGE)) {
            return;
        }
        Optional<Holder<Item>> rune = BuiltInRegistries.ITEM.getTag(RUNES_TAG)
                .flatMap(holders -> holders.getRandomElement(player.getRandom()));
        rune.ifPresent(holder -> entity.spawnAtLocation(holder.value()));
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
