package com.lirxowo.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过 Sinytra Connector 在 Forge 端访问其它模组内容的统一入口。
 * dev 环境（纯 Fabric Loom）不会加载这些 Forge 模组，所有访问均为空安全。
 */
public final class WPCompat {

    private WPCompat() {
        throw new AssertionError("No instances");
    }

    public static final String ALEX_CAVES = "alexscaves";
    public static final String ALEX_CAVES_DIMENSIONS = "alex_caves_dimensions";
    public static final String CHAMPIONS = "champions";
    public static final String CATACLYSM = "cataclysm";

    public static final ResourceLocation DIM_FORLORN_HOLLOWS = dimensions("forlorn_hollows");
    public static final ResourceLocation DIM_CANDY_CAVITY = dimensions("candy_cavity");
    public static final ResourceLocation DIM_ABYSSAL_CHASM = dimensions("abyssal_chasm");
    public static final ResourceLocation DIM_PRIMORDIAL_CAVES = dimensions("primordial_caves");
    public static final ResourceLocation DIM_MAGNETIC_CAVES = dimensions("magnetic_caves");

    public static final ResourceLocation EFFECT_IRRADIATED = alexCaves("irradiated");
    public static final ResourceLocation EFFECT_DEEPSIGHT = alexCaves("deepsight");
    public static final ResourceLocation EFFECT_DARKNESS_INCARNATE = alexCaves("darkness_incarnate");

    public static final ResourceLocation EFFECT_PARALYSIS = champions("paralysis");
    public static final ResourceLocation EFFECT_STUN = cataclysm("stun");

    public static final ResourceLocation ITEM_LAVA_POWER_CELL = cataclysm("lava_power_cell");

    public static final ResourceKey<DamageType> DAMAGE_REFLECTION =
            ResourceKey.create(Registries.DAMAGE_TYPE, champions("reflection"));

    public static final String SPELL_POWER = "spell_power";
    public static final String RANGED_WEAPON = "ranged_weapon";

    /**
     * 「全伤害类型」属性集合：spell_power 本体实际注册的六系（arcane/fire/frost/healing/lightning/soul）
     * + 远程武器伤害 + 原版近战伤害。
     * air/earth/water 为附属模组可能注册的额外学派，本体不存在时 attribute() 返回 null 自动跳过。
     */
    public static final List<ResourceLocation> ALL_DAMAGE_ATTRIBUTES = List.of(
            spellPower("arcane"),
            spellPower("fire"),
            spellPower("frost"),
            spellPower("healing"),
            spellPower("lightning"),
            spellPower("soul"),
            spellPower("air"),
            spellPower("earth"),
            spellPower("water"),
            new ResourceLocation(RANGED_WEAPON, "damage"),
            new ResourceLocation("minecraft", "generic.attack_damage"));

    private static final Map<ResourceLocation, Optional<MobEffect>> EFFECT_CACHE = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, Optional<Item>> ITEM_CACHE = new ConcurrentHashMap<>();

    public static boolean isLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isInDimension(Level level, ResourceLocation dimensionId) {
        return level.dimension().location().equals(dimensionId);
    }

    public static MobEffect effect(ResourceLocation id) {
        return EFFECT_CACHE.computeIfAbsent(id,
                key -> Optional.ofNullable(BuiltInRegistries.MOB_EFFECT.get(key))).orElse(null);
    }

    public static Item item(ResourceLocation id) {
        return ITEM_CACHE.computeIfAbsent(id,
                key -> Optional.ofNullable(BuiltInRegistries.ITEM.get(key))).orElse(null);
    }

    public static Attribute attribute(ResourceLocation id) {
        return BuiltInRegistries.ATTRIBUTE.get(id);
    }

    public static boolean hasEffect(LivingEntity entity, ResourceLocation effectId) {
        MobEffect effect = effect(effectId);
        return effect != null && entity.hasEffect(effect);
    }

    public static void applyEffect(LivingEntity entity, ResourceLocation effectId, int duration, int amplifier) {
        MobEffect effect = effect(effectId);
        if (effect != null) {
            entity.addEffect(new MobEffectInstance(effect, duration, amplifier));
        }
    }

    public static void removeEffect(LivingEntity entity, ResourceLocation effectId) {
        MobEffect effect = effect(effectId);
        if (effect != null && entity.hasEffect(effect)) {
            entity.removeEffect(effect);
        }
    }

    public static boolean isDamageType(DamageSource source, ResourceKey<DamageType> type) {
        return source.is(type);
    }

    private static ResourceLocation alexCaves(String path) {
        return new ResourceLocation(ALEX_CAVES, path);
    }

    private static ResourceLocation dimensions(String path) {
        return new ResourceLocation(ALEX_CAVES_DIMENSIONS, path);
    }

    private static ResourceLocation champions(String path) {
        return new ResourceLocation(CHAMPIONS, path);
    }

    private static ResourceLocation cataclysm(String path) {
        return new ResourceLocation(CATACLYSM, path);
    }

    private static ResourceLocation spellPower(String path) {
        return new ResourceLocation(SPELL_POWER, path);
    }
}
