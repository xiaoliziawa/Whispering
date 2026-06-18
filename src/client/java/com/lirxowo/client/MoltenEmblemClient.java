package com.lirxowo.client;

import com.lirxowo.item.AccessoryHelper;
import com.lirxowo.item.reg.AllCurios;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class MoltenEmblemClient {

    private static final int SCAN_DEPTH = 1;
    private static final double MAX_RISE_SPEED = 0.3D;

    private static final int FLAME_RING_COUNT = 10;
    private static final double FLAME_RING_RADIUS = 0.6D;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(MoltenEmblemClient::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null || player.isShiftKeyDown()) {
            return;
        }
        if (player.getAbilities().flying || player.isFallFlying()) {
            return;
        }
        if (!AccessoryHelper.isWorn(player, AllCurios.MOLTEN_EMBLEM)) {
            return;
        }

        Level level = player.level();
        BlockPos feet = player.blockPosition();
        int lavaTopY = Integer.MIN_VALUE;
        for (int i = 0; i <= SCAN_DEPTH; i++) {
            BlockPos pos = feet.below(i);
            if (level.getBlockState(pos).getFluidState().is(FluidTags.LAVA)) {
                lavaTopY = pos.getY() + 1;
                break;
            }
        }
        if (lavaTopY == Integer.MIN_VALUE) {
            return;
        }

        spawnFlameRing(player, level);

        double diff = lavaTopY - player.getY();
        if (diff <= 0) {
            return;
        }

        double rise = Math.min(diff, MAX_RISE_SPEED);
        Vec3 velocity = player.getDeltaMovement();
        player.setDeltaMovement(velocity.x, Math.max(velocity.y, rise), velocity.z);
        player.fallDistance = 0.0F;
        player.clearFire();
    }

    private static void spawnFlameRing(LocalPlayer player, Level level) {
        double angleOffset = (player.tickCount % 360) * 0.1D;
        for (int i = 0; i < FLAME_RING_COUNT; i++) {
            double angle = (Math.PI * 2 / FLAME_RING_COUNT) * i + angleOffset;
            double x = player.getX() + Math.cos(angle) * FLAME_RING_RADIUS;
            double z = player.getZ() + Math.sin(angle) * FLAME_RING_RADIUS;
            level.addParticle(ParticleTypes.FLAME, x, player.getY() + 0.05D, z, 0.0D, 0.01D, 0.0D);
        }
    }
}
