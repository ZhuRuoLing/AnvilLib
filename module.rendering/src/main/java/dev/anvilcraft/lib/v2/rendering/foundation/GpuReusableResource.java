package dev.anvilcraft.lib.v2.rendering.foundation;

public interface GpuReusableResource extends AutoCloseable {

    void acquire();

    void release();

    boolean isAcquired();

    @Override
    void close();
}
