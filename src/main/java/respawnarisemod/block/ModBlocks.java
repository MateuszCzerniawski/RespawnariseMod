package respawnarisemod.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.block.custom.SoulMatrixBlock;
import respawnarisemod.item.ModItems;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RespawnariseMod.MOD_ID);

    public static final  DeferredBlock<SoulMatrixBlock> SOUL_MATRIX_BLOCK = BLOCKS.registerBlock(
            "soul_matrix_block",SoulMatrixBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredItem<BlockItem> SOUL_MATRIX_ITEM = ModItems.ITEMS.registerSimpleBlockItem("soul_matrix_block",SOUL_MATRIX_BLOCK);


    private static DeferredBlock<Block> registerBlock(String name, BlockBehaviour.Properties props){
        DeferredBlock<Block> block = BLOCKS.registerSimpleBlock(name, props);
        ModItems.ITEMS.registerSimpleBlockItem(name,block);
        return block;
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
