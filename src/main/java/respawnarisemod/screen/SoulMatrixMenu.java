package respawnarisemod.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.network.PacketDistributor;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.block.entity.SoulMatrixBlockEntity;
import respawnarisemod.util.SoulMatrixSetDistancePayload;
import respawnarisemod.util.SoulMatrixSetMaimedPayload;
import respawnarisemod.util.SoulMatrixSetPowerRequirementPayload;

public class SoulMatrixMenu extends AbstractContainerMenu {

    public SoulMatrixBlockEntity blockEntity;
    private final Container container;
    private final Level level;
    private final ContainerData data;


    public SoulMatrixMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory,new SimpleContainer(1),new SimpleContainerData(1));
        blockEntity = (SoulMatrixBlockEntity) Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos());
    }

    public SoulMatrixMenu(int containerId, Inventory playerInventory, Container container, ContainerData data){
        super(ModMenuTypes.SOUL_MATRIX_MENU.get(),containerId);
        this.container = container;
        this.level=playerInventory.player.level();
        this.data = data;
        if(container instanceof SoulMatrixBlockEntity){
            this.blockEntity=((SoulMatrixBlockEntity)container);
            this.addSlot(new SlotItemHandler(blockEntity.itemStackHandler,SoulMatrixBlockEntity.INPUT_SLOT,80,9));
        }else{
            this.addSlot(new Slot(container,SoulMatrixBlockEntity.INPUT_SLOT,18,33));
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
        int matrixSlots = SoulMatrixBlockEntity.CONTAINER_SIZE;
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

    public void changeDistance(boolean value){
        PacketDistributor.sendToServer(new SoulMatrixSetDistancePayload(getPos(),value));
    }
    public void changeMaimed(boolean value){
        PacketDistributor.sendToServer(new SoulMatrixSetMaimedPayload(getPos(),value));
    }
    public void changeSignalRequirement(boolean value){
        PacketDistributor.sendToServer(new SoulMatrixSetPowerRequirementPayload(getPos(),value));
    }
    private BlockPos getPos(){
        return blockEntity.getBlockPos();
    }
    public boolean getMaimed(){
        return this.blockEntity.isSpawnMaimed();
    }
    public boolean getDistance(){
        return this.blockEntity.isLongDistance();
    }
    public boolean getRequireSignal(){
        return this.blockEntity.isRequireSignal();
    }
}
