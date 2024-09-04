package dev.anvilcraft.lib.registrator;

import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.registrator.builder.BlockEntityBuilder;
import dev.anvilcraft.lib.registrator.builder.CreativeModeTabBuilder;
import dev.anvilcraft.lib.registrator.builder.EntityBuilder;
import dev.anvilcraft.lib.registrator.builder.EntryBuilder;
import dev.anvilcraft.lib.registrator.builder.ItemBuilder;
import dev.anvilcraft.lib.registrator.builder.BlockBuilder;
import dev.anvilcraft.lib.registrator.builder.MenuBuilder;
import dev.anvilcraft.lib.registrator.entry.TagKeyEntry;
import dev.anvilcraft.lib.util.TripleFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class AbstractRegistrator {
    protected final BuilderManager manager = new BuilderManager();
    protected final Map<DataProviderType<? extends DataProvider>, List<Consumer<? extends DataProvider>>> dataProviders = Collections.synchronizedMap(new HashMap<>());
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

    public <T extends Entity> EntityBuilder<T> entity(String id, BiFunction<EntityType<T>, Level, T> factory, MobCategory category) {
        return EntityBuilder.create(this, id, factory, category);
    }

    public <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String id, TripleFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory) {
        return BlockEntityBuilder.create(this, id, factory);
    }

    public EntryBuilder<CreativeModeTab> tab(String id, Consumer<CreativeModeTab.Builder> consumer) {
        return CreativeModeTabBuilder.create(this, id, consumer);
    }

    public <T extends AbstractContainerMenu> MenuBuilder<T> menu(String id, MenuBuilder.MenuFactory<T> menuFactory, MenuBuilder.ScreenFactory<T> screenFactory) {
        return MenuBuilder.create(this, id, menuFactory, screenFactory);
    }

    public <T extends AbstractContainerMenu> MenuBuilder<T> menu(String id, MenuBuilder.ForgeMenuFactory<T> menuFactory, MenuBuilder.ScreenFactory<T> screenFactory) {
        return MenuBuilder.create(this, id, menuFactory, screenFactory);
    }

    public <P extends DataProvider> void data(DataProviderType<P> type, Consumer<P> consumer) {
        List<Consumer<? extends DataProvider>> list = this.dataProviders.getOrDefault(type, Collections.synchronizedList(new ArrayList<>()));
        list.add(consumer);
        this.dataProviders.put(type, list);
    }

    public void init() {
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T extends DataProvider> AbstractRegistrator initDatagen(DataGenerator.PackGenerator generator) {
        for (Map.Entry<DataProviderType<? extends DataProvider>, List<Consumer<? extends DataProvider>>> entry : this.dataProviders.entrySet()) {
            entry.getKey().create(this.modid, generator, entry.getValue());
        }
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

    public void lang(String key, String name) {
        this.data(DataProviderType.LANG, provider -> provider.add(key, name));
    }
}
