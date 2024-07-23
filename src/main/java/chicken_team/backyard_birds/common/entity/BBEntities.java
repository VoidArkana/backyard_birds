package chicken_team.backyard_birds.common.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;

public class BBEntities {

	public static final DeferredRegister<EntityType<?>> REGISTER =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BackyardBirds.MOD_ID);

	public static final RegistryObject<EntityType<BBChicken>> BBCHICKEN =
			REGISTER.register("bbchicken",
					() -> EntityType.Builder.of(BBChicken::new, MobCategory.CREATURE)
							.sized(0.7f, 0.9f)
							.build(new ResourceLocation(BackyardBirds.MOD_ID, "bbchicken").toString()));

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}

}
