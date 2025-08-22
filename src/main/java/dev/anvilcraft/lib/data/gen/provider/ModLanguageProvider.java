package dev.anvilcraft.lib.data.gen.provider;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.AnvilLibConfig;
import dev.anvilcraft.lib.config.ConfigData;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AnvilLib.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ConfigData.readConfigClass(this, AnvilLibConfig.class);
    }
}
