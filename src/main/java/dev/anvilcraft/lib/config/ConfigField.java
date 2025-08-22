package dev.anvilcraft.lib.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;

public record ConfigField(
    Object object,
    Field field,
    ModConfigSpec.ConfigValue<?> value
) {
    public void load() {
        boolean isFinal = field.accessFlags().contains(AccessFlag.FINAL);
        boolean isStatic = field.accessFlags().contains(AccessFlag.STATIC);
        boolean isPublic = field.accessFlags().contains(AccessFlag.PUBLIC);
        if (isFinal || !isPublic) return;
        try {
            if (isStatic) {
                field.set(null, value.get());
            } else {
                field.set(object, value.get());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
