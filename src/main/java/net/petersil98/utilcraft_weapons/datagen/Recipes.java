package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.petersil98.utilcraft_weapons.items.SmokeGrenade;
import net.petersil98.utilcraft_weapons.items.UtilcraftWeaponsItems;
import net.petersil98.utilcraft_weapons.utils.BlockItemUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    private static final List<ColorEntry> COLORS = new ArrayList<>(DyeColor.values().length);


    public Recipes(DataGenerator generator) {
        super(generator);
        COLORS.add(new ColorEntry("white", DyeColor.WHITE, Tags.Items.DYES_WHITE));
        COLORS.add(new ColorEntry("orange", DyeColor.ORANGE, Tags.Items.DYES_ORANGE));
        COLORS.add(new ColorEntry("magenta", DyeColor.MAGENTA, Tags.Items.DYES_MAGENTA));
        COLORS.add(new ColorEntry("light_blue", DyeColor.LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE));
        COLORS.add(new ColorEntry("yellow", DyeColor.YELLOW, Tags.Items.DYES_YELLOW));
        COLORS.add(new ColorEntry("lime", DyeColor.LIME, Tags.Items.DYES_LIME));
        COLORS.add(new ColorEntry("pink", DyeColor.PINK, Tags.Items.DYES_PINK));
        COLORS.add(new ColorEntry("gray", DyeColor.GRAY, Tags.Items.DYES_GRAY));
        COLORS.add(new ColorEntry("light_gray", DyeColor.LIGHT_GRAY, Tags.Items.DYES_LIGHT_GRAY));
        COLORS.add(new ColorEntry("cyan", DyeColor.CYAN, Tags.Items.DYES_CYAN));
        COLORS.add(new ColorEntry("purple", DyeColor.PURPLE, Tags.Items.DYES_PURPLE));
        COLORS.add(new ColorEntry("blue", DyeColor.BLUE, Tags.Items.DYES_BLUE));
        COLORS.add(new ColorEntry("brown", DyeColor.BROWN, Tags.Items.DYES_BROWN));
        COLORS.add(new ColorEntry("green", DyeColor.GREEN, Tags.Items.DYES_GREEN));
        COLORS.add(new ColorEntry("red", DyeColor.RED, Tags.Items.DYES_RED));
        COLORS.add(new ColorEntry("black", DyeColor.BLACK, Tags.Items.DYES_BLACK));
    }

    @Override
    protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        registerShapedRecipes(consumer);
        registerShapelessRecipes(consumer);
        registerSmeltingRecipes(consumer);
        registerSushiMakerRecipes(consumer);
    }

    private void registerShapedRecipes(Consumer<FinishedRecipe> consumer) {
    }

    private void registerShapelessRecipes(Consumer<FinishedRecipe> consumer) {
        registerSmokeGrenadeColors(consumer);
    }

    private void registerSmeltingRecipes(Consumer<FinishedRecipe> consumer) {
    }

    private void registerSushiMakerRecipes(Consumer<FinishedRecipe> consumer) {

    }

    private void registerSmokeGrenadeColors(Consumer<FinishedRecipe> consumer) {
        for(ColorEntry color: COLORS) {
            ItemStack stack = new ItemStack(UtilcraftWeaponsItems.SMOKE_GRENADE.get());
            SmokeGrenade.setColor(stack, color.dyeColor);
            ResourceLocation path = new ResourceLocation(BlockItemUtils.namespace(UtilcraftWeaponsItems.SMOKE_GRENADE.get()), BlockItemUtils.name(UtilcraftWeaponsItems.SMOKE_GRENADE.get()) + "_" + color.colorName);
            ShapelessStackRecipeBuilder.shapelessRecipe(stack)
                    .addIngredient(UtilcraftWeaponsItems.SMOKE_GRENADE.get())
                    .addIngredient(color.colorTag)
                    .addCriterion("smoke_grenade", InventoryChangeTrigger.TriggerInstance.hasItems(UtilcraftWeaponsItems.SMOKE_GRENADE.get()))
                    .addCriterion(color.colorName + "_dye", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(color.colorTag).build()))
                    .build(consumer, path);
        }
    }

    private record ColorEntry(String colorName, DyeColor dyeColor,
                              Tags.IOptionalNamedTag<Item> colorTag) {

    }
}