package chicken_team.backyard_birds.common.network;

import chicken_team.backyard_birds.BackyardBirds;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class BBNetworkHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BackyardBirds.MOD_ID, "channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SuppressWarnings("UnusedAssignment")
    public static void init() {
        int id = 0;
        CHANNEL.registerMessage(id++, OpenChickenScreenPacket.class, OpenChickenScreenPacket::encode, OpenChickenScreenPacket::decode, OpenChickenScreenPacket.Handler::handle);
    }
}
