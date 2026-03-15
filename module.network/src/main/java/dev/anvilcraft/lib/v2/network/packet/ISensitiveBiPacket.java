package dev.anvilcraft.lib.v2.network.packet;

import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 双端网络包，允许两端各自向对方发送
 *
 * <p>处理逻辑时方向敏感，两端各自用一套逻辑</p>
 */
public interface ISensitiveBiPacket extends IClientboundPacket, IServerboundPacket {
    /**
     * 双端处理器
     *
     * @param ctx 网络包上下文
     */
    default void bidirectionalHandler(IPayloadContext ctx) {
        if (ctx.flow().isClientbound()) {
            this.clientHandler(ctx);
        } else if (ctx.flow().isServerbound()) {
            this.serverHandler(ctx);
        }
    }
}
