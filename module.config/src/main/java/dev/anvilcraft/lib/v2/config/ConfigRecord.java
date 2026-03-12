package dev.anvilcraft.lib.v2.config;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public record ConfigRecord(
    String modId,
    ModConfig.Type type,
    ModConfigSpec spec,
    Object object,
    @Unmodifiable List<ConfigField> values,
    AtomicBoolean registered
) {
    public ConfigRecord(
        String modId,
        ModConfig.Type type,
        ModConfigSpec spec,
        Object object,
        @Unmodifiable List<ConfigField> values
    ) {
        this(modId, type, spec, object, values, new AtomicBoolean(false));
    }

    public void load() {
        if (!this.spec.isLoaded()) return;
        this.values.forEach(ConfigField::load);
    }

    public String getFileName() {
        return "%s-%s.toml".formatted(this.modId, this.type.extension());
    }
}
