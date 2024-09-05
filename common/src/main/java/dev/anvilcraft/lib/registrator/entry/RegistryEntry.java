package dev.anvilcraft.lib.registrator.entry;

import dev.anvilcraft.lib.util.Callback;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class RegistryEntry<T> implements Supplier<T> {
    private T content = null;
    private Callback<T> onRegister;

    @Override
    public T get() {
        return this.content;
    }

    public void onRegister() {
        if (onRegister != null)
            onRegister.invoke(content);
    }

    public void setOnRegisterCallback(Callback<T> onRegister) {
        this.onRegister = onRegister;
    }

    public void set(T item) {
        if (this.content != null) throw new RuntimeException("Cannot specify duplicate content for RegistryEntry");
        this.content = item;
    }
}
