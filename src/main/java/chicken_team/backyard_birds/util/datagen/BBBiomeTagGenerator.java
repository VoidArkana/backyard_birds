package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.util.BBTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BBBiomeTagGenerator extends BiomeTagsProvider {

    public BBBiomeTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, BackyardBirds.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BBTags.Biomes.VANILLA_CHICKEN_REMOVED_BIOMES).addTag(BiomeTags.IS_OVERWORLD);

        this.tag(BBTags.Biomes.OPEN_BIOMES).addTag(BiomeTags.IS_MOUNTAIN).add(Biomes.PLAINS).add(Biomes.SUNFLOWER_PLAINS).add(Biomes.SNOWY_PLAINS);
    }
}
