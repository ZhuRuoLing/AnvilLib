package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.data.provider.AnvilLibItemModelProvider;
import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.data.provider.RegistratorRecipeProvider;
import dev.anvilcraft.lib.mixin.ItemPropertiesAccessor;
import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.ItemEntry;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
@SuppressWarnings("unchecked")
public class ItemBuilder<T extends Item, B extends ItemBuilder<T, B>> extends EntryBuilder<T> {
    private final ItemEntry<T> entry;
    private Item.Properties properties = new Item.Properties();
    private final Function<Item.Properties, T> factory;

    private Supplier<Item.Properties> propertiesSupplier = Item.Properties::new;
    private Consumer<Item.Properties> propertiesBuilder = properties -> {
    };

    public ItemBuilder(AbstractRegistrator registrator, String id, Function<Item.Properties, T> factory) {
        super(registrator, id);
        this.factory = factory;
        this.entry = new ItemEntry<>(this);
        this.lang(toTitleCase(this.id));
    }

    public B model(BiConsumer<ItemEntry<T>, AnvilLibItemModelProvider> consumer) {
        this.registrator.data(DataProviderType.ITEM_MODEL, p -> consumer.accept(this.entry, p));
        return (B) this;
    }

    @SafeVarargs
    public final B tag(Supplier<TagKey<Item>>... tags) {
        this.registrator.data(DataProviderType.ITEM_TAG, p -> {
            for (Supplier<TagKey<Item>> tag : tags) {
                p.add(tag.get(), this.entry);
            }
        });
        return (B) this;
    }

    @SafeVarargs
    public final B tag(TagKey<Item>... tags) {
        this.registrator.data(DataProviderType.ITEM_TAG, p -> {
            for (TagKey<Item> tag : tags) {
                p.add(tag, this.entry);
            }
        });
        return (B) this;
    }

    public B recipe(BiConsumer<ItemEntry<T>, RegistratorRecipeProvider> consumer) {
        this.registrator.data(DataProviderType.RECIPE, p -> consumer.accept(this.entry, p));
        return (B) this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public B lang(String name) {
        this.registrator.lang(Util.makeDescriptionId("item", this.getId()), name);
        return (B) this;
    }

    public B properties(@NotNull Consumer<Item.Properties> properties) {
        properties.accept(this.properties);
        return (B) this;
    }

    public B properties(@NotNull Supplier<Item.Properties> properties){
        this.properties = properties.get();
        return (B) this;
    }

    public B initProperties(Supplier<Item> supplier) {
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
        return (B) this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public T build() {
        T item = this.factory.apply(this.properties);
        this.entry.set(item);
        return item;
    }

    public ItemEntry<T> register() {
        this.registrator.addBuilder(BuiltInRegistries.ITEM, this);
        return this.entry;
    }

    @Override
    public RegistryEntry<T> entry() {
        return this.entry;
    }
}
