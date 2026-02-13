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
    public static final int CONTAINER_SIZE = 1;
    public static final int INPUT_SLOT = 0;
    public static final boolean DEFAULT_SPAWN_MAIMED = false;
    public static final boolean DEFAULT_IS_LONG_DISTANCE = false;
    public static final boolean DEFAULT_REQUIRE_SIGNAL = false;
    public static final int DEFAULT_RANGE = 2;
    public static final int DEFAULT_SPAWN_COUNT = 3;
    public static final int DEFAULT_SPAWN_DELAY = 20*4; //20ticks x 4s
    public static final int NEARBY_ENTITIES_RESTRICTION = 50;
    public static final int LONG_DISTANCE_PLAYER_DETECTION_RANGE = 60;
    public static final int SHORT_DISTANCE_PLAYER_DETECTION_RANGE = 20;
    protected final ContainerData data;
    private boolean spawnMaimed = DEFAULT_SPAWN_MAIMED;
    private boolean isLongDistance = DEFAULT_IS_LONG_DISTANCE;
    private boolean requireSignal = DEFAULT_REQUIRE_SIGNAL;
    private int delayCounter = 0;

    public SoulMatrixBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOUL_MATRIX_BE.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0->SoulMatrixBlockEntity.this.delayCounter;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> SoulMatrixBlockEntity.this.delayCounter = value;
                    default -> {
                        return;
                    }
                }
            }

            @Override
            public int getCount() {
                return 1;
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
        tag.putBoolean("require_signal",this.requireSignal);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries,tag.getCompound("inventory"));
        this.spawnMaimed = tag.getBoolean("maimed");
        this.isLongDistance = tag.getBoolean("long_distance");
        this.requireSignal = tag.getBoolean("require_signal");
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
            if(canSpawn(pos)){
                delayCounter++;
            }
            if(delayCounter >= DEFAULT_SPAWN_DELAY){
                delayCounter=0;
                var random = serverLevel.getRandom();
                for(int i=0;i<DEFAULT_SPAWN_COUNT;i++){
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

    private EntityType getEntityType(){
        ItemStack stack = itemStackHandler.getStackInSlot(INPUT_SLOT);
        SoulVisageComponent comp = (stack!=null && !stack.isEmpty() && (stack.getItem() instanceof SoulVisage))?stack.get(ModDataComponents.VISAGE_CONTENT):null;
        EntityType type = (comp!=null && comp.typeKey()!=null)?EntityType.byString(comp.typeKey()).get():null;
        return type;
    }

    private boolean canSpawn(BlockPos pos){
        int detectionRange = isLongDistance?LONG_DISTANCE_PLAYER_DETECTION_RANGE:SHORT_DISTANCE_PLAYER_DETECTION_RANGE;
        boolean isPowered = level.hasNeighborSignal(worldPosition);
        AABB box = new AABB(pos).inflate(detectionRange);
        int players = level.getEntitiesOfClass(Player.class,box).size();
        int count = level.getEntitiesOfClass(Mob.class,box).size();
        return players>0 && count<NEARBY_ENTITIES_RESTRICTION && (isPowered || !requireSignal);
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
