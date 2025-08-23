package dev.anvilcraft.lib.recipe.trigger;

import com.mojang.serialization.Codec;
import dev.anvilcraft.lib.init.LibRegistries;
import dev.anvilcraft.lib.recipe.util.IPrioritized;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

/**
 * 配方触发器接口，用于定义配方的触发条件
 * 实现该接口的类表示一种可以触发配方执行的条件
 */
public interface IRecipeTrigger extends IPrioritized {
    Codec<IRecipeTrigger> CODEC = LibRegistries.TRIGGER_REGISTRY.byNameCodec();

    /**
     * 获取配方触发器的ID
     *
     * @return ID
     */
    default @Nullable ResourceLocation getId() {
        return LibRegistries.TRIGGER_REGISTRY.getKey(this);
    }

    record Impl(ResourceLocation id) implements IRecipeTrigger {
        @Override
        public ResourceLocation getId() {
            return this.id();
        }
    }
}
