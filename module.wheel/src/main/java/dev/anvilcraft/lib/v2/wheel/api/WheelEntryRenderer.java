package dev.anvilcraft.lib.v2.wheel.api;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;

@FunctionalInterface
public interface WheelEntryRenderer {
    void render(GuiGraphicsExtractor graphics, Matrix3x2fStack pose, int width, int height);
}

