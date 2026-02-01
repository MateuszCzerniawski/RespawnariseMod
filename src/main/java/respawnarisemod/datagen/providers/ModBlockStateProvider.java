package respawnarisemod.datagen.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import respawnarisemod.RespawnariseMod;

public class ModBlockStateProvider  extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RespawnariseMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(),cubeAll(deferredBlock.get()));
    }
}
