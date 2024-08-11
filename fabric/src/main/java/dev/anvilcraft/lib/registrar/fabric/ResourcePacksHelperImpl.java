package dev.anvilcraft.lib.registrar.fabric;

import dev.anvilcraft.lib.registrar.ResourcePacksHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ResourcePacksHelperImpl {
    public static void registerBuiltinResourcePack(@NotNull ResourceLocation pack, ResourcePacksHelper.PackType type) {
        String modid = pack.getNamespace();
        ModContainer modContainer = FabricLoader.getInstance()
            .getModContainer(modid)
            .orElseThrow(() -> new IllegalStateException("%s's ModContainer couldn't be found!".formatted(modid)));
        ResourceManagerHelper.registerBuiltinResourcePack(
            pack, modContainer,
            Component.translatable("pack.%s.%s.description".formatted(pack.getNamespace(), pack.getPath())),
            ResourcePackActivationType.NORMAL
        );
    }
}
