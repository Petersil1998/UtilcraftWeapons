package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator generator, BlockTags blockTags, ExistingFileHelper existingFileHelper) {
        super(generator, blockTags, UtilcraftWeapons.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
