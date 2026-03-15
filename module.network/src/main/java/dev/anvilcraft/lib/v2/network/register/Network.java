package dev.anvilcraft.lib.v2.network.register;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 表示该软件包为网络包软件包
 */
@Target(ElementType.PACKAGE)
public @interface Network {
    /**
     * 返回该软件包下所有网络包的连接协议
     *
     * @return 该软件包下所有网络包的连接协议
     */
    PacketProtocol protocol() default PacketProtocol.PLAY;
}
