package dev.anvilcraft.lib.data.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpsideDownLanguageProvider extends LanguageProvider {
    public UpsideDownLanguageProvider(PackOutput dataOutput, String namespace) {
        super(dataOutput, namespace, "en_ud");
    }

    @Override
    public @NotNull String getName() {
        return "UpsideDownLanguage";
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        JsonObject object = new JsonObject();
        this.add((translationKey, value) -> object.addProperty(translationKey, upsideDown(value)));
        return DataProvider.saveStable(output, object, this.getLangFilePath(this.languageCode));
    }

    private static @NotNull String upsideDown(@NotNull String text) {
        ArrayList<String> result = new ArrayList<>();
        String placeholder = "%s|%\\d+\\$s";
        final Pattern pattern = Pattern.compile(placeholder);
        final Matcher matcher = pattern.matcher(text);
        int lastEnd = 0;
        while (matcher.find()) {
            if (lastEnd < matcher.start()) {
                result.add(text.substring(lastEnd, matcher.start()));
            }
            result.add(matcher.group());
            lastEnd = matcher.end();
        }
        if (lastEnd < text.length()) {
            result.add(text.substring(lastEnd));
        }
        for (int i = 0; i < result.size(); i++) {
            String str = result.get(i);
            if (str.matches(placeholder)) continue;
            StringBuilder builder = new StringBuilder();
            for (int j = str.length() - 1; j >= 0; j--) {
                builder.append(getUpsideDown(str.charAt(j)));
            }
            result.set(i, builder.toString());
        }
        StringBuilder resultStr = new StringBuilder();
        for (int i = result.size() - 1; i >= 0; i--) {
            resultStr.append(result.get(i));
        }
        return resultStr.toString();
    }

    private static char getUpsideDown(char c) {
        String raw = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789!@#$%^&*()/\\_?[]{}<>'";
        String upside = ",<>{}[]¿‾\\/()*&^%$#@¡68ㄥ9ϛㄣƐᄅƖzʎxʍʌnʇsɹbdouɯןʞظıɥbɟǝpɔqɐZʎXMΛ∩⟘SᴚὉԀONWꞀʞſIH⅁ℲƎᗡƆᗺⱯ";
        int index = raw.indexOf(c);
        if (index != -1) {
            return upside.charAt(upside.length() - 1 - index);
        } else return c;
    }
}
