package chicken_team.backyard_birds.common.event;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import chicken_team.backyard_birds.common.entity.BBEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BackyardBirds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BBCommonEventBus {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BBEntities.BBCHICKEN.get(), BBChicken.createAttributes().build());
    }
}
