package dev.anvilcraft.lib.data;

import dev.anvilcraft.lib.data.provider.AnvilLibBlockModelProvider;
import dev.anvilcraft.lib.data.provider.AnvilLibBlockStateProvider;
import dev.anvilcraft.lib.data.provider.AnvilLibItemModelProvider;
import dev.anvilcraft.lib.data.provider.LanguageProvider;
import dev.anvilcraft.lib.data.provider.RegistratorRecipeProvider;
import dev.anvilcraft.lib.data.provider.RegistratorTagsProvider;
import dev.anvilcraft.lib.data.provider.UpsideDownLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface DataProviderType<P extends DataProvider> {
    DataProviderType<AnvilLibItemModelProvider> ITEM_MODEL = new DataProviderType<>() {
        @Override
        public void create(DataGenerator.@NotNull PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<AnvilLibItemModelProvider>> consumer) {
            generator.addProvider(packOutput -> new AnvilLibItemModelProvider(
                    "models/item",
                    namespace,
                    packOutput
            ));
        }
    };
    DataProviderType<AnvilLibBlockModelProvider> BLOCK_MODEL = new DataProviderType<>() {
        @Override
        public void create(DataGenerator.@NotNull PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<AnvilLibBlockModelProvider>> consumer) {
            generator.addProvider(packOutput -> new AnvilLibBlockModelProvider(
                    "models/block",
                    namespace,
                    packOutput
            ));
        }
    };
    DataProviderType<AnvilLibBlockStateProvider> BLOCK_STATE = new DataProviderType<AnvilLibBlockStateProvider>() {
        @Override
        public void create(DataGenerator.@NotNull PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<AnvilLibBlockStateProvider>> consumer) {
            generator.addProvider(packOutput -> new AnvilLibBlockStateProvider(
                    "blockstates",
                    namespace,
                    packOutput
            ));
        }
    };
    DataProviderType<RegistratorRecipeProvider> RECIPE = new DataProviderType<>() {
        @Override
        public void create(@NotNull DataGenerator.PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<RegistratorRecipeProvider>> consumer) {
            generator.addProvider(output -> {
                RegistratorRecipeProvider provider = new RegistratorRecipeProvider(output);
                consumer.forEach(c -> c.accept(provider));
                return provider;
            });
        }
    };
    DataProviderType<RegistratorTagsProvider<Item>> ITEM_TAG = new DataProviderType<>() {
        @Override
        public void create(@NotNull DataGenerator.PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<RegistratorTagsProvider<Item>>> consumer) {
            generator.addProvider(output -> {
                RegistratorTagsProvider<Item> provider = new RegistratorTagsProvider.ItemProvider(output);
                consumer.forEach(c -> c.accept(provider));
                return provider;
            });
        }
    };
    DataProviderType<RegistratorTagsProvider<Block>> BLOCK_TAG = new DataProviderType<>() {
        @Override
        public void create(@NotNull DataGenerator.PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<RegistratorTagsProvider<Block>>> consumer) {
            generator.addProvider(output -> {
                RegistratorTagsProvider<Block> provider = new RegistratorTagsProvider.BlockProvider(output);
                consumer.forEach(c -> c.accept(provider));
                return provider;
            });
        }
    };
    DataProviderType<LanguageProvider> LANG = new DataProviderType<>() {
        @Override
        public void create(@NotNull DataGenerator.PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<LanguageProvider>> consumer) {
            generator.addProvider(output -> {
                LanguageProvider provider = new LanguageProvider(output, namespace);
                consumer.forEach(c -> c.accept(provider));
                return provider;
            });
            generator.addProvider(output -> {
                LanguageProvider provider = new UpsideDownLanguageProvider(output, namespace);
                consumer.forEach(c -> c.accept(provider));
                return provider;
            });
        }
    };

    default void create(@NotNull DataGenerator.PackGenerator generator, @NotNull String namespace, @NotNull List<Consumer<P>> consumer) {
    }

    @SuppressWarnings("unchecked")
    default void create(@NotNull String namespace, @NotNull DataGenerator.PackGenerator generator, @NotNull List<Consumer<? extends DataProvider>> consumer) {
        this.create(generator, namespace, consumer.stream().map(c -> (Consumer<P>) c).toList());
    }
}
