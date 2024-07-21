package dev.anvilcraft.lib.registrar;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class Registrar extends AbstractRegistrar {
    protected Registrar(String modid) {
        super(modid);
    }

    @ExpectPlatform
    public static @NotNull Registrar create(String modid) {
        throw new AssertionError();
    }
}
