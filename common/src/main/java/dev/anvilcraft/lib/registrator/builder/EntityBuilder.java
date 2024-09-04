package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import dev.anvilcraft.lib.util.Side;
import dev.anvilcraft.lib.util.SideExecutor;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class EntityBuilder<T extends Entity> extends EntryBuilder<EntityType<T>> {
    protected final BiFunction<EntityType<T>, Level, T> factory;
    protected final MobCategory category;
    protected final RegistryEntry<EntityType<T>> entry = new RegistryEntry<>() {
    };
    protected Consumer<EntityType<T>> onRegister = t -> {
    };
    protected Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer = null;

    protected EntityBuilder(AbstractRegistrator registrator, String id, BiFunction<EntityType<T>, Level, T> factory, MobCategory category) {
        super(registrator, id);
        this.factory = factory;
        this.category = category;
        this.lang(toTitleCase(this.id));
    }

    @ExpectPlatform
    public static <T extends Entity> @NotNull EntityBuilder<T> create(AbstractRegistrator registrator, String id, BiFunction<EntityType<T>, Level, T> factory, MobCategory category) {
        throw new AssertionError();
    }

    @SuppressWarnings("UnusedReturnValue")
    public EntityBuilder<T> lang(String name) {
        this.registrator.lang(Util.makeDescriptionId("entity", this.getId()), name);
        return this;
    }

    protected abstract void registerRenderer();

    public EntityBuilder<T> renderer(Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer) {
        if (this.renderer == null) SideExecutor.execute(Side.CLIENT, () -> this::registerRenderer);
        this.renderer = renderer;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public EntityBuilder<T> onRegister(Consumer<EntityType<T>> consumer) {
        this.onRegister = consumer;
        return this;
    }

    @Override
    public abstract EntityType<T> build();

    @Override
    public RegistryEntry<EntityType<T>> register() {
        this.registrator.addBuilder(BuiltInRegistries.ENTITY_TYPE, this);
        return this.entry;
    }

    @Override
    public RegistryEntry<EntityType<T>> entry() {
        return this.entry;
    }
}
