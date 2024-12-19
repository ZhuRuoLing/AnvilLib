package dev.anvilcraft.lib.integration;

public @interface Integrations {
    String value();

    String version() default "*";
}
