package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.query;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.anvilcraft.lib.v2.rendering.foundation.GpuReusableResource;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.ubo.FullTransformsUbo;

public final class QueryBufferPack implements GpuReusableResource {

    /// each bounding box 3 * 4 * 4 * 6 byte = 288 byte,
    /// total 4096 bounding boxes = 1.1 MiB
    ///
    /// no that's shit,
    /// use single unit box and model view matrix instead
    /// ((3 float * 4) for each quad * 6) for cube * 1
    public static final int DEFAULT_VERTEX_BUFFER_SIZE = 3 * 4 * 4 * 6;

    private final GpuBuffer vertexBuffer;
    private final GpuBuffer transformsBuffer;
    private final FullTransformsUbo transformsUbo;

    private boolean closed = false;
    private boolean acquired = false;

    public QueryBufferPack(
        GpuBuffer vertexBuffer,
        GpuBuffer transformsBuffer,
        FullTransformsUbo transformsUbo
    ) {
        this.vertexBuffer = vertexBuffer;
        this.transformsBuffer = transformsBuffer;
        this.transformsUbo = transformsUbo;
    }


    public static QueryBufferPack newInstance(CreationContext context, int index) {
        QueryBufferPack bufferPack = new QueryBufferPack(
            context.device.createBuffer(
                () -> "Gpu Occlusion Query Vertex Buffer #" + index,
                GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST,
                DEFAULT_VERTEX_BUFFER_SIZE
            ),
            context.device.createBuffer(
                () -> "Gpu Occlusion Query Uniform Buffer #" + index,
                GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
                FullTransformsUbo.SIZE
            ),
            new FullTransformsUbo()
        );

        prepareMesh(context, bufferPack);

        return bufferPack;
    }

    private static void prepareMesh(CreationContext context, QueryBufferPack bufferPack) {
        float x0 = 0;
        float y0 = 0;
        float z0 = 0;
        float x1 = 1;
        float y1 = 1;
        float z1 = 1;

        BufferBuilder bufferBuilder = Tesselator.getInstance()
            .begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION
            );

        // z+
        bufferBuilder.addVertex(x0, y0, z1);
        bufferBuilder.addVertex(x1, y0, z1);
        bufferBuilder.addVertex(x1, y1, z1);
        bufferBuilder.addVertex(x0, y1, z1);

        // z-
        bufferBuilder.addVertex(x0, y0, z0);
        bufferBuilder.addVertex(x0, y1, z0);
        bufferBuilder.addVertex(x1, y1, z0);
        bufferBuilder.addVertex(x1, y0, z0);

        // x+
        bufferBuilder.addVertex(x1, y0, z0);
        bufferBuilder.addVertex(x1, y0, z1);
        bufferBuilder.addVertex(x1, y1, z1);
        bufferBuilder.addVertex(x1, y1, z0);

        // x-
        bufferBuilder.addVertex(x0, y0, z0);
        bufferBuilder.addVertex(x0, y1, z0);
        bufferBuilder.addVertex(x0, y1, z1);
        bufferBuilder.addVertex(x0, y0, z1);

        // y+
        bufferBuilder.addVertex(x0, y1, z0);
        bufferBuilder.addVertex(x0, y1, z1);
        bufferBuilder.addVertex(x1, y1, z1);
        bufferBuilder.addVertex(x1, y1, z0);

        // y-
        bufferBuilder.addVertex(x0, y0, z0);
        bufferBuilder.addVertex(x1, y0, z0);
        bufferBuilder.addVertex(x1, y0, z1);
        bufferBuilder.addVertex(x0, y0, z1);

        MeshData orThrow = bufferBuilder.buildOrThrow();

        context.commandEncoder.writeToBuffer(bufferPack.vertexBuffer.slice(), orThrow.vertexBuffer());

        orThrow.close();
    }

    @Override
    public void acquire() {
        this.acquired = true;
    }

    @Override
    public void release() {
        this.acquired = false;
    }

    @Override
    public boolean isAcquired() {
        return this.acquired;
    }

    @Override
    public void close() {
        if (!closed) {
            this.vertexBuffer.close();
            this.transformsBuffer.close();
            this.closed = true;
        }
    }

    public GpuBuffer vertexBuffer() {
        return vertexBuffer;
    }

    public GpuBuffer transformsBuffer() {
        return transformsBuffer;
    }

    public FullTransformsUbo transformsUbo() {
        return transformsUbo;
    }

    public record CreationContext(
        CommandEncoder commandEncoder,
        GpuDevice device
    ) {
    }
}
