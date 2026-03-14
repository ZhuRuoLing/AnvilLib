package dev.anvilcraft.lib.v2.network.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

public sealed interface IPacket extends CustomPacketPayload permits IClientboundPacket, IServerboundPacket {
    static <T extends IPacket> Type<T> type(ResourceLocation id) {
        return new Type<>(id);
    }
}
