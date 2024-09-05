package dev.anvilcraft.lib.data.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.anvilcraft.lib.data.TagBuilder;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class RegistratorTagsProvider<T> extends TagsProvider<T> {
    protected final Map<TagKey<T>, List<RegistryEntry<? extends T>>> tags = Collections.synchronizedMap(new HashMap<>());
    private final Map<ResourceLocation, TagBuilder< ? extends T>> tagBuilders = new HashMap();
    protected RegistratorTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey) {
        super(output, registryKey, CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor()));
    }

    @SuppressWarnings("unchecked")
    public final TagBuilder<T> create(TagKey<T> tag){
        return (TagBuilder<T>) tagBuilders.computeIfAbsent(
            tag.location(),
            (t) -> new TagBuilder<>(new net.minecraft.tags.TagBuilder(), this.registryKey)
        );
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        List<? extends CompletableFuture<?>> futures = tagBuilders.entrySet().stream().map(it -> {
            ResourceLocation location = it.getKey();
            TagBuilder<? extends T> tagBuilder = it.getValue();
            List<TagEntry> tagEntries = tagBuilder.getParent().build();
            JsonElement je = TagFile.CODEC.encodeStart(
                JsonOps.INSTANCE,
                new TagFile(tagEntries, tagBuilder.isReplace())
            ).getOrThrow(false, ignored ->{});
            Path path = this.pathProvider.json(location);
            return DataProvider.saveStable(output,je, path);
        }).toList();
        CompletableFuture<?>[] fs = new CompletableFuture[futures.size() + 1];
        int i = 0;
        fs[i++] = super.run(output);
        for (CompletableFuture<?> future : futures) {
            fs[i++] = future;
        }
        return CompletableFuture.allOf(fs);
    }

    @SafeVarargs
    public final <E extends T> void add(TagKey<T> tag, RegistryEntry<E>... values) {
        List<RegistryEntry<? extends T>> list = this.tags.getOrDefault(tag, Collections.synchronizedList(new ArrayList<>()));
        list.addAll(List.of(values));
        this.tags.put(tag, list);
    }

    public static class ItemProvider extends RegistratorTagsProvider<Item> {
        public ItemProvider(PackOutput output) {
            super(output, Registries.ITEM);
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            for (var entry : this.tags.entrySet()) {
                var builder = this.getOrCreateRawBuilder(entry.getKey());
                for (RegistryEntry<? extends Item> item : entry.getValue()) {
                    builder.add(TagEntry.optionalElement(BuiltInRegistries.ITEM.getKey(item.get())));
                }
            }
        }
    }

    public static class BlockProvider extends RegistratorTagsProvider<Block> {
        public BlockProvider(PackOutput output) {
            super(output, Registries.BLOCK);
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            for (var entry : this.tags.entrySet()) {
                var builder = this.getOrCreateRawBuilder(entry.getKey());
                for (RegistryEntry<? extends Block> block : entry.getValue()) {
                    builder.add(TagEntry.optionalElement(BuiltInRegistries.BLOCK.getKey(block.get())));
                }
            }
        }
    }

    public static class FluidProvider extends RegistratorTagsProvider<Fluid> {
        public FluidProvider(PackOutput output) {
            super(output, Registries.FLUID);
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            for (var entry : this.tags.entrySet()) {
                var builder = this.getOrCreateRawBuilder(entry.getKey());
                for (RegistryEntry<? extends Fluid> fluid : entry.getValue()) {
                    builder.add(TagEntry.optionalElement(BuiltInRegistries.FLUID.getKey(fluid.get())));
                }
            }
        }
    }
}
