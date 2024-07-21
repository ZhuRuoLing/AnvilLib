package dev.anvilcraft.lib.registrar.builder;

import dev.anvilcraft.lib.data.AnvilLibItemModelProvider;
import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.mixin.ItemPropertiesAccessor;
import dev.anvilcraft.lib.registrar.AbstractRegistrar;
import dev.anvilcraft.lib.registrar.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemBuilder<T extends Item> extends EntryBuilder<T> {
    private final ItemEntry<T> entry;
    private final Item.Properties properties = new Item.Properties();
    private final Function<Item.Properties, T> factory;

    public ItemBuilder(AbstractRegistrar registrar, String id, Function<Item.Properties, T> factory) {
        super(registrar, id);
        this.factory = factory;
        this.entry = new ItemEntry<>(this);
    }

    public ItemBuilder<T> model(BiConsumer<ItemEntry<T>, AnvilLibItemModelProvider> consumer) {
        this.registrar.data(DataProviderType.ITEM_MODEL, p -> consumer.accept(this.entry, p));
        return this;
    }

    @SafeVarargs
    public final ItemBuilder<T> tag(Supplier<TagKey<Item>>... tags) {
        return this;
    }

    public ItemBuilder<T> recipe(BiConsumer<ItemEntry<T>, RecipeProvider> consumer) {
        this.registrar.data(DataProviderType.RECIPE, p -> consumer.accept(this.entry, p));
        return this;
    }

    public ItemBuilder<T> lang(String name) {
        return this;
    }

    public ItemBuilder<T> properties(@NotNull Consumer<Item.Properties> properties) {
        properties.accept(this.properties);
        return this;
    }

    public ItemBuilder<T> initProperties(Supplier<Item> supplier) {
        ItemPropertiesAccessor defaultProperties = (ItemPropertiesAccessor) new Item.Properties();
        ItemPropertiesAccessor thisAccessor = (ItemPropertiesAccessor) this.properties;
        if (supplier instanceof ItemEntry<?> itemEntry) {
            ItemPropertiesAccessor accessor = (ItemPropertiesAccessor) itemEntry.getItemBuilder().properties;
            if (
                accessor.getMaxStackSize() != defaultProperties.getMaxStackSize()
                    && thisAccessor.getMaxStackSize() == defaultProperties.getMaxStackSize()
            ) {
                this.properties.stacksTo(accessor.getMaxStackSize());
            }
            if (
                accessor.getMaxDamage() != defaultProperties.getMaxDamage()
                    && thisAccessor.getMaxDamage() == defaultProperties.getMaxDamage()
            ) {
                this.properties.durability(accessor.getMaxDamage());
            }
            if (accessor.getCraftingRemainingItem() != null && thisAccessor.getCraftingRemainingItem() == null) {
                this.properties.craftRemainder(accessor.getCraftingRemainingItem());
            }
            if (
                accessor.getRarity() != defaultProperties.getRarity()
                    && thisAccessor.getRarity() == defaultProperties.getRarity()
            ) {
                this.properties.rarity(accessor.getRarity());
            }
            if (accessor.getFoodProperties() != null && thisAccessor.getFoodProperties() == null) {
                this.properties.food(accessor.getFoodProperties());
            }
            if (accessor.isFireResistant() && !thisAccessor.isFireResistant()) {
                this.properties.fireResistant();
            }
        } else {
            Item item = supplier.get();
            if (item != null) {
                if (
                    item.getMaxDamage() != defaultProperties.getMaxDamage()
                        && thisAccessor.getMaxDamage() == defaultProperties.getMaxDamage()
                ) {
                    this.properties.durability(item.getMaxDamage());
                }
                if (item.getCraftingRemainingItem() != null && thisAccessor.getCraftingRemainingItem() == null) {
                    this.properties.craftRemainder(item.getCraftingRemainingItem());
                }
                if (
                    item.getRarity(new ItemStack(item)) != defaultProperties.getRarity()
                        && thisAccessor.getRarity() == defaultProperties.getRarity()
                ) {
                    this.properties.rarity(item.getRarity(new ItemStack(item)));
                }
                if (
                    item.getMaxStackSize() != defaultProperties.getMaxStackSize()
                        && thisAccessor.getMaxStackSize() == defaultProperties.getMaxStackSize()
                ) {
                    this.properties.stacksTo(item.getMaxStackSize());
                }
                if (
                    item.getFoodProperties() != null
                        && thisAccessor.getFoodProperties() == null
                ) {
                    this.properties.food(item.getFoodProperties());
                }
                if (
                    item.isFireResistant() && !thisAccessor.isFireResistant()
                ) {
                    this.properties.fireResistant();
                }
            }
        }
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public T build() {
        T item = this.factory.apply(this.properties);
        this.entry.set(item);
        return item;
    }

    public ItemEntry<T> register() {
        this.registrar.addItemBuilder(this);
        return this.entry;
    }
}
