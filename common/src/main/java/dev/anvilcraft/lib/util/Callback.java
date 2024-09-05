package dev.anvilcraft.lib.util;

@FunctionalInterface
public interface Callback<T> {
    void invoke(T t);
}
