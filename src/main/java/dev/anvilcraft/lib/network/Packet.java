package dev.anvilcraft.lib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * 网络包
 */
@SuppressWarnings("unused")
public interface Packet {
    ResourceLocation getType();

    void encode(@NotNull FriendlyByteBuf buf);

    default void handler(@NotNull MinecraftServer server, ServerPlayer player) {
    }

    @OnlyIn(Dist.CLIENT)
    default void handler() {
    }

    default void send(ServerPlayer player) {
        Network.sendPacket(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    default void send() {
        Network.sendPacket(this);
    }

    default void broadcast() {
        Network.broadcastPacketAll(this);
    }

    default void broadcast(LevelChunk chunk) {
        Network.broadcastPacketTrackingChunk(chunk, this);
    }
}
