package respawnarisemod.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import respawnarisemod.item.ModToolTiers;
import respawnarisemod.item.components.SacrifiseDaggerComponent;
import respawnarisemod.util.ModDataComponents;

public class SoulTouchedSword extends SacrifiseDagger{
    public SoulTouchedSword() {
        super(ModToolTiers.SOUL_TOUCHED_TIER,(new Item.Properties())
                .attributes(SwordItem.createAttributes(ModToolTiers.SOUL_TOUCHED_TIER, 4, 0.0F))
                .component(ModDataComponents.DAGGER_CONTENT.get(), SacrifiseDaggerComponent.empty())
        );
    }
}
