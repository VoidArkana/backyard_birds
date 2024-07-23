package chicken_team.backyard_birds.client.layers;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BBChickenBlackMarkLayer extends GeoRenderLayer<BBChicken> {

    public BBChickenBlackMarkLayer(GeoRenderer<BBChicken> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, BBChicken entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        if (entity.getBlackMarking()!=0){

            RenderType cameo = RenderType.entityCutout(new ResourceLocation(BackyardBirds.MOD_ID, "textures/entity/bbchicken/"+entity.getSex()+"/"+entity.getSex()+"_black_layer.png"));
            ResourceLocation model = new ResourceLocation(BackyardBirds.MOD_ID, "geo/"+entity.getSex()+".geo.json");

            this.getRenderer().reRender(this.getGeoModel().getBakedModel(model), poseStack, bufferSource, entity, renderType,
                    bufferSource.getBuffer(cameo), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        }
    }
}
