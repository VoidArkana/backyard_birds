package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BBEntityTagsGenerator extends EntityTypeTagsProvider {

    public BBEntityTagsGenerator(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_,
                                 @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, p_256572_, BackyardBirds.MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {

    }
}
