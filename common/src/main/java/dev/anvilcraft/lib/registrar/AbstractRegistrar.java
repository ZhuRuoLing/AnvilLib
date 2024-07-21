package dev.anvilcraft.lib.registrar;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.registrar.builder.EntryBuilder;
import dev.anvilcraft.lib.registrar.builder.ItemBuilder;
import dev.anvilcraft.lib.registrar.builder.BlockBuilder;
import dev.anvilcraft.lib.registrar.entry.TagKeyEntry;
import dev.anvilcraft.lib.util.TripleConsumer;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class AbstractRegistrar {
    protected final Map<Registry<?>, List<EntryBuilder<?>>> builders = Collections.synchronizedMap(new HashMap<>());
    protected final Map<DataProviderType<?>, Consumer<DataProvider>> dataProviders = Collections.synchronizedMap(new HashMap<>());
    private final String modid;

    protected AbstractRegistrar(String modid) {
        this.modid = modid;
    }

    public ResourceLocation of(String path) {
        return new ResourceLocation(modid, path);
    }

    public <T> TagKeyEntry<T> tag(ResourceKey<? extends Registry<T>> registry, @NotNull String path) {
        return TagKeyEntry.create(this, registry, path, null);
    }

    public <T> TagKeyEntry<T> tag(ResourceKey<? extends Registry<T>> registry, @NotNull String fabricPath, String forgePath) {
        return TagKeyEntry.create(this, registry, fabricPath, forgePath);
    }

    public <T> TagKeyEntry<T> dict(ResourceKey<? extends Registry<T>> registry, @NotNull String path) {
        return TagKeyEntry.create(null, registry, path, null);
    }

    public <T> TagKeyEntry<T> dict(ResourceKey<? extends Registry<T>> registry, @NotNull String fabricPath, String forgePath) {
        return TagKeyEntry.create(null, registry, fabricPath, forgePath);
    }

    public <T extends Item> ItemBuilder<T> item(String id, Function<Item.Properties, T> factory) {
        return new ItemBuilder<>(this, id, factory);
    }

    public <T extends Block> BlockBuilder<T> block(String id, Function<BlockBehaviour.Properties, T> factory) {
        return new BlockBuilder<>(this, id, factory);
    }

    public <P extends DataProvider> void data(DataProviderType<P> type, Consumer<P> consumer) {
    }

    public void init() {
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T extends DataProvider> AbstractRegistrar initDatagen(DataProviderType<T> type, Consumer<DataProvider> consumer) {
        this.dataProviders.put(type, consumer);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> AbstractRegistrar addBuilder(Registry<T> registry, EntryBuilder<? extends T> builder) {
        List<EntryBuilder<?>> builderList = this.builders.getOrDefault(registry, Collections.synchronizedList(new ArrayList<>()));
        builderList.add(builder);
        return this;
    }

    @SuppressWarnings("unchecked")
    protected <V, T extends V> List<EntryBuilder<T>> getBuilders(Registry<V> registry) {
        List<EntryBuilder<?>> builderList = this.builders.getOrDefault(registry, Collections.synchronizedList(new ArrayList<>()));
        return builderList.stream()
            .map(builder -> (EntryBuilder<T>) builder)
            .toList();
    }

    protected <V, T extends V> boolean register(Registry<V> registry, EntryBuilder<T> builder) {
        try {
            Registry.register(registry, builder.getId(), builder.build());
        } catch (Exception e) {
            if (e instanceof ClassCastException) return false;
            AnvilLib.LOGGER.error(e.getMessage(), e);
        }
        return true;
    }
}
