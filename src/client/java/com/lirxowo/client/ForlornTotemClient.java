package com.lirxowo.client;

import com.lirxowo.item.AccessoryHelper;
import com.lirxowo.item.reg.AllCurios;
import com.lirxowo.network.Network;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.lwjgl.glfw.GLFW;

public final class ForlornTotemClient {

    private static KeyMapping abilityKey;

    public static void register() {
        abilityKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.whispering.forlorn_totem",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.whispering"));
        ClientTickEvents.END_CLIENT_TICK.register(ForlornTotemClient::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }
        boolean pressed = false;
        while (abilityKey.consumeClick()) {
            pressed = true;
        }
        if (!pressed) {
            return;
        }
        if (!AccessoryHelper.isWorn(player, AllCurios.FORLORN_TOTEM)) {
            return;
        }
        if (player.getCooldowns().isOnCooldown(AllCurios.FORLORN_TOTEM)) {
            return;
        }
        ClientPlayNetworking.send(Network.FORLORN_ABILITY, PacketByteBufs.empty());
    }
}
