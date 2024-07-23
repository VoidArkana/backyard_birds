package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.util.datagen.BBLootTables.BBBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class BBLootTablesProvider {
    public static LootTableProvider create(PackOutput output){
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BBBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
