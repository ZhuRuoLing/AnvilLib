package dev.anvilcraft.lib.registrar.builder;

import dev.anvilcraft.lib.registrar.entry.RegistryEntry;

public abstract class EntryBuilder<T> {
    public abstract RegistryEntry<T> build();
}
