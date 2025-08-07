package dev.anvilcraft.lib.integration;

import lombok.Getter;
import lombok.Setter;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class IntegrationHook {
    @Getter
    @Setter
    private static GatherDataEvent event = null;
}
