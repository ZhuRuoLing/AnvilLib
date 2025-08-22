package dev.anvilcraft.lib.config;

import com.google.gson.annotations.SerializedName;
import dev.anvilcraft.lib.util.FormattingUtil;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ConfigData {
    public static final String TITLE_STRING = "%s.configuration.title";
    public static final String TOML_STRING = "%s.configuration.section.%s.%s.toml";
    public static final String TOML_TITLE_STRING = "%s.configuration.section.%s.%s.toml.title";
    public static final String OPTION_STRING = "%s.configuration.%s";
    public static final String TOOLTIP_STRING = OPTION_STRING + ".tooltip";

    public static void readConfigClass(LanguageProvider provider, Class<?> configClass) {
        if (!configClass.isAnnotationPresent(Config.class)) return;
        Config config = configClass.getAnnotation(Config.class);
        String name = config.name();
        ModConfig.Type type = config.type();
        provider.add(TITLE_STRING.formatted(name), "%s Configuration".formatted(FormattingUtil.toEnglishName(name)));
        provider.add(
            TOML_STRING.formatted(name, name, type.extension()),
            "%s %s Configuration".formatted(FormattingUtil.toEnglishName(name), FormattingUtil.toEnglishName(type.extension()))
        );
        provider.add(
            TOML_TITLE_STRING.formatted(name, name, type.extension()),
            "%s %s Configuration".formatted(FormattingUtil.toEnglishName(name), FormattingUtil.toEnglishName(type.extension()))
        );
        ConfigData.readConfigClass(provider, name, type, configClass, null);
    }

    private static void readConfigClass(
        LanguageProvider provider,
        String modId,
        ModConfig.Type type,
        Class<?> configClass,
        @Nullable String parent
    ) {
        ConfigData.providerAdd(provider, modId, type, configClass.getDeclaredFields(), parent);
    }

    private static void providerAdd(LanguageProvider provider, String modId, ModConfig.Type type, Field[] fields, @Nullable String parent) {
        for (Field field : fields) {
            String fieldName = FormattingUtil.toLowerCaseUnder(field.getName());
            if (field.isAnnotationPresent(CollapsibleObject.class)) {
                ConfigData.readConfigClass(provider, modId, type, field.getType(), fieldName);
            }
            String name;
            if (field.isAnnotationPresent(SerializedName.class)) {
                name = field.getAnnotation(SerializedName.class).value();
            } else {
                name = FormattingUtil.toEnglishName(fieldName);
            }
            if (parent != null) fieldName = parent + "." + fieldName;
            provider.add(OPTION_STRING.formatted(modId, fieldName), name);
            if (field.isAnnotationPresent(Comment.class)) {
                Comment comment = field.getAnnotation(Comment.class);
                provider.add(TOOLTIP_STRING.formatted(modId, fieldName), comment.value());
            }
        }
    }
}
