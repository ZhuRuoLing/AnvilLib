package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;

public abstract class EntryBuilder<T> {
    protected final AbstractRegistrator registrar;
    protected final String id;

    protected EntryBuilder(AbstractRegistrator registrar, String id) {
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
