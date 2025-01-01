package net.minecraft.entity.titan;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.entity.titan.animation.skeletontitan.*;
import net.minecraft.entity.titanminion.EntitySkeletonMinion;
import net.minecraft.entity.titanminion.EntityWitherMinion;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.theTitans.ClientProxy;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraftforge.entity.PartEntity;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class EntitySkeletonTitan extends EntityTitan implements IEntityMultiPartTitan, IAnimatedEntity {
    public static final DataParameter<Boolean> t13 = EntityDataManager.defineId(EntitySkeletonTitan.class, DataSerializers.BOOLEAN);
    public boolean shouldBeWitherSkeleton;
    public int attackTimer;
    public boolean isStunned;
    public EntityTitanPart[] partArray;
    public EntityTitanPart head;
    public EntityTitanPart pelvis;
    public EntityTitanPart spine;
    public EntityTitanPart ribCage;
    public EntityTitanPart rightArm;
    public EntityTitanPart leftArm;
    public EntityTitanPart rightLeg;
    public EntityTitanPart leftLeg;
    public EntitySkeletonTitan(EntityType<? extends EntityTitan> type, World worldIn) {
        super(type, worldIn);
        this.head = new EntityTitanPart(worldIn, this, "head", 8.0f, 8.0f);
        this.pelvis = new EntityTitanPart(worldIn, this, "pelvis", 8.0f, 6.0f);
        this.spine = new EntityTitanPart(worldIn, this, "spine", 2.0f, 12.0f);
        this.ribCage = new EntityTitanPart(worldIn, this, "ribcage", 8.0f, 8.0f);
        this.rightArm = new EntityTitanPart(worldIn, this, "rightarm", 2.0f, 2.0f);
        this.leftArm = new EntityTitanPart(worldIn, this, "leftarm", 2.0f, 2.0f);
        this.rightLeg = new EntityTitanPart(worldIn, this, "rightleg", 2.0f, 12.0f);
        this.leftLeg = new EntityTitanPart(worldIn, this, "leftleg", 2.0f, 12.0f);
        this.partArray = new EntityTitanPart[]{this.head, this.pelvis, this.spine, this.ribCage, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg};
        if (this.getSkeletonType() == 1) {
            this.setSize(14.0f, 56.0f);
            this.xpReward = 50000;
        }
        this.setSize(8.0f, 32.0f);
        this.xpReward = 20000;
        if (worldIn != null && !worldIn.isClientSide) {
            this.setCombatTask();
        }
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntityZombieTitan.class, 0, false));
    }

    @Override
    protected void applyEntityAI() {
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanCreation(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanDeath(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanLightningHand(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanLightningSword(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanStun(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanAttack3(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanAttack2(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanRangedAttack1(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanRangedAttack2(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanAttack5(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanAttack1(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanAttack4(this));
        this.goalSelector.addGoal(1, new AnimationSkeletonTitanAntiTitanAttack(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        if (TitanConfig.TitansFFAMode) {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SkeletonTitanSorter));
        } else {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SearchForAThingToKill));
        }
    }

    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 4.0f || TitanConfig.NightmareMode || TitanConfig.TotalDestructionMode;
    }

    public boolean canAttack(final Class p_70686_1_) {
        return (p_70686_1_ != this.head.getClass() && p_70686_1_ != this.pelvis.getClass() && p_70686_1_ != this.rightArm.getClass() && p_70686_1_ != this.leftArm.getClass() && p_70686_1_ != this.rightLeg.getClass() && p_70686_1_ != this.leftLeg.getClass() && p_70686_1_ != EntitySkeletonMinion.class && p_70686_1_ != EntitySkeletonTitan.class && p_70686_1_ != EntitySkeletonTitanGiantArrow.class && p_70686_1_ != EntityWitherMinion.class) || p_70686_1_.getName().contains("MutantSkeleton");
    }

    @Override
    public int getMinionCap() {
        return 160;
    }

    @Override
    public int getPriestCap() {
        return 90;
    }

    @Override
    public int getZealotCap() {
        return 40;
    }

    @Override
    public int getBishopCap() {
        return 20;
    }

    @Override
    public int getTemplarCap() {
        return 10;
    }

    @Override
    public int getSpecialMinionCap() {
        return 6;
    }

    @Override
    public int getMinionSpawnRate() {
        if (this.getSkeletonType() == 1) {
            return TitanConfig.WitherSkeletonTitanMinionSpawnrate;
        }
        return TitanConfig.SkeletonTitanMinionSpawnrate;
    }

    @Override
    public float getRenderSizeModifier() {
        float f = 16.0f;
        if (this.getSkeletonType() == 1) {
            f *= 1.75f;
        }
        return f;
    }

    @Override
    public int getParticleCount() {
        if (this.getSkeletonType() == 1) {
            return 48;
        }
        return super.getParticleCount();
    }

    @Override
    public BasicParticleType getParticles() {
        if (this.getSkeletonType() == 1) {
            this.shouldParticlesBeUpward = true;
            return ParticleTypes.LARGE_SMOKE;
        }
        return super.getParticles();
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return EntityTitan.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 140.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public boolean canBeHurtByPlayer() {
        return this.isStunned && !this.isInvulnerable();
    }

    @Override
    public EnumTitanStatus getTitanStatus() {
        return (this.getSkeletonType() == 1) ? EnumTitanStatus.GREATER : EnumTitanStatus.AVERAGE;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(t13, false);
    }

    @Override
    public int getFootStepModifer() {
        return (this.getSkeletonType() == 1) ? 2 : 3;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return (this.isStunned || this.getWaiting() || this.animID == 13) ? null : ((this.getSkeletonType() == 1) ? TitanSounds.titanWitherSkeletonLiving : TitanSounds.titanSkeletonLiving);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return (this.getSkeletonType() == 1) ? TitanSounds.titanWitherSkeletonGrunt : TitanSounds.titanSkeletonGrunt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return (this.getSkeletonType() == 1) ? TitanSounds.titanWitherSkeletonDeath : TitanSounds.titanSkeletonDeath;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(TitanSounds.titanStep, 10.0f, 1.0f);
        this.playSound(SoundEvents.SKELETON_STEP, 10.0f, 0.5f);
        this.shakeNearbyPlayerCameras(4000.0);
        if (!this.getWaiting() && this.animID != 13) {
            final float f3 = this.yRot * 3.1415927f / 180.0f;
            final float f4 = MathHelper.sin(f3);
            final float f5 = MathHelper.cos(f3);
            if (this.footID == 0) {
                this.destroyBlocksInAABB(this.rightLeg.getBoundingBox().move(0.0, -1.0, 0.0));
                this.collideWithEntities(this.rightLeg, this.level.getEntities(this, this.rightLeg.getBoundingBox().inflate(1.0, 1.0, 1.0).move(f4 * 4.0f, 0.0, f5 * 4.0f)));
                ++this.footID;
            } else {
                this.destroyBlocksInAABB(this.leftLeg.getBoundingBox().move(0.0, -1.0, 0.0));
                this.collideWithEntities(this.leftLeg, this.level.getEntities(this, this.leftLeg.getBoundingBox().inflate(1.0, 1.0, 1.0).move(f4 * 4.0f, 0.0, f5 * 4.0f)));
                this.footID = 0;
            }
        }
    }

    @Override
    public boolean doHurtTarget(final Entity p_70652_1_) {
        return false;
    }

    @Override
    public float getSpeed() {
        return (float) ((this.getSkeletonType() == 1) ? (0.4 + this.getExtraPower() * 0.001) : (0.3 + this.getExtraPower() * 0.001));
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    public void becomeWitherSkeleton(final boolean skelly) {
        if (skelly) {
            this.setSkeletonType(1);
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
    }

    @Override
    public boolean canAttack() {
        return false;
    }

    @Override
    public boolean shouldMove() {
        return this.animID == 0 && !this.isStunned && !this.getWaiting() && this.getTarget() != null && super.shouldMove();
    }

    @Override
    public double getMeleeRange() {
        return this.getBbWidth() * this.getBbWidth() + ((this.getTarget().getBbWidth() > 48.0f) ? 2304.0f : (this.getTarget().getBbWidth() * this.getTarget().getBbWidth())) + ((this.getSkeletonType() == 1) ? 2000.0 : 800.0);
    }

    @Override
    public void aiStep() {
        this.setCombatTask();
        if (this.tickCount == 1) {
            this.setSkeletonType(this.getSkeletonType());
        }
        final float dis = (this.getSkeletonType() == 1) ? 32.0f : 16.0f;
        final float xfac = MathHelper.sin(this.yBodyRot * 3.1415927f / 180.0f);
        final float zfac = MathHelper.cos(this.yBodyRot * 3.1415927f / 180.0f);
        if (!this.isPassenger() && !this.getWaiting() && !this.isStunned && this.animID == 0) {
            if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) > this.getMeleeRange() + (this.getTarget().isOnGround() ? 8000.0 : 1000.0)) {
                if (this.getY() <= this.getTarget().getY() + 12.0 && this.getY() < 256.0 - this.getBbHeight()) {
                    this.fallDistance = 0.0f;
                    this.addTitanVelocity(0.0, 0.9 - this.getDeltaMovement().y, 0.0);
                    if (this.getDeltaMovement().y < 0.0) {
                        this.setDeltaMovement(this.getDeltaMovement().x, 0.0, this.getDeltaMovement().z);
                    }
                }
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.6, this.getDeltaMovement().z);
            }
            if (!this.onGround) {
                final float f = (this.random.nextFloat() - 0.5f) * ((this.getSkeletonType() == 1) ? 30.0f : 10.0f);
                final float f2 = (this.random.nextFloat() - 0.5f);
                final float f3 = (this.random.nextFloat() - 0.5f) * ((this.getSkeletonType() == 1) ? 30.0f : 10.0f);
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + f, this.getY() + ((this.getSkeletonType() == 1) ? 15.0 : 5.0) + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
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
                // player.triggerAchievement(TitansAchievments.locateTitan);
            }
        } else {
            if (this.getAnimID() == 13) {
                this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
                if (this.getDeltaMovement().y > 0.0) {
                    this.setDeltaMovement(this.getDeltaMovement().x, 0.0, this.getDeltaMovement().z);
                }
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 1) {
                this.playSound(TitanSounds.titanBirth, 1000.0f, (this.getSkeletonType() == 1) ? 0.875f : 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 10) {
                this.playSound(TitanSounds.titanSkeletonAwaken, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 100) {
                this.playSound(TitanSounds.titanRumble, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 160) {
                this.playSound(TitanSounds.titanSkeletonBeginMove, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 280) {
                this.playSound(TitanSounds.titanQuake, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 300) {
                this.playSound(TitanSounds.titanRumble, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && this.getAnimTick() == 520) {
                this.playSound(TitanSounds.titanSkeletonGetUp, this.getSoundVolume(), 1.0f);
            }
            if (this.getAnimID() == 13 && (this.getAnimTick() == 230 || this.getAnimTick() == 550 || this.getAnimTick() == 590 || this.getAnimTick() == 610)) {
                this.playStepSound(new BlockPos(0, 0, 0), Blocks.STONE.defaultBlockState());
                this.playSound(TitanSounds.titanPress, this.getSoundVolume(), 1.0f);
            }
        }
        if (this.getSkeletonType() == 1) {
            this.setSize(14.0f, 56.0f);
        } else {
            this.setSize(8.0f, 32.0f);
        }
        final float f4 = this.yBodyRot * 3.1415927f / 180.0f;
        final float f5 = MathHelper.sin(f4);
        final float f6 = MathHelper.cos(f4);
        if (this.tickCount > 5) {
            this.head.moveTo(this.getX(), this.getY() + ((this.getSkeletonType() == 1) ? 42.0 : 24.0), this.getZ(), 0.0f, 0.0f);
            this.pelvis.moveTo(this.getX(), this.getY() + ((this.getSkeletonType() == 1) ? 21.0 : 12.0), this.getZ(), 0.0f, 0.0f);
            this.spine.moveTo(this.getX() + f5 * ((this.getSkeletonType() == 1) ? 3.5 : 2.0), this.getY() + ((this.getSkeletonType() == 1) ? 21.0 : 12.0), this.getZ() - f6 * ((this.getSkeletonType() == 1) ? 3.5 : 2.0), 0.0f, 0.0f);
            this.ribCage.moveTo(this.getX(), this.getY() + ((this.getSkeletonType() == 1) ? 33.25 : 19.0), this.getZ(), 0.0f, 0.0f);
            this.rightArm.moveTo(this.getX() + f6 * ((this.getSkeletonType() == 1) ? 8.75 : 5.0), this.getY() + ((this.getSkeletonType() == 1) ? 20.125 : 11.5), this.getZ() + f5 * ((this.getSkeletonType() == 1) ? 8.75 : 5.0), 0.0f, 0.0f);
            this.leftArm.moveTo(this.getX() - f6 * ((this.getSkeletonType() == 1) ? 8.75 : 5.0), this.getY() + ((this.getSkeletonType() == 1) ? 20.125 : 11.5), this.getZ() - f5 * ((this.getSkeletonType() == 1) ? 8.75 : 5.0), 0.0f, 0.0f);
            this.rightLeg.moveTo(this.getX() + f6 * ((this.getSkeletonType() == 1) ? 3.5 : 2.0), this.getY(), this.getZ() + f5 * ((this.getSkeletonType() == 1) ? 3.5 : 2.0), 0.0f, 0.0f);
            this.leftLeg.moveTo(this.getX() - f6 * ((this.getSkeletonType() == 1) ? 3.5 : 2.0), this.getY(), this.getZ() - f5 * ((this.getSkeletonType() == 1) ? 3.5 : 2.0), 0.0f, 0.0f);
            if (this.isAlive() && !this.isStunned) {
                this.collideWithEntities(this.head, this.level.getEntities(this, this.head.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.pelvis, this.level.getEntities(this, this.pelvis.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.spine, this.level.getEntities(this, this.spine.getBoundingBox()));
                this.collideWithEntities(this.ribCage, this.level.getEntities(this, this.ribCage.getBoundingBox()));
                this.collideWithEntities(this.rightArm, this.level.getEntities(this, this.rightArm.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.leftArm, this.level.getEntities(this, this.leftArm.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.leftLeg, this.level.getEntities(this, this.leftLeg.getBoundingBox().inflate(1.0, 0.0, 1.0)));
                this.collideWithEntities(this.rightLeg, this.level.getEntities(this, this.rightLeg.getBoundingBox().inflate(1.0, 0.0, 1.0)));
            }
            this.destroyBlocksInAABB(this.head.getBoundingBox());
            this.destroyBlocksInAABB(this.pelvis.getBoundingBox());
            this.destroyBlocksInAABB(this.spine.getBoundingBox());
            this.destroyBlocksInAABB(this.ribCage.getBoundingBox());
            this.destroyBlocksInAABB(this.rightArm.getBoundingBox());
            this.destroyBlocksInAABB(this.leftArm.getBoundingBox());
            this.destroyBlocksInAABB(this.leftLeg.getBoundingBox());
            this.destroyBlocksInAABB(this.rightLeg.getBoundingBox());
        }
        if (this.isStunned || this.deathTicks > 0) {
            this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
        }
        final List list3 = this.level.getEntities(this, this.getBoundingBox().inflate(256.0, 256.0, 256.0));
        if (list3 != null && !list3.isEmpty() && this.tickCount % 400 == 0) {
            for (int i9 = 0; i9 < list3.size(); ++i9) {
                final Entity entity3 = (Entity) list3.get(i9);
                if (entity3 != null && entity3 instanceof ArrowEntity) {
                    entity3.remove(false);
                }
            }
        }
        if ((this.getAnimID() == 5 || this.getAnimID() == 11) && this.getAnimTick() >= 40) {
            ++this.attackTimer;
        } else if (this.getAnimID() != 5 && this.getAnimID() != 11) {
            this.attackTimer = 0;
        }
        if (this.attackTimer < 0) {
            this.attackTimer = 0;
        }
        if (!this.level.isClientSide && this.getTarget() != null && this.getAnimID() == 5 && this.getAnimTick() >= 100 && this.getAnimTick() <= 300) {
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
            this.attackEntityWithRangedAttack(this.getTarget(), 1.0f);
        }
        if (!this.level.isClientSide && this.getTarget() != null && this.getAnimID() == 11 && this.getAnimTick() == 78) {
            this.attackEntityWithEnlargedRangedAttack(this.getTarget(), 1.0f);
        }
        if (!AnimationAPI.isEffectiveClient() && this.getTarget() != null && !this.isStunned && this.getAnimID() == 0) {
            final double d9 = this.distanceToSqr(this.getTarget());
            if (d9 < this.getMeleeRange()) {
                if (this.getTarget() instanceof EntityTitan || this.getTarget().getBbHeight() >= 6.0f || this.getTarget().getY() > this.getY() + 6.0) {
                    AnimationAPI.sendAnimPacket(this, 1);
                    this.setAnimID(1);
                } else {
                    switch (this.random.nextInt(5)) {
                        case 0: {
                            AnimationAPI.sendAnimPacket(this, 6);
                            this.setAnimID(6);
                            break;
                        }
                        case 1: {
                            AnimationAPI.sendAnimPacket(this, 3);
                            this.setAnimID(3);
                            break;
                        }
                        case 2: {
                            AnimationAPI.sendAnimPacket(this, 7);
                            this.setAnimID(7);
                            break;
                        }
                        case 3: {
                            AnimationAPI.sendAnimPacket(this, 2);
                            this.setAnimID(2);
                            break;
                        }
                        case 4: {
                            AnimationAPI.sendAnimPacket(this, 4);
                            this.setAnimID(4);
                            break;
                        }
                    }
                }
            } else if (this.animID == 0 && this.getRandom().nextInt(80) == 0) {
                switch (this.random.nextInt(6)) {
                    case 0: {
                        if (this.getSkeletonType() != 1) {
                            AnimationAPI.sendAnimPacket(this, 5);
                            this.setAnimID(5);
                            break;
                        }
                        AnimationAPI.sendAnimPacket(this, 9);
                        this.setAnimID(9);
                        break;
                    }
                    case 1: {
                        AnimationAPI.sendAnimPacket(this, 12);
                        this.setAnimID(12);
                        break;
                    }
                    case 2: {
                        if (this.getSkeletonType() != 1) {
                            AnimationAPI.sendAnimPacket(this, 11);
                            this.setAnimID(11);
                            break;
                        }
                        AnimationAPI.sendAnimPacket(this, 12);
                        this.setAnimID(12);
                        break;
                    }
                }
            }
        }
        if (this.animID == 1 && this.animTick == 1) {
            this.antiTitanAttackAnimeID = this.getRandom().nextInt(4);
        }
        if (this.getSkeletonType() == 1) {
            this.setCustomName(new TranslationTextComponent("entity.WitherSkeletonTitan.name"));
            if (TitanConfig.NightmareMode) {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15000.0 + this.getExtraPower() * 750);
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(400000.0 + this.getExtraPower() * 40000);
            } else {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5000.0 + this.getExtraPower() * 250);
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200000.0 + this.getExtraPower() * 20000);
            }
        } else {
            this.setCustomName(new TranslationTextComponent("entity.SkeletonTitan.name"));
            if (TitanConfig.NightmareMode) {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(360.0 + this.getExtraPower() * 60);
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40000.0 + this.getExtraPower() * 2000);
            } else {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(120.0 + this.getExtraPower() * 30);
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20000.0 + this.getExtraPower() * 1000);
            }
        }
        if (this.random.nextInt(2) == 0 && this.getSkeletonType() == 1 && this.isInWaterOrRain()) {
            this.playSound(SoundEvents.FIRE_EXTINGUISH, 10.0f, 1.0f);
        }
//        if (!this.getWaiting() && this.animID != 13 && !(this.worldObj.provider instanceof WorldProviderVoid)) {
//            if ((this.rand.nextInt(this.getMinionSpawnRate()) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.worldObj.isRemote && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
//                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.rand.nextInt(100) == 0 && this.getSkeletonType() == 1) {
//                    final EntityWitherMinion entitychicken = new EntityWitherMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken);
//                    entitychicken.master = this;
//                    entitychicken.addVelocity(0.0, 0.25, 0.0);
//                    entitychicken.playSound("thetitans:titansummonminion", 10.0f, 0.6f);
//                    entitychicken.onSpawnWithEgg(null);
//                    this.worldObj.spawnEntityInWorld(entitychicken);
//                    ++this.numSpecialMinions;
//                } else if (this.numMinions < this.getMinionCap()) {
//                    final EntitySkeletonMinion entitychicken2 = new EntitySkeletonMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken2);
//                    entitychicken2.master = this;
//                    entitychicken2.playSound("thetitans:titansummonminion", 2.0f, 1.0f);
//                    entitychicken2.addVelocity(0.0, 0.9, 0.0);
//                    entitychicken2.onSpawnWithEgg(null);
//                    entitychicken2.setMinionType(0);
//                    this.worldObj.spawnEntityInWorld(entitychicken2);
//                    entitychicken2.addPotionEffect(new PotionEffect(Potion.resistance.id, 40, 4, false));
//                    final Block block3 = this.worldObj.getBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ);
//                    this.worldObj.playAuxSFX(2001, (int) entitychicken2.posX, (int) entitychicken2.posY, (int) entitychicken2.posZ, Block.getIdFromBlock(block3));
//                    ++this.numMinions;
//                    if (block3 == Blocks.grass) {
//                        this.worldObj.setBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ, Blocks.dirt);
//                    }
//                    if (this.getSkeletonType() == 1) {
//                        entitychicken2.setSkeletonType(1);
//                    } else {
//                        entitychicken2.setSkeletonType(0);
//                        entitychicken2.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Integer.MAX_VALUE, 0, false));
//                    }
//                }
//            }
//            if ((this.rand.nextInt(this.getMinionSpawnRate() * 2) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.worldObj.isRemote && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
//                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.rand.nextInt(100) == 0 && this.getSkeletonType() == 1) {
//                    final EntityWitherMinion entitychicken = new EntityWitherMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken);
//                    entitychicken.master = this;
//                    entitychicken.addVelocity(0.0, 0.25, 0.0);
//                    entitychicken.playSound("thetitans:titansummonminion", 10.0f, 0.6f);
//                    entitychicken.onSpawnWithEgg(null);
//                    this.worldObj.spawnEntityInWorld(entitychicken);
//                    ++this.numSpecialMinions;
//                } else if (this.numPriests < this.getPriestCap()) {
//                    final EntitySkeletonMinion entitychicken2 = new EntitySkeletonMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken2);
//                    entitychicken2.master = this;
//                    entitychicken2.playSound("thetitans:titansummonminion", 2.0f, 1.0f);
//                    entitychicken2.addVelocity(0.0, 0.9, 0.0);
//                    entitychicken2.onSpawnWithEgg(null);
//                    entitychicken2.setMinionType(1);
//                    this.worldObj.spawnEntityInWorld(entitychicken2);
//                    entitychicken2.addPotionEffect(new PotionEffect(Potion.resistance.id, 40, 4, false));
//                    final Block block3 = this.worldObj.getBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ);
//                    this.worldObj.playAuxSFX(2001, (int) entitychicken2.posX, (int) entitychicken2.posY, (int) entitychicken2.posZ, Block.getIdFromBlock(block3));
//                    ++this.numPriests;
//                    if (block3 == Blocks.grass) {
//                        this.worldObj.setBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ, Blocks.dirt);
//                    }
//                    if (this.getSkeletonType() == 1) {
//                        entitychicken2.setSkeletonType(1);
//                    } else {
//                        entitychicken2.setSkeletonType(0);
//                        entitychicken2.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Integer.MAX_VALUE, 0, false));
//                    }
//                }
//            }
//            if ((this.rand.nextInt(this.getMinionSpawnRate() * 4) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.worldObj.isRemote && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
//                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.rand.nextInt(100) == 0 && this.getSkeletonType() == 1) {
//                    final EntityWitherMinion entitychicken = new EntityWitherMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken);
//                    entitychicken.master = this;
//                    entitychicken.addVelocity(0.0, 0.25, 0.0);
//                    entitychicken.playSound("thetitans:titansummonminion", 10.0f, 0.6f);
//                    entitychicken.onSpawnWithEgg(null);
//                    this.worldObj.spawnEntityInWorld(entitychicken);
//                    ++this.numSpecialMinions;
//                } else if (this.numZealots < this.getZealotCap()) {
//                    final EntitySkeletonMinion entitychicken2 = new EntitySkeletonMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken2);
//                    entitychicken2.master = this;
//                    entitychicken2.playSound("thetitans:titansummonminion", 2.0f, 1.0f);
//                    entitychicken2.addVelocity(0.0, 0.9, 0.0);
//                    entitychicken2.onSpawnWithEgg(null);
//                    entitychicken2.setMinionType(2);
//                    this.worldObj.spawnEntityInWorld(entitychicken2);
//                    entitychicken2.addPotionEffect(new PotionEffect(Potion.resistance.id, 40, 4, false));
//                    final Block block3 = this.worldObj.getBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ);
//                    this.worldObj.playAuxSFX(2001, (int) entitychicken2.posX, (int) entitychicken2.posY, (int) entitychicken2.posZ, Block.getIdFromBlock(block3));
//                    ++this.numZealots;
//                    if (block3 == Blocks.grass) {
//                        this.worldObj.setBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ, Blocks.dirt);
//                    }
//                    if (this.getSkeletonType() == 1) {
//                        entitychicken2.setSkeletonType(1);
//                    } else {
//                        entitychicken2.setSkeletonType(0);
//                        entitychicken2.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Integer.MAX_VALUE, 0, false));
//                    }
//                }
//            }
//            if ((this.rand.nextInt(this.getMinionSpawnRate() * 8) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.worldObj.isRemote && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
//                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.rand.nextInt(100) == 0 && this.getSkeletonType() == 1) {
//                    final EntityWitherMinion entitychicken = new EntityWitherMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken);
//                    entitychicken.master = this;
//                    entitychicken.addVelocity(0.0, 0.25, 0.0);
//                    entitychicken.playSound("thetitans:titansummonminion", 10.0f, 0.6f);
//                    entitychicken.onSpawnWithEgg(null);
//                    this.worldObj.spawnEntityInWorld(entitychicken);
//                    ++this.numSpecialMinions;
//                } else if (this.numBishop < this.getBishopCap()) {
//                    final EntitySkeletonMinion entitychicken2 = new EntitySkeletonMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken2);
//                    entitychicken2.master = this;
//                    entitychicken2.playSound("thetitans:titansummonminion", 2.0f, 1.0f);
//                    entitychicken2.addVelocity(0.0, 0.9, 0.0);
//                    entitychicken2.onSpawnWithEgg(null);
//                    entitychicken2.setMinionType(3);
//                    this.worldObj.spawnEntityInWorld(entitychicken2);
//                    entitychicken2.addPotionEffect(new PotionEffect(Potion.resistance.id, 40, 4, false));
//                    final Block block3 = this.worldObj.getBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ);
//                    this.worldObj.playAuxSFX(2001, (int) entitychicken2.posX, (int) entitychicken2.posY, (int) entitychicken2.posZ, Block.getIdFromBlock(block3));
//                    ++this.numBishop;
//                    if (block3 == Blocks.grass) {
//                        this.worldObj.setBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ, Blocks.dirt);
//                    }
//                    if (this.getSkeletonType() == 1) {
//                        entitychicken2.setSkeletonType(1);
//                    } else {
//                        entitychicken2.setSkeletonType(0);
//                        entitychicken2.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Integer.MAX_VALUE, 0, false));
//                    }
//                }
//            }
//            if ((this.rand.nextInt(this.getMinionSpawnRate() * 16) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.worldObj.isRemote && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
//                if (this.numSpecialMinions < this.getSpecialMinionCap() && this.rand.nextInt(100) == 0 && this.getSkeletonType() == 1) {
//                    final EntityWitherMinion entitychicken = new EntityWitherMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken);
//                    entitychicken.master = this;
//                    entitychicken.addVelocity(0.0, 0.25, 0.0);
//                    entitychicken.playSound("thetitans:titansummonminion", 10.0f, 0.6f);
//                    entitychicken.onSpawnWithEgg(null);
//                    this.worldObj.spawnEntityInWorld(entitychicken);
//                    ++this.numSpecialMinions;
//                } else if (this.numTemplar < this.getTemplarCap()) {
//                    final EntitySkeletonMinion entitychicken2 = new EntitySkeletonMinion(this.worldObj);
//                    this.teleportEntityRandomly(entitychicken2);
//                    entitychicken2.master = this;
//                    entitychicken2.playSound("thetitans:titansummonminion", 2.0f, 1.0f);
//                    entitychicken2.addVelocity(0.0, 0.9, 0.0);
//                    entitychicken2.onSpawnWithEgg(null);
//                    entitychicken2.setMinionType(4);
//                    this.worldObj.spawnEntityInWorld(entitychicken2);
//                    entitychicken2.addPotionEffect(new PotionEffect(Potion.resistance.id, 40, 4, false));
//                    final Block block3 = this.worldObj.getBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ);
//                    this.worldObj.playAuxSFX(2001, (int) entitychicken2.posX, (int) entitychicken2.posY, (int) entitychicken2.posZ, Block.getIdFromBlock(block3));
//                    ++this.numTemplar;
//                    if (block3 == Blocks.grass) {
//                        this.worldObj.setBlock((int) entitychicken2.posX, (int) (entitychicken2.posY + 1.0), (int) entitychicken2.posZ, Blocks.dirt);
//                    }
//                    if (this.getSkeletonType() == 1) {
//                        entitychicken2.setSkeletonType(1);
//                    } else {
//                        entitychicken2.setSkeletonType(0);
//                        entitychicken2.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Integer.MAX_VALUE, 0, false));
//                    }
//                }
//            }
//        }
        super.aiStep();
    }

    public void attackEntityWithEnlargedRangedAttack(final LivingEntity p_82196_1_, final float p_82196_2_) {
        final float rotationYawHead = this.yHeadRot;
        this.yRot = rotationYawHead;
        this.yBodyRot = rotationYawHead;
        this.lookAt(p_82196_1_, 180.0f, 30.0f);
        final float dis = 10.0f;
        final float xfac = MathHelper.sin(this.yBodyRot * 3.1415927f / 180.0f);
        final float zfac = MathHelper.cos(this.yBodyRot * 3.1415927f / 180.0f);
        final double d0 = p_82196_1_.getX() - (this.getX() - xfac * dis);
        final double d2 = p_82196_1_.getY() - (this.getY() + 17.0);
        final double d3 = p_82196_1_.getZ() - (this.getZ() + zfac * dis);
        final EntitySkeletonTitanGiantArrow entityarrow = new EntitySkeletonTitanGiantArrow(EntityType.ARROW, this.level);
        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        entityarrow.setPos(this.getX() - xfac * dis,  this.getY() + 17.0, this.getZ() + zfac * dis);
        this.level.addFreshEntity(entityarrow);
        this.playSound(SoundEvents.SKELETON_SHOOT, 10.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        if (this.distanceToSqr(p_82196_1_) <= this.getMeleeRange()) {
            this.attackChoosenEntity(p_82196_1_, 10.0f, 5);
        }
    }

    public void attackEntityWithRangedAttack(final LivingEntity p_82196_1_, final float p_82196_2_) {
        final float rotationYawHead = this.yHeadRotO;
        this.yRot = rotationYawHead;
        this.yBodyRot = rotationYawHead;
        this.lookAt(p_82196_1_, 180.0f, 30.0f);
        final float dis = 10.0f;
        final float xfac = MathHelper.sin(this.yBodyRot * 3.1415927f / 180.0f);
        final float zfac = MathHelper.cos(this.yBodyRot * 3.1415927f / 180.0f);
        final EntityHarcadiumArrow entityarrow = new EntityHarcadiumArrow(EntityType.ARROW, this.level, p_82196_2_);
        final double d0 = p_82196_1_.getX() - (this.getX() - xfac * dis);
        final double d2 = p_82196_1_.getY() - (this.getY() + 18.0);
        final double d3 = p_82196_1_.getZ() - (this.getZ() + zfac * dis);
        final float f1 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        entityarrow.shoot(d0, d2 + f1, d3, f1 / 57.295776f * 1.6f, 36.0f);
        entityarrow.setPos(this.getX() - xfac * dis, this.getY() + 18.0, this.getZ() + zfac * dis);
        final int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, this);
        final int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, this);
        final int f2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, this);
        if (TitanConfig.NightmareMode) {
            entityarrow.setBaseDamage(90.0);
        } else {
            entityarrow.setBaseDamage(30.0);
        }
        entityarrow.setCritArrow(true);
        if (i > 0) {
            entityarrow.setBaseDamage(entityarrow.getBaseDamage() + i * 10.0 + 1.0);
        }
        if (j > 1) {
            entityarrow.setKnockback(j);
        } else {
            entityarrow.setKnockback(2);
        }
        if (f2 > 0 || this.getSkeletonType() == 1) {
            entityarrow.setSecondsOnFire(10000);
        }
        this.level.addFreshEntity(entityarrow);
        this.playSound(SoundEvents.SKELETON_SHOOT, 3.0f, 1.9f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        if (this.distanceToSqr(p_82196_1_) <= this.getMeleeRange()) {
            this.attackChoosenEntity(p_82196_1_, 10.0f, 5);
        }
    }

    public void setCombatTask() {
        this.meleeTitan = true;
    }

    private void setSkeletonType(int p_82201_1_) {
        this.entityData.set(t13, p_82201_1_ == 1);
        if (p_82201_1_ == 1) {
            this.setSize(14.0f, 56.0f);
            this.xpReward = 50000;
            final EntityTitanPart head = this.head;
            final EntityTitanPart head2 = this.head;
            final float n = 14.0f;
            head2.height = n;
            head.width = n;
            this.pelvis.width = 10.5f;
            this.spine.height = 21.0f;
            this.ribCage.height = 8.75f;
            this.ribCage.width = 12.25f;
            final EntityTitanPart pelvis = this.pelvis;
            final EntityTitanPart spine = this.spine;
            final EntityTitanPart rightLeg = this.rightLeg;
            final EntityTitanPart leftLeg = this.leftLeg;
            final EntityTitanPart leftArm = this.leftArm;
            final EntityTitanPart rightArm = this.rightArm;
            final float n2 = 3.5f;
            rightArm.width = n2;
            leftArm.width = n2;
            leftLeg.width = n2;
            rightLeg.width = n2;
            spine.width = n2;
            pelvis.height = n2;
            final EntityTitanPart rightLeg2 = this.rightLeg;
            final EntityTitanPart leftLeg2 = this.leftLeg;
            final EntityTitanPart leftArm2 = this.leftArm;
            final EntityTitanPart rightArm2 = this.rightArm;
            final float n3 = 21.0f;
            rightArm2.height = n3;
            leftArm2.height = n3;
            leftLeg2.height = n3;
            rightLeg2.height = n3;
            final EntityTitanPart leftArm3 = this.leftArm;
            final EntityTitanPart rightArm3 = this.rightArm;
            final float n4 = 21.0f;
            rightArm3.height = n4;
            leftArm3.height = n4;
        } else {
            this.setSize(8.0f, 32.0f);
            this.xpReward = 20000;
            final EntityTitanPart head3 = this.head;
            final EntityTitanPart head4 = this.head;
            final float n5 = 8.0f;
            head4.height = n5;
            head3.width = n5;
            this.pelvis.width = 6.0f;
            this.spine.height = 12.0f;
            this.ribCage.height = 5.0f;
            this.ribCage.width = 7.0f;
            final EntityTitanPart pelvis2 = this.pelvis;
            final EntityTitanPart spine2 = this.spine;
            final EntityTitanPart rightLeg3 = this.rightLeg;
            final EntityTitanPart leftLeg3 = this.leftLeg;
            final EntityTitanPart leftArm4 = this.leftArm;
            final EntityTitanPart rightArm4 = this.rightArm;
            final float n6 = 2.0f;
            rightArm4.width = n6;
            leftArm4.width = n6;
            leftLeg3.width = n6;
            rightLeg3.width = n6;
            spine2.width = n6;
            pelvis2.height = n6;
            final EntityTitanPart rightLeg4 = this.rightLeg;
            final EntityTitanPart leftLeg4 = this.leftLeg;
            final EntityTitanPart leftArm5 = this.leftArm;
            final EntityTitanPart rightArm5 = this.rightArm;
            final float n7 = 12.0f;
            rightArm5.height = n7;
            leftArm5.height = n7;
            leftLeg4.height = n7;
            rightLeg4.height = n7;
        }
    }

    public int getSkeletonType() {
        return this.entityData.get(t13) ? 1 : 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putInt("SkeletonType", this.getSkeletonType());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        this.setSkeletonType(tagCompund.getInt("SkeletonType"));
    }

    protected void dropRareDrop(final int p_70600_1_) {
        CompoundNBT type = new CompoundNBT();
        if (this.getSkeletonType() == 1) {
            type.putString("id", "wither_skeleton");
        } else {
            type.putString("id", "skeleton");
        }
        this.spawnAtLocation(new ItemStack(Items.SKELETON_SKULL, 256, type), 0.0f);
    }

    @Override
    protected void inactDeathAction() {
        if (!this.level.isClientSide) {
            this.playSound(TitanSounds.titanSkeletonDeath, this.getSoundVolume(), this.getSoundPitch());
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.dropFewItems(true, 0);
                this.dropEquipment();
                this.dropRareDrop(1);
            }
            final EntityTitanSpirit entitytitan = new EntityTitanSpirit(this.level);
            entitytitan.moveTo(this.getX(), this.getY(), this.getZ(), this.yRotO, 0.0f);
            this.level.addFreshEntity(entitytitan);
            entitytitan.setVesselHunting(false);
            if (this.getSkeletonType() == 1) {
                entitytitan.setSpiritType(5);
            } else {
                entitytitan.setSpiritType(4);
            }
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
            final ArrayList listp = Lists.newArrayList(this.level.players());
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
        if (this.deathTicks >= 500) {
            this.setInvulTime(this.getInvulTime() + 8);
            --this.animTick;
            final float f = (this.random.nextFloat() - 0.5f) * 24.0f;
            final float f2 = (this.random.nextFloat() - 0.5f) * 8.0f;
            final float f3 = (this.random.nextFloat() - 0.5f) * 24.0f;
            this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + f, this.getY() + 2.0 + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
        }
        if (this.getInvulTime() >= this.getThreashHold()) {
            this.removeAfterChangingDimensions();
        }
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
        ILivingEntityData p_180482_2_2 = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setCanPickUpLoot(true);
        this.setWaiting(true);
        if ((this.level.dimensionType().effectsLocation() == DimensionType.END_EFFECTS && this.getRandom().nextInt(5) > 0 && !this.level.isClientSide) || (this.shouldBeWitherSkeleton && !this.level.isClientSide)) {
            this.setSkeletonType(1);
        } else {
            this.setSkeletonType(0);
        }
        if (this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 4 && i == 1 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }
        this.addRandomArmor();
        return p_180482_2_2;
    }

    protected void addRandomArmor() {
        if (this.getSkeletonType() == 1) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        }
    }

    @Override
    public void attackChoosenEntity(final Entity damagedEntity, float damage, int knockbackAmount) {
        if (this.getSkeletonType() == 1 && this.isAlive()) {
            damage += this.numSpecialMinions * 150.0f;
            knockbackAmount += this.numSpecialMinions;
        }
        if (this.isArmored()) {
            damage *= 2.0f;
        }
        super.attackChoosenEntity(damagedEntity, damage, knockbackAmount);
        if (this.getSkeletonType() == 1 && damagedEntity instanceof LivingEntity) {
            ((LivingEntity) damagedEntity).addEffect(new EffectInstance(Effects.WITHER, 800, 3));
            //((LivingEntity) damagedEntity).addEffect(new EffectInstance(ClientProxy.advancedWither.id, 100, 3));
        }
    }

    @Override
    public float getEyeHeightForge(Pose pose, EntitySize size) {
        return (this.getSkeletonType() == 1) ? 46.68f : 28.0f;
    }

    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        if (source.getEntity() instanceof PlayerEntity && !this.level.isClientSide) {
            this.setTarget((LivingEntity) source.getEntity());
            this.setLastHurtByMob((LivingEntity) source.getEntity());
        }
        if (this.isInvulnerable()) {
            return false;
        }
        if (!this.isStunned && source.getEntity() instanceof PlayerEntity) {
            return false;
        }
        if (source.getEntity() instanceof EntitySkeletonMinion || (this.getVehicle() != null && source.getEntity() == this.getVehicle() && this.getVehicle() instanceof EntitySpiderTitan) || source.getEntity() instanceof EntitySkeletonTitan || source.getEntity() instanceof EntityWitherMinion) {
            return false;
        }
        if (this.getSkeletonType() == 1 && source.isFire()) {
            this.heal(amount);
            return false;
        }
        this.lastHurtByPlayerTime = 200;
        final Entity entity = source.getEntity();
        if (entity instanceof LivingEntity && !this.isInvulnerable() && amount > 25.0f) {
            final List list = this.level.getEntities(this, this.getBoundingBox().inflate(256.0, 256.0, 256.0));
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = (Entity) list.get(i);
                if (entity2 instanceof EntitySkeletonTitan) {
                    final EntitySkeletonTitan entitypigzombie = (EntitySkeletonTitan) entity2;
                    entitypigzombie.setTarget((LivingEntity) entity);
                    entitypigzombie.setLastHurtByMob((LivingEntity) entity);
                }
                this.setTarget((LivingEntity) entity);
                this.setLastHurtByMob((LivingEntity) entity);
            }
        }
        return super.hurt(source, amount);
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFromPart(EntityTitanPart p0, DamageSource p1, float p2) {
        this.hurt(p1, p2);
        return true;
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
    public int getAnimID() {
        return this.animID;
    }

    @Override
    public void setAnimID(int var1) {
        this.animID = var1;
    }

    @Override
    public int getAnimTick() {
        return this.animTick;
    }

    @Override
    public void setAnimTick(int var1) {
        this.animTick = var1;
    }
}
