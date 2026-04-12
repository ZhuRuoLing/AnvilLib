package dev.anvilcraft.lib.v2.rendering.test;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.anvilcraft.lib.v2.rendering.ALRendering;
import dev.anvilcraft.lib.v2.rendering.bloom.BloomPostEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.HashSet;
import java.util.Set;

public class ALRTest {


    public static void renderCarrotBloomed() {
        if (!ALRendering.DEBUG) return;
        renderCarrot();
        ALRendering.getBloomPostEffect().beginBloomDraw();
        renderCarrot();
        ALRendering.getBloomPostEffect().endBloomDraw();
    }

    public static void renderCarrot() {
        if (Minecraft.getInstance() == null || Minecraft.getInstance().level == null) return;
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(-0.1, 0, 0.28);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        ItemModel model = minecraft.getModelManager().getItemModel(Identifier.withDefaultNamespace("carrot"));
        if (model instanceof CuboidItemModelWrapper wrapper) {
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            RenderSystem.pushPipelineModifier(BloomPostEffect.REDIRECT_TO_BLOOM);
            SubmitNodeStorage.ItemSubmit submit = new SubmitNodeStorage.ItemSubmit(
                poseStack.last(),
                ItemDisplayContext.FIXED,
                15728880,
                OverlayTexture.NO_OVERLAY,
                0,
                ItemStackRenderState.LayerRenderState.EMPTY_TINTS,
                wrapper.quads.getAll(),
                ItemStackRenderState.FoilType.NONE
            );
            Set<RenderType> uniqueValues = new HashSet<>();
            for (BakedQuad it : submit.quads()) {
                RenderType renderType = it.materialInfo().itemRenderType();
                uniqueValues.add(renderType);
            }

            minecraft.gameRenderer
                .getFeatureRenderDispatcher()
                .itemFeatureRenderer
                .renderItem(
                    bufferSource,
                    minecraft.renderBuffers().outlineBufferSource(),
                    submit
                );

            uniqueValues.forEach(bufferSource::endBatch);
            poseStack.popPose();
            RenderSystem.popPipelineModifier();
        }
    }
}
