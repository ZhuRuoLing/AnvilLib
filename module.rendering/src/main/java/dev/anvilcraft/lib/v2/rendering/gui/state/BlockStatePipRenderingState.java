package dev.anvilcraft.lib.v2.rendering.gui.state;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

import static dev.anvilcraft.lib.v2.rendering.ALRSharedMath.SQRT_2;

public record BlockStatePipRenderingState(
    BlockState blockState,
    @Nullable BlockAndTintGetter level,
    @Nullable BlockPos blockPos,
    int x0,
    int y0,
    int x1,
    int y1,
    int color,
    boolean ambientOcclusion,
    PoseStack.Pose pose3D,
    Matrix3x2f pose,
    @Nullable ScreenRectangle scissorArea,
    @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {

    public BlockStatePipRenderingState(
        BlockState blockState,
        @Nullable BlockAndTintGetter level,
        @Nullable BlockPos blockPos,
        int x0,
        int y0,
        int x1,
        int y1,
        int color,
        boolean ambientOcclusion,
        PoseStack.Pose pose3D,
        Matrix3x2f pose,
        @Nullable ScreenRectangle scissorArea
    ) {
        this(
            blockState,
            level,
            blockPos,
            x0,
            y0,
            x1,
            y1,
            color,
            ambientOcclusion,
            pose3D,
            pose,
            scissorArea,
            PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea)
        );
    }

    @Override
    public float scale() {
        int width = x1 - x0;
        int height = y1 - y0;
        return Math.min(width / (1f + SQRT_2 / 2f), height / SQRT_2);
    }
}