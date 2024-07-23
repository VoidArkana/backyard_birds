package chicken_team.backyard_birds.client;

import chicken_team.backyard_birds.client.renderers.BBChickenRenderer;
import chicken_team.backyard_birds.common.CommonProxy;
import chicken_team.backyard_birds.common.entity.BBEntities;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy extends CommonProxy {

    public void commonInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    public void clientInit() {
        EntityRenderers.register(BBEntities.BBCHICKEN.get(), BBChickenRenderer::new);
    }
}
