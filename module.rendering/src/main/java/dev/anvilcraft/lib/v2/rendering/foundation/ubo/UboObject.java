package dev.anvilcraft.lib.v2.rendering.foundation.ubo;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.CommandEncoder;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public abstract class UboObject<T extends UboObject<T>> {

    protected abstract UboLayoutDefinition<T> getDefinition();

    @SuppressWarnings("unchecked")
    public void upload(CommandEncoder commandEncoder, GpuBufferSlice dest) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer malloc = getDefinition().write(memoryStack.malloc(getDefinition().size()), (T) this);
            commandEncoder.writeToBuffer(dest, malloc);
        }
    }
}
