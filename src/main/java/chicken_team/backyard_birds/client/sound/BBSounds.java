package chicken_team.backyard_birds.client.sound;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BBSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BackyardBirds.MOD_ID);

    public static final RegistryObject<SoundEvent> ROOSTER_CROW = registerSoundEvents("rooster_crow");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BackyardBirds.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
