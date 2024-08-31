package dev.anvilcraft.lib.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface DataProviderType<P extends DataProvider> {
    DataProviderType<AnvilLibItemModelProvider> ITEM_MODEL = new DataProviderType<>() {
    };
    DataProviderType<AnvilLibBlockModelProvider> BLOCK_MODEL = new DataProviderType<>() {
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
    DataProviderType<TagsProvider<Item>> ITEM_TAG = new DataProviderType<>() {
    };
    DataProviderType<TagsProvider<Block>> BLOCK_TAG = new DataProviderType<>() {
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
