package dev.anvilcraft.lib.util;

@FunctionalInterface
public interface TripleFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}
