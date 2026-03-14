package dev.anvilcraft.lib.v2.network.packet;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IInsensitiveBiPacket extends IClientboundPacket, IServerboundPacket {
    default void bidirectionalHandler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> this.bidirectionalHandler(ctx.player()));
    }

    void bidirectionalHandler(Player player);

    @Override
    default void clientHandler(IPayloadContext ctx) {
        this.bidirectionalHandler(ctx);
    }

    @Override
    default void clientHandler(Player player) {
        this.bidirectionalHandler(player);
    }

    @Override
    default void serverHandler(IPayloadContext ctx) {
        this.bidirectionalHandler(ctx);
    }

    @Override
    default void serverHandler(Player player) {
        this.bidirectionalHandler(player);
    }
}
