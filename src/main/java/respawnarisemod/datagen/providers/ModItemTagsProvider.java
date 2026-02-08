package respawnarisemod.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider.TagLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.ModItems;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, RespawnariseMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        /*tag(ItemTags.SWORDS)
                .add(ModItems.BISMUTH_SWORD.get());
        tag(ItemTags.PICKAXES)
                .add(ModItems.BISMUTH_PICKAXE.get());
        tag(ItemTags.SHOVELS)
                .add(ModItems.BISMUTH_SHOVEL.get());
        tag(ItemTags.AXES)
                .add(ModItems.BISMUTH_AXE.get());
        tag(ItemTags.HOES)
                .add(ModItems.BISMUTH_HOE.get());*/
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.SOUL_TOUCHED_HELMET.get())
                .add(ModItems.SOUL_TOUCHED_CHESTPLATE.get())
                .add(ModItems.SOUL_TOUCHED_LEGGINGS.get())
                .add(ModItems.SOUL_TOUCHED_BOOTS.get())
                .add(ModItems.WINGED_SOUL_TOUCHED_CHESTPLATE.get());

    }
}
