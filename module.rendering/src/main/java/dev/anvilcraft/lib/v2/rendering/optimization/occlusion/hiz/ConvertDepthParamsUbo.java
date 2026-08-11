package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz;

import dev.anvilcraft.lib.v2.rendering.foundation.buffers.layout.BufferLayout;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObject;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutDefinition;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutEntry;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.ShaderBufferObjectUsage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

/// ```glsl
/// layout(std140, binding = 0) uniform ConvertParam {
///     int uWidth;
///     int uHeight;
///     float uPadValue;
/// };
/// ```
@Getter
@Setter
@ApiStatus.Internal
public class ConvertDepthParamsUbo extends BufferObject<ConvertDepthParamsUbo> {

    public static final BufferObjectLayoutDefinition<ConvertDepthParamsUbo> DEFINITION = BufferObjectLayoutDefinition.create(
        BufferObjectLayoutEntry.<ConvertDepthParamsUbo>ofInt().forGetter(ConvertDepthParamsUbo::getHeight).build(),
        BufferObjectLayoutEntry.<ConvertDepthParamsUbo>ofInt().forGetter(ConvertDepthParamsUbo::getWidth).build(),
            BufferObjectLayoutEntry.<ConvertDepthParamsUbo>ofFloat().forGetter(ConvertDepthParamsUbo::getPadValue).build()
    );

    public static final int SIZE = DEFINITION.size(BufferLayout.STD140);

    private int width;
    private int height;
    private float padValue = 1f;

    protected ConvertDepthParamsUbo() {
        super(BufferLayout.STD140, ShaderBufferObjectUsage.UBO);
    }

    @Override
    protected BufferObjectLayoutDefinition<ConvertDepthParamsUbo> getDefinition() {
        return DEFINITION;
    }
}
