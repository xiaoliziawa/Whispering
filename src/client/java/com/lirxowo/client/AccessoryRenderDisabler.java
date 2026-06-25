package com.lirxowo.client;

import com.lirxowo.Whispering;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public final class AccessoryRenderDisabler {

    private AccessoryRenderDisabler() {
    }

    public static void register() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof AccessoryItem
                    && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(Whispering.MOD_ID)) {
                AccessoriesRendererRegistry.registerNoRenderer(item);
            }
        }
    }
}
