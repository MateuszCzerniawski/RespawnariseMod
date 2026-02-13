package respawnarisemod.block.entity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.components.SoulVisageComponent;
import respawnarisemod.item.custom.SoulVisage;
import respawnarisemod.screen.SoulMatrixMenu;
import respawnarisemod.util.ModDataComponents;

import java.util.Optional;

public class SoulMatrixBlockEntity extends BaseContainerBlockEntity implements MenuProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(CONTAINER_SIZE){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        }
    };
    public static final int CONTAINER_SIZE = 4;
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int INPUT_SLOT_3 = 2;
    public static final int INPUT_SLOT_4 = 3;
    public static final boolean DEFAULT_SPAWN_MAIMED = false;
    public static final boolean DEFAULT_IS_LONG_DISTANCE = false;
    public static final int DEFAULT_RANGE = 5;
    public static final int DEFAULT_SPAWN_COUNT = 2;
    public static final int DEFAULT_SPAWN_DELAY = 20*5; //20ticks x 5s
    public static final int SPAWN_COUNT_INCREASE = 1;
    public static final int SPAWN_DELAY_REDUCTION = 20; //20ticks x 1s
    public static final int NEARBY_ENTITIES_RESTRICTION = 50;
    public static final int LONG_DISTANCE_PLAYER_DETECTION_RANGE = 40;
    public static final int SHORT_DISTANCE_PLAYER_DETECTION_RANGE = 20;
    protected final ContainerData data;
    private boolean spawnMaimed = DEFAULT_SPAWN_MAIMED;
    private boolean isLongDistance = DEFAULT_IS_LONG_DISTANCE;
    private int delayCounter = 0;

    public SoulMatrixBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOUL_MATRIX_BE.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 ->SoulMatrixBlockEntity.this.spawnMaimed?1:0;
                    case 1->SoulMatrixBlockEntity.this.isLongDistance?1:0;
                    case 2->SoulMatrixBlockEntity.this.delayCounter;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> SoulMatrixBlockEntity.this.spawnMaimed = value==1;
                    case 1 -> SoulMatrixBlockEntity.this.isLongDistance = value==1;
                    case 2 -> SoulMatrixBlockEntity.this.delayCounter = value;
                    default -> {
                        return;
                    }
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.respawnarisemod.soul_matrix");
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.respawnarisemod.soul_matrix");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> list = NonNullList.create();
        for(int i=0;i<itemStackHandler.getSlots();i++){
            list.add(itemStackHandler.getStackInSlot(i));
        }
        return list;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            itemStackHandler.setStackInSlot(i,items.get(i));
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return createMenu(containerId,playerInventory);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SoulMatrixMenu(containerId,inventory,this,this.data);
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i,itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level,this.worldPosition,inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory",itemStackHandler.serializeNBT(registries));
        tag.putBoolean("maimed",this.spawnMaimed);
        tag.putBoolean("long_distance",this.isLongDistance);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries,tag.getCompound("inventory"));
        this.spawnMaimed = tag.getBoolean("maimed");
        this.isLongDistance = tag.getBoolean("long_distance");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void tick(Level level, BlockPos pos, BlockState state, SoulMatrixBlockEntity blockEntity) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            int visagesCount = getVisagesCount();
            if(canSpawn(pos)){
                delayCounter++;
            }
            if(delayCounter >= DEFAULT_SPAWN_DELAY-(visagesCount-1)*SPAWN_DELAY_REDUCTION){
                delayCounter=0;
                var random = serverLevel.getRandom();
                for(int i=0;i<DEFAULT_SPAWN_COUNT+(visagesCount-1)*SPAWN_COUNT_INCREASE;i++){
                    int dx = random.nextInt(2*DEFAULT_RANGE+1) - DEFAULT_RANGE;
                    int dz = random.nextInt(2*DEFAULT_RANGE+1) - DEFAULT_RANGE;
                    BlockPos spawnPos = pos.offset(dx, 0, dz);
                    EntityType type = getEntityType();
                    if(type!=null){
                        Entity entity =  type.spawn(serverLevel,spawnPos, MobSpawnType.SPAWNER);
                        if(this.spawnMaimed && entity instanceof LivingEntity livingEntity){
                            livingEntity.setHealth(1);
                        }
                    }
                }
            }
        }
    }
    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    private int getVisagesCount(){
        int counter = 0;
        EntityType type=null;
        ItemStack stack1 =  itemStackHandler.getStackInSlot(INPUT_SLOT_1);
        if(stack1!=null && !stack1.isEmpty()){
            if(stack1.getItem() instanceof SoulVisage){
                SoulVisageComponent comp1 = stack1.get(ModDataComponents.VISAGE_CONTENT);
                if(comp1!=null && comp1.typeKey()!=null){
                    EntityType type1 = EntityType.byString(comp1.typeKey()).get();
                    if(type==null){
                        type=type1;
                    }
                    if(type1==type){
                        counter++;
                    }else{
                        return 0;
                    }
                }
            }else{
                return 0;
            }
        }
        ItemStack stack2 =  itemStackHandler.getStackInSlot(INPUT_SLOT_2);
        if(stack2!=null && !stack2.isEmpty()){
            if(stack2.getItem() instanceof SoulVisage){
                SoulVisageComponent comp2 = stack2.get(ModDataComponents.VISAGE_CONTENT);
                if(comp2!=null && comp2.typeKey()!=null){
                    EntityType type2 = EntityType.byString(comp2.typeKey()).get();
                    if(type==null){
                        type=type2;
                    }
                    if(type2==type){
                        counter++;
                    }else{
                        return 0;
                    }
                }
            }else{
                return 0;
            }
        }
        ItemStack stack3 =  itemStackHandler.getStackInSlot(INPUT_SLOT_3);
        if(stack3!=null && !stack3.isEmpty()){
            if(stack3.getItem() instanceof SoulVisage){
                SoulVisageComponent comp3 = stack3.get(ModDataComponents.VISAGE_CONTENT);
                if(comp3!=null && comp3.typeKey()!=null){
                    EntityType type3 = EntityType.byString(comp3.typeKey()).get();
                    if(type==null){
                        type=type3;
                    }
                    if(type3==type){
                        counter++;
                    }else{
                        return 0;
                    }
                }
            }else{
                return 0;
            }
        }
        ItemStack stack4 =  itemStackHandler.getStackInSlot(INPUT_SLOT_4);
        if(stack4!=null && !stack4.isEmpty()){
            if(stack4.getItem() instanceof SoulVisage){
                SoulVisageComponent comp4 = stack4.get(ModDataComponents.VISAGE_CONTENT);
                if(comp4!=null && comp4.typeKey()!=null){
                    EntityType type4 = EntityType.byString(comp4.typeKey()).get();
                    if(type==null){
                        type=type4;
                    }
                    if(type4==type){
                        counter++;
                    }else{
                        return 0;
                    }
                }
            }else{
                return 0;
            }
        }
        return counter;
    }

    private EntityType getEntityType(){
        ItemStack stack1 =  itemStackHandler.getStackInSlot(INPUT_SLOT_1);
        ItemStack stack2 =  itemStackHandler.getStackInSlot(INPUT_SLOT_2);
        ItemStack stack3 =  itemStackHandler.getStackInSlot(INPUT_SLOT_3);
        ItemStack stack4 =  itemStackHandler.getStackInSlot(INPUT_SLOT_4);
        SoulVisageComponent comp1 = (stack1!=null && !stack1.isEmpty() && (stack1.getItem() instanceof SoulVisage))?stack1.get(ModDataComponents.VISAGE_CONTENT):null;
        SoulVisageComponent comp2 = (stack2!=null && !stack2.isEmpty() && (stack2.getItem() instanceof SoulVisage))?stack2.get(ModDataComponents.VISAGE_CONTENT):null;
        SoulVisageComponent comp3 = (stack3!=null && !stack3.isEmpty() && (stack3.getItem() instanceof SoulVisage))?stack3.get(ModDataComponents.VISAGE_CONTENT):null;
        SoulVisageComponent comp4 = (stack4!=null && !stack4.isEmpty() && (stack4.getItem() instanceof SoulVisage))?stack4.get(ModDataComponents.VISAGE_CONTENT):null;
        EntityType type1 = (comp1!=null && comp1.typeKey()!=null)?EntityType.byString(comp1.typeKey()).get():null;
        if(type1!=null){
            return type1;
        }
        EntityType type2 = (comp2!=null && comp2.typeKey()!=null)?EntityType.byString(comp2.typeKey()).get():null;
        if(type2!=null){
            return type2;
        }
        EntityType type3 = (comp3!=null && comp3.typeKey()!=null)?EntityType.byString(comp3.typeKey()).get():null;
        if(type3!=null){
            return type3;
        }
        EntityType type4 = (comp4!=null && comp4.typeKey()!=null)?EntityType.byString(comp4.typeKey()).get():null;
        return type4;
    }

    private boolean canSpawn(BlockPos pos){
        int detectionRange = isLongDistance?LONG_DISTANCE_PLAYER_DETECTION_RANGE:SHORT_DISTANCE_PLAYER_DETECTION_RANGE;
        AABB box = new AABB(pos).inflate(detectionRange);
        int players = level.getEntitiesOfClass(Player.class,box).size();
        int count = level.getEntitiesOfClass(Mob.class,box).size();
        return players>0 && count<NEARBY_ENTITIES_RESTRICTION;
    }

    public boolean isSpawnMaimed() {
        return spawnMaimed;
    }

    public void setSpawnMaimed(boolean spawnMaimed) {
        this.spawnMaimed = spawnMaimed;
        data.set(0,spawnMaimed?1:0);
        setChanged();
        level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
    }

    public boolean isLongDistance() {
        return isLongDistance;
    }

    public void setLongDistance(boolean longDistance) {
        isLongDistance = longDistance;
        data.set(1,longDistance?1:0);
        setChanged();
        level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
    }
}
