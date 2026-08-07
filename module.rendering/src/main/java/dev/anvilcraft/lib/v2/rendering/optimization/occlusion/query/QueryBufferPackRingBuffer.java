package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.query;

import dev.anvilcraft.lib.v2.rendering.foundation.GpuReusableResourceRingBuffer;

public class QueryBufferPackRingBuffer extends GpuReusableResourceRingBuffer<QueryBufferPack, QueryBufferPack.CreationContext> {
    public QueryBufferPackRingBuffer(QueryBufferPack.CreationContext context) {
        super(2, context);
    }

    @Override
    protected QueryBufferPack createInstance(QueryBufferPack.CreationContext context, int i) {
        return QueryBufferPack.newInstance(context, i);
    }
}
