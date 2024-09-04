package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import dev.anvilcraft.lib.util.Side;
import dev.anvilcraft.lib.util.SideExecutor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MenuBuilder<T extends AbstractContainerMenu> extends EntryBuilder<MenuType<T>> {
    protected final RegistryEntry<MenuType<T>> entry = new RegistryEntry<>() {
    };
    protected final MenuFactory<T> menuFactory;
    protected final ScreenFactory<T> screenFactory;

    public MenuBuilder(AbstractRegistrator registrator, String id, MenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        super(registrator, id);
        this.menuFactory = menuFactory;
        this.screenFactory = screenFactory;
    }

    @Override
    public MenuType<T> build() {
        MenuType<T> ret = new MenuType<>((syncId, inventory) -> menuFactory.create(this.entry().get(), syncId, inventory), FeatureFlags.VANILLA_SET);
        SideExecutor.execute(Side.CLIENT, () -> () -> MenuScreens.register(ret, this.screenFactory::create));
        this.entry.set(ret);
        return ret;
    }

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
    public interface ScreenFactory<T extends AbstractContainerMenu> {
        AbstractContainerScreen<T> create(T menu, Inventory playerInventory, Component title);
    }
}
