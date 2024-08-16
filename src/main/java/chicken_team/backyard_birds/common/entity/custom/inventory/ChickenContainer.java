package chicken_team.backyard_birds.common.entity.custom.inventory;

import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ChickenContainer extends SimpleContainer {

    private final BBChicken chicken;

    public ChickenContainer(BBChicken chicken) {
        super(0);
        this.chicken = chicken;
    }

    public BBChicken getChicken() {
        return this.chicken;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
    }
}
