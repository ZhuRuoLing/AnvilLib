package dev.anvilcraft.lib.v2.rendering.mixins;

import com.mojang.blaze3d.platform.Window;
import dev.anvilcraft.lib.v2.rendering.ALRendering;
import dev.anvilcraft.lib.v2.rendering.bloom.BloomPostEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Final
    private Window window;

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void onCreateInstance(GameConfig gameConfig, CallbackInfo ci) {
        ALRendering.createPipelines();
    }

    @Inject(
        method = "resizeGui",
        at = @At("RETURN")
    )
    private void onResizeGui(CallbackInfo ci) {
        BloomPostEffect bloomPostEffect = ALRendering.getBloomPostEffect();
        if (bloomPostEffect != null) {
            bloomPostEffect.resize(
                this.window.getWidth(),
                this.window.getHeight()
            );
        }
    }
}
