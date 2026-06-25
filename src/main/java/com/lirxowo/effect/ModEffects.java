package com.lirxowo.effect;

import com.lirxowo.Whispering;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public final class ModEffects {

    public static final KillingFrenzyEffect KILLING_FRENZY =
            register("killing_frenzy", new KillingFrenzyEffect());

    private ModEffects() {
    }

    public static void init() {
    }

    private static <T extends KillingFrenzyEffect> T register(String name, T effect) {
        Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Whispering.MOD_ID, name), effect);
        return effect;
    }
}
