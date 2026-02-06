package respawnarisemod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.components.SacrifiseDaggerComponent;
import respawnarisemod.item.components.SoulVisageComponent;
import respawnarisemod.util.ModDataComponents;
import java.util.List;

public class SacrifiseDagger extends SwordItem {
    public SacrifiseDagger() {
        super(Tiers.WOOD,(new Item.Properties())
                .attributes(SwordItem.createAttributes(Tiers.WOOD, 4, -3.0F))
                .component(ModDataComponents.DAGGER_CONTENT.get(), SacrifiseDaggerComponent.empty())
        );
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide &&
                attacker instanceof Player player &&
                !(target instanceof Player) &&
                target.isDeadOrDying()
        ) {
            SacrifiseDaggerComponent content = (SacrifiseDaggerComponent)stack.get(ModDataComponents.DAGGER_CONTENT);
            if(content!=null){
                ItemStack contentStack = content.content();
                if(contentStack.getItem() instanceof SoulVisage){
                    String typeKey = EntityType.getKey(target.getType()).toString();
                    SoulVisageComponent newVisageComponent = new SoulVisageComponent(typeKey);
                    SacrifiseDaggerComponent newDaggerComponent = new SacrifiseDaggerComponent(contentStack.copy());
                    newDaggerComponent.content().set(ModDataComponents.VISAGE_CONTENT, newVisageComponent);
                    stack.set(ModDataComponents.DAGGER_CONTENT, newDaggerComponent);
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
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
            String message="";
            if(content.content().isEmpty()){
                message = "No Visage Installed";
            }else if(content.content().getItem() instanceof SoulVisage){
                SoulVisageComponent captured = content.content().get(ModDataComponents.VISAGE_CONTENT);
                if(captured!=null){
                    message=captured.typeKey()==null?"No entity captured": ("Captured: "+captured.typeKey());
                }
            }
            tooltipComponents.add(Component.literal(message));
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
