package dev.anvilcraft.lib.v2.test.client.tesr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.math.Axis;
import dev.anvilcraft.lib.v2.rendering.ALRendering;
import dev.anvilcraft.lib.v2.test.block.tile.TestBloomTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TestBloomTESR implements BlockEntityRenderer<TestBloomTile, TestBloomTESR.TestBloomRenderState> {

    public TestBloomTESR(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public TestBloomRenderState createRenderState() {
        return new TestBloomRenderState();
    }

    @Override
    public void submit(
        TestBloomRenderState testBloomRenderState,
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        CameraRenderState cameraRenderState
    ) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Minecraft minecraft = Minecraft.getInstance();
        poseStack.mulPose(Axis.YP.rotationDegrees((minecraft.level.getGameTime() + minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true)) * 2.25f));
        ItemStackRenderState renderState = new ItemStackRenderState();
        minecraft.getItemModelResolver().updateForTopItem(
            renderState,
            Items.CARROT.getDefaultInstance(),
            ItemDisplayContext.FIXED,
            minecraft.level,
            minecraft.player,
            42
        );
        renderState.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
        PoseStack.Pose pose = poseStack.last().copy();
        ALRendering.getBloomPostEffect().drawBloomed((nodeCollector, poseStack1) -> {
            poseStack1.pushPose();
            poseStack1.last().set(pose);
            renderState.submit(poseStack1, nodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
            poseStack1.popPose();
        });
        poseStack.popPose();
    }

    public static class TestBloomRenderState extends BlockEntityRenderState {

    }
}
