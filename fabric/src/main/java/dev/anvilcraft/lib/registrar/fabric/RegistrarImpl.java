package dev.anvilcraft.lib.registrar.fabric;

import dev.anvilcraft.lib.registrar.Registrar;
import dev.anvilcraft.lib.registrar.builder.ItemBuilder;
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
        for (ItemBuilder<?> builder : this.itemBuilders) {
            builder.build();
        }
    }
}
