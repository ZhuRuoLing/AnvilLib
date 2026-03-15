package dev.anvilcraft.lib.v2.network.packet;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.ApiStatus;

/**
 * 服务端侧网络包，允许客户端向服务端发送
 */
public non-sealed interface IServerboundPacket extends IPacket {
    /**
     * 服务端处理器
     *
     * @param ctx 网络包上下文
     */
    default void serverHandler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> this.handleOnServer(ctx.player()));
    }

    /**
     * 服务端侧处理逻辑
     *
     * @param player 服务端玩家。其类型恒为 {@link net.minecraft.server.level.ServerPlayer ServerPlayer}
     */
    @ApiStatus.OverrideOnly
    void handleOnServer(Player player);
}
