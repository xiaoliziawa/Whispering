package com.lirxowo.effect;

import com.lirxowo.compat.WPCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public class KillingFrenzyEffect extends MobEffect {

    private static final int COLOR = 0x1f6f7a;
    private static final double BONUS_PER_LEVEL = 0.02D;

    private static final String[] MODIFIER_UUIDS = {
            "a3f1c0e2-1d4b-4a8e-9c10-0001f0d2a001",
            "a3f1c0e2-1d4b-4a8e-9c10-0002f0d2a002",
            "a3f1c0e2-1d4b-4a8e-9c10-0003f0d2a003",
            "a3f1c0e2-1d4b-4a8e-9c10-0004f0d2a004",
            "a3f1c0e2-1d4b-4a8e-9c10-0005f0d2a005",
            "a3f1c0e2-1d4b-4a8e-9c10-0006f0d2a006"
    };

    private boolean modifiersInitialized = false;

    public KillingFrenzyEffect() {
        super(MobEffectCategory.BENEFICIAL, COLOR);
    }

    public void ensureAttributeModifiers() {
        if (modifiersInitialized) {
            return;
        }
        modifiersInitialized = true;
        List<ResourceLocation> ids = WPCompat.SPELL_SCHOOL_ATTRIBUTES;
        for (int i = 0; i < ids.size(); i++) {
            Attribute attribute = WPCompat.attribute(ids.get(i));
            if (attribute != null) {
                addAttributeModifier(attribute, MODIFIER_UUIDS[i], BONUS_PER_LEVEL, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
        }
    }
}
