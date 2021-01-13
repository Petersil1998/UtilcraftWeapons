package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, UtilcraftWeapons.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
    }
}
