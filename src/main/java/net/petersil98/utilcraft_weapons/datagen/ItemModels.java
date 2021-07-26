package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.petersil98.utilcraft_weapons.utils.BlockItemUtils;
import net.petersil98.utilcraft_weapons.UtilcraftWeapons;
import net.petersil98.utilcraft_weapons.items.UtilcraftWeaponsItems;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, UtilcraftWeapons.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerHandheld(UtilcraftWeaponsItems.SNIPER_RIFLE);
        registerHandheld(UtilcraftWeaponsItems.SMOKE_GRENADE);
    }

    private void registerHandheld(Item item) {
        ResourceLocation location = new ResourceLocation(BlockItemUtils.namespace(item), ITEM_FOLDER +"/"+BlockItemUtils.name(item));
        singleTexture(BlockItemUtils.name(item), mcLoc(ITEM_FOLDER+"/handheld"), "layer0", location);
    }

    private void registerGeneratedItem(Block block, Block realItem) {
        ResourceLocation location = new ResourceLocation(BlockItemUtils.namespace(realItem), ITEM_FOLDER +"/"+BlockItemUtils.name(realItem));
        singleTexture(BlockItemUtils.name(block), mcLoc(ITEM_FOLDER+"/generated"), "layer0", location);
    }

    private void registerGeneratedBlock(Block block, Block realItem) {
        ResourceLocation location = new ResourceLocation(BlockItemUtils.namespace(realItem), BLOCK_FOLDER +"/"+BlockItemUtils.name(realItem));
        singleTexture(BlockItemUtils.name(block), mcLoc(ITEM_FOLDER+"/generated"), "layer0", location);
    }

    private void registerBlockItem(Block block) {
        registerBlockItem(block, "");
    }

    private void registerBlockItem(Block block, String key) {
        ResourceLocation location = new ResourceLocation(BlockItemUtils.namespace(block), BLOCK_FOLDER +"/"+BlockItemUtils.name(block)+key);
        withExistingParent(BlockItemUtils.name(block), location);
    }
}
