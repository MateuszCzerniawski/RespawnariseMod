package respawnarisemod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RespawnariseMod.MOD_ID);

    public static final Supplier<CreativeModeTab> RESPAWNARISE_TAB = buildTab("respawnarise_tab",ModItems.SOUL_VISAGE,
            ModItems.SOUL_VISAGE,
            ModBlocks.SOUL_MATRIX_BLOCK,
            ModItems.SOUL_TOUCHED_CORE,
            ModItems.SOUL_TOUCHED_ROD,
            ModItems.SOUL_TOUCHED_HELMET,
            ModItems.SOUL_TOUCHED_CHESTPLATE,
            ModItems.WINGED_SOUL_TOUCHED_CHESTPLATE,
            ModItems.SOUL_TOUCHED_LEGGINGS,
            ModItems.SOUL_TOUCHED_BOOTS,
            ModItems.MULTI_TOOL,
            ModItems.SACRIFISE_DAGGER

            );
    private static Supplier<CreativeModeTab> buildTab(String name, ItemLike icon, ItemLike... items){
        CreativeModeTab.Builder builder= CreativeModeTab.builder();
        builder.icon(()->new ItemStack(icon));
        builder.title(Component.translatable("creativetab.respawnarisemod."+name));
        builder.displayItems((itemDisplayParameters,output)->{
            for(ItemLike item : items){
                output.accept(item);
            }
        });
        return CREATIVE_MOD_TAB.register(name,()->builder.build());
    }
    public static void register(IEventBus eventBus){
        CREATIVE_MOD_TAB.register(eventBus);
    }

}
