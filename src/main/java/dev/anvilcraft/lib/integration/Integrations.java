package dev.anvilcraft.lib.integration;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

public @interface Integrations {
    String value();

    String version() default "*";

    Class<? extends Event> event() default FMLLoadCompleteEvent.class;
}
