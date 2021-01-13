package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class GlobalLootModifiers extends GlobalLootModifierProvider {

    public GlobalLootModifiers(DataGenerator generator) {
        super(generator, UtilcraftWeapons.MOD_ID);
    }

    @Override
    protected void start() {

    }
}
