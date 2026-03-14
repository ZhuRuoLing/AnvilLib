package dev.anvilcraft.lib.v2.network.packet;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Client -> Server
 */
public non-sealed interface IServerboundPacket extends IPacket {
    default void serverHandler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> this.serverHandler(ctx.player()));
    }

    void serverHandler(Player player);
}
