package superlord.backyard_birds.common.entity;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class BBChicken extends Animal {
	
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BLACK_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> WHITE_MARKING = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TILT = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> MALE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FACIAL_TISSUE = SynchedEntityData.defineId(BBChicken.class, EntityDataSerializers.BOOLEAN);


	public BBChicken(EntityType<? extends Animal> p_27557_, Level p_27558_) {
		super(p_27557_, p_27558_);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D);
	}
	
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(COLOR, 0);
		this.entityData.define(BLACK_MARKING, 0);
		this.entityData.define(WHITE_MARKING, 0);
		this.entityData.define(TILT, 0);
		this.entityData.define(MALE, false);
		this.entityData.define(FACIAL_TISSUE, false);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Color", this.getColor());
		tag.putInt("BlackMarking", this.getBlackMarking());
		tag.putInt("WhiteMarking", this.getWhiteMarking());
		tag.putInt("Tilt", this.getTilt());
		tag.putBoolean("Male", this.getMale());
		tag.putBoolean("FacialTissue", this.getFacialTissue());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		this.setColor(tag.getInt("Color"));
		this.setBlackMarking(tag.getInt("BlackMarking"));
		this.setWhiteMarking(tag.getInt("WhiteMarking"));
		this.setTilt(tag.getInt("Tilt"));
		this.setMale(tag.getBoolean("Male"));
		this.setFacialTissue(tag.getBoolean("FacialTissue"));
	}
	
	public int getColor() {
		return this.entityData.get(COLOR);
	}

	public void setColor(int color) {
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
	
	public boolean getMale() {
		return this.entityData.get(MALE);
	}
	
	public void setMale(boolean male) {
		this.entityData.set(MALE, male);
	}
	
	public boolean getFacialTissue() {
		return this.entityData.get(FACIAL_TISSUE);
	}
	
	public void setFacialTissue(boolean facialTissue) {
		this.entityData.set(FACIAL_TISSUE, facialTissue);
	}
	
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		this.setColor(random.nextInt(6));
		this.setBlackMarking(random.nextInt(3));
		this.setWhiteMarking(random.nextInt(3));
		this.setTilt(random.nextInt(5));
		this.setMale(random.nextBoolean());
		this.setFacialTissue(random.nextBoolean());
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
		return null;
	}

}
