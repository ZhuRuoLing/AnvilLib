package dev.anvilcraft.lib.v2.rendering.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.anvilcraft.lib.v2.rendering.ALRendering;
import dev.anvilcraft.lib.v2.rendering.bloom.BloomPostEffect;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public class GameRendererMixin {
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;endFrame()V"))
    void beforeEndFrame(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        if (Minecraft.getInstance() == null || Minecraft.getInstance().level == null)return;
        ALRendering.getBloomPostEffect().beginFrame();
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.scale(0.2f, 0.2f, 0.2f);
        ItemModel model = minecraft.getModelManager().getItemModel(Identifier.withDefaultNamespace("carrot"));
        if (model instanceof CuboidItemModelWrapper wrapper) {
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            RenderSystem.pushPipelineModifier(BloomPostEffect.REDIRECT_TO_BLOOM);
            minecraft
                .gameRenderer
                .getFeatureRenderDispatcher()
                .itemFeatureRenderer
                .renderItem(
                    bufferSource,
                    minecraft.renderBuffers().outlineBufferSource(),
                    new SubmitNodeStorage.ItemSubmit(
                        poseStack.last(),
                        ItemDisplayContext.FIXED,
                        15728880,
                        OverlayTexture.NO_OVERLAY,
                        -1,
                        ItemStackRenderState.LayerRenderState.EMPTY_TINTS,
                        wrapper.quads.getAll(),
                        ItemStackRenderState.FoilType.NONE
                    )
                );
            bufferSource.endLastBatch();
            poseStack.popPose();
            RenderSystem.popPipelineModifier();
        }
    }
}
