package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.effects.UtilcraftWeaponsEffects;
import net.petersil98.utilcraft_weapons.items.UtilcraftWeaponsItems;

import javax.annotation.Nonnull;

public class Languages {

    @Nonnull
    public static English getEnglish(DataGenerator generator){
        return new English(generator);
    }

    @Nonnull
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
            add(UtilcraftWeaponsEffects.STEALTH, "Stealth");
            add(UtilcraftWeaponsItems.BULLET_ITEM,"Bullet");
            add(UtilcraftWeaponsItems.SMOKE_GRENADE, "Smoke Grenade");

            add(String.format("itemGroup.%s", UtilcraftWeapons.MOD_ID), "Utilcraft Weapons");

            add(String.format("%s.kill.sniper", UtilcraftWeapons.MOD_ID), "%s got sniped by %s");
            add(String.format("%s.kill.knife", UtilcraftWeapons.MOD_ID), "%s got stabbed by %s");
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
