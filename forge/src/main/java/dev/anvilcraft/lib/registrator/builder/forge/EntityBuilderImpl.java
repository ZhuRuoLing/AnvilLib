package dev.anvilcraft.lib.registrator.builder.forge;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.builder.EntityBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class EntityBuilderImpl<T extends Entity> extends EntityBuilder<T> {
    protected EntityBuilderImpl(AbstractRegistrator registrator, String id, BiFunction<EntityType<T>, Level, T> factory, MobCategory category) {
        super(registrator, id, factory, category);
    }

    @NotNull
    public static <T extends Entity> EntityBuilder<T> create(AbstractRegistrator registrator, String id, BiFunction<EntityType<T>, Level, T> factory, MobCategory category) {
        return new EntityBuilderImpl<>(registrator, id, factory, category);
    }

    @Override
    protected void registerRenderer() {
        MinecraftForge.EVENT_BUS.addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        var renderer = this.renderer;
        if (renderer != null) {
            try {
                var provider = renderer.get();
                event.registerEntityRenderer(this.entry().get(), provider::apply);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to register renderer for Entity " + this.getId(), e);
            }
        }
    }

    @Override
    public EntityType<T> build() {
        EntityType<T> type = EntityType.Builder.of(factory::apply, this.category).build(this.id);
        this.entry.set(type);
        this.onRegister.accept(type);
        return type;
    }
}
