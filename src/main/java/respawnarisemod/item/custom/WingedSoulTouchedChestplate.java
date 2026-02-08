package respawnarisemod.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import respawnarisemod.item.ModItems;


public class WingedSoulTouchedChestplate extends ArmorItem {
    public WingedSoulTouchedChestplate() {
        super(ModArmorMaterials.SOUL_TOUCHED, ArmorItem.Type.CHESTPLATE,
                new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(40)));
    }

    public static boolean isFlyEnabled(ItemStack elytraStack) {
        return elytraStack.getDamageValue() < elytraStack.getMaxDamage() - 1;
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(Items.PHANTOM_MEMBRANE) || repair.is(ModItems.SOUL_TOUCHED_CORE.get());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return isFlyEnabled(stack);
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide) {
            int nextFlightTick = flightTicks + 1;
            if (nextFlightTick % 10 == 0) {
                if (nextFlightTick % 20 == 0) {
                    stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
                }

                entity.gameEvent(GameEvent.ELYTRA_GLIDE);
            }
        }

        return true;
    }

    public Holder<SoundEvent> getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}
