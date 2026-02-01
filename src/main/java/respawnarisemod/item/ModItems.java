package respawnarisemod.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.custom.SacrifiseDagger;
import respawnarisemod.item.custom.SoulVisage;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RespawnariseMod.MOD_ID);

    public static DeferredItem<Item> SOUL_VISAGE = ITEMS.registerItem("soul_visage",(p)->new SoulVisage());

    public static DeferredItem<Item> SACRIFISE_DAGGER = ITEMS.registerItem("sacrifise_dagger", (p)->new SacrifiseDagger());

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
