package com.lirxowo.client;

import com.lirxowo.item.WaterRuneItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class WaterRuneClient {

    private static final int MAX_AIR_JUMPS = 2;
    private static final double JUMP_VELOCITY = 0.42D;

    private static int airJumpsUsed = 0;
    private static boolean jumpWasDown = false;

    private WaterRuneClient() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(WaterRuneClient::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) {
            jumpWasDown = false;
            return;
        }
        boolean onGround = player.onGround();
        if (onGround) {
            airJumpsUsed = 0;
        }

        boolean jumpDown = client.options.keyJump.isDown();
        boolean pressed = jumpDown && !jumpWasDown;
        jumpWasDown = jumpDown;

        if (!pressed || onGround || airJumpsUsed >= MAX_AIR_JUMPS) {
            return;
        }
        if (player.getAbilities().flying || player.isFallFlying() || player.isPassenger()
                || player.isInWater() || player.onClimbable()) {
            return;
        }
        if (!WaterRuneItem.isWorn(player)) {
            return;
        }

        Vec3 velocity = player.getDeltaMovement();
        player.setDeltaMovement(velocity.x, JUMP_VELOCITY, velocity.z);
        player.hasImpulse = true;
        player.fallDistance = 0.0F;
        airJumpsUsed++;
    }
}
