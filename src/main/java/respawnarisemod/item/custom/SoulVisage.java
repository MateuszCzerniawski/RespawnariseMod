package respawnarisemod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import respawnarisemod.item.components.SoulVisageComponent;
import respawnarisemod.util.ModDataComponents;

import java.util.List;

public class SoulVisage extends Item {
    public SoulVisage() {
        super((new Properties()).stacksTo(1).component(ModDataComponents.VISAGE_CONTENT, SoulVisageComponent.empty()));
    }
    public SoulVisage(EntityType type){
        super((new Properties()).stacksTo(1).component(ModDataComponents.VISAGE_CONTENT, new SoulVisageComponent(EntityType.getKey(type).toString())));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        SoulVisageComponent content = (SoulVisageComponent)stack.get(ModDataComponents.VISAGE_CONTENT);
        if(content!=null){
            tooltipComponents.add(Component.literal(content.typeKey()==null?"No soul captured":("Captured: "+content.typeKey())));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
