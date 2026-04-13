package dev.anvilcraft.lib.v2.wheel.client.init;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.anvilcraft.lib.v2.wheel.AnvilLibWheel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class LibRenders {
    public static final RenderPipeline.Snippet WHEEL_SNIPPET = RenderPipeline.builder(RenderPipelines.MATRICES_COLOR_SNIPPET)
        .withUniform("Center", UniformType.VEC2)
        .withUniform("InnerDiameter", UniformType.FLOAT)
        .withUniform("OuterDiameter", UniformType.FLOAT)
        .withUniform("AntiAliasingRadius", UniformType.FLOAT)
        .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
        .withBlend(BlendFunction.OVERLAY)
        .buildSnippet();

    @Getter
    private static final @Nullable RenderPipeline RING_PIPELINE = RenderPipeline.builder(LibRenders.WHEEL_SNIPPET)
        .withLocation("pipeline/stars")
        .withVertexShader("core/position_color")
        .withFragmentShader(AnvilLibWheel.of("core/ring"))
        .withBlend(BlendFunction.OVERLAY)
        .withDepthWrite(false)
        .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
        .build();

    @Getter
    private static final @Nullable RenderPipeline SELECTION_PIPELINE = RenderPipeline.builder(LibRenders.WHEEL_SNIPPET)
        .withLocation("pipeline/stars")
        .withVertexShader("core/position_color")
        .withFragmentShader(AnvilLibWheel.of("core/selection"))
        .withBlend(BlendFunction.OVERLAY)
        .withDepthWrite(false)
        .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
        .build();

    @Getter
    private static final RenderType RING = RenderType.create(
        "anvillib_ring",
        1536,
        false,
        true,
        LibRenders.RING_PIPELINE,
        RenderType.CompositeState.builder().createCompositeState(false)
    );

    @Getter
    private static final RenderType SELECTION = RenderType.create(
        "anvillib_selection",
        1536,
        false,
        true,
        LibRenders.SELECTION_PIPELINE,
        RenderType.CompositeState.builder().createCompositeState(false)
    );
}
