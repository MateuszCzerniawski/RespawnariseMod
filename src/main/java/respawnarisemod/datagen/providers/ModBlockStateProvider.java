package respawnarisemod.datagen.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.block.ModBlocks;

public class ModBlockStateProvider  extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RespawnariseMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        orientedBlockWithItem(ModBlocks.SOUL_MATRIX_BLOCK);
    }
    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(),cubeAll(deferredBlock.get()));
    }
    private void orientedBlockWithItem(DeferredBlock<?> deferredBlock){
        String name = deferredBlock.getRegisteredName().substring(RespawnariseMod.MOD_ID.length()+1);
        var model = models().cube(
                name,
                modLoc("block/"+name+"_down"),
                modLoc("block/"+name+"_up"),
                modLoc("block/"+name+"_north"),
                modLoc("block/"+name+"_south"),
                modLoc("block/"+name+"_east"),
                modLoc("block/"+name+"_west")
        ).texture("particle",modLoc("block/"+name+"_down"));
        simpleBlock(deferredBlock.get(),model);
        simpleBlockItem(deferredBlock.get(),model);
    }
}
