package chicken_team.backyard_birds.client.screen;

import chicken_team.backyard_birds.common.entity.custom.inventory.ChickenContainer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class BBChickenMenu extends AbstractContainerMenu {
    private final Container ratInventory;

    public BBChickenMenu(int pContainerId, Container ratInventory, Inventory playerInventory) {
        super(BBMenuTypes.CHICKEN_CONTAINER.get(), pContainerId);
        this.ratInventory = ratInventory;
        ratInventory.startOpen(playerInventory.player);
    }

    public BBChickenMenu(int i, Inventory inventory) {
        this(i, new SimpleContainer(6), inventory);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.ratInventory.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.ratInventory.stopOpen(player);
        if (this.ratInventory instanceof ChickenContainer container) {
            container.getChicken().closeInventory();
        }
    }
}
