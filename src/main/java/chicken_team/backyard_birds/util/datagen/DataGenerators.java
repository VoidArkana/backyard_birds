package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BackyardBirds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new BBRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), BBLootTablesProvider.create(packOutput));

//      generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new BBItemModelProvider(packOutput, existingFileHelper));

        BBBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new BBBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(),new BBItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        generator.addProvider(true, new BBEntityTagsGenerator(packOutput, lookupProvider, existingFileHelper));

//        generator.addProvider(event.includeServer(), new ModWorldGenProvider(packOutput, lookupProvider));
    }
}
