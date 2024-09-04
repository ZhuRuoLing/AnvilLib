package dev.anvilcraft.lib.data.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.anvilcraft.lib.data.file.ResourceFile;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ResourceFileProvider<T extends ResourceFile> implements DataProvider {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String BLOCK_FOLDER = "block";
    public static final String ITEM_FOLDER = "item";

    protected final Function<ResourceLocation, T> factory;
    protected final Map<ResourceLocation, T> files = new HashMap<>();
    protected final String categoryDirectory;
    protected final String modid;
    protected final PackOutput output;

    public ResourceFileProvider(Function<ResourceLocation, T> factory, String categoryDirectory, String modid, PackOutput output) {
        this.factory = factory;
        this.categoryDirectory = categoryDirectory;
        this.modid = modid;
        this.output = output;
    }

    protected ResourceLocation extendLocation(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), categoryDirectory + "/" + rl.getPath());
    }

    public T getBuilder(ResourceLocation location) {
        System.out.println("location = " + location);
        return files.computeIfAbsent(location, factory);
    }

    public T getBuilder(String path) {
        ResourceLocation location = extendLocation(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(modid, path));
        return getBuilder(location);
    }

    public ResourceLocation modLocation(String name) {
        return new ResourceLocation(modid, name);
    }

    public ResourceLocation mcLocation(String name) {
        return new ResourceLocation(name);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        CompletableFuture<?>[] futures = new CompletableFuture[this.files.size()];
        int i = 0;
        for (T model : this.files.values()) {
            Path target = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(model.getLocation().getNamespace());
            if (!model.getLocation().getPath().contains("/")) {
                target = target.resolve(categoryDirectory);
            }
            target = target.resolve(model.getLocation().getPath() + ".json");
            futures[i++] = DataProvider.saveStable(output, model.toJsonElement(), target);
        }
        return CompletableFuture.allOf(futures);
    }

    abstract String getProviderName();

    @Override
    public @NotNull String getName() {
        return modid + "->" + getProviderName();
    }
}
