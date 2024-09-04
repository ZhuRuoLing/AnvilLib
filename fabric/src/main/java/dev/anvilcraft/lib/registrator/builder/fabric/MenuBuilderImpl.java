package dev.anvilcraft.lib.registrator.builder.fabric;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.builder.MenuBuilder;
import dev.anvilcraft.lib.util.Side;
import dev.anvilcraft.lib.util.SideExecutor;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MenuBuilderImpl<T extends AbstractContainerMenu> extends MenuBuilder<T> {
    private MenuBuilderImpl(AbstractRegistrator registrator, String id, MenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        super(registrator, id, menuFactory, screenFactory);
    }

    private MenuBuilderImpl(AbstractRegistrator registrator, String id, ForgeMenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        super(registrator, id, menuFactory, screenFactory);
    }

    public static <T extends AbstractContainerMenu> @NotNull MenuBuilder<T> create(AbstractRegistrator registrator, String id, MenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        return new MenuBuilderImpl<>(registrator, id, menuFactory, screenFactory);
    }

    public static <T extends AbstractContainerMenu> @NotNull MenuBuilder<T> create(AbstractRegistrator registrator, String id, ForgeMenuFactory<T> menuFactory, ScreenFactory<T> screenFactory) {
        return new MenuBuilderImpl<>(registrator, id, menuFactory, screenFactory);
    }

    @Override
    public MenuType<T> build() {
        MenuType<T> ret;
        if (this.menuFactory != null) {
            ret = new MenuType<>((syncId, inventory) -> menuFactory.create(this.entry().get(), syncId, inventory), FeatureFlags.VANILLA_SET);
        } else if (this.forgeMenuFactory != null) {
            ret = new ExtendedScreenHandlerType<>((windowId, inv, buf) -> forgeMenuFactory.create(this.entry().get(), windowId, inv, buf));
        } else {
            ret = null;
        }
        SideExecutor.execute(Side.CLIENT, () -> () -> {
            if (ret != null) {
                MenuScreens.register(ret, this.screenFactory::create);
            }
        });
        this.entry.set(ret);
        return ret;
    }
}
