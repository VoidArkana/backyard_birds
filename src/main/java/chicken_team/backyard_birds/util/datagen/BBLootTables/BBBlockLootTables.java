package chicken_team.backyard_birds.util.datagen.BBLootTables;

import chicken_team.backyard_birds.common.block.BBBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class BBBlockLootTables extends BlockLootSubProvider {

    public BBBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(BBBlocks.ROOSTING_BAR.get());

        this.dropSelf(BBBlocks.RIBBON_BLUE_ONE.get());
        this.dropSelf(BBBlocks.RIBBON_BLUE_TWO.get());
        this.dropSelf(BBBlocks.RIBBON_BLUE_THREE.get());

        this.dropSelf(BBBlocks.RIBBON_RED_ONE.get());
        this.dropSelf(BBBlocks.RIBBON_RED_TWO.get());
        this.dropSelf(BBBlocks.RIBBON_RED_THREE.get());

        this.dropSelf(BBBlocks.RIBBON_WHITE_ONE.get());
        this.dropSelf(BBBlocks.RIBBON_WHITE_TWO.get());
        this.dropSelf(BBBlocks.RIBBON_WHITE_THREE.get());

        this.dropSelf(BBBlocks.METAL_MESH_FENCE.get());

        this.dropSelf(BBBlocks.WOOD_MESH_FENCE.get());

        this.dropSelf(BBBlocks.BEETLE_BARREL.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BBBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
