package respawnarisemod.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import respawnarisemod.block.ModBlocks;
import respawnarisemod.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOUL_VISAGE.get())
                .pattern("DID")
                .pattern("IPI")
                .pattern("DID")
                .define('P',Items.ENDER_PEARL)
                .define('I',Items.IRON_NUGGET)
                .define('D',Items.DIAMOND)
                .unlockedBy("has_ender_pearl",has(Items.ENDER_PEARL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SACRIFISE_DAGGER.get())
                .pattern("  F")
                .pattern(" L ")
                .pattern("B  ")
                .define('F', Items.FLINT)
                .define('L',Items.LEATHER)
                .define('B',Items.BONE)
                .unlockedBy("has_bone",has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOUL_MATRIX_BLOCK.get())
                .pattern("BBB")
                .pattern("BSB")
                .pattern("BBB")
                .define('B', Items.IRON_BARS)
                .define('S',ModItems.SOUL_VISAGE.get())
                .unlockedBy("has_soul_visage",has(ModItems.SOUL_VISAGE.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOUL_TOUCHED_CORE.get())
                .pattern("BBB")
                .pattern("BNB")
                .pattern("BBB")
                .define('N', Items.NETHER_STAR)
                .define('B', ModItems.SOUL_TOUCHED_ROD.get())
                .unlockedBy("has_soul_visage",has(ModItems.SOUL_VISAGE.get()))
                .save(recipeOutput);
        smithingRecipe(recipeOutput,ModItems.SOUL_VISAGE.get(),Items.NETHERITE_HELMET,ModItems.SOUL_TOUCHED_CORE.get(),ModItems.SOUL_TOUCHED_HELMET.get());
        smithingRecipe(recipeOutput,ModItems.SOUL_VISAGE.get(),Items.NETHERITE_CHESTPLATE,ModItems.SOUL_TOUCHED_CORE.get(),ModItems.SOUL_TOUCHED_CHESTPLATE.get());
        smithingRecipe(recipeOutput,ModItems.SOUL_VISAGE.get(),Items.NETHERITE_LEGGINGS,ModItems.SOUL_TOUCHED_CORE.get(),ModItems.SOUL_TOUCHED_LEGGINGS.get());
        smithingRecipe(recipeOutput,ModItems.SOUL_VISAGE.get(),Items.NETHERITE_BOOTS,ModItems.SOUL_TOUCHED_CORE.get(),ModItems.SOUL_TOUCHED_BOOTS.get());
        smithingRecipe(recipeOutput,Items.ELYTRA,ModItems.SOUL_TOUCHED_CHESTPLATE.get(),Items.NETHERITE_INGOT,ModItems.WINGED_SOUL_TOUCHED_CHESTPLATE.get());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.SOUL_TOUCHED_ROD.get(),2)
                .requires(Items.BLAZE_ROD)
                .requires(Items.SOUL_SAND)
                .requires(Items.BREEZE_ROD)
                .unlockedBy("has_rod",has(Items.BLAZE_ROD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MULTI_TOOL.get())
                .pattern("NCN")
                .pattern("NR ")
                .pattern(" R ")
                .define('N', Items.NETHERITE_INGOT)
                .define('C', ModItems.SOUL_TOUCHED_CORE.get())
                .define('R', ModItems.SOUL_TOUCHED_ROD.get())
                .unlockedBy("has_core",has(ModItems.SOUL_TOUCHED_CORE.get()))
                .save(recipeOutput);


    }
    private void smithingRecipe(RecipeOutput recipeOutput,Item template,Item base,Item addition,Item result){
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        RecipeCategory.TOOLS,
                        result
                )
                .unlocks("has_"+template.getDescriptionId(), this.has(template))
                .save(recipeOutput, result.getDescriptionId()+"_smithing");
    }
}
