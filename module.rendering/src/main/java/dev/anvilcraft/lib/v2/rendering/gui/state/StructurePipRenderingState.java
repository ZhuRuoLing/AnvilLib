package dev.anvilcraft.lib.v2.rendering.gui.state;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

public record StructurePipRenderingState (
    BlockAndTintGetter structureAccess,
    BlockPos startPos,
    BlockPos endPos,
    float fx0,
    float fy0,
    float fx1,
    float fy1,
    float scale,
    boolean ambientOcclusion,
    PoseStack.Pose pose3D,
    Matrix3x2f pose,
    @Nullable ScreenRectangle scissorArea,
    @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {

    public StructurePipRenderingState(
        BlockAndTintGetter structureAccess,
        BlockPos startPos,
        BlockPos endPos,
        float x0,
        float y0,
        float x1,
        float y1,
        float scale,
        boolean ambientOcclusion,
        PoseStack.Pose pose3D,
        Matrix3x2f pose,
        @Nullable ScreenRectangle scissorArea
    ){
        this(
            structureAccess,
            startPos,
            endPos,
            x0,
            y0,
            x1,
            y1,
            scale,
            ambientOcclusion,
            pose3D,
            pose,
            scissorArea,
            PictureInPictureRenderState.getBounds(Mth.floor(x0), Mth.floor(y0), Mth.floor(x1), Mth.floor(y1), scissorArea)
        );
    }

    @Override
    public int x0() {
        return Mth.floor(fx0);
    }

    @Override
    public int y0() {
        return Mth.floor(fy0);
    }

    @Override
    public int x1() {
        return Mth.floor(fx1);
    }

    @Override
    public int y1() {
        return Mth.floor(fy1);
    }
}
