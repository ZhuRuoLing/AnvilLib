package dev.anvilcraft.lib.v2.rendering.bloom;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.anvilcraft.lib.v2.rendering.ALRPipelines;
import dev.anvilcraft.lib.v2.rendering.ALRendering;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.client.pipeline.PipelineModifier;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class BloomPostEffect {
    public static final ResourceKey<PipelineModifier> REDIRECT_TO_BLOOM = ResourceKey.create(
        PipelineModifier.MODIFIERS_KEY,
        ALRendering.location("redirect_to_bloom")
    );

    public static final int UNIFORM_TRANSFORM_SIZE = new Std140SizeCalculator().putMat4f().get();
    public static final int UNIFORM_BLUR_SIZE = new Std140SizeCalculator().putVec2().get();
    public static final int UNIFORM_BLOOM_SIZE = new Std140SizeCalculator().putFloat().get();

    @Getter
    private final RenderTarget bloomInputTarget = new MainTarget(854, 480, false);
    private final RenderTarget bloomTempTarget = new TextureTarget("BloomTemp", 854, 480, false);
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final GpuDevice device = RenderSystem.getDevice();
    private final GpuBuffer uniformBuffer = device.createBuffer(
        () -> "BloomPostEffect UBO",
        GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_UNIFORM,
        UNIFORM_TRANSFORM_SIZE + UNIFORM_BLUR_SIZE + UNIFORM_BLOOM_SIZE
    );

    private final GpuBufferSlice transformUBO = uniformBuffer.slice(0, UNIFORM_TRANSFORM_SIZE);
    private final GpuBufferSlice blurUBO = uniformBuffer.slice(UNIFORM_TRANSFORM_SIZE, UNIFORM_BLUR_SIZE);
    private final GpuBufferSlice bloomUBO = uniformBuffer.slice(UNIFORM_TRANSFORM_SIZE + UNIFORM_BLUR_SIZE, UNIFORM_BLOOM_SIZE);
    private final GpuBuffer vertexBuffer = device.createBuffer(
        () -> "BloomPostEffect Vertex Buffer",
        GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_VERTEX,
        1024
    );

    private final GpuSampler inputSampler = device.createSampler(
        AddressMode.CLAMP_TO_EDGE,
        AddressMode.CLAMP_TO_EDGE,
        FilterMode.LINEAR,
        FilterMode.LINEAR,
        1,
        OptionalDouble.empty()
    );

    private final GpuSampler tempSampler = device.createSampler(
        AddressMode.CLAMP_TO_EDGE,
        AddressMode.CLAMP_TO_EDGE,
        FilterMode.LINEAR,
        FilterMode.LINEAR,
        1,
        OptionalDouble.empty()
    );

    private final GpuSampler mainSampler = device.createSampler(
        AddressMode.CLAMP_TO_EDGE,
        AddressMode.CLAMP_TO_EDGE,
        FilterMode.LINEAR,
        FilterMode.LINEAR,
        1,
        OptionalDouble.empty()
    );

    @Getter
    @Setter
    private float bloomIntensity;
    private int width;
    private int height;
    private int indexCount;

    public BloomPostEffect(float bloomIntensity) {
        this.bloomIntensity = bloomIntensity;
        Window window = Minecraft.getInstance().getWindow();
        this.width = window.getWidth();
        this.height = window.getHeight();
    }

    @SuppressWarnings("DataFlowIssue")
    public void beginFrame() {
        device.createCommandEncoder().clearColorAndDepthTextures(
            bloomInputTarget.getColorTexture(),
            0,
            bloomInputTarget.getDepthTexture(),
            0
        );
        device.createCommandEncoder().clearColorTexture(
            bloomTempTarget.getColorTexture(),
            0
        );
    }

    @SuppressWarnings("DataFlowIssue")
    public void process() {
        CommandEncoder commandEncoder = device.createCommandEncoder();

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            commandEncoder.writeToBuffer(transformUBO, Std140Builder.onStack(memoryStack, UNIFORM_TRANSFORM_SIZE).putMat4f(projectionMatrix).get());
            commandEncoder.writeToBuffer(bloomUBO, Std140Builder.onStack(memoryStack, UNIFORM_BLOOM_SIZE).putFloat(bloomIntensity).get());
        }

        blurOnce(commandEncoder, bloomInputTarget, bloomTempTarget, true);
        blurOnce(commandEncoder, bloomTempTarget, bloomInputTarget, true);
        blurOnce(commandEncoder, bloomInputTarget, bloomTempTarget, false);
        blurOnce(commandEncoder, bloomTempTarget, bloomInputTarget, false);
        bloomInputTarget.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        applyBloom(commandEncoder, bloomInputTarget, Minecraft.getInstance().getMainRenderTarget().getColorTextureView(), bloomTempTarget);
        commandEncoder.copyTextureToTexture(
            bloomTempTarget.getColorTexture(),
            Minecraft.getInstance().getMainRenderTarget().getColorTexture(),
            1,
            0,
            0,
            0,
            0,
            width,
            height
        );
        Minecraft.getInstance().getMainRenderTarget().copyDepthFrom(bloomInputTarget);
    }

    @SuppressWarnings("DataFlowIssue")
    private void applyBloom(
        CommandEncoder commandEncoder,
        RenderTarget inputTarget,
        GpuTextureView gameTexture,
        RenderTarget outputTarget
    ) {
        RenderPass bloomPass = commandEncoder.createRenderPass(
            () -> "BloomPostEffect BloomApply",
            outputTarget.getColorTextureView(),
            OptionalInt.of(0)
        );
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            commandEncoder.writeToBuffer(transformUBO, Std140Builder.onStack(memoryStack, UNIFORM_TRANSFORM_SIZE).putMat4f(projectionMatrix).get());
        }
        bloomPass.setPipeline(ALRPipelines.APPLY_BLOOM);
        bloomPass.setUniform("Transforms", transformUBO);
        bloomPass.setUniform("BloomParameters", bloomUBO);
        bloomPass.bindTexture("DiffuseSampler", inputTarget.getColorTextureView(), inputSampler);
        bloomPass.bindTexture("GameSampler", gameTexture, mainSampler);
        bloomPass.setVertexBuffer(0, vertexBuffer);
        RenderSystem.AutoStorageIndexBuffer sequentialBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        bloomPass.setIndexBuffer(sequentialBuffer.getBuffer(indexCount), sequentialBuffer.type());
        bloomPass.drawIndexed(0, 0, indexCount, 1);
        bloomPass.close();
    }

    @SuppressWarnings("DataFlowIssue")
    private void blurOnce(CommandEncoder commandEncoder, RenderTarget inputTarget, RenderTarget outputTarget, boolean horizontal) {
        RenderPass blurPass = commandEncoder.createRenderPass(
            () -> "BloomPostEffect Blur " + (horizontal ? "H" : "V"),
            outputTarget.getColorTextureView(),
            OptionalInt.of(0)
        );
        try (MemoryStack memoryStack = MemoryStack.stackPush();) {
            if (horizontal) {
                commandEncoder.writeToBuffer(blurUBO, Std140Builder.onStack(memoryStack, UNIFORM_BLUR_SIZE).putVec2(1, 0).get());
            } else {
                commandEncoder.writeToBuffer(blurUBO, Std140Builder.onStack(memoryStack, UNIFORM_BLUR_SIZE).putVec2(0, 1).get());
            }
        }
        blurPass.setPipeline(ALRPipelines.BLUR);
        blurPass.setUniform("Transforms", transformUBO);
        blurPass.setUniform("BlurParameters", blurUBO);
        blurPass.bindTexture("DiffuseSampler", inputTarget.getColorTextureView(), inputSampler);
        blurPass.setVertexBuffer(0, vertexBuffer);
        RenderSystem.AutoStorageIndexBuffer sequentialBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        blurPass.setIndexBuffer(sequentialBuffer.getBuffer(indexCount), sequentialBuffer.type());
        blurPass.drawIndexed(0, 0, indexCount, 1);
        blurPass.close();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        bloomInputTarget.resize(width, height);
        bloomTempTarget.resize(width, height);
        projectionMatrix.setOrtho(
            0, width, height, 0, 0.1f, 1000
        );

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.addVertex(0, 0, 10).setUv(0, 0);
        builder.addVertex(0, height, 10).setUv(0, 1);
        builder.addVertex(width, height, 10).setUv(1, 1);
        builder.addVertex(width, 0, 10).setUv(1, 0);

        MeshData data = builder.buildOrThrow();
        CommandEncoder commandEncoder = device.createCommandEncoder();
        commandEncoder.writeToBuffer(vertexBuffer.slice(), data.vertexBuffer());
        this.indexCount = data.drawState().indexCount();
        data.close();
    }

    public static RenderPipeline applyRedirect(RenderPipeline pipeline, Identifier name) {
        return pipeline;
    }
}
