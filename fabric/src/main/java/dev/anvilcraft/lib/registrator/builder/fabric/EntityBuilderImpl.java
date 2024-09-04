package dev.anvilcraft.lib.registrator.builder.fabric;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.builder.EntityBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
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
        this.onRegister(entry -> {
            try {
                EntityRendererRegistry.register(entry, renderer.get()::apply);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to register renderer for Entity " + this.getId(), e);
            }
        });
    }

    @Override
    public EntityType<T> build() {
        EntityType<T> type = FabricEntityTypeBuilder.create(this.category, this.factory::apply).build();
        this.entry.set(type);
        this.onRegister.accept(type);
        return type;
    }
}
