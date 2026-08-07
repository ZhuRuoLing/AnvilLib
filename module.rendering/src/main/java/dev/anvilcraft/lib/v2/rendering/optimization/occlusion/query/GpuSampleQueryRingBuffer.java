package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.query;

import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;
import dev.anvilcraft.lib.v2.rendering.foundation.GpuReusableResourceRingBuffer;
import org.jspecify.annotations.NonNull;

public class GpuSampleQueryRingBuffer extends GpuReusableResourceRingBuffer<GpuQueryObject, ALRGpuDeviceExtension> {
    public GpuSampleQueryRingBuffer(ALRGpuDeviceExtension context) {
        super(16, context);
    }

    @Override
    @NonNull
    protected GpuQueryObject createInstance(@NonNull ALRGpuDeviceExtension context, int i) {
        return context.alrCreateSamplesQuery();
    }
}
