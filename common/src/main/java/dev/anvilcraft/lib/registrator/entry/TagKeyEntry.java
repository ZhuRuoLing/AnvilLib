package dev.anvilcraft.lib.registrator.entry;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public final class TagKeyEntry<T> extends RegistryEntry<TagKey<T>> {
    private TagKey<T> self = null;
    private final AbstractRegistrator registrar;
    private final ResourceKey<? extends Registry<T>> registry;
    @NotNull
    private final String defaultPath;
    private final String forgePath;

    private TagKeyEntry(AbstractRegistrator registrar, ResourceKey<? extends Registry<T>> registry, @NotNull String defaultPath, String forgePath) {
        this.registrar = registrar;
        this.registry = registry;
        this.defaultPath = defaultPath;
        this.forgePath = forgePath;
    }

    @NotNull
    public static <T> TagKeyEntry<T> create(AbstractRegistrator registrar, ResourceKey<? extends Registry<T>> registry, @NotNull String fabricPath, String forgePath) {
        return new TagKeyEntry<>(registrar, registry, fabricPath, forgePath);
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public @NotNull TagKey<T> get() {
        if (this.self != null) return this.self;
        String path = switch (AnvilLib.getPlatform()) {
            case FORGE -> this.forgePath != null ? this.forgePath : this.defaultPath;
            default -> this.defaultPath;
        };
        ResourceLocation location = this.registrar != null ?
            this.registrar.of(path) :
            switch (AnvilLib.getPlatform()) {
                case FORGE -> new ResourceLocation("forge", path);
                default -> new ResourceLocation("c", path);
            };
        return this.self = TagKey.create(this.registry, location);
    }
}
