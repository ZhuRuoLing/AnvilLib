package dev.anvilcraft.lib.registrator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class Registrator extends AbstractRegistrator {
    protected Registrator(String modid) {
        super(modid);
    }

    @ExpectPlatform
    public static @NotNull Registrator create(String modid) {
        throw new AssertionError();
    }
}
