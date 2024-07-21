package dev.anvilcraft.lib.registrar.fabric;

import dev.anvilcraft.lib.registrar.Registrar;
import dev.anvilcraft.lib.registrar.builder.EntryBuilder;
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
        for (Registry<?> registry : this.manager) {
            this.init(registry);
        }
    }

    private <T> void init(Registry<T> registry) {
        for (EntryBuilder<T> builder : this.getBuilders(registry)) {
            this.register(registry, builder);
        }
    }
}
