package chicken_team.backyard_birds.client.screen;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BBMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BackyardBirds.MOD_ID);

    public static final RegistryObject<MenuType<BBChickenMenu>> CHICKEN_CONTAINER = MENUS.register("chicken_container", () -> new MenuType<>(BBChickenMenu::new, FeatureFlags.REGISTRY.allFlags()));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
