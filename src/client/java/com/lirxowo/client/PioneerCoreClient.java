package com.lirxowo.client;

import com.lirxowo.item.PioneerCoreItem;
import com.lirxowo.network.Network;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class PioneerCoreClient {

    private static final double VERTICAL_ACCELERATION = 0.16D;
    private static final double MAX_VERTICAL_SPEED = 0.7D;
    private static final double FORWARD_ACCELERATION = 0.18D;
    private static final double MAX_HORIZONTAL_SPEED = 0.85D;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(PioneerCoreClient::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null || !client.options.keyJump.isDown()) {
            return;
        }
        if (player.getAbilities().flying || player.isFallFlying() || player.isPassenger()) {
            return;
        }
        if (!PioneerCoreItem.isWorn(player) || !PioneerCoreItem.hasFuel(player)) {
            return;
        }

        Vec3 velocity = player.getDeltaMovement();
        double verticalSpeed = Math.min(velocity.y + VERTICAL_ACCELERATION, MAX_VERTICAL_SPEED);
        double horizontalX = velocity.x;
        double horizontalZ = velocity.z;

        if (client.options.keyUp.isDown()) {
            double radians = Math.toRadians(player.getYRot());
            horizontalX += -Math.sin(radians) * FORWARD_ACCELERATION;
            horizontalZ += Math.cos(radians) * FORWARD_ACCELERATION;
            double horizontalSpeed = Math.sqrt(horizontalX * horizontalX + horizontalZ * horizontalZ);
            if (horizontalSpeed > MAX_HORIZONTAL_SPEED) {
                double scale = MAX_HORIZONTAL_SPEED / horizontalSpeed;
                horizontalX *= scale;
                horizontalZ *= scale;
            }
        }

        player.setDeltaMovement(horizontalX, verticalSpeed, horizontalZ);
        player.fallDistance = 0.0F;

        if (player.tickCount % PioneerCoreItem.FUEL_DRAIN_INTERVAL_TICKS == 0) {
            ClientPlayNetworking.send(Network.JETPACK_BURN, PacketByteBufs.empty());
        }
    }
}
