package dev.anvilcraft.lib.registrar.entry;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class RegistryEntry<T> implements Supplier<T> {
    @Override
    public abstract T get();
}
