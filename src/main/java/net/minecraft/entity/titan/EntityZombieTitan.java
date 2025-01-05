package net.minecraft.entity.titan;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.entity.titan.animation.zombietitan.*;
import net.minecraft.entity.titanminion.EntityGiantZombieBetter;
import net.minecraft.entity.titanminion.EntityZombieMinion;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.entity.PartEntity;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class EntityZombieTitan extends EntityTitan implements IAnimatedEntity, IEntityMultiPartTitan {
    public static final DataParameter<Boolean> t12 = EntityDataManager.defineId(EntityZombieTitan.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> t13 = EntityDataManager.defineId(EntityZombieTitan.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> t15 = EntityDataManager.defineId(EntityZombieTitan.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> t16 = EntityDataManager.defineId(EntityZombieTitan.class, DataSerializers.BOOLEAN);
    public boolean isStunned;
    public EntityTitanPart[] partArray;
    public EntityTitanPart head;
	public EntityTitanPart topBody;
	public EntityTitanPart middleBody;
    public EntityTitanPart body;
    public EntityTitanPart rightArm;
	public EntityTitanPart rightFore;
    public EntityTitanPart leftArm;
	public EntityTitanPart leftFore;
    public EntityTitanPart rightLeg;
	public EntityTitanPart rightCalf;
    public EntityTitanPart leftLeg;
	public EntityTitanPart leftCalf;

    public EntityZombieTitan(EntityType<? extends EntityTitan> type, World worldIn) {
        super(type, worldIn);
        this.head = new EntityTitanPart(worldIn, this, "head", 8.0f, 8.0f);
        this.topBody = new EntityTitanPart(worldIn, this, "topbody", 8.0f, 4.0f);
        this.middleBody = new EntityTitanPart(worldIn, this, "middlebody", 8.0f, 4.0f);
        this.body = new EntityTitanPart(worldIn, this, "body", 8.0f, 4.0f);
        this.rightArm = new EntityTitanPart(worldIn, this, "rightarm", 4.0f, 4.0f);
        this.rightFore = new EntityTitanPart(worldIn, this, "rightfore", 4.0f, 4.0f);
        this.leftArm = new EntityTitanPart(worldIn, this, "leftarm", 4.0f, 4.0f);
        this.leftFore = new EntityTitanPart(worldIn, this, "leftfore", 4.0f, 4.0f);
        this.rightLeg = new EntityTitanPart(worldIn, this, "rightleg", 4.0f, 6.0f);
        this.rightCalf = new EntityTitanPart(worldIn, this, "rightcalf", 4.0f, 6.0f);
        this.leftLeg = new EntityTitanPart(worldIn, this, "leftleg", 4.0f, 6.0f);
        this.leftCalf = new EntityTitanPart(worldIn, this, "leftcalf", 4.0f, 6.0f);
        this.partArray = new EntityTitanPart[]{this.head, this.body, this.topBody, this.middleBody, this.rightArm, this.leftArm, this.rightFore, this.leftFore, this.leftCalf, this.rightCalf, this.rightLeg, this.leftLeg};
        this.setSize(8.0f, 32.0f);
        this.xpReward = 10000;
        this.goalSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        this.goalSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    @Override
    protected void applyEntityAI() {
        this.footID = 1;
        this.goalSelector.addGoal(1, new AnimationZombieTitanCreation(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanDeath(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanAttack4(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanStun(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanAttack3(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanAttack2(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanRangedAttack(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanLightningAttack(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanRoar(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanAntiTitanAttack(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanAttack1(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanReformSword(this));
        this.goalSelector.addGoal(1, new AnimationZombieTitanAttack5(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        if (TitanConfig.TitansFFAMode) {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.ZombieTitanSorter));
        } else {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SearchForAThingToKill));
        }
    }

    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 4.0f || TitanConfig.NightmareMode || TitanConfig.TotalDestructionMode;
    }

    public boolean canAttack(final Entity entity) {
        Class p_70686_1_ = entity.getClass();
        return p_70686_1_ != this.head.getClass() && p_70686_1_ != this.body.getClass() && p_70686_1_ != this.rightArm.getClass() && p_70686_1_ != this.leftArm.getClass() && p_70686_1_ != this.rightLeg.getClass() && p_70686_1_ != this.leftLeg.getClass() && p_70686_1_ != EntityZombieMinion.class && p_70686_1_ != EntityGiantZombieBetter.class && p_70686_1_ != EntityZombieTitan.class;
    }

    @Override
    public float getRenderSizeModifier() {
        float f = 16.0f;
        if (this.isBaby()) {
            f *= 0.6f;
        }
        return f;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public int getRegenTime() {
        if (!this.level.isDay()) {
            return 5;
        }
        return super.getRegenTime();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(t12, false);
        getEntityData().define(t13, false);
        getEntityData().define(t15, false);
        getEntityData().define(t16, false);
    }

    public boolean isSwordSoft() {
        return getEntityData().get(t16);
    }

    private void setSwordSoft(boolean b) {
        getEntityData().set(t16, b);
    }

    public boolean isVillager() {
        return getEntityData().get(t13);
    }

    public void setVillager(boolean b) {
        getEntityData().set(t13, b);
    }

    public boolean isArmed() {
        return getEntityData().get(t15);
    }

    public void setArmed(boolean b) {
        getEntityData().set(t15, b);
    }

    public boolean isBaby() {
        return getEntityData().get(t12);
    }

    public void setBaby(boolean b) {
        getEntityData().set(t12, b);
    }

    @Override
    public float getSpeed() {
        return (float) (this.isBaby() ? (0.6 + this.getExtraPower() * 0.001) : (this.isArmored() ? (0.45 + this.getExtraPower() * 0.001) : (0.3 + this.getExtraPower() * 0.001)));
    }

    @Override
    public boolean canBeHurtByPlayer() {
        return !this.isArmed() && !this.isInvulnerable();
    }

    @Override
    public int getMinionSpawnRate() {
        return TitanConfig.ZombieTitanMinionSpawnrate;
    }

    @Override
    public int getMinionCap() {
        return (this.animID == 11 && this.animTick > 80) ? 600 : 200;
    }

    @Override
    public int getPriestCap() {
        return (this.animID == 11 && this.animTick > 80) ? 300 : 100;
    }

    @Override
    public int getZealotCap() {
        return (this.animID == 11 && this.animTick > 80) ? 100 : 50;
    }

    @Override
    public int getBishopCap() {
        return (this.animID == 11 && this.animTick > 80) ? 40 : 20;
    }

    @Override
    public int getTemplarCap() {
        return (this.animID == 11 && this.animTick > 80) ? 20 : 10;
    }

    @Override
    public int getSpecialMinionCap() {
        return 10;
    }

    @Override
    public EnumTitanStatus getTitanStatus() {
        return EnumTitanStatus.AVERAGE;
    }

    @Override
    public int getFootStepModifer() {
        return 3;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Nullable
    @Override
    public PartEntity<?>[] getParts() {
        return this.partArray;
    }

    @Override
    public boolean shouldMove() {
        return this.animID == 0 && !this.isStunned && !this.getWaiting() && this.getTarget() != null && super.shouldMove();
    }

    @Override
    public boolean doHurtTarget(Entity p_70652_1_) {
        return false;
    }

    public void attackEntityWithRangedAttack(final LivingEntity p_82196_1_, final float p_82196_2_) {
        final float rotationYawHead = this.yHeadRot;
        this.yRot = rotationYawHead;
        this.yBodyRot = rotationYawHead;
        this.lookAt(p_82196_1_, 180.0f, 30.0f);
        final double d8 = 12.0;
        final Vector3d vec3 = this.getLookAngle();
        final double dx = vec3.x * d8;
        final double dz = vec3.z * d8;
        final EntityProtoBall entityarrow = new EntityProtoBall(this.level, this);
        final double d9 = p_82196_1_.getX() + p_82196_1_.getDeltaMovement().x - (this.head.getX() + dx);
        final double d10 = p_82196_1_.getY() + p_82196_1_.getEyeHeight() - 8.0 - this.head.getY();
        final double d11 = p_82196_1_.getZ() + p_82196_1_.getDeltaMovement().z - (this.head.getZ() + dz);
        final float f1 = MathHelper.sqrt(d9 * d9 + d11 * d11);
        entityarrow.shoot(d9, d10 + f1, d11, 0.95f, (float) (45 - this.level.getDifficulty().getId() * 5));
        entityarrow.setPos(this.head.getX() + dx, this.head.getY(), this.head.getZ() + dz);
        if (!this.level.isClientSide) {
            this.level.addFreshEntity(entityarrow);
        }
        final EntityProtoBall entityProtoBall = entityarrow;
        entityProtoBall.setDeltaMovement(entityProtoBall.getDeltaMovement().x * 1.5, entityProtoBall.getDeltaMovement().y, entityProtoBall.getDeltaMovement().z);
        final EntityProtoBall entityProtoBall2 = entityarrow;
        entityProtoBall2.setDeltaMovement(entityProtoBall.getDeltaMovement().x, entityProtoBall.getDeltaMovement().y, entityProtoBall.getDeltaMovement().z * 1.5);
        this.level.levelEvent(null, 1008, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
        if (this.distanceToSqr(p_82196_1_) < 400.0) {
            this.attackChoosenEntity(p_82196_1_, 10.0f, 5);
        }
    }

    @Override
    public void aiStep() {
        if (this.getVehicle() == null && !this.getWaiting() && !this.isStunned && this.animID == 0) {
            if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) > this.getMeleeRange() + (this.getTarget().isOnGround() ? 8000.0 : 1000.0)) {
                if (this.getY() <= this.getTarget().getY() + 12.0 && this.getY() < 256.0 - this.getBbHeight()) {
                    this.fallDistance = 0.0f;
                    this.pushSelf(0.0, 0.9 - this.getDeltaMovement().y, 0.0);
                    if (this.getDeltaMovement().y < 0.0) {
                        this.setDeltaMovement(this.getDeltaMovement().x, 0.0, this.getDeltaMovement().z);
                    }
                }
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.6, this.getDeltaMovement().z);
            }
            if (!this.onGround) {
                final float f = (this.random.nextFloat() - 0.5f) * 10.0f;
                final float f2 = (this.random.nextFloat() - 0.5f);
                final float f3 = (this.random.nextFloat() - 0.5f) * 10.0f;
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + f, this.getY() + 5.0 + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
            }
        }
        if (this.getWaiting()) {
            AnimationAPI.sendAnimPacket(this, 0);
            AnimationAPI.sendAnimPacket(this, 13);
            this.setAnimTick(0);
            final PlayerEntity player = this.level.getNearestPlayer(this, 16.0);
            if (player != null) {
                this.setWaiting(false);
                this.lookAt(player, 180.0f, 0.0f);
                //player.triggerAchievement(TitansAchievments.locateTitan);
            }
        } else {
            if (this.getAnimID() == 13) {
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
                if (this.getDeltaMovement().y > 0.0) {
                    this.setDeltaMovement(0, 0, 0);
                }
            }
            if (this.getAnimID() == 13 && this.getAnimTick() > 4 && this.getAnimTick() <= 48) {
                this.destroyBlocksInAABBGriefingBypass(this.body.getBoundingBox().move(0.0, -24 + this.getAnimTick(), 0.0).inflate(0.0, 8.0, 0.0));
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 2) {
                this.playSound(TitanSounds.titanBirth, 1000.0f, 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 10) {
                this.playSound(TitanSounds.titanRumble, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 360) {
                this.playSound(TitanSounds.titanQuake, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 160) {
                this.playSound(TitanSounds.titanZombieCreation, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 440) {
                this.playSound(TitanSounds.titanSkeletonGetUp, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && (this.getAnimTick() == 80 || this.getAnimTick() == 150 || this.getAnimTick() == 370 || this.getAnimTick() == 430 || this.getAnimTick() == 470 || this.getAnimTick() == 490)) {
                this.playStepSound(new BlockPos(0, 0, 0), Blocks.STONE.defaultBlockState());
                this.playSound(TitanSounds.titanPress, this.getSoundVolume(), 1.0f);
            }
        }
        if (this.getAnimID() == 5) {
            if (this.getAnimTick() == 34) {
                this.playSound(TitanSounds.lightningCharge, 100.0f, 1.0f);
            }
            if (this.getAnimTick() <= 46 && this.getAnimTick() >= 26) {
                final float ex = this.isBaby() ? 4.5f : 9.5f;
                final float fl = this.yBodyRot * 3.1415927f / 180.0f;
                final float fl2 = MathHelper.sin(fl);
                final float fl3 = MathHelper.cos(fl);
                this.level.addFreshEntity(new EntityGammaLightning(this.level, this.getX() - fl3 * ex, this.getY() + (this.isBaby() ? 13.0 : 26.0), this.getZ() - fl2 * ex, 0.0f, 0.56f, 0.0f));
                this.level.addFreshEntity(new EntityGammaLightning(this.level, this.getX() + fl3 * ex, this.getY() + (this.isBaby() ? 13.0 : 26.0), this.getZ() + fl2 * ex, 0.0f, 0.56f, 0.0f));
                if (this.getTarget() == null && !this.level.isClientSide) {
                    this.heal(50.0f);
                }
            }
            if (this.getAnimTick() == 64) {
                this.playSound(TitanSounds.lightningThrow, 100.0f, 1.0f);
                final double d8 = this.isBaby() ? 6.0 : 12.0;
                final Vector3d vec3 = this.getLookAngle();
                final double dx = vec3.x * d8;
                final double dz = vec3.z * d8;
                this.level.explode(this, this.getX() + dx, this.getY() + 26.0, this.getZ() + dz, 1.0f, false, Explosion.Mode.NONE);
                this.level.addFreshEntity(new EntityGammaLightning(this.level, this.getX() + dx, this.getY() + (this.isBaby() ? 9.0 : 18.0), this.getZ() + dz, 0.0f, 0.56f, 0.0f));
                this.level.addFreshEntity(new EntityGammaLightning(this.level, this.getX() + dx, this.getY() + (this.isBaby() ? 9.0 : 18.0), this.getZ() + dz, 0.0f, 0.56f, 0.0f));
                this.level.addFreshEntity(new EntityGammaLightning(this.level, this.getX() + dx, this.getY() + (this.isBaby() ? 9.0 : 18.0), this.getZ() + dz, 0.0f, 0.56f, 0.0f));
                this.level.addFreshEntity(new EntityGammaLightning(this.level, this.getX() + dx, this.getY() + (this.isBaby() ? 9.0 : 18.0), this.getZ() + dz, 0.0f, 0.56f, 0.0f));
                LivingEntity target = this.getTarget();
                if (target != null) {
                    if (target.getBbWidth() * target.getBbHeight() <= 24.0F) {
                        this.level.explode(this, target.getX(), target.getY(), target.getZ(), 2.0f, false, Explosion.Mode.NONE);
                    }
                    this.level.addFreshEntity(new EntityGammaLightning(this.level, target.getX(), target.getY(), target.getZ(), 0.0f, 0.56f, 0.0f));
                    final float da = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
                    final int i = this.getKnockbackAmount();
                    this.attackChoosenEntity(target, da * 3.0f, i);
                    final LivingEntity attackTarget = target;
                    attackTarget.push(0.0, 1.0f + this.random.nextFloat(), 0.0);
                    target.hurt(DamageSourceExtra.lightningBolt, da);
                    final List list1 = this.level.getEntities(target, target.getBoundingBox().inflate(12.0, 12.0, 12.0));
                    if (list1 != null && !list1.isEmpty()) {
                        for (int i2 = 0; i2 < list1.size(); ++i2) {
                            final Entity entity1 = (Entity) list1.get(i2);
                            if (entity1 instanceof LivingEntity && this.canAttack((LivingEntity) entity1)) {
                                this.level.addFreshEntity(new EntityGammaLightning(this.level, entity1.getX(), entity1.getY(), entity1.getZ(), 0.2f, 1.0f, 0.0f));
                                this.attackChoosenEntity(entity1, da, i);
                                if (!(entity1 instanceof EntityTitan)) {
                                    final Entity entity3 = entity1;
                                    entity3.push(0.0, 1.0f + this.random.nextFloat(), 0.0);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.isStunned) {
            this.setTarget(null);
            AnimationAPI.sendAnimPacket(this, 8);
            this.setAnimID(8);
        }
        if (this.animID == 10) {
            if (this.animTick == 30 || this.animTick == 70) {
                this.playStepSound(new BlockPos(0, 0, 0), Blocks.STONE.defaultBlockState());
            }
            if (this.animTick == 190) {
                this.playSound(TitanSounds.titanFall, 20.0f, 1.0f);
                this.playSound(TitanSounds.groundSmash, 20.0f, 1.0f);
                this.shakeNearbyPlayerCameras(10000.0);
                this.collideWithEntities(this.leftLeg, this.level.getEntities(this, this.leftLeg.getBoundingBox().inflate(48.0, 48.0, 48.0)));
                this.collideWithEntities(this.rightLeg, this.level.getEntities(this, this.rightLeg.getBoundingBox().inflate(48.0, 48.0, 48.0)));
                this.collideWithEntities(this.leftLeg, this.level.getEntities(this, this.leftLeg.getBoundingBox().inflate(48.0, 48.0, 48.0)));
                this.collideWithEntities(this.rightLeg, this.level.getEntities(this, this.rightLeg.getBoundingBox().inflate(48.0, 48.0, 48.0)));
            }
            if (this.animTick == 200) {
                this.playSound(TitanSounds.distantLargeFall, 10000.0f, 1.0f);
            }
        }
        if (this.animID == 8) {
            this.isStunned = this.animTick <= 138;
        }
        if (this.getAnimID() == 8 && this.getAnimTick() == 4 && !this.level.isClientSide) {
            this.dropSword();
        }
        if (this.getAnimID() == 8 && this.getAnimTick() >= 80 && this.getAnimTick() <= 100) {
            this.playSound(this.getAmbientSound(), this.getSoundVolume(), 1.1f);
        }
        if (this.getAnimID() == 7 && this.getAnimTick() == 122) {
            final double d8 = this.isBaby() ? 16.0 : 32.0;
            final Vector3d vec3 = this.getLookAngle();
            final double dx = vec3.x * d8;
            final double dz = vec3.z * d8;
            final int y = MathHelper.floor(this.getY());
            final int x = MathHelper.floor(this.getX() + dx);
            final int z = MathHelper.floor(this.getZ() + dz);
            if (this.level.getBlockState(new BlockPos(x, y - 1, z)).getMaterial() != Material.AIR) {
                this.playSound(TitanSounds.titanStrike, 20.0f, 1.0f);
                this.playSound(TitanSounds.titanSlam, 20.0f, 1.0f);
                this.playSound(TitanSounds.titanPress, 100.0f, 1.0f);
            }
            for (int l1 = -4; l1 <= 4; ++l1) {
                for (int i3 = -4; i3 <= 4; ++i3) {
                    for (int j = -1; j <= 1; ++j) {
                        final int j2 = x + l1;
                        final int k = y + j;
                        final int m = z + i3;
                        final Block block = this.level.getBlockState(new BlockPos(j2, k, m)).getBlock();
                        if (!block.defaultBlockState().isAir(this.level, new BlockPos(j2, k, m))) {
                            this.level.levelEvent(2001, new BlockPos(j2, k, m), Block.getId(block.defaultBlockState()));
                            if (block == Blocks.GRASS_BLOCK) {
                                this.level.setBlock(new BlockPos(j2, k, m), Blocks.DIRT.defaultBlockState(), 0, 3);
                            }
                        }
                        if (block.getExplosionResistance() > 500.0f && block.defaultBlockState().isSolidRender(level, new BlockPos(j2, k, m)) && !this.isSwordSoft()) {
                            this.setAnimTick(0);
                            AnimationAPI.sendAnimPacket(this, 8);
                            this.setAnimID(8);
                            this.playSound(SoundEvents.ANVIL_LAND, 20.0f, 0.5f);
                            this.isStunned = true;
                        } else if (block.getExplosionResistance() <= 1.5f && block.defaultBlockState().isSolidRender(level, new BlockPos(j2, k, m)) && this.isSwordSoft() && block != Blocks.AIR && block != Blocks.NETHERRACK && block != Blocks.DIRT && block != Blocks.GRASS_BLOCK && block != Blocks.GLASS && block != Blocks.GLASS_PANE) {
                            this.setAnimTick(0);
                            AnimationAPI.sendAnimPacket(this, 8);
                            this.setAnimID(8);
                            this.playSound(SoundEvents.ANVIL_LAND, 20.0f, 0.5f);
                            this.isStunned = true;
                        }
                    }
                }
            }
        }
        if (this.getAnimID() == 2 && this.getAnimTick() == 160) {
            final double d8 = 12.0;
            final Vector3d vec3 = this.getLookAngle();
            final double dx = vec3.x * d8;
            final double dz = vec3.z * d8;
            final int y = MathHelper.floor(this.getY());
            final int x = MathHelper.floor(this.getX() + dx);
            final int z = MathHelper.floor(this.getZ() + dz);
            for (int l1 = -2; l1 <= 2; ++l1) {
                for (int i3 = -2; i3 <= 2; ++i3) {
                    for (int j = -1; j <= 1; ++j) {
                        final int j2 = x + l1;
                        final int k = y + j;
                        final int m = z + i3;
                        final Block block = this.level.getBlockState(new BlockPos(j2, k, m)).getBlock();
                        if (!block.defaultBlockState().isAir(this.level, new BlockPos(j2, k, m))) {
                            this.level.levelEvent(2001, new BlockPos(j2, k, m), Block.getId(block.defaultBlockState()));
                            if (block == Blocks.GRASS_BLOCK) {
                                this.level.setBlock(new BlockPos(j2, k, m), Blocks.DIRT.defaultBlockState(), 0, 3);
                            }
                        }
                        if (block.getExplosionResistance() > 500.0f) {
                            this.setSwordSoft(false);
                        } else if (block.getExplosionResistance() <= 500.0f) {
                            this.setSwordSoft(true);
                        }
                    }
                }
            }
        }
        final float f = this.yBodyRot * 3.1415927f / 180.0f;
        final float f2 = MathHelper.sin(f);
        final float f3 = MathHelper.cos(f);
        if (this.isBaby()) {
            this.setSize(6.0f, 18.0f);
        } else {
            this.setSize(8.0f, 32.0f);
        }
        if (this.tickCount > 5) {
            final EntityTitanPart head = this.head;
            final EntityTitanPart head2 = this.head;
            final float n = this.isBaby() ? 6.0f : 8.0f;
            head2.width = n;
            head.height = n;
            this.body.height = (this.isBaby() ? 2.0f : 4.0f);
            this.body.width = (this.isBaby() ? 3.5f : 7.0f);
			this.topBody.height = this.body.height;
            this.topBody.width = this.body.width;
			this.middleBody.height = this.body.height;
            this.middleBody.width = this.body.width;
            final EntityTitanPart leftLeg = this.leftLeg;
            final EntityTitanPart rightLeg = this.rightLeg;
			final EntityTitanPart leftCalf = this.leftCalf;
            final EntityTitanPart rightCalf = this.rightCalf;
            final float n2 = this.isBaby() ? 6.0f : 6.0f;
            rightLeg.height = n2;
            leftLeg.height = n2;
			rightCalf.height = n2;
            leftCalf.height = n2;
            final EntityTitanPart leftLeg2 = this.leftLeg;
            final EntityTitanPart rightLeg2 = this.rightLeg;
			final EntityTitanPart leftCalf2 = this.leftCalf;
            final EntityTitanPart rightCalf2 = this.rightCalf;
            final float n3 = this.isBaby() ? 2.0f : 4.0f;
            rightLeg2.width = n3;
            leftLeg2.width = n3;
			rightCalf2.width = n3;
            leftCalf2.width = n3;
            final EntityTitanPart rightArm = this.rightArm;
            final EntityTitanPart leftArm = this.leftArm;
            final EntityTitanPart rightArm2 = this.rightArm;
            final EntityTitanPart leftArm2 = this.leftArm;
			final EntityTitanPart rightFore = this.rightFore;
            final EntityTitanPart leftFore = this.leftFore;
            final EntityTitanPart rightFore2 = this.rightFore;
            final EntityTitanPart leftFore2 = this.leftFore;
            float width2;
            final float width;
            final float n4 = width = (this.isBaby() ? (width2 = 2.0f) : (width2 = 4.0f));
            leftArm2.height = n4;
            rightArm2.height = n4;
            leftArm.width = width;
            rightArm.width = width2;
			leftFore2.height = n4;
            rightFore2.height = n4;
            leftFore.width = width;
            rightFore.width = width2;
            this.head.moveTo(this.getX(), this.getY() + (this.isBaby() ? 12.0 : 24.0), this.getZ(), 0.0f, 0.0f);
            this.body.moveTo(this.getX(), this.getY() + (this.isBaby() ? 2.0 : 12.0), this.getZ(), 0.0f, 0.0f);
            this.middleBody.moveTo(this.getX(), this.getY() + (this.isBaby() ? 4.0 : 16.0), this.getZ(), 0.0f, 0.0f);
            this.topBody.moveTo(this.getX(), this.getY() + (this.isBaby() ? 6.0 : 20.0), this.getZ(), 0.0f, 0.0f);
			
			
            this.rightArm.moveTo(this.getX() + f3 * (this.isBaby() ? 3.0 : 6.0), this.getY() + (this.isBaby() ? 10.0 : 20.0), this.getZ() + f2 * (this.isBaby() ? 3.0 : 6.0), 0.0f, 0.0f);
            this.leftArm.moveTo(this.getX() - f3 * (this.isBaby() ? 3.0 : 6.0), this.getY() + (this.isBaby() ? 10.0 : 20.0), this.getZ() - f2 * (this.isBaby() ? 3.0 : 6.0), 0.0f, 0.0f);
            this.rightFore.moveTo(this.rightArm.getX() + f3 * (this.isBaby() ? 3.0 : 12.0), this.rightArm.getY(), this.rightArm.getZ() + f2 * (this.isBaby() ? 3.0 : 12.0), 0.0f, 0.0f);
            this.leftFore.moveTo(this.leftArm.getX() - f3 * (this.isBaby() ? 3.0 : 12.0), this.getY(), this.leftArm.getZ() - f2 * (this.isBaby() ? 3.0 : 12.0), 0.0f, 0.0f);
            
			this.rightLeg.moveTo(this.getX() + f3 * (this.isBaby() ? 1.0 : 2.0), this.getY() + (this.isBaby() ? 3.0 : 6.0), this.getZ() + f2 * (this.isBaby() ? 1.0 : 2.0), 0.0f, 0.0f);
            this.leftLeg.moveTo(this.getX() - f3 * (this.isBaby() ? 1.0 : 2.0), this.getY() + (this.isBaby() ? 3.0 : 6.0), this.getZ() - f2 * (this.isBaby() ? 1.0 : 2.0), 0.0f, 0.0f);
            this.rightCalf.moveTo(this.getX() + f3 * (this.isBaby() ? 1.0 : 2.0), this.getY(), this.getZ() + f2 * (this.isBaby() ? 1.0 : 2.0), 0.0f, 0.0f);
            this.leftCalf.moveTo(this.getX() - f3 * (this.isBaby() ? 1.0 : 2.0), this.getY(), this.getZ() - f2 * (this.isBaby() ? 1.0 : 2.0), 0.0f, 0.0f);
            if (this.isAlive() && !this.isStunned) {
                this.collideWithEntities(this.head, this.level.getEntities(this, this.head.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.body, this.level.getEntities(this, this.body.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.rightArm, this.level.getEntities(this, this.rightArm.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.leftArm, this.level.getEntities(this, this.leftArm.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.leftLeg, this.level.getEntities(this, this.leftLeg.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.rightLeg, this.level.getEntities(this, this.rightLeg.getBoundingBox().inflate(1.0, 0.0, 1.0)));
            }
            this.destroyBlocksInAABB(this.head.getBoundingBox());
            this.destroyBlocksInAABB(this.body.getBoundingBox());
            this.destroyBlocksInAABB(this.rightArm.getBoundingBox());
            this.destroyBlocksInAABB(this.leftArm.getBoundingBox());
            this.destroyBlocksInAABB(this.leftLeg.getBoundingBox().inflate(1.0, 0.0, 1.0));
            this.destroyBlocksInAABB(this.rightLeg.getBoundingBox().inflate(1.0, 0.0, 1.0));
            for (int i4 = 0; i4 < this.partArray.length; ++i4) {
                final List list2 = this.level.getEntities(this, this.partArray[i4].getBoundingBox().inflate(0.5, 0.5, 0.5));
                if (list2 != null && !list2.isEmpty()) {
                    for (int j3 = 0; j3 < list2.size(); ++j3) {
                        final Entity entity2 = (Entity) list2.get(j3);
                        if (entity2 instanceof FireballEntity && ((FireballEntity) entity2).getOwner() != this && !(entity2 instanceof EntityLightningBall) && !(entity2 instanceof EntityGargoyleTitanFireball) && !(entity2 instanceof EntityWebShot)) {
                            entity2.hurt(DamageSource.thorns(this), 0.0f);
                        }
                        if (entity2 instanceof EntityTitanFireball && ((EntityTitanFireball) entity2).getOwner() != null && ((EntityTitanFireball) entity2).getOwner() != this) {
                            ((EntityTitanFireball) entity2).onHitEntity(new EntityRayTraceResult(this));
                        }
                        if (entity2 instanceof EntityGargoyleTitanFireball) {
                            this.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                            this.level.explode((((EntityGargoyleTitanFireball) entity2).getOwner() != null) ? ((EntityGargoyleTitanFireball) entity2).getOwner() : entity2, entity2.getX(), entity2.getY(), entity2.getZ(), 8.0f, false, Explosion.Mode.NONE);
                            this.attackEntityFromPart(this.partArray[i4], DamageSource.fireball((FireballEntity) entity2, (((EntityGargoyleTitanFireball) entity2).getOwner() != null) ? ((EntityGargoyleTitanFireball) entity2).getOwner() : entity2), 1000.0f);
                            entity2.remove();
                        }
                        if (entity2 instanceof EntityHarcadiumArrow) {
                            this.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                            this.attackEntityFromPart(this.partArray[i4], DamageSourceExtra.causeHarcadiumArrowDamage((EntityHarcadiumArrow) entity2, (((EntityHarcadiumArrow) entity2).getOwner() != null) ? ((EntityHarcadiumArrow) entity2).getOwner() : entity2), 500.0f);
                            entity2.remove();
                        }
                        if (entity2 instanceof EntityWebShot && ((EntityWebShot) entity2).getOwner() != this) {
                            this.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                            this.attackEntityFromPart(this.partArray[i4], DamageSourceExtra.causeAntiTitanDamage((((EntityWebShot) entity2).getOwner() != this) ? ((EntityWebShot) entity2).getOwner() : entity2), 300.0f);
                            final int i5 = MathHelper.floor(this.partArray[i4].getY());
                            final int i6 = MathHelper.floor(this.partArray[i4].getX());
                            final int j4 = MathHelper.floor(this.partArray[i4].getZ());
                            final boolean flag = false;
                            for (int l2 = -2 - this.random.nextInt(4); l2 <= 2 + this.random.nextInt(4); ++l2) {
                                for (int i7 = -2 - this.random.nextInt(4); i7 <= 2 + this.random.nextInt(4); ++i7) {
                                    for (int h = -2; h <= 2 + this.random.nextInt(5); ++h) {
                                        final int j5 = i6 + l2;
                                        final int k2 = i5 + h;
                                        final int l3 = j4 + i7;
                                        final Block block2 = this.level.getBlockState(new BlockPos(j5, k2, l3)).getBlock();
                                        if (!block2.defaultBlockState().isSolidRender(level, new BlockPos(j5, k2, l3))) {
                                            this.level.setBlock(new BlockPos(j5, k2, l3), Blocks.COBWEB.defaultBlockState(), 0, 3);
                                        }
                                    }
                                }
                            }
                            entity2.remove();
                        }
                    }
                }
            }
			//this.head.update();
			//this.body.update();
			//this.middleBody.update();
			//this.topBody.update();
			//this.leftArm.update();
			//this.leftFore.update();
			//this.rightArm.update();
			//this.rightFore.update();
			//this.leftLeg.update();
			//this.leftCalf.update();
			//this.rightLeg.update();
			//this.rightCalf.update();
        }
		
        this.meleeTitan = true;
        if (this.getAnimID() == 12 && this.getTarget() != null && this.getAnimTick() == 55) {
            for (int i4 = 0; i4 < 4 + 2 * this.level.getDifficulty().getId(); ++i4) {
                this.attackEntityWithRangedAttack(this.getTarget(), 0.0f);
            }
        }
        if (this.isStunned || this.deathTicks > 0) {
            this.setDeltaMovement(0, getDeltaMovement().y, 0);
        }
        if (!AnimationAPI.isEffectiveClient() && this.getTarget() != null && !this.isStunned && this.getAnimID() == 0 && this.tickCount > 5) {
            final double d9 = this.distanceToSqr(this.getTarget());
            if (d9 < this.getMeleeRange()) {
                if (this.getTarget() instanceof EntityTitan || this.getTarget().getBbHeight() >= 6.0f || this.getTarget().getY() > this.getY() + 6.0) {
                    AnimationAPI.sendAnimPacket(this, 1);
                    this.setAnimID(1);
                } else {
                    AnimationAPI.sendAnimPacket(this, 7);
                    this.setAnimID(7);
                    switch (this.random.nextInt(6)) {
                        case 0 -> {
                            AnimationAPI.sendAnimPacket(this, 6);
                            this.setAnimID(6);
                        }
                        case 1 -> {
                            if (this.isArmed()) {
                                AnimationAPI.sendAnimPacket(this, 7);
                                this.setAnimID(7);
                                break;
                            }
                            if (this.getRandom().nextInt(2) == 0) {
                                AnimationAPI.sendAnimPacket(this, 2);
                                this.setAnimID(2);
                                break;
                            }
                            AnimationAPI.sendAnimPacket(this, 4);
                            this.setAnimID(4);
                        }
                        case 2 -> {
                            AnimationAPI.sendAnimPacket(this, 9);
                            this.setAnimID(9);
                        }
                        case 3 -> {
                            AnimationAPI.sendAnimPacket(this, 4);
                            this.setAnimID(4);
                        }
                        case 4 -> {
                            AnimationAPI.sendAnimPacket(this, 3);
                            this.setAnimID(3);
                        }
                        case 5 -> {
                            AnimationAPI.sendAnimPacket(this, 11);
                            this.setAnimID(11);
                        }
                    }
                }
            } else if (this.getAnimID() == 0 && this.getRandom().nextInt(100) == 0) {
                switch (this.random.nextInt(4)) {
                    case 0 -> {
                        AnimationAPI.sendAnimPacket(this, 5);
                        this.setAnimID(5);
                    }
                    case 1 -> {
                        AnimationAPI.sendAnimPacket(this, 12);
                        this.setAnimID(12);
                    }
                    case 2 -> {
                        if (this.getRandom().nextInt(4) == 0) {
                            AnimationAPI.sendAnimPacket(this, 11);
                            this.setAnimID(11);
                            break;
                        }
                        AnimationAPI.sendAnimPacket(this, 5);
                        this.setAnimID(5);
                    }
                    case 3 -> {
                        if (this.isArmed()) {
                            AnimationAPI.sendAnimPacket(this, 5);
                            this.setAnimID(5);
                            break;
                        }
                        if (this.getRandom().nextInt(5) == 0) {
                            AnimationAPI.sendAnimPacket(this, 2);
                            this.setAnimID(2);
                            break;
                        }
                        AnimationAPI.sendAnimPacket(this, 5);
                        this.setAnimID(5);
                    }
                }
            }
        }
        if (this.animID == 1 && this.animTick == 1) {
            this.antiTitanAttackAnimeID = this.getRandom().nextInt(4);
        }
        if (this.isVillager() && this.isBaby()) {
            this.setCustomName(new TranslationTextComponent("entity.ZombieTitan.name.babyvillager"));
        } else if (!this.isVillager() && this.isBaby()) {
            this.setCustomName(new TranslationTextComponent("entity.ZombieTitan.name.baby"));
        } else if (this.isVillager() && !this.isBaby()) {
            this.setCustomName(new TranslationTextComponent("entity.ZombieTitan.name.villager"));
        } else {
            this.setCustomName(new TranslationTextComponent("entity.ZombieTitan.name"));
        }
        if (TitanConfig.NightmareMode) {
            if (this.isBaby()) {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20000.0 + this.getExtraPower() * 1000);
                if (this.isArmed()) {
                    this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(480.0 + this.getExtraPower() * 90);
                } else {
                    this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(240.0 + this.getExtraPower() * 45);
                }
            } else {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40000.0 + this.getExtraPower() * 2000);
                if (this.isArmed()) {
                    this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(720.0 + this.getExtraPower() * 180);
                } else {
                    this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(360.0 + this.getExtraPower() * 90);
                }
            }
        } else if (this.isBaby()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10000.0 + this.getExtraPower() * 500);
            if (this.isArmed()) {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(160.0 + this.getExtraPower() * 30);
            } else {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(80.0 + this.getExtraPower() * 15);
            }
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20000.0 + this.getExtraPower() * 1000);
            if (this.isArmed()) {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(240.0 + this.getExtraPower() * 60);
            } else {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(120.0 + this.getExtraPower() * 30);
            }
        }
        if (this.getAnimID() == 2 && this.getDeltaMovement().y > 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.0, this.getDeltaMovement().z);
        }
        if (!this.isArmed() && this.getTarget() != null && this.getTarget() instanceof EntityTitan && this.animID == 0) {
            AnimationAPI.sendAnimPacket(this, 2);
        }
        if (this.random.nextInt(120) == 0 && this.getTarget() != null && this.distanceToSqr(this.getTarget()) > 512.0 && this.onGround && this.animID == 0) {
            this.playSound(this.getHurtSound(null), this.getSoundVolume(), this.getSoundPitch());
            if (this.random.nextInt(4) == 0) {
                this.jumpFromGround();
                final double d10 = this.getTarget().getX() - this.getX();
                final double d11 = this.getTarget().getZ() - this.getZ();
                final float f4 = MathHelper.sqrt(d10 * d10 + d11 * d11);
                final double hor = 2.0;
                this.setDeltaMovement(d10 / f4 * hor * hor + this.getDeltaMovement().x * hor, this.getDeltaMovement().y, d11 / f4 * hor * hor + this.getDeltaMovement().z * hor);
            } else {
                this.jumpAtEntity(this.getTarget());
            }
        }
        if (!this.getWaiting() && this.animID != 13 && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
            if ((this.random.nextInt(this.getMinionSpawnRate()) == 0 || this.getInvulTime() > 1 || (this.animID == 11 && this.animTick > 80)) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.random.nextInt(100) == 0) {
                    final EntityGiantZombieBetter entitychicken = new EntityGiantZombieBetter(this.level);
                    this.teleportEntityRandomly(entitychicken);
                    entitychicken.master = this;
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
                    this.level.addFreshEntity(entitychicken);
                    entitychicken.push(0.0, 1.0, 0.0);
                    entitychicken.playSound(TitanSounds.titanSummonMinion, 10.0f, 0.5f);
                    ++this.numSpecialMinions;
                } else if (this.numMinions < this.getMinionCap()) {
                    final EntityZombieMinion entitychicken2 = new EntityZombieMinion(this.level);
                    this.teleportEntityRandomly(entitychicken2);
                    entitychicken2.master = this;
                    entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken2.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken2.setMinionType(0);
                    entitychicken2.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 40, 4, false, true));
                    entitychicken2.push(0.0, 0.8, 0.0);
                    this.level.addFreshEntity(entitychicken2);
                    entitychicken2.playSound(TitanSounds.titanSummonMinion, 2.0f, 1.0f);
                    entitychicken2.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, true));
                    ++this.numMinions;
                    if (this.isVillager()) {
                        entitychicken2.setVillager(true);
                    }
                    if (this.isBaby()) {
                        entitychicken2.setBaby(true);
                    }
                    final Block block3 = this.level.getBlockState(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ())).getBlock();
                    this.level.levelEvent(2001, new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Block.getId(block3.defaultBlockState()));
                    if (block3 == Blocks.GRASS_BLOCK) {
                        this.level.setBlock(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Blocks.DIRT.defaultBlockState(), 0, 3);
                    }
                }
            }
            if ((this.random.nextInt(this.getMinionSpawnRate() * 2) == 0 || this.getInvulTime() > 1 || (this.animID == 11 && this.animTick > 80)) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.random.nextInt(100) == 0) {
                    final EntityGiantZombieBetter entitychicken = new EntityGiantZombieBetter(this.level);
                    this.teleportEntityRandomly(entitychicken);
                    entitychicken.master = this;
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
                    this.level.addFreshEntity(entitychicken);
                    entitychicken.push(0.0, 1.0, 0.0);
                    entitychicken.playSound(TitanSounds.titanSummonMinion, 10.0f, 0.5f);
                    ++this.numSpecialMinions;
                } else if (this.numPriests < this.getPriestCap()) {
                    final EntityZombieMinion entitychicken2 = new EntityZombieMinion(this.level);
                    this.teleportEntityRandomly(entitychicken2);
                    entitychicken2.master = this;
                    entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken2.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken2.setMinionType(1);
                    entitychicken2.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 40, 4, false, true));
                    entitychicken2.push(0.0, 0.8, 0.0);
                    this.level.addFreshEntity(entitychicken2);
                    entitychicken2.playSound(TitanSounds.titanSummonMinion, 2.0f, 1.0f);
                    entitychicken2.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, true));
                    ++this.numPriests;
                    if (this.isVillager()) {
                        entitychicken2.setVillager(true);
                    }
                    if (this.isBaby()) {
                        entitychicken2.setBaby(true);
                    }
                    final Block block3 = this.level.getBlockState(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ())).getBlock();
                    this.level.levelEvent(2001, new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Block.getId(block3.defaultBlockState()));
                    if (block3 == Blocks.GRASS_BLOCK) {
                        this.level.setBlock(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Blocks.DIRT.defaultBlockState(), 0, 3);
                    }
                }
            }
            if ((this.random.nextInt(this.getMinionSpawnRate() * 4) == 0 || this.getInvulTime() > 1 || (this.animID == 11 && this.animTick > 80)) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.random.nextInt(100) == 0) {
                    final EntityGiantZombieBetter entitychicken = new EntityGiantZombieBetter(this.level);
                    this.teleportEntityRandomly(entitychicken);
                    entitychicken.master = this;
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
                    this.level.addFreshEntity(entitychicken);
                    entitychicken.push(0.0, 1.0, 0.0);
                    entitychicken.playSound(TitanSounds.titanSummonMinion, 10.0f, 0.5f);
                    ++this.numSpecialMinions;
                } else if (this.numZealots < this.getZealotCap()) {
                    final EntityZombieMinion entitychicken2 = new EntityZombieMinion(this.level);
                    this.teleportEntityRandomly(entitychicken2);
                    entitychicken2.master = this;
                    entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken2.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken2.setMinionType(2);
                    entitychicken2.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 40, 4, false, true));
                    entitychicken2.push(0.0, 0.8, 0.0);
                    this.level.addFreshEntity(entitychicken2);
                    entitychicken2.playSound(TitanSounds.titanSummonMinion, 2.0f, 1.0f);
                    entitychicken2.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, true));
                    ++this.numZealots;
                    if (this.isVillager()) {
                        entitychicken2.setVillager(true);
                    }
                    if (this.isBaby()) {
                        entitychicken2.setBaby(true);
                    }
                    final Block block3 = this.level.getBlockState(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ())).getBlock();
                    this.level.levelEvent(2001, new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Block.getId(block3.defaultBlockState()));
                    if (block3 == Blocks.GRASS_BLOCK) {
                        this.level.setBlock(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Blocks.DIRT.defaultBlockState(), 0, 3);
                    }
                }
            }
            if ((this.random.nextInt(this.getMinionSpawnRate() * 8) == 0 || this.getInvulTime() > 1 || (this.animID == 11 && this.animTick > 80)) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.random.nextInt(100) == 0) {
                    final EntityGiantZombieBetter entitychicken = new EntityGiantZombieBetter(this.level);
                    this.teleportEntityRandomly(entitychicken);
                    entitychicken.master = this;
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
                    this.level.addFreshEntity(entitychicken);
                    entitychicken.push(0.0, 1.0, 0.0);
                    entitychicken.playSound(TitanSounds.titanSummonMinion, 10.0f, 0.5f);
                    ++this.numSpecialMinions;
                } else if (this.numBishop < this.getBishopCap()) {
                    final EntityZombieMinion entitychicken2 = new EntityZombieMinion(this.level);
                    this.teleportEntityRandomly(entitychicken2);
                    entitychicken2.master = this;
                    entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken2.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken2.setMinionType(3);
                    entitychicken2.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 40, 4, false, true));
                    entitychicken2.push(0.0, 0.8, 0.0);
                    this.level.addFreshEntity(entitychicken2);
                    entitychicken2.playSound(TitanSounds.titanSummonMinion, 2.0f, 1.0f);
                    entitychicken2.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, true));
                    ++this.numBishop;
                    if (this.isVillager()) {
                        entitychicken2.setVillager(true);
                    }
                    if (this.isBaby()) {
                        entitychicken2.setBaby(true);
                    }
                    final Block block3 = this.level.getBlockState(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ())).getBlock();
                    this.level.levelEvent(2001, new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Block.getId(block3.defaultBlockState()));
                    if (block3 == Blocks.GRASS_BLOCK) {
                        this.level.setBlock(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Blocks.DIRT.defaultBlockState(), 0, 3);
                    }
                }
            }
            if ((this.random.nextInt(this.getMinionSpawnRate() * 16) == 0 || this.getInvulTime() > 1 || (this.animID == 11 && this.animTick > 80)) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.random.nextInt(100) == 0) {
                    final EntityGiantZombieBetter entitychicken = new EntityGiantZombieBetter(this.level);
                    this.teleportEntityRandomly(entitychicken);
                    entitychicken.master = this;
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
                    this.level.addFreshEntity(entitychicken);
                    entitychicken.push(0.0, 1.0, 0.0);
                    entitychicken.playSound(TitanSounds.titanSummonMinion, 10.0f, 0.5f);
                    ++this.numSpecialMinions;
                } else if (this.numTemplar < this.getTemplarCap()) {
                    final EntityZombieMinion entitychicken2 = new EntityZombieMinion(this.level);
                    this.teleportEntityRandomly(entitychicken2);
                    entitychicken2.master = this;
                    entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken2.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken2.setMinionType(4);
                    entitychicken2.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 40, 4, false, true));
                    entitychicken2.push(0.0, 0.8, 0.0);
                    this.level.addFreshEntity(entitychicken2);
                    entitychicken2.playSound(TitanSounds.titanSummonMinion, 2.0f, 1.0f);
                    entitychicken2.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, true));
                    ++this.numTemplar;
                    if (this.isVillager()) {
                        entitychicken2.setVillager(true);
                    }
                    if (this.isBaby()) {
                        entitychicken2.setBaby(true);
                    }
                    final Block block3 = this.level.getBlockState(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ())).getBlock();
                    this.level.levelEvent(2001, new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Block.getId(block3.defaultBlockState()));
                    if (block3 == Blocks.GRASS_BLOCK) {
                        this.level.setBlock(new BlockPos((int) entitychicken2.getX(), (int) entitychicken2.getY(), (int) entitychicken2.getZ()), Blocks.DIRT.defaultBlockState(), 0, 3);
                    }
                }
            }
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        final List list11 = this.level.getEntities(this, this.getBoundingBox());
        if (list11 != null && !list11.isEmpty()) {
            for (int i1 = 0; i1 < list11.size(); ++i1) {
                final Entity entity = (Entity) list11.get(i1);
                if (entity instanceof LivingEntity && entity.isOnGround() && !(entity instanceof EntityTitan) && this.isAlive() && !this.isStunned) {
                    final float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
                    entity.hurt(DamageSourceExtra.causeSquishingDamage(this), f / 2.0f);
                }
            }
        }
        super.customServerAiStep();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getWaiting() && this.animID != 13) {
            this.playSound(SoundEvents.ZOMBIE_AMBIENT, this.getSoundVolume(), this.getSoundPitch() - 0.6f);
        }
        return (this.getWaiting() || this.animID == 13) ? null : TitanSounds.titanZombieLiving;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        this.playSound(SoundEvents.ZOMBIE_HURT, this.getSoundVolume(), this.getSoundPitch() - 0.6f);
        return TitanSounds.titanZombieGrunt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(SoundEvents.ZOMBIE_DEATH, this.getSoundVolume(), this.getSoundPitch() - 0.6f);
        return TitanSounds.titanZombieDeath;
    }

    @Override
    protected void playStepSound(final BlockPos pos, final BlockState state) {
        this.playSound(TitanSounds.titanStep, 10.0f, 1.0f);
        this.shakeNearbyPlayerCameras(4000.0);
        if (!this.getWaiting() && this.animID != 13) {
            final float f3 = this.yRot * 3.1415927f / 180.0f;
            final float f4 = MathHelper.sin(f3);
            final float f5 = MathHelper.cos(f3);
            if (this.footID == 0) {
                this.destroyBlocksInAABB(this.leftLeg.getBoundingBox().move(0.0, -1.0, 0.0));
                this.collideWithEntities(this.leftLeg, this.level.getEntities(this, this.leftLeg.getBoundingBox().inflate(1.0, 1.0, 1.0).move(f4 * 4.0f, 0.0, f5 * 4.0f)));
                ++this.footID;
            } else {
                this.destroyBlocksInAABB(this.rightLeg.getBoundingBox().move(0.0, -1.0, 0.0));
                this.collideWithEntities(this.rightLeg, this.level.getEntities(this, this.rightLeg.getBoundingBox().inflate(1.0, 1.0, 1.0).move(f4 * 4.0f, 0.0, f5 * 4.0f)));
                this.footID = 0;
            }
        }
    }

    private void dropSword() {
        if (this.isArmed() && !this.level.isClientSide) {
            for (int l = 0; l < 16; ++l) {
                this.playSound(SoundEvents.ITEM_BREAK, 100.0f, 0.5f);
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 8.0f - 4.0f), this.getY() + 32.0 + this.random.nextFloat() * 8.0f, this.getZ() + (this.random.nextFloat() * 8.0f - 4.0f), new ItemStack(Items.STICK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 32; ++l) {
                this.playSound(SoundEvents.ITEM_BREAK, 100.0f, 0.5f);
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 8.0f - 4.0f), this.getY() + 40.0 + this.random.nextFloat() * 16.0f, this.getZ() + (this.random.nextFloat() * 8.0f - 4.0f), new ItemStack(Items.IRON_INGOT));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            this.setArmed(false);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        if (this.isBaby()) {
            tagCompound.putBoolean("IsBaby", true);
        }
        if (this.isVillager()) {
            tagCompound.putBoolean("IsVillager", true);
        }
        if (this.isArmed()) {
            tagCompound.putBoolean("IsArmed", true);
        }
        tagCompound.putBoolean("Stunned", this.isStunned);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        if (tagCompund.getBoolean("IsBaby")) {
            this.setBaby(true);
        }
        if (tagCompund.getBoolean("IsVillager")) {
            this.setVillager(true);
        }
        if (tagCompund.getBoolean("IsArmed")) {
            this.setArmed(true);
        }
        this.isStunned = tagCompund.getBoolean("Stunned");
    }

    @Override
    public float getEyeHeightForge(Pose pose, EntitySize size) {
        float f = 27.6f;
        if (this.isBaby()) {
            f = 14.8f;
        }
        return f;
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
        ILivingEntityData p_180482_2_2 = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setCanPickUpLoot(true);
        this.setArmed(true);
        this.setWaiting(true);
        AgeableEntity.AgeableData p_213386_4_ = new AgeableEntity.AgeableData(true);
        if (p_180482_2_2 == null) {
            p_180482_2_2 = new GroupData(this.level.random.nextFloat() < p_213386_4_.getBabySpawnChance(), this.level.random.nextFloat() < 0.05f, null);
        }
        if (p_180482_2_2 instanceof GroupData groupdata) {
            if (groupdata.canSpawnJockey) {
                this.setVillager(true);
            }
            if (groupdata.isBaby) {
                this.setBaby(true);
            }
        }
        if (this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }
        return p_180482_2_2;
    }

    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (this.deathTicks > 0) {
            for (int x = 0; x < 16; ++x) {
                final EntityXPBomb entitylargefireball = new EntityXPBomb(this.level, this.getX(), this.getY() + 4.0, this.getZ());
                entitylargefireball.setPos(this.getX(), this.getY() + 4.0, this.getZ());
                final EntityXPBomb entityXPBomb = entitylargefireball;
                entityXPBomb.push(0.0, 1.0, 0.0);
                entitylargefireball.setXPCount(12000);
                this.level.addFreshEntity(entitylargefireball);
            }
            if (this.isArmed()) {
                for (int l = 0; l < 16; ++l) {
                    final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.STICK));
                    entityitem.setPickUpDelay(40);
                    this.level.addFreshEntity(entityitem);
                }
                for (int l = 0; l < 32; ++l) {
                    final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.IRON_INGOT));
                    entityitem.setPickUpDelay(40);
                    this.level.addFreshEntity(entityitem);
                }
            }
            for (int l = 0; l < 128 + this.random.nextInt(128 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.ROTTEN_FLESH));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 32 + this.random.nextInt(32 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.BONE));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 32 + this.random.nextInt(32 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.COAL));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 32 + this.random.nextInt(32 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.IRON_INGOT));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 8 + this.random.nextInt(8 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.EMERALD));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 8 + this.random.nextInt(8 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.DIAMOND));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < this.random.nextInt(4 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(TitanItems.harcadium));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            if (this.random.nextInt(10) == 0) {
                final ItemEntity entityitem2 = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.BEDROCK));
                entityitem2.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem2);
            }
        }
    }

    protected void dropRareDrop(final int p_70600_1_) {
        switch (this.random.nextInt(3)) {
            case 0: {
                this.spawnAtLocation(Items.IRON_INGOT, 64);
                break;
            }
            case 1: {
                this.spawnAtLocation(Items.CARROT, 64);
                break;
            }
            case 2: {
                this.spawnAtLocation(Items.POTATO, 64);
                break;
            }
        }
    }

    @Override
    protected void inactDeathAction() {
        if (!this.level.isClientSide) {
            this.playSound(SoundEvents.ZOMBIE_DEATH, this.getSoundVolume(), this.getSoundPitch());
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.dropFewItems(true, 0);
                this.dropEquipment();
                this.dropRareDrop(1);
            }
            final EntityTitanSpirit entitytitan = new EntityTitanSpirit(this.level);
            entitytitan.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.level.addFreshEntity(entitytitan);
            entitytitan.setVesselHunting(false);
            entitytitan.setSpiritType(6);
        }
    }

    @Override
    protected void onTitanDeathUpdate() {
        getEntityData().set(t6, MathHelper.clamp(this.getEntityData().get(t5), 0.0f, this.getMaxHealth()));
        if (this.getEntityData().get(t5) <= 0.0f) {
            ++this.deathTicks;
            AnimationAPI.sendAnimPacket(this, 10);
            this.setAnimID(10);
            this.setTitanHealth(0.0f);
        } else {
            this.hurt(DamageSource.OUT_OF_WORLD, 25.0f);
            this.setTitanHealth(this.getEntityData().get(t5));
            this.setHealth(this.getEntityData().get(t5));
            this.deathTicks = 0;
            if (this.animID == 10) {
                this.animID = 0;
            }
        }
        this.setDeltaMovement(0.0, getDeltaMovement().y, 0.0);
        this.setTarget(null);
        if (this.deathTicks == 1 && !this.level.isClientSide) {
            this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
            final ArrayList listp = new ArrayList(this.level.players());
            if (listp != null && !listp.isEmpty()) {
                for (int i1 = 0; i1 < listp.size(); ++i1) {
                    final Entity entity = (Entity) listp.get(i1);
                    if (entity != null && entity instanceof PlayerEntity) {
                        //((PlayerEntity) entity).triggerAchievement(this.getAchievement());
                    }
                }
            }
        }
        if (this.deathTicks == 1) {
            this.setAnimTick(1);
        }
        if (this.deathTicks >= 80 && this.isArmed()) {
            this.dropSword();
        }
        if (this.deathTicks >= 500) {
            this.setInvulTime(this.getInvulTime() + 8);
            --this.animTick;
            final float f = (this.random.nextFloat() - 0.5f) * 12.0f;
            final float f2 = (this.random.nextFloat() - 0.5f) * 3.0f;
            final float f3 = (this.random.nextFloat() - 0.5f) * 12.0f;
            this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + f, this.getY() + 2.0 + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
        }
        if (this.getInvulTime() >= this.getThreashHold()) {
            this.removeAfterChangingDimensions();
        }
    }

    @Override
    public void killed(ServerWorld p_241847_1_, LivingEntity entityLivingIn) {
        super.killed(p_241847_1_, entityLivingIn);
        if (entityLivingIn instanceof VillagerEntity) {
            final EntityZombieMinion entityzombie = new EntityZombieMinion(this.level);
            entityzombie.copyPosition(entityLivingIn);
            this.level.getChunk(entityLivingIn.xChunk, entityLivingIn.zChunk).removeEntity(entityLivingIn);
            entityzombie.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
            entityzombie.setVillager(true);
            if (entityLivingIn.isBaby()) {
                entityzombie.setBaby(true);
            }
            this.level.addFreshEntity(entityzombie);
            this.level.levelEvent(null, 1016, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
        }
    }

    public boolean attackZombieFrom(final DamageSource source, float amount) {
        if (this.isArmored()) {
            amount /= 4.0f;
        }
        if (this.isInvulnerable()) {
            return false;
        }
        if (this.isArmed() && source.getEntity() instanceof PlayerEntity) {
            return false;
        }
        if (this.isArmored() && source instanceof IndirectEntityDamageSource) {
            return false;
        }
        if (source.getEntity() instanceof EntityZombieMinion || source.getEntity() instanceof EntityZombieTitan || source.getEntity() instanceof EntityGiantZombieBetter) {
            return false;
        }
        final Entity entity = source.getEntity();
        if (entity instanceof LivingEntity && !this.isInvulnerable() && amount > 25.0f) {
            final List list = this.level.getEntities(this, this.getBoundingBox().inflate(100.0, 100.0, 100.0));
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = (Entity) list.get(i);
                if (entity2 instanceof EntityZombieTitan entitypigzombie) {
                    entitypigzombie.setTarget((LivingEntity) entity);
                    entitypigzombie.setLastHurtByMob((LivingEntity) entity);
                }
                this.setTarget((LivingEntity) entity);
                this.setLastHurtByMob((LivingEntity) entity);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        return this.attackZombieFrom(source, amount);
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFromPart(final EntityTitanPart p_70965_1_, final DamageSource source, final float amount) {
        this.func_82195_e(source, amount);
        return true;
    }

    protected boolean func_82195_e(final DamageSource p_82195_1_, final float p_82195_2_) {
        return this.attackZombieFrom(p_82195_1_, p_82195_2_);
    }

    protected float getSoundPitch() {
        return isBaby() ? 2.0f : 1.0f;
    }

    @Override
    public int getAnimID() {
        return this.animID;
    }

    @Override
    public void setAnimID(final int id) {
        this.animID = id;
    }

    @Override
    public int getAnimTick() {
        return this.animTick;
    }

    @Override
    public void setAnimTick(final int tick) {
        this.animTick = tick;
    }

    public static class GroupData implements ILivingEntityData {
        public final boolean isBaby;
        public final boolean canSpawnJockey;

        public GroupData(boolean p_i231567_1_, boolean p_i231567_2_) {
            this.isBaby = p_i231567_1_;
            this.canSpawnJockey = p_i231567_2_;
        }

        GroupData(final boolean p_i2349_2_, final boolean p_i2349_3_, final Object p_i2349_4_) {
            this(p_i2349_2_, p_i2349_3_);
        }
    }
}
