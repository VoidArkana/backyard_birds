package chicken_team.backyard_birds.common.block;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.block.custom.PerchBlock;
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
