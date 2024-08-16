package chicken_team.backyard_birds.common.block;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.block.custom.BeetleBarrelBlock;
import chicken_team.backyard_birds.common.block.custom.MeshFenceBlock;
import chicken_team.backyard_birds.common.block.custom.PerchBlock;
import chicken_team.backyard_birds.common.block.custom.RibbonBlock;
import chicken_team.backyard_birds.common.items.BBItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BBBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BackyardBirds.MOD_ID);

    public static final RegistryObject<Block> ROOSTING_BAR = registerBlock("roosting_bar",
            ()-> new PerchBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion().instabreak()));


    public static final RegistryObject<Block> RIBBON_BLUE_ONE = registerBlock("ribbon_blue_one",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));

    public static final RegistryObject<Block> RIBBON_BLUE_TWO = registerBlock("ribbon_blue_two",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));

    public static final RegistryObject<Block> RIBBON_BLUE_THREE = registerBlock("ribbon_blue_three",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));


    public static final RegistryObject<Block> RIBBON_RED_ONE = registerBlock("ribbon_red_one",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));

    public static final RegistryObject<Block> RIBBON_RED_TWO = registerBlock("ribbon_red_two",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));

    public static final RegistryObject<Block> RIBBON_RED_THREE = registerBlock("ribbon_red_three",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));


    public static final RegistryObject<Block> RIBBON_WHITE_ONE = registerBlock("ribbon_white_one",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));

    public static final RegistryObject<Block> RIBBON_WHITE_TWO = registerBlock("ribbon_white_two",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));

    public static final RegistryObject<Block> RIBBON_WHITE_THREE = registerBlock("ribbon_white_three",
            ()-> new RibbonBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).noOcclusion().instabreak().noCollission()));

    public static final RegistryObject<Block> METAL_MESH_FENCE = registerBlock("metal_mesh_fence",
            ()-> new MeshFenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).noOcclusion()));

    public static final RegistryObject<Block> WOOD_MESH_FENCE = registerBlock("wooden_mesh_fence",
            ()-> new MeshFenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE).noOcclusion()));

    public static final RegistryObject<Block> BEETLE_BARREL = registerBlock("beetle_barrel",
            ()-> new BeetleBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name,RegistryObject<T> block){
        return BBItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
