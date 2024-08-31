package dev.anvilcraft.lib.registrator.entry;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class RegistryEntry<T> implements Supplier<T> {
    private T content = null;

    @Override
    public T get() {
        return this.content;
    }

    public void set(T item) {
        if (this.content != null) throw new RuntimeException("Cannot specify duplicate content for RegistryEntry");
        this.content = item;
    }
}
