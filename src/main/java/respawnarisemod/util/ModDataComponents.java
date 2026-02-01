package respawnarisemod.util;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.components.SacrifiseDaggerComponent;
import respawnarisemod.item.components.SoulVisageComponent;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RespawnariseMod.MOD_ID);

    public static final Supplier<DataComponentType<SacrifiseDaggerComponent>> DAGGER_CONTENT = REGISTRAR.registerComponentType("dagger_content",
            builder->builder.persistent(SacrifiseDaggerComponent.CODEC).networkSynchronized(SacrifiseDaggerComponent.STREAM_CODEC));

    public static final Supplier<DataComponentType<SoulVisageComponent>> VISAGE_CONTENT = REGISTRAR.registerComponentType("visage_content",
            builder->builder.persistent(SoulVisageComponent.CODEC).networkSynchronized(SoulVisageComponent.STREAM_CODEC));

    public static void register(IEventBus eventBus){
        REGISTRAR.register(eventBus);
    }
}
