package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.common.block.BBBlocks;
import chicken_team.backyard_birds.common.items.BBItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import javax.swing.text.html.HTML;
import java.util.function.Consumer;

public class BBRecipeProvider extends RecipeProvider {
    public BBRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BBBlocks.METAL_MESH_FENCE.get(), 3)
                .pattern("NNN")
                .pattern("INI")
                .pattern("INI")
                .define('N', Items.IRON_NUGGET)
                .define('I', Blocks.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BBBlocks.WOOD_MESH_FENCE.get(), 3)
                .pattern("SBS")
                .pattern("SIS")
                .pattern("SBS")
                .define('S', Items.STICK)
                .define('I', Blocks.IRON_BARS)
                .define('B', ItemTags.WOODEN_SLABS)
                .unlockedBy(getHasName(Blocks.OAK_PLANKS), has(ItemTags.LOGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BBBlocks.BEETLE_BARREL.get(), 1)
                .pattern("BBB")
                .pattern("BRB")
                .pattern("BBB")
                .define('R', Blocks.BARREL)
                .define('B', BBItems.BEETLE.get())
                .unlockedBy(getHasName(BBItems.BEETLE.get()), has(BBItems.BEETLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BBItems.MULTIPURPOSE_FEED.get(), 16)
                .requires(Items.BEETROOT_SEEDS)
                .requires(Items.MELON_SEEDS)
                .requires(Items.PUMPKIN_SEEDS)
                .requires(Items.WHEAT_SEEDS)
                .unlockedBy(getHasName(Items.WHEAT_SEEDS), has(Items.WHEAT_SEEDS))
                .save(consumer);

    }
}
