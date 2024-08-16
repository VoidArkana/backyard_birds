package chicken_team.backyard_birds.common.entity.custom.ai;

import chicken_team.backyard_birds.common.block.BBBlocks;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ChickenSeekRandomShelterGoal extends Goal {
    protected final BBChicken mob;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    private final Level level;

    public ChickenSeekRandomShelterGoal(BBChicken pMob, double pSpeedModifier) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.level = pMob.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!mob.getIsSleeping() && mob.ticks_till_sleep>0) {
            if (mob.level().isThundering() && mob.level().canSeeSky(this.mob.blockPosition())) {
                return this.setWantedPos();
            }else {
                BlockPos blockpos = mob.blockPosition();
                return mob.level().isNight() && mob.level().canSeeSky(blockpos) && this.setWantedPos() && !mob.isOnFire();
            }
        } else {
            return false;
        }
    }

    public void start() {
        mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        mob.setLookingForShelter(true);
    }

    protected boolean setWantedPos() {
        Vec3 vec3 = this.getHidePos();
        if (vec3 == null) {
            return false;
        } else {
            this.wantedX = vec3.x;
            this.wantedY = vec3.y;
            this.wantedZ = vec3.z;
            return true;
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }


    @Nullable
    protected Vec3 getHidePos() {
        RandomSource randomsource = this.mob.getRandom();
        BlockPos blockpos = this.mob.blockPosition();
        for(int i = 0; i < 25; ++i) {
            BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(20) - 10, randomsource.nextInt(4) - 3,
                    randomsource.nextInt(20) - 10);
            BlockState state = level.getBlockState(blockpos1);

            if (state.is(BBBlocks.ROOSTING_BAR.get()) && !this.level.canSeeSky(blockpos1) && this.mob.getWalkTargetValue(blockpos1) < 0.0F) {
                return Vec3.atBottomCenterOf(blockpos1);
            }
        }

        return null;
    }
}
