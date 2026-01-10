package dev.anvilcraft.lib.recipe.data.gen.provider;

import dev.anvilcraft.lib.recipe.AnvilLibRecipe;
import dev.anvilcraft.lib.recipe.AnvilLibRecipeConfig;
import dev.anvilcraft.lib.config.ConfigData;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AnvilLibRecipe.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ConfigData.readConfigClass(this, AnvilLibRecipeConfig.class);
    }
}
