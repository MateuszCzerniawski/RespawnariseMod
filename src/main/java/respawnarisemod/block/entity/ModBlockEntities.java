package respawnarisemod.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RespawnariseMod.MOD_ID);
    public static final Supplier<BlockEntityType<SoulMatrixBlockEntity>> SOUL_MATRIX_BE =
            BLOCK_ENTITIES.register("soul_matrix_be",()-> BlockEntityType.Builder.of(
                    SoulMatrixBlockEntity::new,ModBlocks.SOUL_MATRIX_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
