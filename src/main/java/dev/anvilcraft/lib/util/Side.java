package dev.anvilcraft.lib.util;

public enum Side {
    CLIENT,
    SERVER,
    BOTH;

    public boolean isClient() {
        return this == CLIENT || this == BOTH;
    }

    public boolean isServer() {
        return this == SERVER || this == BOTH;
    }
}
