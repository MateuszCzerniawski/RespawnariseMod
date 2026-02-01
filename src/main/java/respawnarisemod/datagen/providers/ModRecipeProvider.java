package respawnarisemod.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
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
        /*ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.YOUR_NAME_HERE.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.YOUR_ITEM_HERE.get())
                .unlockedBy("has_YOUR_ITEM_HERE",has(ModItems.YOUR_ITEM_HERE))
                .save(recipeOutput);*/
        /*ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.YOUR_ITEM_HERE.get(),9)
                .requires(ModItems.YOUR_ITEM_HERE)
                .unlockedBy("has_YOUR_ITEM_HERE",has(ModItems.YOUR_ITEM_HERE))
                .save(recipeOutput);*/


    }
}
