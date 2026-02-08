package respawnarisemod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.datagen.providers.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//@Mod(value = RespawnariseMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = RespawnariseMod.MOD_ID, value = Dist.CLIENT)
public class DataGenerators {
    /*More datagen can be done https://docs.neoforged.net/docs/resources/
    Language, Particle Description, SoundDefinition, Advancements, DataMaps, DatapackBuiltinEntries, GlobalLootModifier
    */


    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator =  event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(),new LootTableProvider(
                packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        ));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput,lookupProvider));
        BlockTagsProvider blockTagsProvider = new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput,existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput,existingFileHelper));
    }
}
