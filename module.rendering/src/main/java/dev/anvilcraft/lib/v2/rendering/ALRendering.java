package dev.anvilcraft.lib.v2.rendering;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.anvilcraft.lib.v2.rendering.bloom.BloomPostEffect;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.pipeline.RegisterPipelineModifiersEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(value = ALRendering.MODID, dist = Dist.CLIENT)
@EventBusSubscriber
public class ALRendering {
    public static final boolean DEBUG = System.getProperty("anvillib.rendering.debugMode") != null;

    public static final String MODID = "anvillib_rendering";
    private static final Logger logger = LoggerFactory.getLogger("anvillib_rendering");
    @Getter
    private static BloomPostEffect bloomPostEffect;


    public ALRendering(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(ALRendering::onGuiPost);
    }

    public static Identifier location(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public static void createPipelines() {
        bloomPostEffect = new BloomPostEffect(0.5f);
        logger.info("Created pipelines");
    }

    @SubscribeEvent
    public static void on(RegisterPipelineModifiersEvent event) {
        event.register(BloomPostEffect.REDIRECT_TO_BLOOM, BloomPostEffect::applyRedirect);
    }

    private static void onGuiPost(RenderGuiEvent.Post event) {




    }
}
