package chicken_team.backyard_birds.util.datagen;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.items.BBItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BBItemModelProvider extends ItemModelProvider {
    public BBItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BackyardBirds.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(BBItems.MULTIPURPOSE_FEED);
        simpleItem(BBItems.ROOSTER_FEATHER);
        withExistingParent(BBItems.CHICKEN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        simpleItem(BBItems.BEETLE);
    }


    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BackyardBirds.MOD_ID, "item/" + item.getId().getPath()));
    }
}
