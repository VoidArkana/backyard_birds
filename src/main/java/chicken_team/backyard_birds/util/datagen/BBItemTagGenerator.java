package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BBItemTagGenerator extends ItemTagsProvider {
    public BBItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> providerCompletableFuture,
                              CompletableFuture<TagLookup<Block>> tagLookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerCompletableFuture, tagLookupCompletableFuture, BackyardBirds.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
