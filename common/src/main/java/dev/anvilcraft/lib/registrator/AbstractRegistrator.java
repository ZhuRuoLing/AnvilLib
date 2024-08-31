package dev.anvilcraft.lib.registrator;

import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.registrator.builder.EntryBuilder;
import dev.anvilcraft.lib.registrator.builder.ItemBuilder;
import dev.anvilcraft.lib.registrator.builder.BlockBuilder;
import dev.anvilcraft.lib.registrator.entry.TagKeyEntry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class AbstractRegistrator {
    protected final BuilderManager manager = new BuilderManager();
    protected final Map<DataProviderType<?>, Consumer<? extends DataProvider>> dataProviders = Collections.synchronizedMap(new HashMap<>());
    private final String modid;

    protected AbstractRegistrator(String modid) {
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
        this.dataProviders.put(type, consumer);
    }

    public void init() {
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T extends DataProvider> AbstractRegistrator initDatagen(DataGenerator generator) {
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> AbstractRegistrator addBuilder(Registry<T> registry, EntryBuilder<? extends T> builder) {
        this.manager.addBuilder(registry, builder);
        return this;
    }

    protected <V, T extends V> List<EntryBuilder<T>> getBuilders(Registry<V> registry) {
        return this.manager.getBuilders(registry);
    }
}
