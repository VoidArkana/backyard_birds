package superlord.backyard_birds.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superlord.backyard_birds.BackyardBirds;
import superlord.backyard_birds.common.entity.BBChicken;

public class BBEntities {

	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BackyardBirds.MOD_ID);

	public static final RegistryObject<EntityType<BBChicken>> CHICKEN = REGISTER.register("chicken", () -> EntityType.Builder.<BBChicken>of(BBChicken::new, MobCategory.MISC).sized(1, 1).build(new ResourceLocation(BackyardBirds.MOD_ID, "chicken").toString()));
	
}
