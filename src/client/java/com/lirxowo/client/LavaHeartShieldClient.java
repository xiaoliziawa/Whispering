package com.lirxowo.client;

import com.lirxowo.item.reg.AllCurios;
import com.lirxowo.network.Network;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public final class LavaHeartShieldClient {

    private static final int PARTICLE_COUNT = 30;
    private static final double PARTICLE_SPEED = 0.02D;

        public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(Network.TOTEM_ANIMATION, (client, handler, buf, responseSender) -> {
            int entityId = buf.readVarInt();
            client.execute(() -> playAnimation(client, entityId));
        });
    }

    private static void playAnimation(Minecraft client, int entityId) {
        if (client.level == null) {
            return;
        }
        Entity entity = client.level.getEntity(entityId);
        if (entity == null) {
            return;
        }

        RandomSource random = RandomSource.create();
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            client.level.addParticle(ParticleTypes.TOTEM_OF_UNDYING,
                    entity.getRandomX(1.0), entity.getRandomY(), entity.getRandomZ(1.0),
                    random.nextGaussian() * PARTICLE_SPEED,
                    random.nextGaussian() * PARTICLE_SPEED,
                    random.nextGaussian() * PARTICLE_SPEED);
        }
        client.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);

        if (entity == client.player) {
            client.gameRenderer.displayItemActivation(new ItemStack(AllCurios.LAVA_HEART_SHIELD));
        }
    }
}
