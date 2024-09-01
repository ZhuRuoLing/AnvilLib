package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class RegistratorTagsProvider<T> extends TagsProvider<T> {
    protected final Map<TagKey<T>, List<RegistryEntry<? extends T>>> tags = Collections.synchronizedMap(new HashMap<>());

    protected RegistratorTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey) {
        super(output, registryKey, CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor()));
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
}
