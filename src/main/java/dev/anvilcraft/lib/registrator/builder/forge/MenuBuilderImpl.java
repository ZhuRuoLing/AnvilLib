package dev.anvilcraft.lib.registrator.builder.forge;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.builder.MenuBuilder;
import dev.anvilcraft.lib.util.Side;
import dev.anvilcraft.lib.util.SideExecutor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
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
            ret = IForgeMenuType.create((windowId, inv, $) -> menuFactory.create(this.entry().get(), windowId, inv));
        } else if (this.forgeMenuFactory != null) {
            ret = IForgeMenuType.create((windowId, inv, buf) -> forgeMenuFactory.create(this.entry().get(), windowId, inv, buf));
        } else {
            ret = null;
        }
        if (ret != null) SideExecutor.execute(Side.CLIENT, () -> () -> MenuScreens.register(ret, this.screenFactory::create));
        this.entry.set(ret);
        return ret;
    }
}
