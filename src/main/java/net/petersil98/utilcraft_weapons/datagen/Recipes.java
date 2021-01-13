package net.petersil98.utilcraft_weapons.datagen;

import net.minecraft.data.*;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        registerShapedRecipes(consumer);
        registerShapelessRecipes(consumer);
        registerSmeltingRecipes(consumer);
        registerSushiMakerRecipes(consumer);
    }

    private void registerShapedRecipes(Consumer<IFinishedRecipe> consumer) {
    }

    private void registerShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
    }

    private void registerSmeltingRecipes(Consumer<IFinishedRecipe> consumer) {
    }

    private void registerSushiMakerRecipes(Consumer<IFinishedRecipe> consumer) {

    }
}