package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;

public abstract class EntryBuilder<T> {
    protected final AbstractRegistrator registrator;
    protected final String id;

    protected EntryBuilder(AbstractRegistrator registrator, String id) {
        this.registrator = registrator;
        this.id = id;
    }

    public abstract T build();

    @SuppressWarnings("UnusedReturnValue")
    public abstract RegistryEntry<T> register();

    public ResourceLocation getId() {
        return this.registrator.of(this.id);
    }
}
