package chicken_team.backyard_birds.common.items;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.entity.BBEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BBItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BackyardBirds.MOD_ID);

    public static final RegistryObject<Item> CHICKEN_SPAWN_EGG = ITEMS.register("bbchicken_spawn_egg",
            ()-> new ForgeSpawnEggItem(BBEntities.BBCHICKEN, 0xfff1f1, 0x9d2d2d, new Item.Properties()));

    public static final RegistryObject<Item> MULTIPURPOSE_FEED = ITEMS.register("multipurpose_feed",
            ()-> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
