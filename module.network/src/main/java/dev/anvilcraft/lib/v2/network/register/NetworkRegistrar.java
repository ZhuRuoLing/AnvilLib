package dev.anvilcraft.lib.v2.network.register;

import dev.anvilcraft.lib.v2.network.packet.IPacket;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.modscan.ModAnnotation;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;

@Slf4j
public class NetworkRegistrar {
    public static final String ANNOTATION_NAME = "L" + Network.class.getName().replace(".", "/") + ";";
    public static final String PACKET_PACKAGE_PREFIX = "L" + IPacket.class.getPackageName().replace(".", "/");

    @SuppressWarnings("unchecked")
    public static void register(PayloadRegistrar registrar, String modId) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        IModFileInfo fileInfo = ModList.get().getModFileById(modId);
        ModFileScanData scanData = fileInfo.getFile().getScanResult();
        for (ModFileScanData.AnnotationData annotation : scanData.getAnnotations()) {
            if (
                !annotation.annotationType().getDescriptor().equals(ANNOTATION_NAME)
                || annotation.targetType() != ElementType.PACKAGE
            ) {
                continue;
            }

            ModAnnotation.EnumHolder protocolHolder = (ModAnnotation.EnumHolder) annotation.annotationData().get("protocol");
            PacketProtocol protocol = PacketProtocol.PLAY;
            if (protocolHolder != null) {
                protocol = switch (protocolHolder.value()) {
                    case "CONFIGURATION" -> PacketProtocol.CONFIGURATION;
                    case "PLAY" -> PacketProtocol.PLAY;
                    case "COMMON" -> PacketProtocol.COMMON;
                    default -> throw new IllegalArgumentException("Unknown packet protocol: " + protocolHolder.value());
                };
            }
            String packageName = annotation.memberName().substring(0, annotation.memberName().lastIndexOf('.') - 1);
            log.info("Considering network package {}", packageName);

            for (ModFileScanData.ClassData classData : scanData.getClasses()) {
                if (!classData.clazz().getClassName().startsWith(packageName)) continue;

                String interfaceName = null;
                for (Type anInterface : classData.interfaces()) {
                    if (!anInterface.getDescriptor().startsWith(NetworkRegistrar.PACKET_PACKAGE_PREFIX)) continue;
                    interfaceName = anInterface.getClassName();
                    break;
                }
                if (interfaceName == null) continue;

                try {
                    Class<? extends IPacket> packetClass = (Class<? extends IPacket>) loader.loadClass(classData.clazz().getClassName());
                    NetworkRegistrar.register(registrar, protocol, packetClass);
                } catch (ClassNotFoundException e) {
                    log.error("Cannot register packet {}", classData.clazz().getClassName(), e);
                    throw new IllegalStateException();
                }
            }
        }
    }

    private static <T extends IPacket> void register(PayloadRegistrar registrar, PacketProtocol protocol, Class<T> packetClass) {
        switch (protocol) {
            case CONFIGURATION -> {
                PacketData<? super FriendlyByteBuf, T> data = PacketData.find(packetClass);
                switch (data.direction()) {
                    case CLIENTBOUND -> registrar.configurationToClient(data.type(), data.streamCodec(), data.handler());
                    case SERVERBOUND -> registrar.configurationToServer(data.type(), data.streamCodec(), data.handler());
                    case BIDIRECTIONAL -> registrar.configurationBidirectional(data.type(), data.streamCodec(), data.handler());
                }
            }
            case PLAY -> {
                PacketData<? super RegistryFriendlyByteBuf, T> data = PacketData.find(packetClass);
                switch (data.direction()) {
                    case CLIENTBOUND -> registrar.playToClient(data.type(), data.streamCodec(), data.handler());
                    case SERVERBOUND -> registrar.playToServer(data.type(), data.streamCodec(), data.handler());
                    case BIDIRECTIONAL -> registrar.playBidirectional(data.type(), data.streamCodec(), data.handler());
                }
            }
            case COMMON -> {
                PacketData<? super FriendlyByteBuf, T> data = PacketData.find(packetClass);
                switch (data.direction()) {
                    case CLIENTBOUND -> registrar.commonToClient(data.type(), data.streamCodec(), data.handler());
                    case SERVERBOUND -> registrar.commonToServer(data.type(), data.streamCodec(), data.handler());
                    case BIDIRECTIONAL -> registrar.commonBidirectional(data.type(), data.streamCodec(), data.handler());
                }
            }
        }
    }
}
