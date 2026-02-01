package respawnarisemod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.ModItems;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RespawnariseMod.MOD_ID);

    private static DeferredBlock<Block> registerBlock(String name, BlockBehaviour.Properties props){
        DeferredBlock<Block> block = BLOCKS.registerSimpleBlock(name, props);
        ModItems.ITEMS.registerSimpleBlockItem(name,block);
        return block;
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
