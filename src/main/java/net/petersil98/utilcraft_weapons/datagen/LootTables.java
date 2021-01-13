package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.petersil98.utilcraft.blocks.sideslabs.SideSlabBlock;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {

    }

    private void addSimpleBlock(Block block) {
        lootTables.put(block, createSimpleTable(block));
    }

    private void addSimpleBlockWithName(Block block) {
        lootTables.put(block, createSimpleTableWithName(block));
    }

    private void addSlab(SlabBlock block) {
        lootTables.put(block, createSlabTable(block));
    }

    private void addSideSlab(SideSlabBlock block) {
        lootTables.put(block, createSideSlabTable(block));
    }

    private void addOreBlock(Block block, Item drop) {
        lootTables.put(block, createOreTable(block, drop));
    }

    private void addDoor(DoorBlock block) {
        lootTables.put(block, createDoorTable(block));
    }

    private void addLeave(LeavesBlock block, SaplingBlock sapling, boolean dropSticks, boolean dropApples) {
        lootTables.put(block, createLeaveTable(block, sapling, dropSticks, dropApples));
    }
}
