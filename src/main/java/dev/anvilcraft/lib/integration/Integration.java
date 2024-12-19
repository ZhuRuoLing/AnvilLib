package dev.anvilcraft.lib.integration;

/**
 * 集成
 */
@Deprecated(
    since = "1.3.0"
)
public interface Integration {
    default void apply() {
    }

    default void applyClient() {
    }
}
