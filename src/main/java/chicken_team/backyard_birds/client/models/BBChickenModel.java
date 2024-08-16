package chicken_team.backyard_birds.client.models;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;

public class BBChickenModel extends GeoModel<BBChicken> {

    @Override
    public ResourceLocation getModelResource(BBChicken bbChicken) {
        return bbChicken.isBaby() ? new ResourceLocation(BackyardBirds.MOD_ID, "geo/chick.geo.json")
        : bbChicken.getIsMale() ? new ResourceLocation(BackyardBirds.MOD_ID, "geo/rooster.geo.json")
                : new ResourceLocation(BackyardBirds.MOD_ID, "geo/hen.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BBChicken bbChicken) {
        if (bbChicken.isBaby()){
            return new ResourceLocation(BackyardBirds.MOD_ID, "textures/entity/bbchicken/chick/chick.png");
        }else {
            return new ResourceLocation(BackyardBirds.MOD_ID, "textures/entity/bbchicken/"+bbChicken.getSex()+"/"+bbChicken.getSex()+"_"+bbChicken.getColorName()+".png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(BBChicken bbChicken) {
        return new ResourceLocation(BackyardBirds.MOD_ID, "animations/chicken.animation.json");
    }

    @Override
    public void setCustomAnimations(BBChicken chicken, long instanceId, @Nullable AnimationState<BBChicken> animationEvent) {

        super.setCustomAnimations(chicken, instanceId, animationEvent);

        if (animationEvent == null) return;

        CoreGeoBone head = this.getAnimationProcessor().getBone("head_rot");
        CoreGeoBone neck = this.getAnimationProcessor().getBone("neck_rot");
        CoreGeoBone neck_base = this.getAnimationProcessor().getBone("neck_bump_rot");
        CoreGeoBone body = this.getAnimationProcessor().getBone("body_rot");
        CoreGeoBone main = this.getAnimationProcessor().getBone("root");

        EntityModelData entityData = animationEvent.getData(DataTickets.ENTITY_MODEL_DATA);

        float multiplier = chicken.isBaby() ? 0.8F : (chicken.getIsMale() ? 1 : -0.2F);
        float multiplier2 = chicken.isBaby() ? 0.8F : chicken.getIsMale() ? 1 : 1.1F;
        float multiplier3 = chicken.isBaby() ? 0.8F : chicken.getIsMale() ? 1 : 1.75F;
        float multiplier4 = chicken.isBaby() ? 1F : chicken.getIsMale() ? 1 : 1.1F;

        main.setPosZ(chicken.getTilt() * 0.01F * chicken.getTiltForSleep());
        body.setRotX((chicken.getTilt() * 10* ((float) Math.PI / 180F)) * multiplier4 * chicken.getTiltForSleep());
        neck_base.setRotX(-(chicken.getTilt() * 7.5F * ((float) Math.PI / 180F)) * multiplier * chicken.getTiltForSleep());
        neck.setRotX(-(chicken.getTilt() * 5 * ((float) Math.PI / 180F)) * multiplier3 * chicken.getTiltForSleep());

        head.setRotX((entityData.headPitch() * ((float) Math.PI / 180F))/4-(chicken.getTilt()*1.5F* ((float) Math.PI / 180F))*multiplier2);
        head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F)/4);

        if (!chicken.isBaby()) {
            body.setPosZ(-chicken.getTilt()*0.5F);
        }

        if (!chicken.isBaby()) {
            CoreGeoBone tail = this.getAnimationProcessor().getBone("tail_rot");
            tail.setRotX(-(chicken.getTilt() * 10 * ((float) Math.PI / 180F)) * multiplier2 * chicken.getTiltForSleep());
        }

    }
}
