package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.block.BBBlocks;
import chicken_team.backyard_birds.util.BBTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
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

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BBBlocks.METAL_MESH_FENCE.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE).add(BBBlocks.WOOD_MESH_FENCE.get()).add(BBBlocks.BEETLE_BARREL.get());

        this.tag(BBTags.Blocks.MESH_FENCES).add(BBBlocks.METAL_MESH_FENCE.get())
                .add(BBBlocks.WOOD_MESH_FENCE.get());

        this.tag(BBTags.Blocks.MESH_FENCES).add(BBBlocks.WOOD_MESH_FENCE.get());

    }
}
