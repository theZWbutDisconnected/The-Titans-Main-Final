package net.minecraft.entity.titanminion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.entity.titan.ITitan;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TargetingSorter;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

public class EntityGiantZombieBetter extends GiantEntity implements IMinion {
    public MobEntity master;
    private TargetingSorter TargetSorter;

    public EntityGiantZombieBetter(EntityType<? extends GiantEntity> p_i50205_1_, World p_i50205_2_) {
        super(p_i50205_1_, p_i50205_2_);
    }

    public EntityGiantZombieBetter(World p_i50205_2_) {
        this(RenderTheTitans.giantZombie, p_i50205_2_);
        this.TargetSorter = null;
        this.setSize(3.0f, 12.0f);
        this.maxUpStep = 3.0f;
        this.setPersistenceRequired();
        this.TargetSorter = new TargetingSorter(this);
        fireImmune();
        this.xpReward = 1000;
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        if (TitanConfig.TitansFFAMode) {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.ZombieTitanSorter));
        } else {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SearchForAThingToKill));
        }
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return GiantEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 50.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.MAX_HEALTH, 4000.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }    private EntitySize entitySize = this.setSize(3.0f, 12.0f);

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return entitySize;
    }

    public EntitySize setSize(float width, float height) {
        entitySize = EntitySize.fixed(width, height);
        this.refreshDimensions();
        return entitySize;
    }

    public int getArmorValue() {
        return 20;
    }

    @Override
    protected void removeAfterChangingDimensions() {
        super.removeAfterChangingDimensions();
        if (this.master != null && this.master instanceof EntityTitan) {
            ((EntityTitan) this.master).retractMinionNumFromType(this.getMinionType());
        }
    }

    public EnumMinionType getMinionType() {
        return EnumMinionType.SPECIAL;
    }

    public boolean canAttack(final Entity target) {
        if (target instanceof LivingEntity) {
            Class p_70686_1_ = target.getClass();
            return (p_70686_1_ != EntityZombieMinion.class && p_70686_1_ != EntityGiantZombieBetter.class && p_70686_1_ != EntityZombieTitan.class) || p_70686_1_.getName().contains("MutantZombie");
        }
        return false;
    }

    @Override
    public float getEyeHeightForge(Pose pose, EntitySize size) {
        return 10.440001f;
    }

    private LivingEntity doJumpDamage(final double X, final double Y, final double Z, final double dist, final double damage, final int knock) {
        final AxisAlignedBB bb = new AxisAlignedBB(X - dist, Y - 10.0, Z - dist, X + dist, Y + 10.0, Z + dist);
        final List<Entity> var5 = this.level.getEntitiesOfClass(LivingEntity.class, bb, ITitan.ZombieTitanSorter);
        //var5.sort(this.TargetSorter);
        final Iterator<Entity> var6 = var5.iterator();
        Entity var7 = null;
        LivingEntity var8 = null;
        while (var6.hasNext()) {
            var7 = var6.next();
            var8 = (LivingEntity) var7;
            if (var8 != null && var8 != this && var8.isAlive() && !(var8 instanceof EntityZombieTitan) && !(var8 instanceof EntityGiantZombieBetter) && !(var8 instanceof EntityZombieMinion)) {
                DamageSource var9 = null;
                var9 = DamageSource.explosion((Explosion) null);
                var9.setExplosion();
                if (this.random.nextInt(2) == 0) {
                    var9.bypassArmor();
                }
                var8.hurt(var9, (float) damage);
                var8.hurt(DamageSource.FALL, (float) damage / 4.0f);
                var8.playSound(SoundEvents.GENERIC_EXPLODE, 0.85f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.5f);
                if (knock == 0) {
                    continue;
                }
                final double ks = 0.75 + this.random.nextDouble() + this.random.nextDouble();
                final double inair = 0.75;
                final float f3 = (float) Math.atan2(var8.getZ() - this.getZ(), var8.getX() - this.getX());
                var8.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                if (this.random.nextInt(5) != 0) {
                    continue;
                }
                var8.hurtDuration = 0;
            }
        }
        return null;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.tickCount % 20 == 0 && (!this.level.isDay() || this.random.nextInt(5) == 0)) {
            this.heal(this.level.isDay() ? (1.0f + this.random.nextFloat() * 4.0f) : (5.0f + this.random.nextFloat() * 15.0f));
        }
        if (this.level.isClientSide) {
            this.setSize(3.0f, 12.0f);
        } else {
            this.setSize(1.5f, 6.0f);
        }
        this.noCulling = true;
        if (this.getDeltaMovement().x != 0.0 && this.getDeltaMovement().z != 0.0 && this.random.nextInt(5) == 0) {
            final int i = MathHelper.floor(this.getX());
            final int j = MathHelper.floor(this.getY() - 0.20000000298023224);
            final int k = MathHelper.floor(this.getZ());
            final Block block = this.level.getBlockState(new BlockPos(i, j, k)).getBlock();
            if (block.defaultBlockState().getMaterial() != Material.AIR) {
                this.level.addParticle(ParticleTypes.ASH, this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), this.getBoundingBox().minY + 0.1, this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), 4.0 * (this.random.nextFloat() - 0.5), 0.5, (this.random.nextFloat() - 0.5) * 4.0);
            }
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity p_70624_1_) {
        Field f;
        try {
            f = MobEntity.class.getDeclaredField("target");
        } catch (Exception e) {
            TheTitans.error("No Mapping!");
            try {
                f = MobEntity.class.getDeclaredField("field_70696_bz");
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        }
        f.setAccessible(true);
        try {
            f.set(this, p_70624_1_);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public float getWalkTargetValue(final BlockPos p_70783_1_) {
        return 0.5f - this.level.getBrightness(p_70783_1_);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource s) {
        return SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 2.0f, 0.9f);
        final double dx = this.getX() + 4.0 * -Math.sin(Math.toRadians(this.yBodyRot));
        final double dz = this.getZ() - 4.0 * -Math.cos(Math.toRadians(this.yBodyRot));
        this.doJumpDamage(dx, this.getY(), dz, 6.0, 10 + this.random.nextInt(90), 1);
    }

    protected float getSoundVolume() {
        return 7.0f;
    }

    protected float getSoundPitch() {
        return this.isBaby() ? ((this.random.nextFloat() - this.random.nextFloat()) * 0.1f + 1.0f) : ((this.random.nextFloat() - this.random.nextFloat()) * 0.1f + 0.5f);
    }

    @Override
    public void killed(ServerWorld p_241847_1_, LivingEntity entityLivingIn) {
        super.killed(p_241847_1_, entityLivingIn);
        if (entityLivingIn.getMaxHealth() <= 100.0) {
            entityLivingIn.push(0.0, 15.0, 0.0);
        }
        this.heal(this.level.isDay() ? (5.0f + this.random.nextFloat() * 15.0f) : (15.0f + this.random.nextFloat() * 30.0f));
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

    @Override
    public boolean doHurtTarget(Entity par1Entity) {
        if (super.doHurtTarget(par1Entity)) {
            if (par1Entity != null && par1Entity instanceof LivingEntity) {
                if (par1Entity.isOnGround()) {
                    final double ks = 1.25;
                    final double inair = 1.25;
                    final float f3 = (float) Math.atan2(par1Entity.getZ() - this.getZ(), par1Entity.getX() - this.getX());
                    par1Entity.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                } else {
                    final double ks = 3.0;
                    final double inair = 0.25;
                    final int var2 = 6;
                    final float f4 = (float) Math.atan2(par1Entity.getZ() - this.getZ(), par1Entity.getX() - this.getX());
                    par1Entity.push(Math.cos(f4) * ks, inair, Math.sin(f4) * ks);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
        super.push(p_70024_1_ * 0.1, p_70024_3_ * 0.1, p_70024_5_ * 0.1);
    }

    @Override
    protected void customServerAiStep() {
        if (this.getTarget() != null && this.onGround) {
            this.getMoveControl().setWantedPosition(this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 1.0);
        }
        if (this.horizontalCollision && this.master != null) {
            this.setDeltaMovement(getDeltaMovement().x, 0.2, getDeltaMovement().z);
        }
        if (this.getDeltaMovement().y < -0.95) {
            double df = 1.0;
            if (this.getDeltaMovement().y < -1.5) {
                df = 1.5;
            }
            this.doJumpDamage(this.getX(), this.getY(), this.getZ(), 12.0, 100.0 * df, 0);
            this.doJumpDamage(this.getX(), this.getY(), this.getZ(), 14.0, 50.0 * df, 0);
            this.doJumpDamage(this.getX(), this.getY(), this.getZ(), 16.0, 25.0 * df, 0);
        }
        if (!this.level.isClientSide && this.getTarget() != null && this.tickCount % 30 == 0 && this.level.random.nextInt(3) == 0) {
            final float rotationYawHead = this.yHeadRot;
            this.yRot = rotationYawHead;
            this.yBodyRot = rotationYawHead;
            this.push(0.0, 1.25, 0.0);
            this.setPos(getX(), getY() + 1.5499999523162842, getZ());
            double d1 = this.getTarget().getX() - this.getX();
            final double d2 = this.getTarget().getZ() - this.getZ();
            final float d3 = (float) Math.atan2(d2, d1);
            this.lookAt(this.getTarget(), 10.0f, (float) this.getMaxHeadXRot());
            d1 = Math.sqrt(d1 * d1 + d2 * d2);
            if (this.distanceToSqr(this.getTarget()) > (10.0f + this.getTarget().getBbWidth() / 2.0f) * (10.0f + this.getTarget().getBbWidth() / 2.0f) + 45.0) {
                this.push(d1 * 0.05 * Math.cos(d3), 0.0, d1 * 0.05 * Math.sin(d3));
            }
        }
        if (this.getTarget() != null) {
            this.getLookControl().setLookAt(this.getTarget(), 10.0f, 40.0f);
        }
        if (this.getTarget() != null && this.tickCount % 20 == 0 && this.distanceToSqr(this.getTarget()) <= (14.0f + this.getTarget().getBbWidth() / 2.0f) * (14.0f + this.getTarget().getBbWidth() / 2.0f)) {
            this.doHurtTarget(this.getTarget());
        }
        if (this.master != null) {
            if (this.master.getTarget() != null) {
                this.setTarget(this.master.getTarget());
            } else if (this.distanceToSqr(this.master) > 5000.0) {
                this.getMoveControl().setWantedPosition(this.master.getX(), this.master.getY(), this.master.getZ(), 2.0);
            }
        } else {
            final List list = this.level.getEntities(this, this.getBoundingBox().inflate(100.0, 100.0, 100.0));
            if (list != null && !list.isEmpty()) {
                for (int i1 = 0; i1 < list.size(); ++i1) {
                    final Entity entity = (Entity) list.get(i1);
                    if (entity != null && entity instanceof EntityZombieTitan) {
                        this.master = (MobEntity) entity;
                    }
                }
            }
        }
        super.customServerAiStep();
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
    }

    @Override
    public boolean hurt(DamageSource source, float p_70097_2_) {
        if (this.isInvulnerable()) {
            return false;
        }
        if (source.getEntity() instanceof EntityZombieMinion || source.getEntity() instanceof EntityZombieTitan || source.getEntity() instanceof EntityGiantZombieBetter) {
            return false;
        }
        final Entity entity = source.getEntity();
        if (source.getEntity() instanceof LivingEntity) {
            this.setTarget((LivingEntity) entity);
            this.setLastHurtByMob((LivingEntity) entity);
            if (!this.level.isClientSide && this.level.random.nextInt(4) == 0) {
                final float rotationYawHead = this.yHeadRot;
                this.yRot = rotationYawHead;
                this.yBodyRot = rotationYawHead;
                this.push(0.0, 1.75, 0.0);
                this.setPos(getX(), getY() + 1.5499999523162842, getZ());
                double d1 = entity.getX() - this.getX();
                final double d2 = entity.getZ() - this.getZ();
                final float d3 = (float) Math.atan2(d2, d1);
                this.lookAt(entity, 10.0f, (float) this.getMaxHeadXRot());
                d1 = Math.sqrt(d1 * d1 + d2 * d2);
                if (this.distanceToSqr(entity) > (10.0f + entity.getBbWidth() / 2.0f) * (10.0f + entity.getBbWidth() / 2.0f) + 45.0) {
                    this.push(d1 * 0.1 * Math.cos(d3), 0.0, d1 * 0.1 * Math.sin(d3));
                }
            }
        }
        return super.hurt(source, p_70097_2_);
    }




}
