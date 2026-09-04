package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.buffers;

import dev.anvilcraft.lib.v2.rendering.foundation.buffers.layout.BufferLayout;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObject;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutDefinition;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutEntry;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.BufferObjectLayoutEntryType;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.object.ShaderBufferObjectUsage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2i;

import java.util.Objects;

/// ```glsl
/// layout(std430, binding = 0) buffer ShaderInput {
///     ivec2 mipLayers[MAX_MIP_LEVELS + 1];
///     AABB aabbs[];
/// };
/// ```
@Getter
@Setter
@ApiStatus.Internal
public class HizOcclusionTestSSBO extends BufferObject<HizOcclusionTestSSBO> {

    public static final int MAX_MIP_LEVELS = 12;
    public static final int MIP_LAYER_COUNT = MAX_MIP_LEVELS + 1;

    private Vector2i[] mipLayers = createDefaultMipLayers();
    private Aabb[] aabbs = new Aabb[0];

    public HizOcclusionTestSSBO() {
        super(BufferLayout.STD430, ShaderBufferObjectUsage.SSBO);
    }

    public void setMipLayers(Vector2i[] mipLayers) {
        Objects.requireNonNull(mipLayers, "mipLayers");
        if (mipLayers.length != MIP_LAYER_COUNT) {
            throw new IllegalArgumentException("mipLayers must contain " + MIP_LAYER_COUNT + " entries");
        }
        for (Vector2i mipLayer : mipLayers) {
            Objects.requireNonNull(mipLayer, "mipLayers element");
        }
        this.mipLayers = mipLayers;
    }

    public void setAabbs(Aabb[] aabbs) {
        Objects.requireNonNull(aabbs, "aabbs");
        for (Aabb aabb : aabbs) {
            Objects.requireNonNull(aabb, "aabbs element");
        }
        this.aabbs = aabbs;
    }

    @Override
    protected BufferObjectLayoutDefinition<HizOcclusionTestSSBO> getDefinition() {
        return BufferObjectLayoutDefinition.create(
            BufferObjectLayoutEntry
                .<Vector2i[], HizOcclusionTestSSBO>builder(
                    BufferObjectLayoutEntryType.createIVec2Array(MIP_LAYER_COUNT)
                )
                .forGetter(HizOcclusionTestSSBO::getMipLayers)
                .build(),
            BufferObjectLayoutEntry
                .<Aabb[], HizOcclusionTestSSBO>builder(
                    BufferObjectLayoutEntryType.createStructArray(Aabb.DEFINITION, this.aabbs.length)
                )
                .forGetter(HizOcclusionTestSSBO::getAabbs)
                .build()
        );
    }

    private static Vector2i[] createDefaultMipLayers() {
        Vector2i[] mipLayers = new Vector2i[MIP_LAYER_COUNT];
        for (int i = 0; i < mipLayers.length; i++) {
            mipLayers[i] = new Vector2i();
        }
        return mipLayers;
    }
}
