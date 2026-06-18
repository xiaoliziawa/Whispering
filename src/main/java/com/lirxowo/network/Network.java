package com.lirxowo.network;

import com.lirxowo.Whispering;
import com.lirxowo.item.ForlornTotemItem;
import com.lirxowo.item.PioneerCoreItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public final class Network {

    private Network() {
        throw new AssertionError("No instances");
    }

    public static final ResourceLocation JETPACK_BURN = new ResourceLocation(Whispering.MOD_ID, "jetpack_burn");
    public static final ResourceLocation TOTEM_ANIMATION = new ResourceLocation(Whispering.MOD_ID, "totem_animation");
    public static final ResourceLocation FORLORN_ABILITY = new ResourceLocation(Whispering.MOD_ID, "forlorn_ability");

    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(JETPACK_BURN, (server, player, handler, buf, responseSender) ->
                server.execute(() -> {
                    if (PioneerCoreItem.isWorn(player)) {
                        PioneerCoreItem.consumeFuel(player);
                    }
                }));

        ServerPlayNetworking.registerGlobalReceiver(FORLORN_ABILITY, (server, player, handler, buf, responseSender) ->
                server.execute(() -> ForlornTotemItem.activateAbility(player)));
    }

    public static void sendTotemAnimation(ServerPlayer player) {
        Set<ServerPlayer> viewers = new HashSet<>(PlayerLookup.tracking(player));
        viewers.add(player);
        for (ServerPlayer viewer : viewers) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(player.getId());
            ServerPlayNetworking.send(viewer, TOTEM_ANIMATION, buf);
        }
    }
}
