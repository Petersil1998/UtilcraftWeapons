package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.petersil98.utilcraft.items.ModItems;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.items.UtilcraftWeaponsItems;

public class Languages {

    public static English getEnglish(DataGenerator generator){
        return new English(generator);
    }

    public static German getGerman(DataGenerator generator){
        return new German(generator);
    }

    private static class English extends LanguageProvider {

        public English(DataGenerator generator) {
            super(generator, UtilcraftWeapons.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            add(UtilcraftWeaponsItems.ASSASSINS_KNIFE, "Assassin's Knife");
            add(UtilcraftWeaponsItems.SNIPER_RIFLE, "Sniper Rifle");
        }
    }

    private static class German extends LanguageProvider {

        public German(DataGenerator generator) {
            super(generator, UtilcraftWeapons.MOD_ID, "de_de");
        }

        @Override
        protected void addTranslations() {
        }
    }
}
