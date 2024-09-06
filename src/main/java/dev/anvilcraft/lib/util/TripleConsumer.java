package dev.anvilcraft.lib.util;

public interface TripleConsumer<T, U, V> {
    void accept(T t, U u, V v);
}
