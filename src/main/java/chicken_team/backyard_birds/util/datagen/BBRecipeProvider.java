package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.common.items.BBItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class BBRecipeProvider extends RecipeProvider {
    public BBRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

//        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SIGILLARIA_MOSAIC.get(), 4)
//                .pattern("S")
//                .pattern("S")
//                .define('S', ModBlocks.SIGILLARIA_SLAB.get())
//                .unlockedBy(getHasName(ModBlocks.SIGILLARIA_STEM.get()), has(ModBlocks.SIGILLARIA_STEM.get()))
//                .save(consumer);

        //Mushroom Stew From Prototaxites
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BBItems.MULTIPURPOSE_FEED.get(), 16)
                .requires(Items.BEETROOT_SEEDS)
                .requires(Items.MELON_SEEDS)
                .requires(Items.PUMPKIN_SEEDS)
                .requires(Items.WHEAT_SEEDS)
                .unlockedBy(getHasName(Items.WHEAT_SEEDS), has(Items.WHEAT_SEEDS))
                .save(consumer);

    }
}
