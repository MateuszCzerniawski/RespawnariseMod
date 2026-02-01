package respawnarisemod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.components.SacrifiseDaggerComponent;
import respawnarisemod.util.ModDataComponents;

import java.util.List;
import java.util.Optional;

public class SacrifiseDagger extends SwordItem {
    public SacrifiseDagger(boolean primitive) {
        super(Tiers.WOOD,(new Item.Properties())
                .durability(primitive?1:100)
                .attributes(SwordItem.createAttributes(Tiers.WOOD, 99, -3.0F))
                .component(ModDataComponents.DAGGER_CONTENT.get(), SacrifiseDaggerComponent.empty())
        );
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }
    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() == 1 && action == ClickAction.SECONDARY) {
            SacrifiseDaggerComponent content = (SacrifiseDaggerComponent)stack.get(ModDataComponents.DAGGER_CONTENT);
            if (content != null) {
                ItemStack other = slot.getItem();
                if (other.isEmpty() && content.content() != null && !content.content().isEmpty()) {
                    playRemoveSound(player);
                    slot.safeInsert(content.content());
                    stack.set(ModDataComponents.DAGGER_CONTENT,new SacrifiseDaggerComponent(ItemStack.EMPTY));
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (stack.getCount() != 1) {
            return false;
        } else if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            SacrifiseDaggerComponent content = (SacrifiseDaggerComponent)stack.get(ModDataComponents.DAGGER_CONTENT);
            if (content != null) {
                if (other.getItem() instanceof SoulVisage && content.content().isEmpty()) {
                    playInsertSound(player);
                    stack.set(ModDataComponents.DAGGER_CONTENT,new SacrifiseDaggerComponent(other));
                    access.set(ItemStack.EMPTY);
                    return true;
                } else if (other.isEmpty() && !content.content().isEmpty()) {
                    playRemoveSound(player);
                    access.set(content.content());
                    stack.set(ModDataComponents.DAGGER_CONTENT,new SacrifiseDaggerComponent(ItemStack.EMPTY));
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        SacrifiseDaggerComponent content = (SacrifiseDaggerComponent)stack.get(ModDataComponents.DAGGER_CONTENT);
        if(content!=null){
            tooltipComponents.add(Component.literal(content.content().isEmpty()?"No Visage Installed":"Visage installed"));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private void playRemoveSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

}
