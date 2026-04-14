package dev.anvilcraft.lib.v2.rendering.bloom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

public interface BloomRenderCallback {
    void render(SubmitNodeCollector nodeCollector, PoseStack poseStack);
}
