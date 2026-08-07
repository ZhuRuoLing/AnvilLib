package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.query;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.anvilcraft.lib.v2.rendering.ALRPipelines;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.ubo.FullTransformsUbo;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionKey;
import it.unimi.dsi.fastutil.objects.Reference2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;

class FrameState implements AutoCloseable {
    private final Map<OcclusionKey, GpuQueryObject> keySamplesMap = new IdentityHashMap<>();
    private final Reference2LongMap<OcclusionKey> results = new Reference2LongLinkedOpenHashMap<>();
    private final Set<OcclusionKey> keys = new HashSet<>();
    private final GpuQueryOcclusionCuller owner;

    public FrameState(GpuQueryOcclusionCuller owner) {
        this.owner = owner;
    }

    public void addKey(OcclusionKey key) {
        GpuQueryObject gpuSamplesQuery = this.keySamplesMap.get(key);
        if (gpuSamplesQuery == null) {
            this.keySamplesMap.put(key, owner.acquireQuery());
        }
    }

    public boolean shouldDraw(OcclusionKey key) {
        return results.getOrDefault(key, 0) > 0;
    }

    public void fetchResults() {
        for (Map.Entry<OcclusionKey, GpuQueryObject> entry : keySamplesMap.entrySet()) {
            results.put(entry.getKey(), entry.getValue().getValue());
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void runQueries(CameraRenderState camera) {
        QueryBufferPack bufferPack = this.owner.acquireBuffer();
        FullTransformsUbo transformsUbo = bufferPack.transformsUbo();

        transformsUbo.getProjMat().set(camera.projectionMatrix);

        CommandEncoder commandEncoder = this.owner.getCommandEncoder();
        RenderTarget target = Minecraft.getInstance().getMainRenderTarget();

        RenderSystem.AutoStorageIndexBuffer sequentialBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer buffer = sequentialBuffer.getBuffer(6 * 6);
        VertexFormat.IndexType type = sequentialBuffer.type();

        GpuDevice device = RenderSystem.getDevice();

        int instanceId = 0;

        ALRGpuDeviceExtension deviceExtension = (ALRGpuDeviceExtension) device;

        deviceExtension.alrPushDebugGroup(() -> "Gpu Occlusion Query Draw");

        for (Map.Entry<OcclusionKey, GpuQueryObject> entry : keySamplesMap.entrySet()) {
            int finalInstanceId = instanceId;

            OcclusionKey key = entry.getKey();
            GpuQueryObject query = entry.getValue();

            Matrix4f modelViewMat = transformsUbo.getModelViewMat();
            modelViewMat.set(camera.viewRotationMatrix);
            modelViewMat.translate(
                (float) -camera.pos.x,
                (float) -camera.pos.y,
                (float) -camera.pos.z
            );

            AABB boundingBox = key.getBoundingBox();

            Vector3f min = new Vector3f(
                (float) boundingBox.minX,
                (float) boundingBox.minY,
                (float) boundingBox.minZ
            );

            Vector3f max = new Vector3f(
                (float) boundingBox.maxX,
                (float) boundingBox.maxY,
                (float) boundingBox.maxZ
            );

            // ChatGPT can make mistakes. Check important info.
            Matrix4f transformation = new Matrix4f()
                .translate(min)
                .scale(
                    max.x - min.x,
                    max.y - min.y,
                    max.z - min.z
                );

            modelViewMat.mul(transformation);

            transformsUbo.upload(commandEncoder, bufferPack.transformsBuffer().slice());

            try (RenderPass renderPass = commandEncoder.createRenderPass(
                () -> "Gpu Occlusion Query Draw #" + finalInstanceId,
                target.getColorTextureView(),
                OptionalInt.empty(),
                target.getDepthTextureView(),
                OptionalDouble.empty()
            )) {
                renderPass.setPipeline(ALRPipelines.OCCLUSION_QUERY);

                renderPass.setVertexBuffer(0, bufferPack.vertexBuffer());
                renderPass.setIndexBuffer(buffer, type);

                renderPass.setUniform("Transforms", bufferPack.transformsBuffer());


                query.begin();
                renderPass.drawIndexed(0, 0, 6 * 6, 1);
                query.end();
            }
        }

        deviceExtension.alrPopDebugGroup();

        this.owner.releaseBuffer(bufferPack);
    }

    @Override
    public void close() {
        for (GpuQueryObject value : keySamplesMap.values()) {
            owner.releaseQuery(value);
        }
    }
}
