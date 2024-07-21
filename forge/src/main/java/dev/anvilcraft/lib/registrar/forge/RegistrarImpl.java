package dev.anvilcraft.lib.registrar.forge;

import dev.anvilcraft.lib.registrar.Registrar;
import org.jetbrains.annotations.NotNull;

public class RegistrarImpl extends Registrar {
    private RegistrarImpl(String modid) {
        super(modid);
    }

    @NotNull
    public static Registrar create(String modid) {
        return new RegistrarImpl(modid);
    }
}
