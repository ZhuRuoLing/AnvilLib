package dev.anvilcraft.lib.config;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record ConfigRecord(
    ModConfig.Type type,
    ModConfigSpec spec,
    Object object,
    @Unmodifiable List<ConfigField> values
) {
    public void load() {
        if (!this.spec.isLoaded()) return;
        this.values.forEach(ConfigField::load);
    }
}
