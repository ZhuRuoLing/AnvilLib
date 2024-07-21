package dev.anvilcraft.lib.registrar.builder;

import dev.anvilcraft.lib.registrar.AbstractRegistrar;
import dev.anvilcraft.lib.registrar.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;

public abstract class EntryBuilder<T> {
    protected final AbstractRegistrar registrar;
    protected final String id;

    protected EntryBuilder(AbstractRegistrar registrar, String id) {
        this.registrar = registrar;
        this.id = id;
    }

    public abstract T build();

    @SuppressWarnings("UnusedReturnValue")
    public abstract RegistryEntry<T> register();

    public ResourceLocation getId() {
        return this.registrar.of(this.id);
    }
}
