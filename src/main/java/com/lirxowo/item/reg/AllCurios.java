package com.lirxowo.item.reg;

import com.lirxowo.Whispering;
import com.lirxowo.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AllCurios {

    public static final Item PIONEER_CORE = register("pioneer_core",
            new PioneerCoreItem(new Item.Properties().stacksTo(1)));

    public static final Item LAVA_HEART_SHIELD = register("lava_heart_shield",
            new LavaHeartShieldItem(new Item.Properties().stacksTo(1)));

    public static final Item PHARAOH_NECKLACE = register("pharaoh_necklace",
            new PharaohNecklaceItem(new Item.Properties().stacksTo(1)));

    public static final Item THUNDER_CORE = register("thunder_core",
            new ThunderCoreItem(new Item.Properties().stacksTo(1)));

    public static final Item FURNACE_CORE = register("furnace_core",
            new FurnaceCoreItem(new Item.Properties().stacksTo(1)));

    public static final Item MOLTEN_EMBLEM = register("molten_emblem",
            new MoltenEmblemItem(new Item.Properties().stacksTo(1)));

    public static final Item LAVA_WINGS = register("lava_wings",
            new LavaWingsItem(new Item.Properties().stacksTo(1)));

    public static final Item FORLORN_TOTEM = register("forlorn_totem",
            new ForlornTotemItem(new Item.Properties().stacksTo(1)));

    public static final Item CANDY_HEART = register("candy_heart",
            new CandyHeartItem(new Item.Properties().stacksTo(1)));

    public static final Item ABYSSAL_SCALE = register("abyssal_scale",
            new AbyssalScaleItem(new Item.Properties().stacksTo(1)));

    public static final Item EMBER_CORE = register("ember_core",
            new EmberCoreItem(new Item.Properties().stacksTo(1)));

    public static final Item MAGNETIC_BADGE = register("magnetic_badge",
            new MagneticBadgeItem(new Item.Properties().stacksTo(1)));

    public static final Item EARTHSHAKER_CRYSTAL = register("earthshaker_crystal",
            new EarthshakerCrystalItem(new Item.Properties().stacksTo(1)));

    public static final Item SCULK_BLADE = register("sculk_blade",
            new SculkBladeItem(new Item.Properties().stacksTo(1)));

    public static final Item WATER_RUNE = register("water_rune",
            new WaterRuneItem(new Item.Properties().stacksTo(1)));

    public static final Item ABYSSAL_HEART = register("abyssal_heart",
            new AbyssalHeartItem(new Item.Properties().stacksTo(1)));

    public static final Item SOUL_GRASP_RING = register("soul_grasp_ring",
            new SoulGraspRingItem(new Item.Properties().stacksTo(1)));

    public static final CreativeModeTab GENERAL_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Whispering.MOD_ID, "general"),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." + Whispering.MOD_ID + ".general"))
                    .icon(() -> new ItemStack(PIONEER_CORE))
                    .displayItems((parameters, output) -> {
                        output.accept(PIONEER_CORE);
                        output.accept(LAVA_HEART_SHIELD);
                        output.accept(PHARAOH_NECKLACE);
                        output.accept(THUNDER_CORE);
                        output.accept(FURNACE_CORE);
                        output.accept(MOLTEN_EMBLEM);
                        output.accept(LAVA_WINGS);
                        output.accept(FORLORN_TOTEM);
                        output.accept(CANDY_HEART);
                        output.accept(ABYSSAL_SCALE);
                        output.accept(EMBER_CORE);
                        output.accept(MAGNETIC_BADGE);
                        output.accept(EARTHSHAKER_CRYSTAL);
                        output.accept(SCULK_BLADE);
                        output.accept(WATER_RUNE);
                        output.accept(ABYSSAL_HEART);
                        output.accept(SOUL_GRASP_RING);
                    })
                    .build());

    public static void init() {
    }

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Whispering.MOD_ID, name), item);
    }
}
