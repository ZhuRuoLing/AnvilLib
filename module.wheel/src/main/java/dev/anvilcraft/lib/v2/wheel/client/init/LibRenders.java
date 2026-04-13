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

public class LibRenders {

    public static final RenderPipeline.Snippet SNIPPET_COMMON = RenderPipeline.builder()
        .withUniform("ColorModulator", UniformType.VEC4)
        .withUniform("ModelViewMat", UniformType.MATRIX4X4)
        .withUniform("ProjMat", UniformType.MATRIX4X4)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .buildSnippet();

    public static final RenderPipeline RING_PIPELINE = RenderPipeline.builder(SNIPPET_COMMON)
        .withLocation(AnvilLibWheel.of("pipeline/ring"))
        .withVertexShader("core/position_color")
        .withFragmentShader(AnvilLibWheel.of("core/ring"))
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
        .withUniform("Center", UniformType.VEC2)
        .withUniform("InnerDiameter", UniformType.FLOAT)
        .withUniform("OuterDiameter", UniformType.FLOAT)
        .withUniform("AntiAliasingRadius", UniformType.FLOAT)
        .build();

    public static final @Nullable RenderPipeline SELECTION_PIPELINE = RenderPipeline.builder(SNIPPET_COMMON)
        .withLocation(AnvilLibWheel.of("pipeline/selection"))
        .withVertexShader("core/position_color")
        .withFragmentShader(AnvilLibWheel.of("core/selection"))
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
        .withUniform("Center", UniformType.VEC2)
        .withUniform("FramebufferSize", UniformType.VEC2)
        .withUniform("Radius", UniformType.FLOAT)
        .withUniform("AntiAliasingRadius", UniformType.FLOAT)
        .build();


    public static final RenderType RING = RenderType.create(
        "anvillib_ring",
        1536,
        false,
        true,
        LibRenders.RING_PIPELINE,
        RenderType.CompositeState.builder().createCompositeState(false)
    );

    public static final RenderType SELECTION = RenderType.create(
        "anvillib_selection",
        1536,
        false,
        true,
        LibRenders.SELECTION_PIPELINE,
        RenderType.CompositeState.builder().createCompositeState(false)
    );
}
