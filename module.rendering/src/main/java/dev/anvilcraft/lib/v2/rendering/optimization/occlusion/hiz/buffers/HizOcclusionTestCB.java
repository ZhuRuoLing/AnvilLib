package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.buffers;

import dev.anvilcraft.lib.v2.rendering.foundation.buffers.layout.BufferLayout;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObject;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutDefinition;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutEntry;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.ShaderBufferObjectUsage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

/// ```glsl
/// layout(std430, binding = 0) uniform CB {
///     int elementCount;
///     int mipLevels;
///     vec2 viewportSize;
///     vec4 cameraPos;
///     mat4 ProjMat;
///     mat4 CameraMat;
/// } cbOcclusionTest;
/// ```
@Getter
@Setter
@ApiStatus.Internal
public class HizOcclusionTestCB extends BufferObject<HizOcclusionTestCB> {

    public static final BufferObjectLayoutDefinition<HizOcclusionTestCB> DEFINITION = BufferObjectLayoutDefinition.create(
        BufferObjectLayoutEntry.<HizOcclusionTestCB>ofInt().forGetter(HizOcclusionTestCB::getElementCount).build(),
        BufferObjectLayoutEntry.<HizOcclusionTestCB>ofInt().forGetter(HizOcclusionTestCB::getMipLevels).build(),
        BufferObjectLayoutEntry.<HizOcclusionTestCB>ofVec2f().forGetter(HizOcclusionTestCB::getViewportSize).build(),
        BufferObjectLayoutEntry.<HizOcclusionTestCB>ofVec4f().forGetter(HizOcclusionTestCB::getCameraPos).build(),
        BufferObjectLayoutEntry.<HizOcclusionTestCB>ofMat4f().forGetter(HizOcclusionTestCB::getProjMat).build(),
        BufferObjectLayoutEntry.<HizOcclusionTestCB>ofMat4f().forGetter(HizOcclusionTestCB::getCameraMat).build()
    );

    public static final int SIZE = DEFINITION.size(BufferLayout.STD430);

    private int elementCount;
    private int mipLevels;
    private Vector2f viewportSize = new Vector2f();
    private Vector4f cameraPos = new Vector4f(0, 0, 0, 1);
    private Matrix4f projMat = new Matrix4f();
    private Matrix4f cameraMat = new Matrix4f();
    public HizOcclusionTestCB() {
        super(BufferLayout.STD430, ShaderBufferObjectUsage.UBO);
    }

    @Override
    protected BufferObjectLayoutDefinition<HizOcclusionTestCB> getDefinition() {
        return DEFINITION;
    }

}
