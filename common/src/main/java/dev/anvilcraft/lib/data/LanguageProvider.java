package dev.anvilcraft.lib.data;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public abstract class LanguageProvider implements DataProvider {
    protected final String namespace;
    protected final String languageCode;
    protected final PackOutput dataOutput;

    protected LanguageProvider(PackOutput dataOutput, String namespace, String languageCode) {
        this.namespace = namespace;
        this.languageCode = languageCode;
        this.dataOutput = dataOutput;
    }

    public abstract void add(TranslationBuilder builder);

    public LanguageProvider(PackOutput dataOutput, String namespace) {
        this(dataOutput, namespace, "en_us");
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        JsonObject object = new JsonObject();
        this.add(object::addProperty);
        return DataProvider.saveStable(output, object, this.getLangFilePath(this.languageCode));
    }

    protected @NotNull Path getLangFilePath(String code) {
        return this.dataOutput
            .createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang")
            .json(new ResourceLocation(this.namespace, code));
    }

    @Override
    public @NotNull String getName() {
        return "Language";
    }

    @FunctionalInterface
    public interface TranslationBuilder {
        void add(String translationKey, String value);
    }
}
