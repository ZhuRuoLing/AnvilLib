package dev.anvilcraft.lib.v2.rendering;

import dev.anvilcraft.lib.v2.rendering.event.RegisterComputePipelinesEvent;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.pipeline.ALRComputePipeline;
import net.minecraft.client.renderer.ShaderDefines;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ALRComputePipelines {
    public static final ALRComputePipeline FFX_SPD_DOWNSAMPLE_PASS = ALRComputePipeline.builder()
        .withName(AnvilLibRendering.location("ffx_spd_downsample_pass"))
        .withShader(AnvilLibRendering.location("compute/ffx_spd_downsample_pass.csh"))
        .withDefines(
            ShaderDefines.builder()
                .define("FFX_SPD_OPTION_DOWNSAMPLE_FILTER", "2") // use max for HZB
                .define("FFX_SPD_OPTION_WAVE_INTEROP_LDS", ALROptions.SPD_OPTION_WAVE_INTEROP_LDS ? 0 : 1) // weird inverted
                .build()
        )
        .withTexture("r_input_downsample_src") // a sampler2DArray in shader, changes needed?
        .withReadWriteImage("rw_input_downsample_src_mid_mip") // a image2DArray in shader, changes needed?
        .withReadWriteImage("rw_input_downsample_src_mips") // an array of image2DArray in shader, changes needed? length=13
        .build();


    @SubscribeEvent
    public static void on(RegisterComputePipelinesEvent event) {
        event.registerPipeline(FFX_SPD_DOWNSAMPLE_PASS);
    }
}
