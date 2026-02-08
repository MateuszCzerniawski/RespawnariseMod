package respawnarisemod.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.custom.*;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RespawnariseMod.MOD_ID);


    public static DeferredItem<Item> SOUL_VISAGE = ITEMS.registerItem("soul_visage",(p)->new SoulVisage());

    public static DeferredItem<Item> SACRIFISE_DAGGER = ITEMS.registerItem("sacrifise_dagger", (p)->new SacrifiseDagger());

    public static DeferredItem<Item> SOUL_TOUCHED_CORE = ITEMS.registerSimpleItem("soul_touched_core",new Item.Properties().fireResistant());
    public static final DeferredItem<ArmorItem> SOUL_TOUCHED_HELMET = ITEMS.register("soul_touched_helmet",
            () -> new ArmorItem(ModArmorMaterials.SOUL_TOUCHED, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredItem<ArmorItem> SOUL_TOUCHED_CHESTPLATE = ITEMS.register("soul_touched_chestplate",
            () -> new ArmorItem(ModArmorMaterials.SOUL_TOUCHED, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredItem<ArmorItem> SOUL_TOUCHED_LEGGINGS = ITEMS.register("soul_touched_leggings",
            () -> new ArmorItem(ModArmorMaterials.SOUL_TOUCHED, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredItem<ArmorItem> SOUL_TOUCHED_BOOTS = ITEMS.register("soul_touched_boots",
            () -> new ArmorItem(ModArmorMaterials.SOUL_TOUCHED, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(40))));

    public static final DeferredItem<WingedSoulTouchedChestplate> WINGED_SOUL_TOUCHED_CHESTPLATE =
            ITEMS.registerItem("winged_soul_touched_chestplate",(p)->new WingedSoulTouchedChestplate());

    public static final DeferredItem<Item> MULTI_TOOL= ITEMS.registerItem("multi_tool",(p)->new MultiTool());

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
