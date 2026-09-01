package dev.anvilcraft.lib.v2.rendering.extension.blaze3d.texture.gl.bindless;

import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.texture.bindless.TextureHandle;

public record GlTextureHandle(long handleId, boolean isTexture) implements TextureHandle {
}
