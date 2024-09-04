package dev.anvilcraft.lib.data.file;

import dev.anvilcraft.lib.data.JsonSerializable;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@Getter
public abstract class ResourceFile implements JsonSerializable {
    protected final ResourceLocation location;

    public ResourceFile(ResourceLocation location) {
        this.location = location;
    }
}
