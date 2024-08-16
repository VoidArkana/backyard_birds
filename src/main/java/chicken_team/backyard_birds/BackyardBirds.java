package chicken_team.backyard_birds;

import chicken_team.backyard_birds.client.ClientProxy;
import chicken_team.backyard_birds.client.screen.BBMenuTypes;
import chicken_team.backyard_birds.client.sound.BBSounds;
import chicken_team.backyard_birds.common.CommonProxy;
import chicken_team.backyard_birds.common.block.BBBlocks;
import chicken_team.backyard_birds.common.entity.BBEntityPlacers;
import chicken_team.backyard_birds.common.event.BBCommonEventBus;
import chicken_team.backyard_birds.common.items.BBItems;
import chicken_team.backyard_birds.common.network.BBNetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import chicken_team.backyard_birds.common.entity.BBEntities;

@Mod(BackyardBirds.MOD_ID)
public class BackyardBirds {

	public static final String MOD_ID = "backyard_birds";

	public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	public BackyardBirds() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::clientSetup);
		bus.addListener(this::commonSetup);

		BBEntities.register(bus);
		BBItems.register(bus);
		BBBlocks.register(bus);

		BBSounds.register(bus);
		BBCreativeTab.register(bus);
		BBMenuTypes.register(bus);

		MinecraftForge.EVENT_BUS.register(this);

		MinecraftForge.EVENT_BUS.register(new BBCommonEventBus());


		PROXY.commonInit();
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> PROXY.clientInit());
	}

	private void commonSetup(final FMLClientSetupEvent event) {
		BBNetworkHandler.init();

		event.enqueueWork(() -> {
			BBEntityPlacers.entityPlacement();
		});
	}

}
