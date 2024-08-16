package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.items.BBItems;
import chicken_team.backyard_birds.util.BBTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
        this.tag(BBTags.Items.BEETLE_FOOD).add(Items.BEEF).add(Items.COOKED_BEEF).add(Items.CHICKEN).add(Items.COOKED_CHICKEN)
                .add(Items.COD).add(Items.COOKED_COD).add(Items.MUTTON).add(Items.COOKED_MUTTON).add(Items.PORKCHOP).add(Items.COOKED_PORKCHOP)
                .add(Items.SALMON).add(Items.COOKED_SALMON).add(Items.ROTTEN_FLESH).add(Items.RABBIT).add(Items.COOKED_RABBIT).add(Items.RABBIT_FOOT)
                .add(Items.RABBIT_HIDE).add(Items.LEATHER).add(Items.FEATHER).add(BBItems.ROOSTER_FEATHER.get()).add(Items.EGG)
                .add(Items.SPIDER_EYE).add(Items.TROPICAL_FISH);
    }
}
