package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public abstract class MenuBuilder<T extends AbstractContainerMenu> extends EntryBuilder<MenuType<T>> {
    protected final RegistryEntry<MenuType<T>> entry = new RegistryEntry<>() {
    };
    protected final MenuFactory<T> menuFactory;
    protected final ForgeMenuFactory<T> forgeMenuFactory;
    protected final ScreenFactory<T> screenFactory;

    protected MenuBuilder(AbstractRegistrator registrator, String id, MenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        super(registrator, id);
        this.menuFactory = menuFactory;
        this.forgeMenuFactory = null;
        this.screenFactory = screenFactory;
    }

    protected MenuBuilder(AbstractRegistrator registrator, String id, ForgeMenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        super(registrator, id);
        this.menuFactory = null;
        this.forgeMenuFactory = menuFactory;
        this.screenFactory = screenFactory;
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuBuilder<T> create(AbstractRegistrator registrator, String id, MenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuBuilder<T> create(AbstractRegistrator registrator, String id, ForgeMenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        throw new AssertionError();
    }

    @Override
    public abstract MenuType<T> build();

    @Override
    public RegistryEntry<MenuType<T>> register() {
        this.registrator.addBuilder(BuiltInRegistries.MENU, this);
        return this.entry;
    }

    @Override
    public RegistryEntry<MenuType<T>> entry() {
        return this.entry;
    }

    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu> {
        T create(MenuType<T> menuType, int containerId, Inventory inventory);
    }

    @FunctionalInterface
    public interface ForgeMenuFactory<T extends AbstractContainerMenu> {
        T create(MenuType<T> menuType, int containerId, Inventory inventory, FriendlyByteBuf extraData);
    }

    @FunctionalInterface
    public interface ScreenFactory<T extends AbstractContainerMenu> {
        AbstractContainerScreen<T> create(T menu, Inventory playerInventory, Component title);
    }
}
