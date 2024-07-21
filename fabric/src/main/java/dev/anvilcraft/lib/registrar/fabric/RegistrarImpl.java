package dev.anvilcraft.lib.registrar.fabric;

import dev.anvilcraft.lib.registrar.Registrar;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.NotNull;

public class RegistrarImpl extends Registrar {
    private RegistrarImpl(String modid) {
        super(modid);
    }

    @NotNull
    public static Registrar create(String modid) {
        return new RegistrarImpl(modid);
    }

    @Override
    public void init() {
        for (Registry<?> registry : this.builders.keySet()) {
            this.build(registry, Registry::register);
        }
    }
}
