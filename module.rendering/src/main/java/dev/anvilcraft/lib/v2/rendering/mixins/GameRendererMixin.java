package dev.anvilcraft.lib.v2.rendering.mixins;

import dev.anvilcraft.lib.v2.rendering.ALRPostEffects;
import dev.anvilcraft.lib.v2.rendering.bloom.BloomPostEffect;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
        method = "resize",
        at = @At("HEAD")
    )
    void onResize(int width, int height, CallbackInfo ci) {
        BloomPostEffect bloomPostEffect = ALRPostEffects.getBloomPostEffect();
        if (bloomPostEffect != null) {
            bloomPostEffect.resize(
                width,
                height
            );
        }
    }
}
