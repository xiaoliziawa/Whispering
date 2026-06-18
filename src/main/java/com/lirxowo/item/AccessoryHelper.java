package com.lirxowo.item;

import com.lirxowo.compat.WPCompat;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public final class AccessoryHelper {

    public static final String BADGE_SLOT = "whispering_badge";
    public static final String NECKLACE_SLOT = "necklace";
    public static final String BACK_SLOT = "back";

    public static boolean isWorn(LivingEntity entity, Item item) {
        AccessoriesCapability capability = AccessoriesCapability.get(entity);
        return capability != null && capability.isEquipped(item);
    }

    public static void applyAllDamageBonus(AccessoryAttributeBuilder builder, ResourceLocation modifierId, double bonus) {
        for (ResourceLocation attributeId : WPCompat.ALL_DAMAGE_ATTRIBUTES) {
            Attribute attribute = WPCompat.attribute(attributeId);
            if (attribute != null) {
                builder.addStackable(attribute, modifierId, bonus, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
        }
    }
}
