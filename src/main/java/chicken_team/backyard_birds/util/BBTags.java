package chicken_team.backyard_birds.util;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class BBTags {
    public static class Biomes {
        public static final TagKey<Biome> VANILLA_CHICKEN_REMOVED_BIOMES = tag("vanilla_chicken_removed_biomes");

        public static final TagKey<Biome> OPEN_BIOMES = tag("open_biomes");

        private static TagKey<Biome> tag(String pName) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(BackyardBirds.MOD_ID, pName));
        }

    }

    public static class Blocks {

        public static final TagKey<Block> MESH_FENCES = tag("mesh_fences");

        public static final TagKey<Block> WOODEN_MESH_FENCES = tag("wooden_mesh_fences");

        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(BackyardBirds.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> BEETLE_FOOD = tag("beetle_food");

        private static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(BackyardBirds.MOD_ID, name));
        }
    }

}
