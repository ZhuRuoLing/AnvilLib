package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CreativeModeTabBuilder extends EntryBuilder<CreativeModeTab> {
    protected final RegistryEntry<CreativeModeTab> tab = new RegistryEntry<>() {
    };
    protected final CreativeModeTab.Builder builder;

    protected CreativeModeTabBuilder(AbstractRegistrator registrator, String id, CreativeModeTab.Builder builder) {
        super(registrator, id);
        this.builder = builder;
    }

    @ExpectPlatform
    public static CreativeModeTabBuilder create(AbstractRegistrator registrator, String id, Consumer<CreativeModeTab.Builder> consumer) {
        throw new AssertionError();
    }

    public CreativeModeTabBuilder title(Component title) {
        this.builder.title(title);
        return this;
    }

    public CreativeModeTabBuilder icon(Supplier<ItemStack> icon) {
        this.builder.icon(icon);
        return this;
    }

    public CreativeModeTabBuilder displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        this.builder.displayItems(displayItemsGenerator);
        return this;
    }

    public CreativeModeTabBuilder alignedRight() {
        this.builder.alignedRight();
        return this;
    }

    public CreativeModeTabBuilder hideTitle() {
        this.builder.hideTitle();
        return this;
    }

    public CreativeModeTabBuilder noScrollBar() {
        this.builder.noScrollBar();
        return this;
    }

    public CreativeModeTabBuilder backgroundSuffix(String backgroundSuffix) {
        this.builder.backgroundSuffix(backgroundSuffix);
        return this;
    }

    @Override
    public CreativeModeTab build() {
        this.tab.set(this.builder.build());
        return this.tab.get();
    }

    @Override
    public RegistryEntry<CreativeModeTab> register() {
        this.registrator.addBuilder(BuiltInRegistries.CREATIVE_MODE_TAB, this);
        return this.tab;
    }
}
