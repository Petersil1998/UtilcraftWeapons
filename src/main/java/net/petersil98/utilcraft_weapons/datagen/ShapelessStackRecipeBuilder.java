package net.petersil98.utilcraft_weapons.datagen;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class ShapelessStackRecipeBuilder {

    private final ItemStack result;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;

    private ShapelessStackRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ShapelessStackRecipeBuilder shapelessRecipe(ItemLike provider) {
        return new ShapelessStackRecipeBuilder(new ItemStack(provider));
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ShapelessStackRecipeBuilder shapelessRecipe(ItemLike result, int count) {
        return new ShapelessStackRecipeBuilder(new ItemStack(result, count));
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ShapelessStackRecipeBuilder shapelessRecipe(ItemStack result) {
        return new ShapelessStackRecipeBuilder(result);
    }

    /**
     * Adds an ingredient that can be any item in the given tag.
     */
    public ShapelessStackRecipeBuilder addIngredient(Tag<Item> tag) {
        return this.addIngredient(Ingredient.of(tag));
    }

    /**
     * Adds an ingredient of the given item.
     */
    public ShapelessStackRecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(item, 1);
    }

    /**
     * Adds the given ingredient multiple times.
     */
    public ShapelessStackRecipeBuilder addIngredient(ItemLike item, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.addIngredient(Ingredient.of(item));
        }

        return this;
    }

    /**
     * Adds an ingredient.
     */
    public ShapelessStackRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }

    /**
     * Adds an ingredient multiple times.
     */
    public ShapelessStackRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    /**
     * Adds a criterion needed to unlock the recipe.
     */
    public ShapelessStackRecipeBuilder addCriterion(String name, CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    public ShapelessStackRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    /**
     * Builds this recipe into an {@link FinishedRecipe}.
     */
    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }

    /**
     * Builds this recipe into an {@link FinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for the result.
     */
    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation saveTo = new ResourceLocation(save);
        if (saveTo.equals(ForgeRegistries.ITEMS.getKey(this.result.getItem()))) {
            throw new IllegalStateException("Shapeless Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, saveTo);
        }
    }

    /**
     * Builds this recipe into an {@link FinishedRecipe}.
     */
    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);
        consumerIn.accept(new Result(id, this.result, this.group == null ? "" : this.group, this.ingredients, this.advancement, new ResourceLocation(id.getNamespace(),
                "recipes/" + this.result.getItem().getItemCategory().getRecipeFolderName() + "/" + id.getPath())));
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void validate(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack result;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, ItemStack result, String group, List<Ingredient> ingredients, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
            this.id = id;
            this.result = result;
            this.group = group;
            this.ingredients = ingredients;
            this.advancement = advancementBuilder;
            this.advancementId = advancementId;
        }

        public void serializeRecipeData(@Nonnull JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray ingredientsJson = new JsonArray();

            for (Ingredient ingredient : this.ingredients) {
                ingredientsJson.add(ingredient.toJson());
            }

            json.add("ingredients", ingredientsJson);
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
            if (this.result.getCount() > 1) {
                resultJson.addProperty("count", this.result.getCount());
            }
            if (this.result.hasTag()) {
                resultJson.addProperty("nbt", NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, this.result.getTag()).toString());
            }

            json.add("result", resultJson);
        }

        @Nonnull
        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPELESS_RECIPE;
        }

        /**
         * Gets the ID for the recipe.
         */
        @Nonnull
        public ResourceLocation getId() {
            return this.id;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}