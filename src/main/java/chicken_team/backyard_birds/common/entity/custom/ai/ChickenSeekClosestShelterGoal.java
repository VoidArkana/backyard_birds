package chicken_team.backyard_birds.common.entity.custom.ai;

import chicken_team.backyard_birds.common.block.BBBlocks;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class ChickenSeekClosestShelterGoal extends MoveToBlockGoal {
    private final BBChicken chicken;

    public ChickenSeekClosestShelterGoal(BBChicken pChicken) {
        super(pChicken, 1.1D, 16);
        this.chicken = pChicken;
    }

    public boolean canUse() {
        return chicken.level().isNight() && chicken.ticks_till_sleep > 0 && !chicken.getIsSleeping() && chicken.ticks_without_shelter==0;
    }

    public boolean canContinueToUse() {
        return canUse() && super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.chicken.setLookingForShelter(true);
    }

    public void tick() {
        super.tick();
        if (!this.isReachedTarget()) {
            this.chicken.getLookControl().setLookAt((double)this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), (double)this.blockPos.getZ() + 0.5D, 10.0F, (float)this.chicken.getMaxHeadXRot());
        }else {
            this.chicken.setLookingForShelter(false);
            this.chicken.getNavigation().stop();
        }
    }

    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {

        BlockState blockstate = pLevel.getBlockState(pPos);

        if (blockstate.is(BBBlocks.ROOSTING_BAR.get()) && !pLevel.canSeeSky(pPos)) {
            return true;
        }else if (blockstate.is(BBBlocks.ROOSTING_BAR.get()) && chicken.ticks_without_best_shelter==0){
            return true;
        }else if (!pLevel.canSeeSky(pPos) && chicken.ticks_without_better_shelter==0 && chicken.ticks_without_best_shelter==0){
            return true;
        }else {
            return false;
        }

    }

}
