package superlord.backyard_birds;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import superlord.backyard_birds.common.entity.BBChicken;
import superlord.backyard_birds.init.BBEntities;

@Mod(BackyardBirds.MOD_ID)
public class BackyardBirds {
	
	public static final String MOD_ID = "backyard_birds";
	
	public BackyardBirds() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		bus.addListener(this::registerEntityAttributes);
		
		BBEntities.REGISTER.register(bus);
	}
	
	private void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(BBEntities.CHICKEN.get(), BBChicken.createAttributes().build());
	}

}
