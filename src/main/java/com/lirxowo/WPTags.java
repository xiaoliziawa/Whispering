package com.lirxowo;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class WPTags {

    private WPTags() {
        throw new AssertionError("No instances");
    }

    public static final TagKey<EntityType<?>> NETHER_CREATURES =
            TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Whispering.MOD_ID, "nether_creatures"));
}
