package dev.anvilcraft.lib.v2.network.packet;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server -> Client
 */
public non-sealed interface IClientboundPacket extends IPacket {
    default void clientHandler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> this.clientHandler(ctx.player()));
    }

    void clientHandler(Player player);
}
