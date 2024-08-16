package chicken_team.backyard_birds.common.network;

import chicken_team.backyard_birds.client.screen.BBChickenMenu;
import chicken_team.backyard_birds.client.screen.BBChickenScreen;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenChickenScreenPacket(int containerId, int entityId) {

    public static OpenChickenScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenChickenScreenPacket(buf.readInt(), buf.readInt());
    }

    public static void encode(OpenChickenScreenPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.containerId());
        buf.writeInt(packet.entityId());
    }

    public static class Handler {

        @SuppressWarnings("Convert2Lambda")
        public static void handle(OpenChickenScreenPacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(new Runnable() {
                @Override
                public void run() {
                    Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
                    if (entity instanceof BBChicken rat) {
                        LocalPlayer localplayer = Minecraft.getInstance().player;
                        SimpleContainer container = new SimpleContainer(6);
                        BBChickenMenu menu = new BBChickenMenu(packet.containerId(), container, localplayer.getInventory());
                        localplayer.containerMenu = menu;
                        Minecraft.getInstance().setScreen(new BBChickenScreen(menu, localplayer.getInventory(), rat));
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
