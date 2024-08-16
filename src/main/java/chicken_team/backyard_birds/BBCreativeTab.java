package chicken_team.backyard_birds;

import chicken_team.backyard_birds.common.block.BBBlocks;
import chicken_team.backyard_birds.common.items.BBItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BBCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BackyardBirds.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BACKYARD_BIRDS_TAB =
            CREATIVE_MODE_TABS.register("backyard_birds_tab", ()-> CreativeModeTab.builder().icon(() -> new ItemStack(Items.EGG))
                    .title(Component.translatable("creativetab.backyard_birds_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(BBItems.CHICKEN_SPAWN_EGG.get());
                        output.accept(BBItems.MULTIPURPOSE_FEED.get());
                        output.accept(BBItems.ROOSTER_FEATHER.get());
                        output.accept(BBBlocks.ROOSTING_BAR.get());

                        output.accept(BBBlocks.RIBBON_BLUE_ONE.get());
                        output.accept(BBBlocks.RIBBON_BLUE_TWO.get());
                        output.accept(BBBlocks.RIBBON_BLUE_THREE.get());

                        output.accept(BBBlocks.RIBBON_RED_ONE.get());
                        output.accept(BBBlocks.RIBBON_RED_TWO.get());
                        output.accept(BBBlocks.RIBBON_RED_THREE.get());

                        output.accept(BBBlocks.RIBBON_WHITE_ONE.get());
                        output.accept(BBBlocks.RIBBON_WHITE_TWO.get());
                        output.accept(BBBlocks.RIBBON_WHITE_THREE.get());

                        output.accept(BBBlocks.METAL_MESH_FENCE.get());
                        output.accept(BBBlocks.WOOD_MESH_FENCE.get());

                        output.accept(BBItems.BEETLE.get());
                        output.accept(BBBlocks.BEETLE_BARREL.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
