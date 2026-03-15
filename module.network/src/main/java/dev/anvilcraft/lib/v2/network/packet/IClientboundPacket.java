package dev.anvilcraft.lib.v2.network.packet;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.ApiStatus;

/**
 * 客户端侧网络包，允许服务端向客户端发送
 */
public non-sealed interface IClientboundPacket extends IPacket {
    /**
     * 客户端处理器
     *
     * @param ctx 网络包上下文
     */
    default void clientHandler(IPayloadContext ctx) {
        ctx.enqueueWork(() -> this.handleOnClient(ctx.player()));
    }

    /**
     * 客户端侧处理逻辑
     *
     * @param player 客户端玩家。其类型恒为 {@link net.minecraft.client.player.LocalPlayer LocalPlayer}
     */
    @ApiStatus.OverrideOnly
    void handleOnClient(Player player);
}
