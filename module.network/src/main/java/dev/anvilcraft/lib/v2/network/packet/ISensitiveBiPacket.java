package dev.anvilcraft.lib.v2.network.packet;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ISensitiveBiPacket extends IClientboundPacket, IServerboundPacket {
    default void bidirectionalHandler(IPayloadContext ctx) {
        if (ctx.flow().isClientbound()) {
            this.clientHandler(ctx);
        } else if (ctx.flow().isServerbound()) {
            this.serverHandler(ctx);
        }
    }
}
