package chicken_team.backyard_birds.client.renderers;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.client.layers.BBChickenBlackMarkLayer;
import chicken_team.backyard_birds.client.layers.BBChickenWhiteMarkLayer;
import chicken_team.backyard_birds.client.models.BBChickenModel;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BBChickenRenderer extends GeoEntityRenderer<BBChicken> {

    public BBChickenRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BBChickenModel());
        this.addRenderLayer(new BBChickenBlackMarkLayer(this));
        this.addRenderLayer(new BBChickenWhiteMarkLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BBChicken bbChicken) {
       return new ResourceLocation(BackyardBirds.MOD_ID, "textures/entity/bbchicken/"+bbChicken.getSex()+"/"+bbChicken.getSex()+"_"+bbChicken.getColorName()+".png");
    }

    @Override
    public void render(BBChicken entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLightIn) {
        if(entity.isBaby()) {
            poseStack.scale(0.6F, 0.6F, 0.6F);
        }
        else {
            poseStack.scale(1.0F, 1.0F, 1.0F);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
    }
}
