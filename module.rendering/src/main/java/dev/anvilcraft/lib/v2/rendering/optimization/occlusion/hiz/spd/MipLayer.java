package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.spd;

import dev.anvilcraft.lib.v2.rendering.foundation.buffers.layout.BufferLayout;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObject;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutDefinition;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.ShaderBufferObjectUsage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MipLayer extends BufferObject<MipLayer> {

    private int width;
    private int height;

    protected MipLayer() {
        super(BufferLayout.STD430, ShaderBufferObjectUsage.SSBO);
    }

    @Override
    protected BufferObjectLayoutDefinition<MipLayer> getDefinition() {
        return null;
    }
}
