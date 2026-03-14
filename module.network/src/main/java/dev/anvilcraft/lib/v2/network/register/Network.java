package dev.anvilcraft.lib.v2.network.register;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 将该软件包标识为网络包软件包
 */
@Target(ElementType.PACKAGE)
public @interface Network {
    /**
     * 决定了该软件包下所有网络包将要注册的连接协议
     */
    PacketProtocol protocol() default PacketProtocol.PLAY;
}
