package dev.anvilcraft.lib.v2.wheel.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.anvilcraft.lib.v2.wheel.AnvilLibWheel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.Nullable;

@Slf4j
@EventBusSubscriber(modid = AnvilLibWheel.MOD_ID, value = Dist.CLIENT)
public class LibShaders {
    @Getter
    static @Nullable ShaderProgram ringShader;
    @Getter
    static @Nullable ShaderProgram selectionShader;

    @SubscribeEvent
    public static void register(RegisterShadersEvent event) {
        try {
            LibShaders.ringShader = new ShaderProgram(
                AnvilLibWheel.of("core/ring"),
                DefaultVertexFormat.POSITION_COLOR,
                ShaderDefines.EMPTY
            );
            event.registerShader(LibShaders.ringShader);
            LibShaders.selectionShader = new ShaderProgram(
                AnvilLibWheel.of("core/selection"),
                DefaultVertexFormat.POSITION_COLOR,
                ShaderDefines.EMPTY
            );
            event.registerShader(LibShaders.selectionShader);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
