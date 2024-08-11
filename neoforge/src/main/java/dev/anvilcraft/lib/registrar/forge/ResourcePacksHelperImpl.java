package dev.anvilcraft.lib.registrar.forge;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class ResourcePacksHelperImpl {
    public static void registerBuiltinResourcePack(@NotNull ResourceLocation pack) {
        String modid = pack.getNamespace();
        IModFileInfo modFileInfo = ModList.get().getModFileById(modid);
        if (modFileInfo == null) {
            throw new IllegalStateException("%s's ModContainer couldn't be found!".formatted(modid));
        }
        IModFile modFile = modFileInfo.getFile();
        MinecraftForge.EVENT_BUS.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() == PackType.SERVER_DATA) {
                registerPack(event, modFile, pack, PackType.SERVER_DATA);
            } else {
                registerPack(event, modFile, pack, PackType.CLIENT_RESOURCES);
            }
        });
    }

    private static void registerPack(@NotNull AddPackFindersEvent event, IModFile modFile, @NotNull ResourceLocation pack, @NotNull PackType packType) {
        event.addRepositorySource(consumer -> {
            Pack resPack = Pack.readMetaAndCreate(
                pack.toString(),
                Component.translatable("pack.%s.%s.description".formatted(pack.getNamespace(), pack.getPath())),
                false,
                id -> new ModFilePackResources(id, modFile, "resourcepacks/%s".formatted(pack.getPath())),
                packType, Pack.Position.TOP, PackSource.BUILT_IN);
            if (resPack != null) {
                consumer.accept(resPack);
            }
        });
    }

    public static class ModFilePackResources extends PathPackResources {
        protected final IModFile modFile;
        protected final String sourcePath;

        public ModFilePackResources(String name, @NotNull IModFile modFile, String sourcePath) {
            super(name, true, modFile.findResource(sourcePath));
            this.modFile = modFile;
            this.sourcePath = sourcePath;
        }

        @Override
        protected @NotNull Path resolve(String @NotNull ... paths) {
            String[] allPaths = new String[paths.length + 1];
            allPaths[0] = sourcePath;
            System.arraycopy(paths, 0, allPaths, 1, paths.length);
            return modFile.findResource(allPaths);
        }
    }
}
