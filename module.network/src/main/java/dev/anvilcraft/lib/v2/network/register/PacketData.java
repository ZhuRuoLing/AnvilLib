package dev.anvilcraft.lib.v2.network.register;

import dev.anvilcraft.lib.v2.network.packet.IClientboundPacket;
import dev.anvilcraft.lib.v2.network.packet.IInsensitiveBiPacket;
import dev.anvilcraft.lib.v2.network.packet.IPacket;
import dev.anvilcraft.lib.v2.network.packet.ISensitiveBiPacket;
import dev.anvilcraft.lib.v2.network.packet.IServerboundPacket;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
record PacketData<B extends ByteBuf, T extends IPacket>(
    CustomPacketPayload.Type<T> type,
    StreamCodec<B, T> streamCodec,
    PacketDirection direction,
    IPayloadHandler<T> handler
) {
    @SuppressWarnings("unchecked")
    static <B extends ByteBuf, T extends IPacket> PacketData<B, T> find(Class<T> packetClass) {
        CustomPacketPayload.Type<T> type = null;
        StreamCodec<B, T> codec = null;
        try {
            for (Field field : packetClass.getDeclaredFields()) {
                Set<AccessFlag> accessFlags = field.accessFlags();
                if (
                    !accessFlags.contains(AccessFlag.STATIC)
                    || !accessFlags.contains(AccessFlag.FINAL)
                ) {
                    continue;
                }
                Class<?> declaringClass = field.getType();
                if (declaringClass.isAssignableFrom(CustomPacketPayload.Type.class)) {
                    field.setAccessible(true);
                    type = (CustomPacketPayload.Type<T>) field.get(null);
                } else if (declaringClass.isAssignableFrom(StreamCodec.class)) {
                    field.setAccessible(true);
                    codec = (StreamCodec<B, T>) field.get(null);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Cannot access the type/codec of packet {}", packetClass.getName(), e);
            throw new IllegalStateException();
        }
        if (type == null) {
            log.error("Cannot find static final type of packet {}", packetClass.getName());
            throw new IllegalArgumentException();
        }
        if (codec == null) {
            log.error("Cannot find static final codec of packet {}", packetClass.getName());
            throw new IllegalArgumentException();
        }

        PacketDirection direction;
        IPayloadHandler<T> handler;
        if (IInsensitiveBiPacket.class.isAssignableFrom(packetClass)) {
            direction = PacketDirection.BIDIRECTIONAL;
            handler = (packet, ctx) -> ((IInsensitiveBiPacket) packet).bidirectionalHandler(ctx);
        } else if (ISensitiveBiPacket.class.isAssignableFrom(packetClass)) {
            direction = PacketDirection.BIDIRECTIONAL;
            handler = (packet, ctx) -> ((ISensitiveBiPacket) packet).bidirectionalHandler(ctx);
        } else if (IClientboundPacket.class.isAssignableFrom(packetClass)) {
            direction = PacketDirection.CLIENTBOUND;
            handler = (packet, ctx) -> ((IClientboundPacket) packet).clientHandler(ctx);
        } else if (IServerboundPacket.class.isAssignableFrom(packetClass)) {
            direction = PacketDirection.SERVERBOUND;
            handler = (packet, ctx) -> ((IServerboundPacket) packet).serverHandler(ctx);
        } else {
            log.error("Class {} extends IPacket, but not extends IClientboundPacket or IServerboundPacket", packetClass.getName());
            throw new IllegalStateException();
        }
        return new PacketData<>(type, codec, direction, handler);
    }
}
