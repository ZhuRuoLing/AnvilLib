package dev.anvilcraft.lib.registrator;

import dev.anvilcraft.lib.registrator.forge.RegistratorImpl;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class Registrator extends AbstractRegistrator {
    protected Registrator(String modid) {
        super(modid);
    }

    public static @NotNull Registrator create(String modid) {
        return RegistratorImpl.create(modid);
    }
}
