package chicken_team.backyard_birds.common.entity.custom;

import javax.annotation.Nullable;

import chicken_team.backyard_birds.client.sound.BBSounds;
import chicken_team.backyard_birds.common.entity.BBEntities;
import chicken_team.backyard_birds.common.items.BBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

//TODO: revamp seek shelter goal
public class BBChicken extends Animal implements GeoEntity {
	public int ticks_till_sleep = 0;
	public int sleep_ticks_when_interrupted = 0;

	private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

	private static final Ingredient FOOD_ITEMS = Ingredient.of(BBItems.MULTIPURPOSE_FEED.get());

	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BLACK_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> WHITE_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TILT = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> CROWING_TIME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> WAKEY_TIME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> TILT_FOR_SLEEP = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> MALE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> TAME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);

	protected static final RawAnimation ROOSTER_WALK = RawAnimation.begin().thenLoop("chicken.animation.rooster_walk");
	protected static final RawAnimation ROOSTER_SLEEP = RawAnimation.begin().thenPlayAndHold("chicken.animation.rooster_sleep");
	protected static final RawAnimation ROOSTER_WAKE_UP = RawAnimation.begin().thenPlayAndHold("chicken.animation.rooster_wake_up");
	protected static final RawAnimation ROOSTER_CROW = RawAnimation.begin().thenPlayAndHold("chicken.animation.rooster_crow");

	protected static final RawAnimation HEN_WALK = RawAnimation.begin().thenLoop("chicken.animation.hen_walk");
	protected static final RawAnimation HEN_SLEEP = RawAnimation.begin().thenPlayAndHold("chicken.animation.hen_sleep");
	protected static final RawAnimation HEN_WAKE_UP = RawAnimation.begin().thenPlayAndHold("chicken.animation.hen_wake_up");

	protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("chicken.animation.idle");

	public BBChicken(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
		this.lookControl = new BBChicken.ChickenLookControl();
		this.moveControl = new BBChicken.ChickenMoveControl();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
		this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.5D));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1D));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
//		this.goalSelector.addGoal(7, new BBChicken.SleepGoal());
		this.goalSelector.addGoal(6, new BBChicken.SeekShelterGoal(this, 1.25D));

		super.registerGoals();
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.15);
	}
	
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(COLOR, 0);
		this.entityData.define(BLACK_MARKING, 0);
		this.entityData.define(WHITE_MARKING, 0);
		this.entityData.define(TILT, 0);
		this.entityData.define(CROWING_TIME, 0);
		this.entityData.define(WAKEY_TIME, 0);
		this.entityData.define(TILT_FOR_SLEEP, 1F);
		this.entityData.define(MALE, false);
		this.entityData.define(TAME, false);
		this.entityData.define(IS_SLEEPING, false);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Color", this.getColor());
		tag.putInt("BlackMarking", this.getBlackMarking());
		tag.putInt("WhiteMarking", this.getWhiteMarking());
		tag.putInt("Tilt", this.getTilt());
		tag.putFloat("TiltForSleep", this.getTiltForSleep());
		tag.putBoolean("Male", this.getIsMale());
		tag.putBoolean("Tame", this.getIsTame());
		tag.putBoolean("IsSleeping", this.getIsSleeping());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		this.setColor(tag.getInt("Color"));
		this.setBlackMarking(tag.getInt("BlackMarking"));
		this.setWhiteMarking(tag.getInt("WhiteMarking"));
		this.setTilt(tag.getInt("Tilt"));
		this.setTiltForSleep(tag.getFloat("TiltForSleep"));
		this.setIsMale(tag.getBoolean("Male"));
		this.setIsTame(tag.getBoolean("Tame"));
		this.setIsSleeping(tag.getBoolean("IsSleeping"));
	}

	public int getWakeyTime() {
		return this.entityData.get(WAKEY_TIME);
	}

	public void setWakeyTime(int color){
		this.entityData.set(WAKEY_TIME, color);
	}

	public int getCrowingTime() {
		return this.entityData.get(CROWING_TIME);
	}

	public void setCrowingTime(int color){
		this.entityData.set(CROWING_TIME, color);
	}

	public int getColor() {
		return this.entityData.get(COLOR);
	}

	public void setColor(int color){
			this.entityData.set(COLOR, color);
	}
	
	public int getBlackMarking() {
		return this.entityData.get(BLACK_MARKING);
	}
	
	public void setBlackMarking(int marking) {
		this.entityData.set(BLACK_MARKING, marking);
	}
	
	public int getWhiteMarking() {
		return this.entityData.get(WHITE_MARKING);
	}
	
	public void setWhiteMarking(int marking) {
		this.entityData.set(WHITE_MARKING, marking);
	}
	
	public int getTilt() {
		return this.entityData.get(TILT);
	}
	
	public void setTilt(int tilt) {
		this.entityData.set(TILT, tilt);
	}

	public float getTiltForSleep() {
		return this.entityData.get(TILT_FOR_SLEEP);
	}

	public void setTiltForSleep(float tilt) {
		this.entityData.set(TILT_FOR_SLEEP, tilt);
	}
	
	public boolean getIsMale() {
		return this.entityData.get(MALE);
	}
	
	public void setIsMale(boolean male) {
		this.entityData.set(MALE, male);
	}

	public boolean getIsTame() {
		return this.entityData.get(TAME);
	}

	public void setIsTame(boolean male) {
		this.entityData.set(TAME, male);
	}

	public boolean getIsSleeping() {
		return this.entityData.get(TAME);
	}

	public void setIsSleeping(boolean male) {
		this.entityData.set(TAME, male);
	}

	public boolean isFood(ItemStack pStack) {
		return FOOD_ITEMS.test(pStack);
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		this.setColor(random.nextInt(6));
		this.setBlackMarking(random.nextInt(3));
		this.setWhiteMarking(random.nextInt(3));
		this.setTilt(random.nextInt(5));
		this.setIsMale(random.nextBoolean());
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		BBChicken otherParent = (BBChicken) pOtherParent;
		BBChicken baby = BBEntities.BBCHICKEN.get().create(pLevel);
		if (baby != null){
			int colorChance = this.random.nextInt(100);
			int tiltChance = this.random.nextInt(100);

			int variant;
			int blackMarkings;
			int whiteMarkings;
			int tilt;

			if (this.getColor() == 3 && otherParent.getColor() == 4 && colorChance < 25){ //red and yellow
				variant = 5; //orange
			}else if (this.getColor() == 3 && otherParent.getColor() == 0 && colorChance < 25){ //red and white
				variant = 4; //yellow
			}else if (this.getColor() == 2 && otherParent.getColor() == 0 && colorChance < 10){ //black and white
				variant = 1; //grey
			}else {
				variant = this.random.nextBoolean() ? this.getColor(): otherParent.getColor();
			}

			blackMarkings = this.random.nextBoolean() ? this.getBlackMarking(): otherParent.getBlackMarking();
			whiteMarkings = this.random.nextBoolean() ? this.getWhiteMarking(): otherParent.getWhiteMarking();

			if (this.getTilt() == otherParent.getColor() && tiltChance < 25 && this.getTilt() < 4){
				tilt = this.getTilt()+1;
			}else {
				tilt = this.random.nextBoolean() ? this.getTilt() : otherParent.getTilt();
			}

			baby.setColor(variant);
			baby.setBlackMarking(blackMarkings);
			baby.setWhiteMarking(whiteMarkings);
			baby.setTilt(tilt);
			baby.setIsMale(this.random.nextBoolean());

		}

		return baby;
	}

	public boolean canMate(Animal pOtherAnimal) {

		if (!(pOtherAnimal instanceof BBChicken)) {
			return false;
		} else {
			BBChicken otherChicken = (BBChicken)pOtherAnimal;
			boolean flag = this.getIsMale() == !otherChicken.getIsMale();

			return flag && super.canMate(pOtherAnimal);
		}
	}

	@Override
	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);

		if (itemstack.is(Items.MELON_SLICE) && !this.getIsTame()){
			this.usePlayerItem(pPlayer, pHand, itemstack);

			if (this.random.nextInt(5) == 0) {
				this.setIsTame(true);
				this.navigation.stop();
				for(int j = 0; j < 5; ++j) {
					this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1), this.getRandomY() + 0.5, this.getRandomZ(1), 0.0, 0.0, 0.0);
				}
			}else{
				for(int j = 0; j < 5; ++j) {
					this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(1), this.getRandomY() + 0.5, this.getRandomZ(1), 0.0, 0.0, 0.0);
				}
			}

			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}

		if (this.getIsTame() && pPlayer.getItemInHand(pHand).isEmpty() && pHand == InteractionHand.MAIN_HAND){
			for(int j = 0; j < 5; ++j) {
				this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1), this.getRandomY() + 0.5, this.getRandomZ(1), 0.0, 0.0, 0.0);
			}
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}

		return super.mobInteract(pPlayer, pHand);
	}

	@Override
	public void aiStep() {

		if (this.getIsSleeping() || this.isImmobile()) {
			this.jumping = false;
			this.xxa = 0.0F;
			this.zza = 0.0F;
		}

		super.aiStep();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isEffectiveAi()){

			if (this.getIsSleeping() && this.getTiltForSleep() > 0){
				float prevTilt = this.getTiltForSleep();
				this.setTiltForSleep(prevTilt-0.025F);
			}

			if ((!this.getIsSleeping() || this.getWakeyTime() > 0) && this.getTiltForSleep() < 1){
				float prevTilt = this.getTiltForSleep();
				this.setTiltForSleep(prevTilt+0.025F);
			}

			if (sleep_ticks_when_interrupted > 0){
				--sleep_ticks_when_interrupted;
			}

			if (this.level().isDay() && ticks_till_sleep < 230){
				ticks_till_sleep = this.random.nextInt(240, 260);
			}

			if (this.getWakeyTime()>0){
				if (this.getWakeyTime()==1 && this.getIsSleeping()){
					if (this.getIsMale()){
						this.setCrowingTime(20);
					}else {
						this.wakeUp();
					}
				}
				int prevWake = this.getWakeyTime();
				this.setWakeyTime(prevWake-1);
			}

			if (this.getCrowingTime()>0){
				if (this.getIsMale()){
					int prevCrowingTime = this.getCrowingTime();
					if (this.getCrowingTime() == 20){
						this.playSound(BBSounds.ROOSTER_CROW.get());


					}

					if (this.getCrowingTime() == 1){
						this.wakeUp();
					}
					this.setCrowingTime(prevCrowingTime-1);
				}else {
					this.setCrowingTime(0);
					this.wakeUp();
				}

			}

			if (this.level().isNight() && this.getWakeyTime()!=0){
				setWakeyTime(0);
			}

			if (this.level().isDay() && this.getIsSleeping() && this.random.nextInt(10)==0 && this.getWakeyTime()==0){
				setWakeyTime(60);
			}

			if (this.level().isNight() && !this.level().canSeeSky(this.blockPosition()) && sleep_ticks_when_interrupted==0){
				if (!this.getIsSleeping()){
					this.setIsSleeping(true);
					this.getNavigation().stop();
					ticks_till_sleep = 0;
				}
			}

			if (this.level().isNight() && ticks_till_sleep>0 && sleep_ticks_when_interrupted==0){
				if (ticks_till_sleep == 1){
					this.setIsSleeping(true);
					this.getNavigation().stop();
				}
				--ticks_till_sleep;
			}

			if (this.getIsSleeping()){
				this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
			}

			if (this.isInWater() && this.isSleeping()) {
				this.wakeUp();
				sleep_ticks_when_interrupted = this.random.nextInt(240, 260);
			}
		}
	}

	public void wakeUp(){
		this.goalSelector.getRunningGoals().forEach(WrappedGoal::start);
		this.setIsSleeping(false);
		setWakeyTime(0);
		this.setCrowingTime(0);
	}

	class ChickenMoveControl extends MoveControl {
		public ChickenMoveControl() {
			super(BBChicken.this);
		}

		public void tick() {
			if (BBChicken.this.canMove()) {
				super.tick();
			}

		}
	}

	public class ChickenLookControl extends LookControl {
		public ChickenLookControl() {
			super(BBChicken.this);
		}

		public void tick() {
			if (!BBChicken.this.getIsSleeping()) {
				super.tick();
			}
		}
	}

	boolean canMove() {
		return !this.getIsSleeping();
	}

//	protected boolean hasShelter() {
//		BlockPos blockpos = BlockPos.containing(this.getX(), this.getBoundingBox().maxY, this.getZ());
//		return !this.level().canSeeSky(blockpos) && this.getWalkTargetValue(blockpos) >= 0.0F;
//	}

//	class SleepGoal extends Goal {
//
//		private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140);
//		private int countdown = BBChicken.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
//
//		public SleepGoal() {
//			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
//		}
//
//		public boolean canUse() {
//			if (BBChicken.this.xxa == 0.0F && BBChicken.this.yya == 0.0F && BBChicken.this.zza == 0.0F) {
//				return this.canSleep() || BBChicken.this.getIsSleeping();
//			} else {
//				return false;
//			}
//		}
//
//		public boolean canContinueToUse() {
//			return this.canSleep();
//		}
//
//		private boolean canSleep() {
//			if (this.countdown > 0) {
//				--this.countdown;
//				return false;
//			} else {
//				return BBChicken.this.level().isNight() && this.hasShelter() && !BBChicken.this.isInPowderSnow;
//			}
//		}
//
//
//		public void stop() {
//			this.countdown = BBChicken.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
//			BBChicken.this.setIsSleeping(false);
//		}
//
//		public void start() {
//			BBChicken.this.setIsSleeping(true);
//			BBChicken.this.getNavigation().stop();
//			BBChicken.this.getMoveControl().setWantedPosition(BBChicken.this.getX(), BBChicken.this.getY(), BBChicken.this.getZ(), 0.0D);
//		}
//	}

	class SeekShelterGoal extends Goal {
		protected final BBChicken mob;
		private double wantedX;
		private double wantedY;
		private double wantedZ;
		private final double speedModifier;
		private final Level level;

		public SeekShelterGoal(BBChicken pMob, double pSpeedModifier) {
			this.mob = pMob;
			this.speedModifier = pSpeedModifier;
			this.level = pMob.level();
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			if (!mob.getIsSleeping() && mob.ticks_till_sleep>0) {
				if (mob.level().isThundering() && BBChicken.this.level().canSeeSky(this.mob.blockPosition())) {
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
			mob.setIsSleeping(false);
			mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
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

		public boolean canContinueToUse() {
			return !this.mob.getNavigation().isDone();
		}


		@Nullable
		protected Vec3 getHidePos() {
			RandomSource randomsource = this.mob.getRandom();
			BlockPos blockpos = this.mob.blockPosition();

			for(int i = 0; i < 10; ++i) {
				BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(20) - 10, randomsource.nextInt(6) - 3,
						randomsource.nextInt(20) - 10);
				if (!this.level.canSeeSky(blockpos1) && this.mob.getWalkTargetValue(blockpos1) < 0.0F) {
					return Vec3.atBottomCenterOf(blockpos1);
				}
			}

			return null;
		}
	}

	protected void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
		if (this.getIsSleeping()){
			this.wakeUp();
			this.ticks_till_sleep = this.random.nextInt(240, 260);
			this.sleep_ticks_when_interrupted = this.random.nextInt(240, 260);
		}

		super.actuallyHurt(pDamageSource, pDamageAmount);
	}

	public String getSex(){
		return this.getIsMale() ? "rooster" : "hen";
	}

	public String getColorName(){
		return switch(this.getColor()){
			case 1 -> "grey";
			case 2 -> "black";
			case 3 -> "red";
			case 4 -> "yellow";
			case 5 -> "orange";
			default ->"white";
		};
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController[]{new AnimationController(this, "Normal", 5, this::Controller)});
	}

	protected <E extends BBChicken> PlayState Controller(AnimationState<E> event) {
		if (this.getIsMale() && this.getCrowingTime()>0){
			event.setAndContinue(ROOSTER_CROW);
		}else if (this.getIsSleeping() && this.getWakeyTime()>0){
			event.setAndContinue(this.getIsMale() ? ROOSTER_WAKE_UP : HEN_WAKE_UP);
		}else if (this.getIsSleeping() && this.getWakeyTime()==0 && this.getCrowingTime()==0){
			event.setAndContinue(this.getIsMale() ? ROOSTER_SLEEP : HEN_SLEEP);
		}else if(this.getDeltaMovement().horizontalDistanceSqr() > 0.00005 && !this.getIsSleeping()) {
			event.setAndContinue(this.getIsMale() ? ROOSTER_WALK : HEN_WALK);
		} else{
			event.setAndContinue(IDLE);
		}
		return PlayState.CONTINUE;
	}

	protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
		this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.getIsSleeping() ? null : SoundEvents.CHICKEN_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.CHICKEN_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.CHICKEN_DEATH;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}
}
