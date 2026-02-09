package respawnarisemod.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {

    public static final Tier SOUL_TOUCHED_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2500,20f,5,30,()->Ingredient.of(ModItems.SOUL_TOUCHED_CORE.get()));
}
