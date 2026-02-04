package respawnarisemod.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
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
        /*ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.YOUR_ITEM_HERE.get(),9)
                .requires(ModItems.YOUR_ITEM_HERE)
                .unlockedBy("has_YOUR_ITEM_HERE",has(ModItems.YOUR_ITEM_HERE))
                .save(recipeOutput);*/


    }
}
