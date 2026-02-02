package respawnarisemod.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import respawnarisemod.block.entity.SoulMatrixBlockEntity;

public class SoulMatrixMenu extends AbstractContainerMenu {

    public SoulMatrixBlockEntity blockEntity;
    private final Container container;
    private final Level level;
    private final ContainerData data;

    public SoulMatrixMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory,new SimpleContainer(4),new SimpleContainerData(3));
    }

    public SoulMatrixMenu(int containerId, Inventory playerInventory, Container container, ContainerData data){
        super(ModMenuTypes.SOUL_MATRIX_MENU.get(),containerId);
        this.container = container;
        this.level=playerInventory.player.level();
        this.data = data;
        if(container instanceof SoulMatrixBlockEntity){
            this.blockEntity=((SoulMatrixBlockEntity)container);
            this.addSlot(new SlotItemHandler(blockEntity.itemStackHandler,SoulMatrixBlockEntity.INPUT_SLOT_1,0,0));
            this.addSlot(new SlotItemHandler(blockEntity.itemStackHandler,SoulMatrixBlockEntity.INPUT_SLOT_2,0,0));
            this.addSlot(new SlotItemHandler(blockEntity.itemStackHandler,SoulMatrixBlockEntity.INPUT_SLOT_3,0,0));
            this.addSlot(new SlotItemHandler(blockEntity.itemStackHandler,SoulMatrixBlockEntity.INPUT_SLOT_4,0,0));
        }else{
            this.addSlot(new Slot(container,SoulMatrixBlockEntity.INPUT_SLOT_1,0,0));
            this.addSlot(new Slot(container,SoulMatrixBlockEntity.INPUT_SLOT_2,0,0));
            this.addSlot(new Slot(container,SoulMatrixBlockEntity.INPUT_SLOT_3,0,0));
            this.addSlot(new Slot(container,SoulMatrixBlockEntity.INPUT_SLOT_4,0,0));
        }
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }
    private void addPlayerInventory(Inventory inventory){
        for(int i=0;i<3;i++){
            for(int j=0;j<9;j++){
                this.addSlot(new Slot(inventory,j+i*9+9,8+j*18,84+i*18));
            }
        }
    }
    private void addPlayerHotbar(Inventory inventory){
        for(int i=0;i<9;i++){
            this.addSlot(new Slot(inventory,i,8+i*18,142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        int matrixSlots = 7;
        int inventorySlots = 27;
        int hotbarSlots = 9;
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < matrixSlots) {
                if (!this.moveItemStackTo(itemstack1, matrixSlots, matrixSlots + inventorySlots + hotbarSlots, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < matrixSlots + inventorySlots) {
                if (!this.moveItemStackTo(itemstack1, matrixSlots + inventorySlots, matrixSlots + inventorySlots + hotbarSlots, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < matrixSlots + inventorySlots + hotbarSlots) {
                if (!this.moveItemStackTo(itemstack1, matrixSlots, matrixSlots + inventorySlots, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
