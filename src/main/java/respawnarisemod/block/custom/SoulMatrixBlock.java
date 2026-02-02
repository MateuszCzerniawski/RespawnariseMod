package respawnarisemod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import respawnarisemod.block.entity.ModBlockEntities;
import respawnarisemod.block.entity.SoulMatrixBlockEntity;

public class SoulMatrixBlock extends BaseEntityBlock {
    public static final MapCodec<SoulMatrixBlock> CODEC = simpleCodec(SoulMatrixBlock::new);

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public SoulMatrixBlock(Properties properties){
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING,Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulMatrixBlockEntity(pos,state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(level.getBlockEntity(pos) instanceof SoulMatrixBlockEntity entity){
            entity.drops(); //TODO
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        openMenu(level,pos,player);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        openMenu(level,pos,player);
        return InteractionResult.SUCCESS;
    }
    private void openMenu(Level level, BlockPos pos, Player player){
        if(!level.isClientSide){
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof SoulMatrixBlockEntity) {
                player.openMenu((MenuProvider)blockentity); //TODO
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()){
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlockEntities.SOUL_MATRIX_BE.get(),
                ((level1, pos, state1, blockEntity) -> blockEntity.tick(level1,pos,state1,blockEntity)));
    }

}
