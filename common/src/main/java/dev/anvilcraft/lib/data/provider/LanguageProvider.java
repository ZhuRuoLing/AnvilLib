package dev.anvilcraft.lib.data.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LanguageProvider implements DataProvider {
    protected final String namespace;
    protected final String languageCode;
    protected final PackOutput dataOutput;
    protected final Map<String, String> translations = Collections.synchronizedMap(new HashMap<>());

    protected LanguageProvider(PackOutput dataOutput, String namespace, String languageCode) {
        this.namespace = namespace;
        this.languageCode = languageCode;
        this.dataOutput = dataOutput;
    }

    public static @NotNull LanguageProvider create(PackOutput dataOutput, String namespace, @NotNull String languageCode) {
        if (languageCode.equals("en_ud")) {
            return new UpsideDownLanguageProvider(dataOutput, namespace);
        }
        return new LanguageProvider(dataOutput, namespace, languageCode);
    }

    protected void add(@NotNull TranslationBuilder builder) {
        this.translations.forEach(builder::add);
    }

    public void add(String translationKey, String value) {
        if (value == null) this.translations.remove(translationKey);
        this.translations.put(translationKey, value);
    }

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
