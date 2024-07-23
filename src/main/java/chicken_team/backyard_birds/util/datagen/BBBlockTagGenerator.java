package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BBBlockTagGenerator extends BlockTagsProvider {

    public BBBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BackyardBirds.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
