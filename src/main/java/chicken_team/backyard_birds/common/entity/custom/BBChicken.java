package chicken_team.backyard_birds.common.entity.custom;

import javax.annotation.Nullable;

import chicken_team.backyard_birds.client.screen.BBChickenMenu;
import chicken_team.backyard_birds.client.sound.BBSounds;
import chicken_team.backyard_birds.common.block.BBBlocks;
import chicken_team.backyard_birds.common.entity.BBEntities;
import chicken_team.backyard_birds.common.entity.custom.ai.ChickenSeekClosestShelterGoal;
import chicken_team.backyard_birds.common.entity.custom.ai.ChickenSeekRandomShelterGoal;
import chicken_team.backyard_birds.common.entity.custom.inventory.ChickenContainer;
import chicken_team.backyard_birds.common.items.BBItems;
import chicken_team.backyard_birds.common.network.BBNetworkHandler;
import chicken_team.backyard_birds.common.network.OpenChickenScreenPacket;
import chicken_team.backyard_birds.util.BBTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public class BBChicken extends Animal implements GeoEntity, ContainerListener {
	public int ticks_till_sleep = 0;
	public int sleep_ticks_when_interrupted = 0;
	public int ticks_without_shelter = 0;
	public int ticks_without_better_shelter = 0;
	public int ticks_without_best_shelter = 0;
	public int featherTime = this.random.nextInt(600) + 6000;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

	private static final Ingredient FOOD_ITEMS = Ingredient.of(BBItems.MULTIPURPOSE_FEED.get());

	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BLACK_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> WHITE_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TILT = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> CROWING_TIME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> WAKEY_TIME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> TILT_FOR_SLEEP = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> MALE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> TAME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LOOKING_FOR_SHELTER = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DOMINANT = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> PREENING_TIME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> HAS_PARENTS = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);

	private static final EntityDataAccessor<String> MOM_NAME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> MOM_COLOR = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MOM_BLACK_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MOM_WHITE_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MOM_TILT = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MOM_SIZE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);

	private static final EntityDataAccessor<String> DAD_NAME = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> DAD_COLOR = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DAD_BLACK_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DAD_WHITE_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DAD_TILT = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DAD_SIZE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);

	protected static final RawAnimation ROOSTER_WALK = RawAnimation.begin().thenLoop("chicken.animation.rooster_walk");
	protected static final RawAnimation ROOSTER_SLEEP = RawAnimation.begin().thenPlayAndHold("chicken.animation.rooster_sleep");
	protected static final RawAnimation ROOSTER_WAKE_UP = RawAnimation.begin().thenPlay("chicken.animation.rooster_wake_up");
	protected static final RawAnimation ROOSTER_CROW = RawAnimation.begin().thenPlay("chicken.animation.rooster_crow");
	protected static final RawAnimation ROOSTER_PREEN = RawAnimation.begin().thenPlay("chicken.animation.rooster_preen");
	protected static final RawAnimation ROOSTER_KICK = RawAnimation.begin().thenPlay("chicken.animation.rooster_kick_attack");

	protected static final RawAnimation HEN_WALK = RawAnimation.begin().thenLoop("chicken.animation.hen_walk");
	protected static final RawAnimation HEN_SLEEP = RawAnimation.begin().thenPlayAndHold("chicken.animation.hen_sleep");
	protected static final RawAnimation HEN_WAKE_UP = RawAnimation.begin().thenPlay("chicken.animation.hen_wake_up");
	protected static final RawAnimation HEN_PREEN = RawAnimation.begin().thenPlay("chicken.animation.hen_preen");

	protected static final RawAnimation CHICK_WALK = RawAnimation.begin().thenLoop("chicken.animation.chick_walk");
	protected static final RawAnimation CHICK_SLEEP = RawAnimation.begin().thenPlayAndHold("chicken.animation.chick_sleep");
	protected static final RawAnimation CHICK_WAKE_UP = RawAnimation.begin().thenPlay("chicken.animation.chick_wake_up");
	protected static final RawAnimation CHICK_IDLE = RawAnimation.begin().thenLoop("chicken.animation.chick_idle");

	protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("chicken.animation.idle");

	public BBChicken(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
		this.createInventory();
		this.lookControl = new BBChicken.ChickenLookControl();
		this.moveControl = new BBChicken.ChickenMoveControl();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new BBChickenMeleeAttackGoal(this, 1.5D, true){

			@Override
			public boolean canUse() {
				return super.canUse() && BBChicken.this.getIsDominant() && BBChicken.this.getIsMale() && !BBChicken.this.isBaby();
			}

			@Override
			public boolean canContinueToUse() {
				return super.canContinueToUse() && BBChicken.this.getIsDominant() && BBChicken.this.getIsMale() && !BBChicken.this.isBaby();
			}
		});
//		this.goalSelector.addGoal(1, new PanicGoal(this, 3D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 2D, FOOD_ITEMS, false));
		this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.5D));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1D));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(1, new ChickenSeekRandomShelterGoal(this, 1.5D));
		this.goalSelector.addGoal(1, new ChickenSeekClosestShelterGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, BBChicken.class, 10, false, false,
				(chicken) -> BBChicken.this.getIsDominant() && ((BBChicken) chicken).getIsDominant()
						&& this.level().isDay() && chicken.level().isDay()
						&& !this.isBaby() && !chicken.isBaby()
						&& (((BBChicken) chicken).getTarget()==null || ((BBChicken) chicken).getTarget()==this)
						&& BBChicken.this.getIsMale() && ((BBChicken) chicken).getIsMale()));
		super.registerGoals();
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.15)
				.add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(COLOR, 0);
		this.entityData.define(BLACK_MARKING, 0);
		this.entityData.define(WHITE_MARKING, 0);
		this.entityData.define(TILT, 0);
		this.entityData.define(SIZE, 2);
		this.entityData.define(MALE, false);
		this.entityData.define(CROWING_TIME, 0);
		this.entityData.define(WAKEY_TIME, 0);
		this.entityData.define(PREENING_TIME, 0);
		this.entityData.define(TILT_FOR_SLEEP, 1F);
		this.entityData.define(TAME, false);
		this.entityData.define(IS_SLEEPING, false);
		this.entityData.define(LOOKING_FOR_SHELTER, false);
		this.entityData.define(DOMINANT, false);
		this.entityData.define(HAS_PARENTS, false);
		this.entityData.define(ANIMATION_STATE, 0);

		this.entityData.define(MOM_NAME, "Chicken");
		this.entityData.define(MOM_COLOR, 0);
		this.entityData.define(MOM_BLACK_MARKING, 0);
		this.entityData.define(MOM_WHITE_MARKING, 0);
		this.entityData.define(MOM_TILT, 0);
		this.entityData.define(MOM_SIZE, 2);

		this.entityData.define(DAD_NAME, "Chicken");
		this.entityData.define(DAD_COLOR, 0);
		this.entityData.define(DAD_BLACK_MARKING, 0);
		this.entityData.define(DAD_WHITE_MARKING, 0);
		this.entityData.define(DAD_TILT, 0);
		this.entityData.define(DAD_SIZE, 2);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Color", this.getColor());
		tag.putInt("BlackMarking", this.getBlackMarking());
		tag.putInt("WhiteMarking", this.getWhiteMarking());
		tag.putInt("Tilt", this.getTilt());
		tag.putInt("Size", this.getSize());
		tag.putBoolean("Male", this.getIsMale());

		tag.putFloat("TiltForSleep", this.getTiltForSleep());
		tag.putBoolean("Tame", this.getIsTame());
		tag.putBoolean("IsSleeping", this.getIsSleeping());
		tag.putBoolean("LookingForShelter", this.getLookingForShelter());
		tag.putBoolean("Dominant", this.getIsDominant());
		tag.putBoolean("HasParents", this.getHasParents());


		tag.putString("MomName", this.getMomName());
		tag.putInt("MomColor", this.getMomColor());
		tag.putInt("MomBlackMarking", this.getMomBlackMarking());
		tag.putInt("MomWhiteMarking", this.getMomWhiteMarking());
		tag.putInt("MomTilt", this.getMomTilt());
		tag.putInt("MomSize", this.getMomSize());

		tag.putString("DadName", this.getDadName());
		tag.putInt("DadColor", this.getDadColor());
		tag.putInt("DadBlackMarking", this.getDadBlackMarking());
		tag.putInt("DadWhiteMarking", this.getDadWhiteMarking());
		tag.putInt("DadTilt", this.getDadTilt());
		tag.putInt("DadSize", this.getDadSize());
	}


	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setColor(tag.getInt("Color"));
		this.setBlackMarking(tag.getInt("BlackMarking"));
		this.setWhiteMarking(tag.getInt("WhiteMarking"));
		this.setTilt(tag.getInt("Tilt"));
		this.setSize(tag.getInt("Size"));
		this.setTiltForSleep(tag.getFloat("TiltForSleep"));
		this.setIsMale(tag.getBoolean("Male"));
		this.setIsTame(tag.getBoolean("Tame"));
		this.setIsSleeping(tag.getBoolean("IsSleeping"));
		this.setLookingForShelter(tag.getBoolean("LookingForShelter"));
		this.setIsDominant(tag.getBoolean("Dominant"));
		this.setHasParents(tag.getBoolean("HasParents"));

		this.setMomName(tag.getString("MomName"));
		this.setMomColor(tag.getInt("MomColor"));
		this.setMomBlackMarking(tag.getInt("MomBlackMarking"));
		this.setMomWhiteMarking(tag.getInt("MomWhiteMarking"));
		this.setMomTilt(tag.getInt("MomTilt"));
		this.setMomSize(tag.getInt("MomSize"));

		this.setDadName(tag.getString("DadName"));
		this.setDadColor(tag.getInt("DadColor"));
		this.setDadBlackMarking(tag.getInt("DadBlackMarking"));
		this.setDadWhiteMarking(tag.getInt("DadWhiteMarking"));
		this.setDadTilt(tag.getInt("DadTilt"));
		this.setDadSize(tag.getInt("DadSize"));
	}

	public int getPreeningTime() {
		return this.entityData.get(PREENING_TIME);
	}

	public void setPreeningTime(int color){
		this.entityData.set(PREENING_TIME, color);
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

	public int getSize() {
		return this.entityData.get(SIZE);
	}

	public void setSize(int color){
		this.entityData.set(SIZE, color);
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

	public boolean getHasParents() {
		return this.entityData.get(HAS_PARENTS);
	}

	public void setHasParents(boolean male) {
		this.entityData.set(HAS_PARENTS, male);
	}

	public boolean getIsTame() {
		return this.entityData.get(TAME);
	}

	public void setIsTame(boolean male) {
		this.entityData.set(TAME, male);
	}

	public boolean getIsSleeping() {
		return this.entityData.get(IS_SLEEPING);
	}

	public void setIsSleeping(boolean male) {
		this.entityData.set(IS_SLEEPING, male);
	}

	public boolean getLookingForShelter() {
		return this.entityData.get(LOOKING_FOR_SHELTER);
	}

	public void setLookingForShelter(boolean male) {
		this.entityData.set(LOOKING_FOR_SHELTER, male);
	}

	public boolean getIsDominant() {
		return this.entityData.get(DOMINANT);
	}

	public void setIsDominant(boolean male) {
		this.entityData.set(DOMINANT, male);
	}

	public boolean isFood(ItemStack pStack) {
		return FOOD_ITEMS.test(pStack);
	}

	public int getAnimationState() {
		return this.entityData.get(ANIMATION_STATE);
	}

	public void setAnimationState(int anim) {
		this.entityData.set(ANIMATION_STATE, anim);
	}



	public String getMomName() {
		return this.entityData.get(MOM_NAME);
	}

	public void setMomName(String color){
		this.entityData.set(MOM_NAME, color);
	}

	public int getMomSize() {
		return this.entityData.get(MOM_SIZE);
	}

	public void setMomSize(int color){
		this.entityData.set(MOM_SIZE, color);
	}

	public int getMomColor() {
		return this.entityData.get(MOM_COLOR);
	}

	public void setMomColor(int color){
		this.entityData.set(MOM_COLOR, color);
	}

	public int getMomBlackMarking() {
		return this.entityData.get(MOM_BLACK_MARKING);
	}

	public void setMomBlackMarking(int marking) {
		this.entityData.set(MOM_BLACK_MARKING, marking);
	}

	public int getMomWhiteMarking() {
		return this.entityData.get(MOM_WHITE_MARKING);
	}

	public void setMomWhiteMarking(int marking) {
		this.entityData.set(MOM_WHITE_MARKING, marking);
	}

	public int getMomTilt() {
		return this.entityData.get(MOM_TILT);
	}

	public void setMomTilt(int tilt) {
		this.entityData.set(MOM_TILT, tilt);
	}



	public String getDadName() {
		return this.entityData.get(DAD_NAME);
	}

	public void setDadName(String color){
		this.entityData.set(DAD_NAME, color);
	}

	public int getDadSize() {
		return this.entityData.get(DAD_SIZE);
	}

	public void setDadSize(int color){
		this.entityData.set(DAD_SIZE, color);
	}

	public int getDadColor() {
		return this.entityData.get(DAD_COLOR);
	}

	public void setDadColor(int color){
		this.entityData.set(DAD_COLOR, color);
	}

	public int getDadBlackMarking() {
		return this.entityData.get(DAD_BLACK_MARKING);
	}

	public void setDadBlackMarking(int marking) {
		this.entityData.set(DAD_BLACK_MARKING, marking);
	}

	public int getDadWhiteMarking() {
		return this.entityData.get(DAD_WHITE_MARKING);
	}

	public void setDadWhiteMarking(int marking) {
		this.entityData.set(DAD_WHITE_MARKING, marking);
	}

	public int getDadTilt() {
		return this.entityData.get(DAD_TILT);
	}

	public void setDadTilt(int tilt) {
		this.entityData.set(DAD_TILT, tilt);
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		int color = getRandomColorVariant(worldIn, this.blockPosition());
		int size = random.nextInt( 1, 4);
		int white;
		int black;
		if (this.random.nextBoolean()){
			black = random.nextInt(3);
		}else {
			black = 0;
		}
		if (this.random.nextBoolean()){
			white = random.nextInt(3);
		}else {
			white = 0;
		}
		int tilt = random.nextInt(2);

		this.setColor(color);
		this.setSize(size);
		this.setBlackMarking(black);
		this.setWhiteMarking(white);
		this.setTilt(tilt);
		boolean male = random.nextBoolean();
		this.setIsMale(male);
		this.setIsDominant(male && random.nextInt(4) == 0);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	private int getRandomColorVariant(LevelAccessor pLevel, BlockPos pPos) {
		Holder<Biome> holder = pLevel.getBiome(pPos);
		int i = pLevel.getRandom().nextInt(100);

		if (this.random.nextInt(25)==0){
			return this.random.nextInt(6);
		}else{
			if (holder.is(Tags.Biomes.IS_COLD_OVERWORLD)){
				return i < 30 ? 0 : i < 65 ? 1 : 2;
			} else if (holder.is(Tags.Biomes.IS_HOT_OVERWORLD)){
				return i < 25 ? 0 : i < 50 ? 3 : i < 75 ? 4 : 5;
			} else {
				if (this.random.nextInt(4)==0){
					return this.random.nextInt(6);
				}
				else {
					return 0;
				}
			}
		}

	}

	@Override
	public BBChicken getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		BBChicken otherParent = (BBChicken) pOtherParent;
		BBChicken baby = BBEntities.BBCHICKEN.get().create(pLevel);

		BBChicken mom = this.getIsMale() ? otherParent : this;
		BBChicken dad = otherParent.getIsMale() ? otherParent : this;

		if (baby != null){
			int colorChance = this.random.nextInt(100);
			int tiltChance = this.random.nextInt(100);
			int sizeChance = this.random.nextInt(100);

			int variant;
			int blackMarkings;
			int whiteMarkings;
			int tilt;
			int size;

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

			if (this.getTilt() == otherParent.getTilt() && tiltChance < 25 && this.getTilt() < 4){
				tilt = this.getTilt()+1;
			}else {
				tilt = this.random.nextBoolean() ? this.getTilt() : otherParent.getTilt();
			}

			if (this.getSize() == otherParent.getSize() && sizeChance < 15 && this.getSize()>0 && this.getSize()<4){
				size = this.random.nextBoolean() ? this.getSize()+1 : this.getSize()-1;
			}else{
				size = this.random.nextBoolean() ? this.getSize() : otherParent.getSize();
			}

			baby.setHasParents(true);

			baby.setColor(variant);
			baby.setBlackMarking(blackMarkings);
			baby.setWhiteMarking(whiteMarkings);
			baby.setTilt(tilt);
			baby.setSize(size);
			baby.setIsMale(this.random.nextBoolean());

			baby.setDadName(dad.getName().getString());
			baby.setDadSize(dad.getSize());
			baby.setDadColor(dad.getColor());
			baby.setDadTilt(dad.getTilt());
			baby.setDadWhiteMarking(dad.getWhiteMarking());
			baby.setDadBlackMarking(dad.getBlackMarking());

			baby.setMomName(mom.getName().getString());
			baby.setMomSize(mom.getSize());
			baby.setMomColor(mom.getColor());
			baby.setMomTilt(mom.getTilt());
			baby.setMomWhiteMarking(mom.getWhiteMarking());
			baby.setMomBlackMarking(mom.getBlackMarking());
		}

		return baby;
	}

	@Override
	public void spawnChildFromBreeding(ServerLevel pLevel, Animal pMate) {
		AgeableMob ageablemob = this.getBreedOffspring(pLevel, pMate);
		AgeableMob ageableMob2 = null;
		AgeableMob ageableMob3 = null;
		AgeableMob ageableMob4 = null;
		AgeableMob ageableMob5 = null;

		final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this, pMate, ageablemob);
		ageablemob = event.getChild();

		if (this.random.nextBoolean() || this.random.nextBoolean()){
			ageableMob2 = this.getBreedOffspring(pLevel, pMate);
			final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event2 = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this, pMate, ageableMob2);
			ageableMob2 = event2.getChild();

			if (this.random.nextBoolean()){
				ageableMob3 = this.getBreedOffspring(pLevel, pMate);
				final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event3 = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this, pMate, ageableMob3);
				ageableMob3 = event3.getChild();

				if (this.random.nextBoolean()){

					ageableMob4 = this.getBreedOffspring(pLevel, pMate);
					final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event4 = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this, pMate, ageableMob4);
					ageableMob4 = event4.getChild();

					if (this.random.nextBoolean()){
						ageableMob5 = this.getBreedOffspring(pLevel, pMate);
						final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event5 = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this, pMate, ageableMob5);
						ageableMob5 = event5.getChild();
					}
				}
			}
		}

		final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		if (cancelled) {
			//Reset the "inLove" state for the animals
			this.setAge(6000);
			pMate.setAge(6000);
			this.resetLove();
			pMate.resetLove();
			return;
		}
		if (ageablemob != null) {
			Animal mom = this.getIsMale() ? pMate : this;

			ageablemob.setBaby(true);
			ageablemob.moveTo(mom.getX(), mom.getY(), mom.getZ(), 0.0F, 0.0F);
			this.finalizeSpawnChildFromBreeding(pLevel, pMate, ageablemob);
			pLevel.addFreshEntityWithPassengers(ageablemob);

			if (ageableMob2 != null){

				ageableMob2.setBaby(true);
				ageableMob2.moveTo(mom.getX(), mom.getY(), mom.getZ(), 0.0F, 0.0F);
				this.finalizeSpawnChildFromBreeding(pLevel, pMate, ageableMob2);
				pLevel.addFreshEntityWithPassengers(ageableMob2);

				if (ageableMob3 != null){

					ageableMob3.setBaby(true);
					ageableMob3.moveTo(mom.getX(), mom.getY(), mom.getZ(), 0.0F, 0.0F);
					this.finalizeSpawnChildFromBreeding(pLevel, pMate, ageableMob3);
					pLevel.addFreshEntityWithPassengers(ageableMob3);

					if (ageableMob4 != null){

						ageableMob4.setBaby(true);
						ageableMob4.moveTo(mom.getX(), mom.getY(), mom.getZ(), 0.0F, 0.0F);
						this.finalizeSpawnChildFromBreeding(pLevel, pMate, ageableMob4);
						pLevel.addFreshEntityWithPassengers(ageableMob4);

						if (ageableMob5 != null){

							ageableMob5.setBaby(true);
							ageableMob5.moveTo(mom.getX(), mom.getY(), mom.getZ(), 0.0F, 0.0F);
							this.finalizeSpawnChildFromBreeding(pLevel, pMate, ageableMob5);
							pLevel.addFreshEntityWithPassengers(ageableMob5);
						}
					}
				}
			}
		}
	}

	public boolean canMate(Animal pOtherAnimal) {

		if (!(pOtherAnimal instanceof BBChicken) || this.isBaby() || pOtherAnimal.isBaby()) {
			return false;
		} else {
			BBChicken otherChicken = (BBChicken)pOtherAnimal;

			Boolean flag = this.getIsMale() && !this.getIsDominant();
			Boolean flag2 = otherChicken.getIsMale() && !otherChicken.getIsDominant();

			return !(flag || flag2) && this.getIsMale() == !otherChicken.getIsMale() && super.canMate(pOtherAnimal);
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

		if (this.getIsDominant() && itemstack.is(Items.FEATHER)){
			this.setIsDominant(false);
			this.usePlayerItem(pPlayer, pHand, itemstack);
			return InteractionResult.SUCCESS;
		}

		if (!this.getIsDominant() && itemstack.is(BBItems.ROOSTER_FEATHER.get())){
			this.setIsDominant(true);
			this.usePlayerItem(pPlayer, pHand, itemstack);
			return InteractionResult.SUCCESS;
		}

		if (this.getIsTame() && pPlayer.getItemInHand(pHand).isEmpty() && pHand == InteractionHand.MAIN_HAND){
			for(int j = 0; j < 5; ++j) {
				this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1), this.getRandomY() + 0.5, this.getRandomZ(1), 0.0, 0.0, 0.0);
			}
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}

		if(itemstack.is(Items.BOOK)){
			this.openGUI(pPlayer);
			return InteractionResult.SUCCESS;
		}

		return super.mobInteract(pPlayer, pHand);
	}

	private boolean inventoryOpen;
	private ChickenContainer inventory;
	private LazyOptional<?> itemHandler = null;

	private void createInventory() {
		SimpleContainer simplecontainer = this.getInventory();
		this.inventory = new ChickenContainer(this);
		if (simplecontainer != null) {
			simplecontainer.removeListener(this);
			int i = Math.min(simplecontainer.getContainerSize(), this.getInventory().getContainerSize());

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = simplecontainer.getItem(j);
				if (!itemstack.isEmpty()) {
					this.getInventory().setItem(j, itemstack.copy());
				}
			}
		}

		this.getInventory().addListener(this);
		this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.getInventory()));
	}

	public void openGUI(Player player) {
		if (!this.level().isClientSide()) {
			ServerPlayer sp = (ServerPlayer) player;
			if (sp.containerMenu != sp.inventoryMenu) {
				sp.closeContainer();
			}

			sp.nextContainerCounter();
			BBNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new OpenChickenScreenPacket(sp.containerCounter, this.getId()));
            sp.containerMenu = new BBChickenMenu(sp.containerCounter, this.getInventory(), sp.getInventory());
			sp.initMenu(sp.containerMenu);
			this.inventoryOpen = true;
			MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(sp, sp.containerMenu));
		}
	}

	@Nullable
	public SimpleContainer getInventory() {
		return this.inventory;
	}

	public void closeInventory() {
		this.inventoryOpen = false;
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}

	public void aiStep() {

		super.aiStep();

		if (this.getIsSleeping() || this.isImmobile()) {
			this.jumping = false;
			this.xxa = 0.0F;
			this.zza = 0.0F;
		}

		Vec3 vec3 = this.getDeltaMovement();
		if (!this.onGround() && vec3.y < 0.0D) {
			this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
		}
	}

	public void tick() {
		super.tick();
		if (this.isEffectiveAi()){

			if (this.getIsDominant() && this.getTarget()!=null){
				if (this.getTarget() instanceof BBChicken chicken && (!chicken.getIsDominant() || this.level().isNight())){
					this.setTarget(null);
				}
			}

			if (this.getIsDominant() && (!this.getIsMale() || this.isBaby())){
				this.setIsDominant(false);
			}

			if (this.getHealth()>this.getMaxHealth()/2 && this.random.nextInt(5000) == 0 && !this.getIsDominant()){
				this.setIsDominant(true);
			}

			if (this.getNavigation().isDone() && this.getLookingForShelter() && this.ticks_without_shelter==0){
				this.setLookingForShelter(false);
			}

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

			if (this.level().isDay() && (ticks_till_sleep < 1000 ||
					(this.level().getBiome(this.blockPosition()).is(BBTags.Biomes.OPEN_BIOMES)) && ticks_till_sleep < 500) ){
				if (this.level().getBiome(this.blockPosition()).is(BBTags.Biomes.OPEN_BIOMES) && !this.getIsTame()){
					ticks_till_sleep = this.random.nextInt(500, 600);
				}else{
					ticks_till_sleep = this.random.nextInt(1000, 1500);
				}
			}

			if (this.getWakeyTime()>0){
				if (this.getWakeyTime()==1 && this.getIsSleeping()){
					if (this.getIsMale() && !this.isBaby()){
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
						this.playSound(BBSounds.ROOSTER_CROW.get(), 1F, this.getVoicePitch());
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

			if (this.level().isDay() && this.getIsSleeping() && this.random.nextInt(100)==0 && this.getWakeyTime()==0){
				setWakeyTime(60);
			}

			if (!this.getLookingForShelter() && !this.getIsSleeping() && this.level().isNight() && sleep_ticks_when_interrupted==0 && this.random.nextInt(10) == 0
					&& (((this.hasShelter(this) && this.ticks_without_best_shelter==0 && this.ticks_without_better_shelter==0))
					|| ((this.hasBetterShelter(this) && this.ticks_without_best_shelter==0))
					|| (this.hasBestShelter(this)))){
				this.setIsSleeping(true);
				this.getNavigation().stop();
				ticks_till_sleep = 0;
			}


			if (this.level().isDay() && (this.ticks_without_best_shelter < 500 ||
					(this.level().getBiome(this.blockPosition()).is(BBTags.Biomes.OPEN_BIOMES) && this.ticks_without_best_shelter < 200))){

				if ( this.level().getBiome(this.blockPosition()).is(BBTags.Biomes.OPEN_BIOMES) && !this.getIsTame() ){
					this.ticks_without_shelter = this.random.nextInt(100, 200);
					this.ticks_without_better_shelter = this.random.nextInt(100, 200);
					this.ticks_without_best_shelter = this.random.nextInt(100, 200);
				}else{
					this.ticks_without_shelter = this.random.nextInt(500, 1500);
					this.ticks_without_better_shelter = this.random.nextInt(500, 1500);
					this.ticks_without_best_shelter = this.random.nextInt(500, 1500);
				}
			}

			if (this.level().isNight() && ticks_till_sleep>0 && sleep_ticks_when_interrupted==0
					&& !this.getLookingForShelter() && !this.getIsSleeping()){
				if (ticks_till_sleep == 1){
					this.setIsSleeping(true);
					this.getNavigation().stop();
					if (this.getLookingForShelter()){
						this.setLookingForShelter(false);
					}
				}

				if (ticks_without_best_shelter>0){
					--ticks_without_best_shelter;
				}else if (ticks_without_better_shelter>0){
					--ticks_without_better_shelter;
				}else if (ticks_without_shelter>0){
					--ticks_without_shelter;
				}else {
					--ticks_till_sleep;
				}
			}

			if (this.getIsSleeping()){
				this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
			}

			if (this.isInWater() && this.getIsSleeping()) {
				this.wakeUp();
				sleep_ticks_when_interrupted = this.random.nextInt(240, 260);
			}

			if (this.getTarget() != null) {
				if (this.getIsDominant() && getTarget() instanceof BBChicken otherChicken && this.random.nextInt(250)==0) {
					if (this.random.nextBoolean()){
						this.setIsDominant(false);
					}else {
						otherChicken.setIsDominant(false);
					}
				}
			}

			if (--this.featherTime == 0 && this.getPreeningTime()==0 && this.onGround() && !this.isBaby()){
				this.setPreeningTime(50);
				this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
				this.getNavigation().stop();
				featherTime = this.random.nextInt(600) + 6000;
			}

			if (this.getPreeningTime()>0){
				int prevPreenTime = this.getPreeningTime();
				this.setPreeningTime(prevPreenTime - 1);
				if (prevPreenTime==1){
					this.goalSelector.getRunningGoals().forEach(WrappedGoal::start);

					if (this.getIsMale()) {
						this.spawnAtLocation(BBItems.ROOSTER_FEATHER.get());
					}else{
						this.spawnAtLocation(Items.FEATHER);
					}
					this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

				}
			}
		}
	}

	public boolean hasShelter(BBChicken chicken){
		return !chicken.level().canSeeSky(chicken.blockPosition());
	}

	public boolean hasBetterShelter(BBChicken chicken){
		BlockState block = chicken.level().getBlockState(chicken.blockPosition());
		BlockState blockBelow = chicken.level().getBlockState(chicken.blockPosition().below());
		return (block.is(BBBlocks.ROOSTING_BAR.get()) || (blockBelow.is(BBBlocks.ROOSTING_BAR.get()) && block.is(Blocks.AIR)) ) ;
	}

	public boolean hasBestShelter (BBChicken chicken){
		BlockState block = chicken.level().getBlockState(chicken.blockPosition());
		BlockState blockBelow = chicken.level().getBlockState(chicken.blockPosition().below());
		return !chicken.level().canSeeSky(chicken.blockPosition()) && (block.is(BBBlocks.ROOSTING_BAR.get()) || (blockBelow.is(BBBlocks.ROOSTING_BAR.get()) && block.is(Blocks.AIR)) ) ;
	}

	public void wakeUp(){
		this.goalSelector.getRunningGoals().forEach(WrappedGoal::start);
		this.setIsSleeping(false);
		this.setLookingForShelter(false);
		setWakeyTime(0);
		this.setCrowingTime(0);
	}

	@Override
	public void containerChanged(Container pContainer) {}

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

		if (this.getPreeningTime()>0 && !this.isBaby()){
			event.setAndContinue(this.getIsMale() ? ROOSTER_PREEN : HEN_PREEN);
		}else if (this.getAnimationState()==21 && !this.isBaby() && this.getIsMale()){
			event.setAndContinue(ROOSTER_KICK);
		}else if (this.getIsMale() && this.getCrowingTime()>0 && !this.isBaby()){
			event.setAndContinue(ROOSTER_CROW);
		}else if (this.getIsSleeping() && this.getWakeyTime()>0){
			event.setAndContinue(this.isBaby() ? CHICK_WAKE_UP : this.getIsMale() ? ROOSTER_WAKE_UP : HEN_WAKE_UP);
		}else if (this.getIsSleeping() && this.getWakeyTime()==0 && this.getCrowingTime()==0){
			event.setAndContinue(this.isBaby() ? CHICK_SLEEP : this.getIsMale() ? ROOSTER_SLEEP : HEN_SLEEP);
		}else if(this.getDeltaMovement().horizontalDistanceSqr() > 0.00005 && !this.getIsSleeping()) {
			event.setAndContinue(this.isBaby() ? CHICK_WALK : this.getIsMale() ? ROOSTER_WALK : HEN_WALK);
		} else{
			event.setAndContinue(this.isBaby() ? CHICK_IDLE : IDLE);
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

	static class BBChickenMeleeAttackGoal extends Goal {
		protected final BBChicken mob;
		private final double speedModifier;
		private final boolean followingTargetEvenIfNotSeen;
		private Path path;
		private double pathedTargetX;
		private double pathedTargetY;
		private double pathedTargetZ;
		private int ticksUntilNextPathRecalculation;
		private int ticksUntilNextAttack;
		private long lastCanUseCheck;
		private int failedPathFindingPenalty = 0;
		private boolean canPenalize = false;
		private int animTime = 0;

		public BBChickenMeleeAttackGoal(BBChicken anomalocaris, double speed, boolean canFollowIfUnseen) {
			this.mob = anomalocaris;
			this.speedModifier = speed;
			this.followingTargetEvenIfNotSeen = canFollowIfUnseen;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		public boolean canUse() {
			long i = this.mob.level().getGameTime();
			this.lastCanUseCheck = i;
			LivingEntity livingentity = this.mob.getTarget();
			if (livingentity == null) {
				return false;
			} else if (!livingentity.isAlive()) {
				return false;
			} else if (this.canPenalize) {
				if (--this.ticksUntilNextPathRecalculation <= 0) {
					this.path = this.mob.getNavigation().createPath(livingentity, 0);
					this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
					return this.path != null;
				} else {
					return true;
				}
			} else {
				this.path = this.mob.getNavigation().createPath(livingentity, 0);
				if (this.path != null) {
					return true;
				} else {
					return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
				}
			}
		}

		public boolean canContinueToUse() {
			LivingEntity livingentity = this.mob.getTarget();
			if (livingentity == null) {
				return false;
			} else if (!livingentity.isAlive()) {
				return false;
			} else if (!this.followingTargetEvenIfNotSeen) {
				return !this.mob.getNavigation().isDone();
			} else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
				return false;
			} else {
				return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
			}
		}

		public void start() {
			this.mob.getNavigation().moveTo(this.path, this.speedModifier);
			this.ticksUntilNextPathRecalculation = 0;
			this.ticksUntilNextAttack = 0;
			this.animTime = 0;
			this.mob.setAnimationState(0);
		}

		public void stop() {
			LivingEntity livingentity = this.mob.getTarget();
			if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
				this.mob.setTarget(null);
			}

			this.mob.setAnimationState(0);
		}

		public void tick() {
			LivingEntity target = this.mob.getTarget();
			double distance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
			double reach = this.getAttackReachSqr(target);
			int animState = this.mob.getAnimationState();
			Vec3 aim = this.mob.getLookAngle();
			new Vec2((float)(aim.x / (1.0 - Math.abs(aim.y))), (float)(aim.z / (1.0 - Math.abs(aim.y))));

			if (animState == 21) {
				this.tickAttack(target);
			} else {
				this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
				this.ticksUntilNextAttack = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
				this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
				this.doMovement(target, distance);
				this.checkForCloseRangeAttack(distance, reach);
			}

		}

		protected void doMovement(LivingEntity livingentity, Double d0) {
			this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
			if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05F)) {
				this.pathedTargetX = livingentity.getX();
				this.pathedTargetY = livingentity.getY();
				this.pathedTargetZ = livingentity.getZ();
				this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
				if (this.canPenalize) {
					this.ticksUntilNextPathRecalculation += this.failedPathFindingPenalty;
					if (this.mob.getNavigation().getPath() != null) {
						Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
						if (finalPathPoint != null && livingentity.distanceToSqr((double)finalPathPoint.x, (double)finalPathPoint.y, (double)finalPathPoint.z) < 1.0) {
							this.failedPathFindingPenalty = 0;
						} else {
							this.failedPathFindingPenalty += 10;
						}
					} else {
						this.failedPathFindingPenalty += 10;
					}
				}

				if (d0 > 1024.0) {
					this.ticksUntilNextPathRecalculation += 10;
				} else if (d0 > 256.0) {
					this.ticksUntilNextPathRecalculation += 5;
				}

				if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
					this.ticksUntilNextPathRecalculation += 15;
				}
			}

		}

		protected void checkForCloseRangeAttack(double distance, double reach) {
			if (distance <= reach && this.ticksUntilNextAttack <= 0) {
				int r = this.mob.getRandom().nextInt(2048);
				if (r <= 600) {
					this.mob.setAnimationState(21);
				}
			}

		}

		protected boolean getRangeCheck() {
			return this.mob.distanceToSqr(this.mob.getTarget().getX(), this.mob.getTarget().getY(), this.mob.getTarget().getZ()) <= 1.7999999523162842 * this.getAttackReachSqr(this.mob.getTarget());
		}

		protected void tickAttack(LivingEntity target) {
			++this.animTime;
			if (this.animTime == 4) {
				//this.mob.swing(InteractionHand.MAIN_HAND);
				if (this.mob.getTarget() instanceof BBChicken){
					this.mob.getTarget().hurt(this.mob.damageSources().mobAttack(mob), 0);

				}else {
					this.mob.doHurtTarget(target);
				}
			}

			if (this.animTime >= 20) {
				this.animTime = 0;
				if (this.getRangeCheck()) {
					this.mob.setAnimationState(22);
				} else {
					this.mob.setAnimationState(0);
					this.resetAttackCooldown();
					this.ticksUntilNextPathRecalculation = 0;
				}
			}
		}

//		protected void preformBiteAttack() {
//			Vec3 pos = this.mob.position();
//			HitboxHelper.LargeAttackWithTargetCheck(this.mob.damageSources().mobAttack(this.mob), 10.0F, 0.2F, this.mob, pos, 5.0, -1.5707963267948966, 1.5707963267948966, -1.0, 3.0);
//		}

		protected void resetAttackCooldown() {
			this.ticksUntilNextAttack = 0;
		}

		protected boolean isTimeToAttack() {
			return this.ticksUntilNextAttack <= 0;
		}

		protected int getTicksUntilNextAttack() {
			return this.ticksUntilNextAttack;
		}

		protected int getAttackInterval() {
			return 5;
		}

		protected double getAttackReachSqr(LivingEntity p_179512_1_) {
			return this.mob.getBbWidth() * 2.5F * this.mob.getBbWidth() * 1.8F + p_179512_1_.getBbWidth();
		}
	}
	
}
