package net.minecraft.entity.titan;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.entity.titan.animation.ultimairongolemtitan.AnimationIronGolemTitanAntiTitanAttack;
import net.minecraft.entity.titan.animation.ultimairongolemtitan.AnimationIronGolemTitanAttack1;
import net.minecraft.entity.titan.animation.ultimairongolemtitan.AnimationIronGolemTitanAttack2;
import net.minecraft.entity.titan.animation.ultimairongolemtitan.AnimationIronGolemTitanAttack3;
import net.minecraft.entity.titan.animation.ultimairongolemtitan.AnimationIronGolemTitanAttack4;
import net.minecraft.entity.titan.animation.ultimairongolemtitan.AnimationIronGolemTitanDeath;
import net.minecraft.entity.titan.animation.ultimairongolemtitan.AnimationIronGolemTitanRangedAttack;
import net.minecraft.entity.titanminion.EntityIronGolemFixed;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.ITag;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class EntityIronGolemTitan extends EntityTitan implements IAnimatedEntity {
    public static final DataParameter<Byte> t16 = EntityDataManager.defineId(EntityIronGolemTitan.class, DataSerializers.BYTE);
    private static final Predicate<Entity> attackEntitySelector;

    static {
        attackEntitySelector = new Predicate<Entity>() {
            public boolean apply(final Entity p_180027_1_) {
                return p_180027_1_.isAlive() && p_180027_1_ instanceof EntityTitan && !(p_180027_1_ instanceof EntityGargoyleTitan) && !(p_180027_1_ instanceof EntityIronGolemTitan) && !(p_180027_1_ instanceof EntitySnowGolemTitan);
            }
        };
    }
    private int homeCheckTimer;
    private int attackTimer;
    private int holdRoseTick;

    public EntityIronGolemTitan(EntityType<? extends EntityTitan> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
        this.setSize(24.0f, 64.0f);
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, (e) -> e instanceof IMob));
        addTitanTargetingTaskToEntity(this);
        this.goalSelector.addGoal(1, new AnimationIronGolemTitanDeath(this));
        this.goalSelector.addGoal(1, new AnimationIronGolemTitanRangedAttack(this));
        this.goalSelector.addGoal(1, new AnimationIronGolemTitanAntiTitanAttack(this));
        this.goalSelector.addGoal(1, new AnimationIronGolemTitanAttack4(this));
        this.goalSelector.addGoal(1, new AnimationIronGolemTitanAttack3(this));
        this.goalSelector.addGoal(1, new AnimationIronGolemTitanAttack2(this));
        this.goalSelector.addGoal(1, new AnimationIronGolemTitanAttack1(this));
    }

    public static void addTitanTargetingTaskToEntity(final CreatureEntity entity) {
        entity.targetSelector.addGoal(0, new EntityAINearestTargetTitan(entity, EntityTitan.class, 0, false, false, EntityIronGolemTitan.attackEntitySelector));
    }

    @Override
    public int getMinionCap() {
        return 30;
    }

    public float getStandingEyeHeight(Pose p, EntitySize s) {
        return 56.0f;
    }

    @Override
    public void kill() {
        this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
        this.removeAfterChangingDimensions();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(t16, (byte) 0);
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return EntityTitan.applyEntityAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    @Override
    public EnumTitanStatus getTitanStatus() {
        return EnumTitanStatus.GREATER;
    }

    @Override
    protected int decreaseAirSupply(final int p_70682_1_) {
        return p_70682_1_;
    }

    @Override
    public int getMinionSpawnRate() {
        return TitanConfig.UltimaIronGolemMinionSpawnrate;
    }

    @Override
    public int getArmorValue() {
        return 24;
    }

    @Override
    public boolean shouldMove() {
        return this.animID == 0 && this.getTarget() != null && super.shouldMove();
    }

    @Override
    public double getMeleeRange() {
        return this.getBbWidth() * this.getBbWidth() + ((this.getTarget().getBbWidth() > 48.0f) ? 2304.0f : (this.getTarget().getBbWidth() * this.getTarget().getBbWidth())) + 2000.0;
    }

    @Override
    public boolean canBeHurtByPlayer() {
        return !this.isPlayerCreated() && !this.isInvulnerable();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setSize(24.0f, 64.0f);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2000.0);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500000.0);
        if (this.animID == 10) {
            if (this.animTick == 30 || this.animTick == 70) {
                this.playStepSound(new BlockPos(0, 0, 0), Blocks.STONE.defaultBlockState());
            }
            if (this.animTick == 190) {
                this.playSound(TitanSounds.titanFall, 20.0f, 0.9f);
                this.playSound(TitanSounds.groundSmash, 20.0f, 1.0f);
                this.shakeNearbyPlayerCameras(40000.0);
            }
            if (this.animTick == 200) {
                this.playSound(TitanSounds.distantLargeFall, 10000.0f, 0.5f);
            }
        }
        if (!this.level.isClientSide && this.getAnimID() == 5 && this.getAnimTick() == 34 && this.getTarget() != null) {
            this.doHurtTarget(this.getTarget());
            final Vector3d vec3 = this.getLookAngle();
            final double d5 = this.getTarget().getX() - (this.getX() + vec3.x * 30.0);
            final double d6 = this.getTarget().getY() - (this.getY() + 30.0);
            final double d7 = this.getTarget().getZ() - (this.getZ() + vec3.z * 30.0);
            final EntityTitanFireball entitylargefireball = new EntityTitanFireball(this.level, this, d5, d6, d7, 1);
            entitylargefireball.setPos(this.getX() + vec3.x * 30.0, this.getY() + 30.0, this.getZ() + vec3.z * 30.0);
            this.level.addFreshEntity(entitylargefireball);
            entitylargefireball.setFireballID(5);
            entitylargefireball.playSound(TitanSounds.titanSwing, 10.0f, 1.0f);
        }
        if (this.deathTicks > 0) {
            this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
        }
        if (!AnimationAPI.isEffectiveClient() && this.getTarget() != null && this.getAnimID() == 0 && this.tickCount > 5) {
            final double d8 = this.distanceToSqr(this.getTarget());
            if (d8 < this.getMeleeRange()) {
                if (this.getTarget() instanceof EntityTitan || this.getTarget().getBbHeight() >= 6.0f || this.getTarget().getY() > this.getY() + 6.0) {
                    AnimationAPI.sendAnimPacket(this, 1);
                    this.setAnimID(1);
                } else {
                    switch (this.random.nextInt(4)) {
                        case 0: {
                            AnimationAPI.sendAnimPacket(this, 6);
                            this.setAnimID(6);
                            break;
                        }
                        case 1: {
                            AnimationAPI.sendAnimPacket(this, 7);
                            this.setAnimID(7);
                            break;
                        }
                        case 2: {
                            AnimationAPI.sendAnimPacket(this, 8);
                            this.setAnimID(8);
                            break;
                        }
                        case 3: {
                            AnimationAPI.sendAnimPacket(this, 9);
                            this.setAnimID(9);
                            break;
                        }
                    }
                }
            } else if (this.getAnimID() == 0 && this.getRandom().nextInt(160) == 0) {
                switch (this.random.nextInt(2)) {
                    case 0: {
                        AnimationAPI.sendAnimPacket(this, 5);
                        this.setAnimID(5);
                        break;
                    }
                    case 1: {
                        AnimationAPI.sendAnimPacket(this, 5);
                        this.setAnimID(5);
                        break;
                    }
                }
            }
        }
        if (this.getDeltaMovement().y > 1.0) {
            this.setDeltaMovement(this.getDeltaMovement().x, 1.0, this.getDeltaMovement().z);
        }
        this.meleeTitan = true;
        this.setCustomName(new StringTextComponent("\u00A77\u00A7lUltima Iron Golem Titan"));
        final List list1 = this.level.getEntities(this, this.getBoundingBox().inflate(96.0, 96.0, 96.0));
        if (list1 != null && !list1.isEmpty()) {
            for (int i1 = 0; i1 < list1.size(); ++i1) {
                final Entity entity = (Entity) list1.get(i1);
                if (entity != null && entity instanceof IronGolemEntity) {
                    if (entity.horizontalCollision) {
                        entity.setDeltaMovement(this.getDeltaMovement().x, 0.25, this.getDeltaMovement().z);
                    }
                    if (((IronGolemEntity) entity).getTarget() == null && entity.distanceToSqr(this) > 4096.0) {
                        ((IronGolemEntity) entity).getLookControl().setLookAt(this, 180.0f, 40.0f);
                        ((IronGolemEntity) entity).getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 1.0);
                    }
                    if (entity.tickCount == 20) {
                        addTitanTargetingTaskToEntity(this);
                    }
                }
            }
        }
        if (this.random.nextInt(this.getMinionSpawnRate()) == 0 && this.getHealth() > 0.0f && !this.level.isClientSide) {
            final EntityIronGolemFixed entitychicken = new EntityIronGolemFixed(EntityType.IRON_GOLEM, this.level);
            entitychicken.moveTo(this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), this.getY() + this.getEyeHeight(), this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), this.yRot, 0.0f);
            entitychicken.setPlayerCreated(this.isPlayerCreated());
            this.level.addFreshEntity(entitychicken);
            entitychicken.getAttribute(Attributes.MAX_HEALTH).setBaseValue(2000.0);
            entitychicken.setHealth(2000.0f);
            entitychicken.setCustomName(new StringTextComponent("Reinforced Iron Golem"));
            addTitanTargetingTaskToEntity(entitychicken);
            entitychicken.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32.0);
            entitychicken.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        }
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }
        if (this.holdRoseTick > 0) {
            --this.holdRoseTick;
        }
    }

    @Override
    protected void customServerAiStep() {
        final int homeCheckTimer = this.homeCheckTimer - 1;
        this.homeCheckTimer = homeCheckTimer;
//        if (homeCheckTimer <= 0) {
//            this.homeCheckTimer = 70 + this.random.nextInt(50);
//            this.villageObj = this.level.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
//            if (this.villageObj == null) {
//                this.detachHome();
//            } else {
//                final ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
//                this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int) (this.villageObj.getVillageRadius() * 0.6f));
//            }
//        }
        final List list11 = this.level.getEntities(this, this.getBoundingBox());
        if (list11 != null && !list11.isEmpty()) {
            for (int i1 = 0; i1 < list11.size(); ++i1) {
                final Entity entity = (Entity) list11.get(i1);
                if ((entity instanceof LivingEntity || (entity instanceof PlayerEntity && !this.isPlayerCreated())) && entity.isOnGround() && !(entity instanceof EntityTitan) && !(entity instanceof IronGolemEntity)) {
                    final float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
                    entity.hurt(DamageSourceExtra.causeSquishingDamage(this), f / 2.0f);
                }
            }
        }
        super.customServerAiStep();
    }

    @Override
    public boolean canAttack(Entity p_213336_1_) {
        Class p_70686_1_ = p_213336_1_.getClass();
        return p_70686_1_ != VillagerEntity.class && p_70686_1_ != IronGolemEntity.class && p_70686_1_ != EntityIronGolemTitan.class && (!this.isPlayerCreated() || !PlayerEntity.class.isAssignableFrom(p_70686_1_));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putBoolean("PlayerCreated", this.isPlayerCreated());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        this.setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
    }

    @Override
    public boolean doHurtTarget(Entity p_70652_1_) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
        if (p_70652_1_ instanceof EntityWitherzilla) {
            f *= 5.0f;
        }
        if (p_70652_1_ instanceof EntityGhastTitan && p_70652_1_.getY() > this.getY() + 32.0) {
            p_70652_1_.push(0.0, -1.0, 0.0);
        }
        for (int l = 0; l < 7 + this.random.nextInt(14); ++l) {
            this.attackChoosenEntity(p_70652_1_, f, this.getKnockbackAmount());
        }
        return super.doHurtTarget(p_70652_1_);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !this.isInvulnerable() && (!(source.getEntity() instanceof PlayerEntity) || !this.isPlayerCreated()) && !(source.getEntity() instanceof IronGolemEntity) && !(source.getEntity() instanceof EntityIronGolemTitan) && super.hurt(source, amount);
    }

    @Override
    public void handleEntityEvent(final byte p_70103_1_) {
        if (p_70103_1_ == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 100.0f, 0.5f);
        } else if (p_70103_1_ == 11) {
            this.holdRoseTick = 800;
        } else {
            super.handleEntityEvent(p_70103_1_);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    public void setHoldingRose(final boolean p_70851_1_) {
        this.holdRoseTick = (p_70851_1_ ? 800 : 0);
        this.level.broadcastEntityEvent(this, (byte) 11);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    protected float getSoundPitch() {
        return this.isBaby() ? ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) : ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.5f);
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 10.0f, 0.5f);
        this.playSound(TitanSounds.titanStep, 10.0f, 1.0f);
        this.shakeNearbyPlayerCameras(6000.0);
    }

    @Override
    protected float getSoundVolume() {
        return 100.0f;
    }

    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int l = 0; l < 512 + this.random.nextInt(512 + p_70628_2_); ++l) {
            this.spawnAtLocation(new ItemStack(Blocks.IRON_BLOCK), 12.0f);
        }
        for (int l = 0; l < 128 + this.random.nextInt(128 + p_70628_2_); ++l) {
            this.spawnAtLocation(new ItemStack(Blocks.CORNFLOWER), 12.0f);
        }
        for (int l = 0; l < 32 + this.random.nextInt(96 + p_70628_2_); ++l) {
            this.spawnAtLocation(new ItemStack(Items.EMERALD), 12.0f);
        }
        for (int l = 0; l < 32 + this.random.nextInt(96 + p_70628_2_); ++l) {
            this.spawnAtLocation(new ItemStack(Items.DIAMOND), 12.0f);
        }
        for (int l = 0; l < this.random.nextInt(16 + p_70628_2_); ++l) {
            this.spawnAtLocation(new ItemStack(TitanItems.harcadium), 12.0f);
        }
        for (int l = 0; l < this.random.nextInt(8); ++l) {
            this.spawnAtLocation(new ItemStack(Blocks.BEDROCK), 12.0f);
        }
    }

    public int getHoldRoseTick() {
        return this.holdRoseTick;
    }

    public boolean isPlayerCreated() {
        return (this.getEntityData().get(t16) & 0x1) != 0x0;
    }

    public void setPlayerCreated(final boolean p_70849_1_) {
        final byte b0 = this.getEntityData().get(t16);
        if (p_70849_1_) {
            this.getEntityData().set(t16, (byte) (b0 | 0x1));
        } else {
            this.getEntityData().set(t16, (byte) (b0 & 0xFFFFFFFE));
        }
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

    @Override
    protected void onTitanDeathUpdate() {
        this.getEntityData().set(t6, MathHelper.clamp(this.getEntityData().get(t5), 0.0f, this.getMaxHealth()));
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
        this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
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
        }
        if (this.getInvulTime() >= this.getThreashHold()) {
            this.removeAfterChangingDimensions();
        }
    }

    //Fuck u stupid collision push
    @Override
    public boolean updateFluidHeightAndDoFluidPushing(ITag<Fluid> p_210500_1_, double p_210500_2_) {
        return false;
    }
}
