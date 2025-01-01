package net.minecraft.entity.titan;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.entity.titan.ai.EntityAITitanLookIdle;
import net.minecraft.entity.titan.ai.EntityAITitanWatchClosest;
import net.minecraft.entity.titanminion.EntityDragonMinion;
import net.minecraft.entity.titanminion.EntityGiantZombieBetter;
import net.minecraft.entity.titanminion.EnumMinionType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.theTitans.items.ItemTitanArmor;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class EntityTitan
        extends CreatureEntity
        implements ITitan {
    public static final DataParameter<Float> t5 = EntityDataManager.defineId(EntityTitan.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> t6 = EntityDataManager.defineId(EntityTitan.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> t20 = EntityDataManager.defineId(EntityTitan.class, DataSerializers.INT);
    private static final DataParameter<Integer> t21 = EntityDataManager.defineId(EntityTitan.class, DataSerializers.INT);
    private static final DataParameter<Integer> t22 = EntityDataManager.defineId(EntityTitan.class, DataSerializers.INT);
    private static final DataParameter<Byte> t23 = EntityDataManager.defineId(EntityTitan.class, DataSerializers.BYTE);
    private static final Predicate<Entity> attackEntitySelector = new Predicate<Entity>() {

        @Override
        public boolean apply(Entity p_180027_1_) {
            return p_180027_1_.isAlive() && p_180027_1_ instanceof EntityTitan;
        }
    };
    public boolean shouldParticlesBeUpward;
    public int deathTicks;
    public boolean isRejuvinating;
    public int animID;
    public int animTick;
    public boolean meleeTitan;
    public int antiTitanAttackAnimeID;
    public int footID;
    public EntitySize entitySize;
    protected int nextStepDistanceTitan;
    protected int numMinions;
    protected int numPriests;
    protected int numZealots;
    protected int numBishop;
    protected int numTemplar;
    protected int numSpecialMinions;

    public EntityTitan(EntityType<? extends EntityTitan> type, World worldIn) {
        super(type, worldIn);
        this.setPersistenceRequired();
        this.applyEntityAI();
        this.noCulling = true;
        this.goalSelector.addGoal(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0f, 0.5f));
        this.goalSelector.addGoal(9, new EntityAITitanWatchClosest(this, PlayerEntity.class, 128.0f));
        this.goalSelector.addGoal(10, new EntityAITitanLookIdle(this));
    }

    public static boolean isOreSpawnBossToExempt(final Entity entity) {
        return false;
    }

    public static boolean whiteListNoDamage(final Entity entity) {
        return entity instanceof PlayerEntity || entity instanceof EntityTitan || entity instanceof EntityGiantZombieBetter || entity instanceof EntityDragonMinion || ((!(entity instanceof LivingEntity) || entity.getBbHeight() <= 6.0f) && (entity instanceof LivingEntity || entity instanceof FireballEntity || entity instanceof ArrowEntity || entity instanceof ThrowableEntity));
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, Double.MAX_VALUE).add(Attributes.ATTACK_DAMAGE).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.FOLLOW_RANGE, 512.0);
    }

    public static void addTitanTargetingTaskToEntity(CreatureEntity entity) {
        entity.goalSelector.addGoal(0, new EntityAINearestTargetTitan(entity, EntityTitan.class, 0, false, false, attackEntitySelector));
    }

    public boolean alreadyHasAName() {
        return false;
    }

    public void setSecondsOnFire(int p_70015_1_) {
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        if (this.noPhysics) {
            this.setBoundingBox(this.getBoundingBox().move(pos));
            this.setLocationFromBoundingbox();
        } else {
            Vector3d vector3d;
            this.level.getProfiler().push("move");
            if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7) {
                pos = pos.multiply(this.stuckSpeedMultiplier);
                this.stuckSpeedMultiplier = Vector3d.ZERO;
                this.setDeltaMovement(Vector3d.ZERO);
            }
            if ((vector3d = this.collide(pos = this.maybeBackOffFromEdge(pos, typeIn))).lengthSqr() > 1.0E-7) {
                this.setBoundingBox(this.getBoundingBox().move(vector3d));
                this.setLocationFromBoundingbox();
            }
            this.level.getProfiler().pop();
            this.level.getProfiler().push("rest");
            this.horizontalCollision = !MathHelper.equal(pos.x, vector3d.x) || !MathHelper.equal(pos.z, vector3d.z);
            this.verticalCollision = pos.y != vector3d.y;
            this.onGround = this.verticalCollision && pos.y < 0.0;
            BlockPos blockpos = this.getOnPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            this.checkFallDamage(vector3d.y, this.onGround, blockstate, blockpos);
            Vector3d vector3d1 = this.getDeltaMovement();
            if (pos.x != vector3d.x) {
                this.setDeltaMovement(0.0, vector3d1.y, vector3d1.z);
            }
            if (pos.z != vector3d.z) {
                this.setDeltaMovement(vector3d1.x, vector3d1.y, 0.0);
            }
            Block block = blockstate.getBlock();
            if (pos.y != vector3d.y) {
                block.updateEntityAfterFallOn(this.level, this);
            }
            if (this.onGround && !this.isSteppingCarefully()) {
                block.stepOn(this.level, blockpos, this);
            }
            if (this.isMovementNoisy() && !this.isPassenger()) {
                double d0 = vector3d.x;
                double d1 = vector3d.y;
                double d2 = vector3d.z;
                if (!block.is(BlockTags.CLIMBABLE)) {
                    d1 = 0.0;
                }
                this.walkDist = (float) ((double) this.walkDist + (double) MathHelper.sqrt(EntityTitan.getHorizontalDistanceSqr(vector3d)) * 0.6);
                this.moveDist = (float) ((double) this.moveDist + (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 0.6);
                if (this.moveDist > (float) this.nextStepDistanceTitan && !blockstate.isAir(this.level, blockpos)) {
                    this.nextStepDistanceTitan = (int) (this.moveDist + (float) this.getFootStepModifer());
                    if (this.isInWater()) {
                        EntityTitan entity = this.isVehicle() && this.getControllingPassenger() != null ? (EntityTitan) this.getControllingPassenger() : this;
                        float f = entity == this ? 0.35f : 0.4f;
                        Vector3d vector3d2 = entity.getDeltaMovement();
                        float f1 = MathHelper.sqrt(vector3d2.x * vector3d2.x * (double) 0.2f + vector3d2.y * vector3d2.y + vector3d2.z * vector3d2.z * (double) 0.2f) * f;
                        if (f1 > 1.0f) {
                            f1 = 1.0f;
                        }
                        this.playSwimSound(f1);
                    } else {
                        this.playStepSound(blockpos, blockstate);
                    }
                }
            }
            try {
                this.checkInsideBlocks();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being checked for collision");
                this.fillCrashReportCategory(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            this.level.getProfiler().pop();
        }
    }

    public int getFootStepModifer() {
        return 2;
    }

    public double distanceToSqr(Entity p_70068_1_) {
        double d0 = this.getX() - p_70068_1_.getX();
        double d1 = this.getY() + (double) (this.getBbHeight() * 0.5f) - p_70068_1_.getY();
        double d2 = this.getZ() - p_70068_1_.getZ();
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    protected void pushEntities() {
        List list = this.level.getEntities(this, this.getBoundingBox().inflate(0.1, 0.1, 0.1));
        if (list != null && !list.isEmpty() && !this.getWaiting() && this.animID != 13) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);
                if (!this.isAlive() || !entity.isAlive() || !entity.isPushable() && !(entity instanceof EntityTitan) && (!(entity instanceof ThrowableEntity) || ((ThrowableEntity) entity).getOwner() == this) && (!(entity instanceof FireballEntity) || ((FireballEntity) entity).getOwner() == this) || !(entity.getBbHeight() > 6.0f) || entity instanceof PlayerEntity)
                    continue;
                this.doPush(entity);
            }
        }
    }

    public void push(Entity p_70108_1_) {
        double d1;
        double d0;
        double d2;
        if (this.canAttack(p_70108_1_) && !this.getWaiting() && this.animID != 13 && !(p_70108_1_ instanceof FireballEntity) && !(p_70108_1_ instanceof ThrowableEntity) && !(p_70108_1_ instanceof EntityTitanPart) && this.isAlive() && (d2 = MathHelper.absMax(d0 = p_70108_1_.getX() - this.getX(), d1 = p_70108_1_.getZ() - this.getZ())) >= (double) 0.01f) {
            d2 = MathHelper.sqrt(d2);
            d0 /= d2;
            d1 /= d2;
            double d3 = 1.0 / d2;
            if (d3 > 1.0) {
                d3 = 1.0;
            }
            d0 *= d3;
            d1 *= d3;
            d0 *= 0.25;
            d1 *= 0.25;
            d0 *= 1.0f - this.pushthrough;
            d1 *= 1.0f - this.pushthrough;
            if (p_70108_1_.getBbHeight() >= 6.0f || p_70108_1_ instanceof EntityTitan) {
                this.addTitanVelocity(-d0, 0.0, -d1);
            }
            p_70108_1_.push(d0 *= 4.0, 0.75, d1 *= 4.0);
        }
    }

    public void setTitanHealth(float p_70606_1_) {
        if (!this.level.isClientSide) {
            this.entityData.set(t6, Float.valueOf(MathHelper.clamp(p_70606_1_, 0.0f, this.getMaxHealth())));
            this.entityData.set(t5, Float.valueOf(MathHelper.clamp(p_70606_1_, 0.0f, this.getMaxHealth())));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadowSize() {
        return 0.0f;
    }

    public boolean isAlive() {
        return !this.dead && this.entityData.get(t5).floatValue() > 0.0f;
    }

    public void setHealth(float p_70606_1_) {
        this.entityData.set(t6, Float.valueOf(MathHelper.clamp(this.entityData.get(t5).floatValue(), 0.0f, this.getMaxHealth())));
    }

    public Vector3d getViewVector(float p_70676_1_) {
        super.getViewVector(p_70676_1_);
        if (p_70676_1_ == 1.0f) {
            float f7 = MathHelper.cos(-this.yHeadRot * ((float) Math.PI / 180) - (float) Math.PI);
            float f8 = MathHelper.sin(-this.yHeadRot * ((float) Math.PI / 180) - (float) Math.PI);
            float f9 = -MathHelper.cos(-this.xRot * ((float) Math.PI / 180));
            float f10 = MathHelper.sin(-this.xRot * ((float) Math.PI / 180));
            return new Vector3d(f8 * f9, f10, f7 * f9);
        }
        float f1 = this.xRotO + (this.xRot - this.xRotO) * p_70676_1_;
        float f2 = this.yHeadRotO + (this.yHeadRot - this.yHeadRotO) * p_70676_1_;
        float f3 = MathHelper.cos(-f2 * ((float) Math.PI / 180) - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * ((float) Math.PI / 180) - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * ((float) Math.PI / 180));
        float f6 = MathHelper.sin(-f1 * ((float) Math.PI / 180));
        return new Vector3d(f4 * f5, f6, f3 * f5);
    }

    public void heal(float p_70691_1_) {
        float f1 = this.getHealth();
        if (f1 > 0.0f && this.entityData.get(t5).floatValue() > 0.0f) {
            this.setTitanHealth(f1 + p_70691_1_);
        }
    }

    protected void actuallyHurt(DamageSource p_70665_1_, float p_70665_2_) {
        if (p_70665_2_ > 800.0f) {
            p_70665_2_ = 800.0f;
        }
        if (!this.isInvulnerableTo(p_70665_1_)) {
            if ((p_70665_2_ = ForgeHooks.onLivingHurt(this, p_70665_1_, p_70665_2_)) <= 0.0f) {
                return;
            }
            p_70665_2_ = this.getDamageAfterArmorAbsorb(p_70665_1_, p_70665_2_);
            float f1 = p_70665_2_ = this.getDamageAfterMagicAbsorb(p_70665_1_, p_70665_2_);
            p_70665_2_ = Math.max(p_70665_2_ - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - f1 - p_70665_2_);
            if (p_70665_2_ != 0.0f) {
                float f2 = this.getHealth();
                this.setTitanHealth(f2 - p_70665_2_);
                this.getCombatTracker().recordDamage(p_70665_1_, f2, p_70665_2_);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - p_70665_2_);
            }
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 2) {
            this.animationSpeed = 1.5f;
            this.getClass();
            this.invulnerableTime = 20;
            this.hurtDuration = 10;
            this.hurtTime = 10;
            this.hurtDir = 0.0f;
            this.playSound(this.getHurtSound(this.getLastDamageSource()), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.hurt(DamageSource.GENERIC, 0.0f);
        }
    }

    public void removeAfterChangingDimensions() {
        if (this.deathTicks > 0 || this instanceof EntitySlimeTitan || this.entityData.get(t5).floatValue() <= 0.0f) {
            this.inactDeathAction();
            ArrayList listp = Lists.newArrayList(this.level.players());
            if (listp != null && !listp.isEmpty() && !this.level.isClientSide && !(this instanceof EntitySlimeTitan) && !(this instanceof EntityGargoyleTitan) && !(this instanceof EntityIronGolemTitan) && !(this instanceof EntitySnowGolemTitan)) {
                for (int i1 = 0; i1 < listp.size(); ++i1) {
                    Entity entity = (Entity) listp.get(i1);
                    if (entity != null && entity instanceof PlayerEntity) {
                        this.playAmbientSound();
                        ((PlayerEntity) entity).sendMessage(new StringTextComponent(this.getName().getString() + ": I will return, " + entity.getName().getString()), this.getUUID());
                    }
                }
            }
            super.removeAfterChangingDimensions();
        }
    }

    protected void inactDeathAction() {
        this.level.explode(this, this.getX(), this.getY() + 3.0, this.getZ(), 0.0f, false, Explosion.Mode.NONE);
        if (this.level.isClientSide || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            this.dropFewItems(true, 0);
            this.dropEquipment();
            //this.dropRareDrop(1);
        }
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        Item item = this.getDropItem();

        if (item != null)
        {
            int j = this.random.nextInt(3);

            if (p_70628_2_ > 0)
            {
                j += this.random.nextInt(p_70628_2_ + 1);
            }

            for (int k = 0; k < j; ++k)
            {
                this.dropItem(item, 1);
            }
        }
    }

    private void dropItem(Item item, int i) {
        this.spawnAtLocation(new ItemStack(item), i);
    }

    private Item getDropItem() {
        return null;
    }

    public void func_82206_m() {
        this.setInvulTime(this.getTitanStatus() == EnumTitanStatus.GOD ? 7100 : (this.getTitanStatus() == EnumTitanStatus.GREATER ? 1310 : 850));
        this.setWaiting(false);
    }

    protected void applyEntityAI() {
    }

    public void setTarget(LivingEntity p_70624_1_) {
        if (!this.getWaiting() && this.animID != 13 && p_70624_1_ != null && p_70624_1_.getY() < 256.0 && p_70624_1_.isAlive() && this.canAttack(p_70624_1_) && p_70624_1_ != this.getVehicle() && p_70624_1_ != this.getRootVehicle()) {
            super.setTarget(p_70624_1_);
            if (!this.level.isClientSide && p_70624_1_ instanceof PlayerEntity && p_70624_1_.tickCount > 100 && !((PlayerEntity) p_70624_1_).inventory.contains(new ItemStack(TitanItems.ultimaBlade)) && p_70624_1_.isAlive() && p_70624_1_.hurtDuration <= 10) {
                if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
                    this.level.getServer().setDifficulty(Difficulty.EASY, false);
                }
                if (!p_70624_1_.hurt(new DamageSource("other").bypassArmor().bypassMagic().bypassInvul(), 1.0f)) {
                    ((PlayerEntity) p_70624_1_).inventory.dropAll();
                    p_70624_1_.setHealth(0.0f);
                    ((ServerPlayerEntity)p_70624_1_).connection.disconnect(new StringTextComponent(this.getName().getString() + " has kicked you for cheating."));
                }
            }
        } else if (p_70624_1_ instanceof EntityTitan && (this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void") || ((EntityTitan) p_70624_1_).getInvulTime() > 0 || ((EntityTitan) p_70624_1_).getWaiting() || ((EntityTitan) p_70624_1_).animID == 13) && !(p_70624_1_ instanceof EntityWitherzilla)) {
            super.setTarget(null);
        } else {
            super.setTarget(null);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(t5, Float.valueOf(Float.MAX_VALUE));
        this.entityData.define(t6, Float.valueOf(Float.MAX_VALUE));
        this.entityData.define(t20, Integer.valueOf(0));
        this.entityData.define(t21, Integer.valueOf(0));
        this.entityData.define(t22, Integer.valueOf(0));
        this.entityData.define(t23, Byte.valueOf((byte) 0));
    }

    protected void jumpAtEntity(LivingEntity e) {
        float f2;
        this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() + 1.25, this.getDeltaMovement().z());
        this.setPos(this.getX(), this.getY() + (double) 1.55f, this.getZ());
        double d1 = e.getX() - this.getX();
        double d2 = e.getZ() - this.getZ();
        float d = (float) Math.atan2(d2, d1);
        this.yRot = f2 = (float) ((double) d * 180.0 / Math.PI) - 90.0f;
        d1 = Math.sqrt(d1 * d1 + d2 * d2);
        this.setDeltaMovement(this.getDeltaMovement().x() + d1 * 0.05 * Math.cos(d), this.getDeltaMovement().y(), this.getDeltaMovement().z() + d1 * 0.05 * Math.sin(d));
        this.hasImpulse = true;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        this.setInvulTime(tagCompund.getInt("Invul"));
        this.setExtraPower(tagCompund.getInt("ExtraPower"));
        this.setWaiting(tagCompund.getBoolean("Waiting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putInt("Invul", this.getInvulTime());
        tagCompound.putInt("ExtraPower", this.getExtraPower());
        tagCompound.putBoolean("Waiting", this.getWaiting());
    }

    public int getMinionCap() {
        return 120;
    }

    public int getPriestCap() {
        return 60;
    }

    public int getZealotCap() {
        return 30;
    }

    public int getBishopCap() {
        return 15;
    }

    public int getTemplarCap() {
        return 6;
    }

    public int getSpecialMinionCap() {
        return 6;
    }

    public int getExtraPower() {
        return this.entityData.get(t21);
    }

    public void setExtraPower(int p_82215_1_) {
        this.entityData.set(t21, p_82215_1_);
    }

    public int getRandomName() {
        return this.entityData.get(t21);
    }

    public void setRandomName(int p_82215_1_) {
        this.entityData.set(t21, p_82215_1_);
    }

    public boolean getWaiting() {
        return this.entityData.get(t23) == 1;
    }

    public void setWaiting(boolean p_82215_1_) {
        this.entityData.set(t23, (byte) (p_82215_1_ ? 1 : 0));
    }

    public int getInvulTime() {
        return this.entityData.get(t20);
    }

    public void setInvulTime(int p_82215_1_) {
        if (!this.level.isClientSide) {
            if (p_82215_1_ < 0) {
                this.entityData.set(t20, 0);
            } else {
                this.entityData.set(t20, p_82215_1_);
                this.xRotO = this.xRot = 0.0f + (float) (p_82215_1_ / this.getThreashHold()) / 180.0f;
            }
        }
    }

    public boolean destroyBlocksInAABBGriefingBypass(final AxisAlignedBB p_70972_1_) {
        final int i = MathHelper.floor(p_70972_1_.minX);
        final int j = MathHelper.floor(p_70972_1_.minY);
        final int k = MathHelper.floor(p_70972_1_.minZ);
        final int l = MathHelper.floor(p_70972_1_.maxX);
        final int i2 = MathHelper.floor(p_70972_1_.maxY);
        final int j2 = MathHelper.floor(p_70972_1_.maxZ);
        final boolean flag = false;
        boolean flag2 = false;
        for (int k2 = i; k2 <= l; ++k2) {
            for (int l2 = j; l2 <= i2; ++l2) {
                for (int i3 = k; i3 <= j2; ++i3) {
                    final BlockState block = this.level.getBlockState(new BlockPos(k2, l2, i3));
                    if (!block.isAir(this.level, new BlockPos(k2, l2, i3)) && !this.level.isClientSide && block.getBlock().getExplosionResistance() != 3600000.0F) {
                        flag2 = (this.level.removeBlock(new BlockPos(k2, l2, i3), false) || flag2);
                    }
                }
            }
        }
        return flag;
    }

    public void destroyBlocksInAABB(final AxisAlignedBB p_70972_1_) {
        final int i = MathHelper.floor(p_70972_1_.minX);
        final int j = MathHelper.floor(p_70972_1_.minY);
        final int k = MathHelper.floor(p_70972_1_.minZ);
        final int l = MathHelper.floor(p_70972_1_.maxX);
        final int i2 = MathHelper.floor(p_70972_1_.maxY);
        final int j2 = MathHelper.floor(p_70972_1_.maxZ);
        for (int k2 = i; k2 <= l; ++k2) {
            for (int l2 = j; l2 <= i2; ++l2) {
                for (int i3 = k; i3 <= j2; ++i3) {
                    final BlockState block = this.level.getBlockState(new BlockPos(k2, l2, i3));
                    if (this.tickCount > 5 && p_70972_1_ != null && this.level.hasChunksAt(new BlockPos(k2, l2, i3), new BlockPos(k2, l2, i3)) && !block.isAir(this.level, new BlockPos(k2, l2, i3)) && !this.level.isClientSide && block.getBlock().getExplosionResistance() != 3600000.0F) {
                        if (block.getMaterial().isLiquid() || block.getBlock() == Blocks.FIRE || block.getBlock() == Blocks.COBWEB) {
                            this.level.removeBlock(new BlockPos(k2, l2, i3), false);
                        } else if (this.random.nextInt(3) == 0) {
                            final EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(this.level, k2 + 0.5, l2 + 0.5, i3 + 0.5, block);
                            entityfallingblock.setPos(k2 + 0.5, l2 + 0.5, i3 + 0.5);
                            final double d0 = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0;
                            final double d2 = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0;
                            final double d3 = entityfallingblock.getX() - d0;
                            final double d4 = entityfallingblock.getZ() - d2;
                            final double d5 = d3 * d3 + d4 * d4;
                            entityfallingblock.setRemainingFireTicks(10);
                            entityfallingblock.push(d3 / d5 * 10.0, 2.0 + this.random.nextGaussian(), d4 / d5 * 10.0);
                            this.level.addFreshEntity(entityfallingblock);
                            this.level.removeBlock(new BlockPos(k2, l2, i3), false);
                        } else if (this.level.getNearestPlayer(this, 16.0) != null) {
                            this.level.removeBlock(new BlockPos(k2, l2, i3), true);
                        } else {
                            this.level.removeBlock(new BlockPos(k2, l2, i3), false);
                            dropBlockAsItem(block.getBlock(), this.level, k2, l2, i3);
                        }
                    }
                }
            }
        }
    }

    private void dropBlockAsItem(Block block, World level, int p1474801, int p1474802, int p1474803) {
        ItemEntity it = new ItemEntity(level, p1474801, p1474802, p1474803, new ItemStack(block.asItem()));
        level.addFreshEntity(it);
        it.push(0, .14, 0);
    }

    public void destroyBlocksInAABBTopless(final AxisAlignedBB p_70972_1_) {
        final int i = MathHelper.floor(p_70972_1_.minX);
        final int j = MathHelper.floor(p_70972_1_.minY);
        final int k = MathHelper.floor(p_70972_1_.minZ);
        final int l = MathHelper.floor(p_70972_1_.maxX);
        final int i2 = MathHelper.floor(p_70972_1_.maxY);
        final int j2 = MathHelper.floor(p_70972_1_.maxZ);
        for (int k2 = i; k2 <= l; ++k2) {
            for (int l2 = j; l2 <= i2; ++l2) {
                for (int i3 = k; i3 <= j2; ++i3) {
                    final BlockState block = this.level.getBlockState(new BlockPos(k2, l2, i3));
                    final BlockState block2 = this.level.getBlockState(new BlockPos(k2, l2 + 1, i3));
                    if (this.tickCount > 5 && p_70972_1_ != null && this.level.hasChunksAt(new BlockPos(k2, l2, i3), new BlockPos(k2, l2, i3)) && block.isSolidRender(level, new BlockPos(k2, l2, i3)) && !block2.isSolidRender(level, new BlockPos(k2, l2 + 1, i3)) && !this.level.isClientSide && block.getBlock().getExplosionResistance() != 3600000.0F) {
                        if (block.getMaterial().isLiquid() || block.getBlock() == Blocks.FIRE || block.getBlock() == Blocks.COBWEB) {
                            this.level.removeBlock(new BlockPos(k2, l2, i3), false);
                        } else if (this.random.nextInt(3) == 0) {
                            final EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(this.level, k2 + 0.5, l2 + 0.5, i3 + 0.5, block);
                            entityfallingblock.setPos(k2 + 0.5, l2 + 0.5, i3 + 0.5);
                            final double d0 = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0;
                            final double d2 = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0;
                            final double d3 = entityfallingblock.getX() - d0;
                            final double d4 = entityfallingblock.getZ() - d2;
                            final double d5 = d3 * d3 + d4 * d4;
                            entityfallingblock.setRemainingFireTicks(10);
                            entityfallingblock.push(d3 / d5 * 10.0, 2.0 + this.random.nextGaussian(), d4 / d5 * 10.0);
                            this.level.addFreshEntity(entityfallingblock);
                            this.level.removeBlock(new BlockPos(k2, l2, i3), false);
                        } else if (this.level.getNearestPlayer(this, 16.0) != null) {
                            this.level.removeBlock(new BlockPos(k2, l2, i3), true);
                        } else {
                            this.level.removeBlock(new BlockPos(k2, l2, i3), false);
                            dropBlockAsItem(block.getBlock(), this.level, k2, l2, i3);
                        }
                    }
                }
            }
        }
    }

    public boolean causeFallDamage(float p_70069_1_, float damageMultiplier) {
        this.onGround = true;
        this.hasImpulse = false;
        float[] ret = ForgeHooks.onLivingFall(this, p_70069_1_, damageMultiplier);
        p_70069_1_ = ret[0];
        damageMultiplier = ret[1];
        if (p_70069_1_ <= 0.0f) {
            return false;
        }
        EffectInstance potioneffect = this.getEffect(Effects.JUMP);
        float f1 = potioneffect != null ? (float) (potioneffect.getAmplifier() + 1) : 0.0f;
        int i = MathHelper.ceil(p_70069_1_ - 24.0f - f1);
        if (i > 0) {
            this.shakeNearbyPlayerCameras(400000.0);
            this.playSound(TitanSounds.groundSmash, 20.0f, 1.0f);
            this.playSound(TitanSounds.titanLand, 10000.0f, 1.0f);
            this.destroyBlocksInAABBTopless(this.getBoundingBox().inflate((this.getTitanStatus() == EnumTitanStatus.LESSER) ? 6.0 : 12.0, 1.0, (this.getTitanStatus() == EnumTitanStatus.LESSER) ? 6.0 : 12.0));
            List list11 = this.level.getEntities(this, this.getBoundingBox().inflate(48.0, 4.0, 48.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i1 = 0; i1 < list11.size(); ++i1) {
                    Entity entity = (Entity) list11.get(i1);
                    if (entity instanceof LivingEntity && this.canAttack(entity) && !(entity instanceof EntityTitan)) {
                        float smash = 50.0f - this.distanceTo(entity);
                        if (smash <= 1.0f) {
                            smash = 1.0f;
                        }
                        entity.hurt(DamageSource.mobAttack(this), smash);
                        double d0 = this.getBoundingBox().minX + this.getBoundingBox().maxX;
                        double d1 = this.getBoundingBox().minZ + this.getBoundingBox().maxZ;
                        double d2 = entity.getX() - d0;
                        double d3 = entity.getZ() - d1;
                        double d4 = d2 * d2 + d3 * d3;
                        entity.push(d2 / d4 * 8.0, 1.0, d3 / d4 * 8.0);
                    }
                }
            }
        }
        return super.causeFallDamage(p_70069_1_, damageMultiplier);
    }

    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public int getArmorValue() {
        switch (this.getTitanStatus()) {
            case GOD: {
                return 24;
            }
            case GREATER: {
                return 23;
            }
            case AVERAGE: {
                return 22;
            }
        }
        return 21;
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    public int getTitanExperienceDropAmount() {
        return 1;
    }

    public float getRenderSizeModifier() {
        return (this.getTitanStatus() == EnumTitanStatus.GREATER) ? 20.0f : 16.0f;
    }

    public boolean addEffect(EffectInstance p_70690_1_) {
        return false;
    }

    protected float getSoundVolume() {
        return 100.0f;
    }

    public void checkDespawn() {
    }

    public int getAmbientSoundInterval() {
        return 120;
    }

    public int getMaxFallDistance() {
        return this.level.getMaxBuildHeight();
    }

    public int getMinionSpawnRate() {
        return 0;
    }

    protected float getWaterSlowDown() {
        return 0;
    }

    protected void lavaHurt() {
    }

    protected int decreaseAirSupply(int p_70682_1_) {
        return p_70682_1_;
    }

    public float getSpeed() {
        return (float) (0.3 + (double) this.getExtraPower() * 0.001);
    }

    public void baseTick() {
        super.baseTick();
        if (!this.getWaiting() && this.animID != 0 && this.deathTicks < this.getThreashHold()) {
            ++this.animTick;
            if (this.isBaby() && this.isAlive()) {
                ++this.animTick;
            }
        }
    }

    public double getMeleeRange() {
        return (double) (this.getBbWidth() * this.getBbWidth() + (this.getTarget().getBbWidth() > 48.0f ? 2304.0f : this.getTarget().getBbWidth() * this.getTarget().getBbWidth())) + 800.0;
    }

    @Override
    protected void doPush(Entity p_82167_1_) {
    }

    public void addTitanVelocity(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
        if (!this.getWaiting() && this.animID != 13) {
            this.setDeltaMovement(this.getDeltaMovement().x() + p_70024_1_, this.getDeltaMovement().y() + p_70024_3_, this.getDeltaMovement().x() + p_70024_5_);
        }
    }

    public boolean canAttack(Entity p_213336_1_) {
        return !(p_213336_1_ instanceof LivingEntity) || super.canAttack((LivingEntity) p_213336_1_);
    }

    @Override
    public boolean canAttack(LivingEntity p_213336_1_) {
        return this.canAttack((Entity) p_213336_1_);
    }

    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
    }

    public void pushSelf(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
        super.push(p_70024_1_, p_70024_3_, p_70024_5_);
    }

    public void aiStep() {
        double d0;
        if (this.getTarget() != null && this.getTarget() instanceof MobEntity && isOreSpawnBossToExempt(this.getTarget())) {
            ((MobEntity) this.getTarget()).setTarget(this);
            ((MobEntity) this.getTarget()).getLookControl().setLookAt(this, 180.0f, 180.0f);
        }
        if (this.animID == 13 && this.animTick == 4 && this.level.getNearestPlayer(this, 32.0) != null) {
            this.getLookControl().setLookAt(this.level.getNearestPlayer(this, 32.0), 180.0f, 0.0f);
        }
        boolean bl = this.hasImpulse = !this.onGround;
        if (this.getDeltaMovement().x() > 1.5) {
            this.setDeltaMovement(1.5, this.getDeltaMovement().y(), this.getDeltaMovement().z());
        }
        if (this.getDeltaMovement().z() > 1.5) {
            this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y(), 1.5);
        }
        if (this.getDeltaMovement().x() < -1.5) {
            this.setDeltaMovement(-1.5, this.getDeltaMovement().y(), this.getDeltaMovement().z());
        }
        if (this.getDeltaMovement().z() < -1.5) {
            this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y(), -1.5);
        }
        if (this.level.getDayTime() > 24000L && !this.level.isClientSide) {
            ((ServerWorld)this.level).setDayTime(0L);
        }
        if (!(this.getWaiting() || this.level.getDayTime() >= 14000L || this.isRejuvinating || this.animID != 13 || this.getWaiting() || this.level.getDayTime() <= 24000L || this.getTarget() == null || this.getTarget().isAlive() && !(this.getTarget().getY() > 256.0) && !(this.getTarget().getY() < -45.0))) {
            this.setTarget(null);
        }
        if (this.getTarget() != null && this.getTarget() instanceof LivingEntity && !(this.getTarget() instanceof EntityTitan) && this.getTarget().getMaxHealth() > 1.0E9f) {
            this.getTarget().playSound(SoundEvents.GENERIC_EXPLODE, 2.0f, 1.0f + this.random.nextFloat());
            this.damageBypassEntity(this.getTarget(), new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), this.getTarget().getHealth() / 2.0f);
            if (this.getTarget().getHealth() <= 1.0f) {
                this.level.explode(this, this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 7.0f, Explosion.Mode.NONE);
                this.getTarget().remove();
            }
        }
        if (this != null && !this.level.isClientSide && this.getTarget() != null && this.getTarget() instanceof PlayerEntity && ((PlayerEntity) this.getTarget()).abilities.invulnerable && !((PlayerEntity) this.getTarget()).abilities.instabuild && this.getTarget() instanceof ServerPlayerEntity mp) {
            mp.connection.disconnect(new StringTextComponent(this.createCommandSourceStack().getTextName() + " has detected you not taking normal damage out of creative, and kicked you for it."));
        }
        if (this.getVehicle() != null) {
            this.yBodyRot = this.yRot = this.getVehicle().yRot;
        }
        if (this.animID == 0) {
            this.animTick = 0;
        } else {
            this.yBodyRot = this.yRot = this.yHeadRot;
        }
        if (this.getWaiting()) {
            this.setDeltaMovement(0.0, this.getDeltaMovement().y(), 0.0);
            if (this.getDeltaMovement().y() > 0.0) {
                this.setDeltaMovement(this.getDeltaMovement().x(), 0.0, this.getDeltaMovement().z());
            }
        }
        if (this.animID == 0 && !this.level.isClientSide && this.getTarget() != null && this.shouldMove()) {
            double d02 = this.getTarget().getX() - this.getX();
            double d1 = this.getTarget().getZ() - this.getZ();
            float f2 = MathHelper.sqrt(d02 * d02 + d1 * d1);
            this.yBodyRot = this.yRot = this.yHeadRot;
            this.setDeltaMovement(d02 / (double) f2 * this.getSpeed() * this.getSpeed() + this.getDeltaMovement().x(), this.getDeltaMovement().y(), d1 / (double) f2 * this.getSpeed() * this.getSpeed() + this.getDeltaMovement().z());
        }
        if (this.deathTime == 1) {
            this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getVoicePitch());
        }
        this.entityData.set(t6, Float.valueOf(MathHelper.clamp(this.entityData.get(t5).floatValue(), 0.0f, this.getMaxHealth())));
        if (TitanConfig.TitansFFAMode && this.getVehicle() != null) {
            this.stopRiding();
        }
        if (this.animID < 0) {
            this.animID = 0;
        }
        if (this.entityData.get(t5).floatValue() <= 0.0f) {
            if (this instanceof EntityWitherzilla && this.getExtraPower() > 5) {
                this.entityData.set(t5, MathHelper.clamp(this.getMaxHealth(), 0.0f, this.getMaxHealth()));
            }
            this.onTitanDeathUpdate();
        } else {
            this.entityData.set(t6, Float.valueOf(MathHelper.clamp(this.entityData.get(t5).floatValue(), 0.0f, this.getMaxHealth())));
            this.deathTicks = 0;
            if (this.animID == 10) {
                this.animID = 0;
            }
        }
        if (this.getY() > 300.0) {
            this.setDeltaMovement(this.getDeltaMovement().x(), 0.0, this.getDeltaMovement().z());
            this.setPos(this.getX() + (double) (this.random.nextFloat() * 32.0f - 16.0f), 60.0, this.getZ() + (double) (this.random.nextFloat() * 32.0f - 16.0f));
        }
        if (this.getY() <= 0.0) {
            this.setPos(this.getX(), 0.0, this.getZ());
            this.onGround = true;
            this.hasImpulse = false;
            this.fallDistance = 0.0f;
            if (this.getDeltaMovement().y() < 0.0) {
                this.setDeltaMovement(this.getDeltaMovement().x(), 0.0, this.getDeltaMovement().z());
            }
        }
        if (this.numMinions < 0) {
            this.numMinions = 0;
        }
        if (this.numPriests < 0) {
            this.numPriests = 0;
        }
        if (this.numZealots < 0) {
            this.numZealots = 0;
        }
        if (this.numTemplar < 0) {
            this.numTemplar = 0;
        }
        if (this.numSpecialMinions < 0) {
            this.numSpecialMinions = 0;
        }
        if (this.getHealth() <= 0.0f && this.getDeltaMovement().y() > 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().x(), 0.0, this.getDeltaMovement().z());
        }
        if (this.animTick < 0) {
            this.animTick = 0;
        }
        this.lastHurt = 2.1474836E9f;
        this.maxUpStep = this.getBbHeight() / 2.0f;
        this.updateSwingTime();
        this.noCulling = true;
        if (this.getTarget() != null && this.animID == 0) {
            this.getLookControl().setLookAt(this.getTarget(), 5.0f, this.getMaxHeadXRot());
        }
        for (int u = 0; u < 30; ++u) {
            final int i = MathHelper.floor(this.getX() + (this.random.nextDouble() * this.getBbWidth() - this.getBbWidth() / 2.0f));
            final int j = MathHelper.floor(this.getY() - 0.20000000298023224);
            final int k = MathHelper.floor(this.getZ() + (this.random.nextDouble() * this.getBbWidth() - this.getBbWidth() / 2.0f));
            final BlockState block = this.level.getBlockState(new BlockPos(i, j, k));
            if (block.getMaterial() != Material.AIR) {
                //this.level.addParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.level.getBlockMetadata(i, j, k), this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), this.getBoundingBox().minY + 0.1, this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth(), 4.0 * (this.random.nextFloat() - 0.5), 0.5, (this.random.nextFloat() - 0.5) * 4.0);
            }
        }
        super.setHealth(this.entityData.get(t5).floatValue());
        super.aiStep();
        if (this.getTarget() != null && this.animID == 0 && this.tickCount % 30 == 0 && this.canAttack()) {
            d0 = this.distanceToSqr(this.getTarget());
            if (d0 < this.getMeleeRange()) {
                this.swing(Hand.MAIN_HAND);
                this.getLookControl().setLookAt(this.getTarget(), 5.0f, (float) this.getMaxHeadXRot());
                final float rotationYawHead3 = this.yHeadRot;
                this.yRot = rotationYawHead3;
                this.yBodyRot = rotationYawHead3;
                this.doHurtTarget(this.getTarget());
            }
        }
        if (this.getTarget() != null && this.getTarget() == this.getVehicle()) {
            this.setTarget(null);
        }
        if (this.level.isClientSide && this.deathTicks < this.getThreashHold() && !(this instanceof EntityWitherzilla)) {
            for (int l = 0; l < this.getParticleCount(); ++l) {
                if (this.shouldParticlesBeUpward) {
                    this.level.addParticle(this.getParticles(), this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.25, 0.0);
                } else {
                    this.level.addParticle(this.getParticles(), this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                }
            }
            if (TitanConfig.TotalDestructionMode && this.random.nextInt(5) == 0) {
                for (int l = 0; l < 5; ++l) {
                    this.level.addParticle(ParticleTypes.EXPLOSION, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                }
            }
            if (TitanConfig.NightmareMode && this.random.nextInt(20) == 0) {
                for (int l = 0; l < 5; ++l) {
                    this.level.addParticle(ParticleTypes.EXPLOSION, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.LAVA, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.SMOKE, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    public boolean isPersistenceRequired() {
        return true;
    }

    public boolean canAttack() {
        return this.meleeTitan;
    }

    public boolean shouldMove() {
        return !(this instanceof EntityWitherzilla) && this.getTarget() != null && this.distanceToSqr(this.getTarget()) > this.getMeleeRange() + (this.meleeTitan ? 0.0 : 10000.0);
    }

    public int getFootID() {
        return this.footID;
    }

    protected void customServerAiStep() {
        List list11 = this.level.getEntities(this, this.getBoundingBox());
        if (list11 != null && !list11.isEmpty()) {
            for (int i1 = 0; i1 < list11.size(); ++i1) {
                Entity entity = (Entity) list11.get(i1);
                this.push(entity);
            }
        }
        if (this.random.nextInt(1000) == 0 && this.getHealth() < this.getMaxHealth() / 20.0f && this.deathTicks <= 0 || this.getHealth() < this.getMaxHealth() / 2.0f && this.deathTicks <= 0 && this.getTarget() != null && this.getTarget() instanceof EntityTitan && !this.isRejuvinating && ((EntityTitan) this.getTarget()).isRejuvinating) {
            this.isRejuvinating = true;
            this.level.globalLevelEvent(1013, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
            this.setExtraPower(this.getExtraPower() + 1);
            this.jumpFromGround();
        }
        if (this.isRejuvinating) {
            this.setInvulTime(this.getInvulTime() + 5);
            this.animateHurt();
            if (this.getInvulTime() > this.getThreashHold()) {
                this.isRejuvinating = false;
            }
        }
        if (this.getInvulTime() > 0) {
            this.setDeltaMovement(this.getDeltaMovement().x() * 0.0, this.getDeltaMovement().y(), this.getDeltaMovement().z() * 0.0);
            final int j = this.getInvulTime() - 1;
            if (j <= 0) {
                if (!(this instanceof EntitySnowGolemTitan) && !(this instanceof EntityIronGolemTitan) && !(this instanceof EntityGargoyleTitan)) {
                    this.level.explode(this, this.getX(), this.getY() + this.getBbHeight() / 2.0f, this.getZ(), this.getBbHeight(), false, this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                }
                this.level.globalLevelEvent(1013, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
                this.destroyBlocksInAABB(this.getBoundingBox());
                if (this instanceof EntityZombieTitan && !((EntityZombieTitan) this).isArmed()) {
                    AnimationAPI.sendAnimPacket((IAnimatedEntity) this, 2);
                }
            }
            this.setInvulTime(j);
            if (0 == 0) {
                this.heal(this.getMaxHealth() / 1000.0f);
            }
        } else {
            super.customServerAiStep();
            if (!(this instanceof EntitySlimeTitan)) {
                float at;
                if (this.getTitanStatus() == EnumTitanStatus.AVERAGE) {
                    at = 3.0f;
                } else if (this.getTitanStatus() == EnumTitanStatus.GREATER) {
                    at = 6.0f;
                } else if (this.getTitanStatus() == EnumTitanStatus.GOD) {
                    at = 20.0f;
                } else {
                    at = 1.0f;
                }
                if ((this instanceof EntityZombieTitan || this instanceof EntitySkeletonTitan) && this.level.isDay()) {
                    at /= 3.0f;
                }
                if (this instanceof EntitySnowGolemTitan || this instanceof EntityIronGolemTitan || this instanceof EntityGargoyleTitan) {
                    at = 0.1f;
                }
                this.heal(at);
                for (int u = 0; u < 1 + this.random.nextInt(10); ++u) {
                    this.heal(at);
                }
            }
            if (this.getTarget() != null && (this.getTarget() instanceof AmbientEntity || this.getTarget() instanceof AnimalEntity || this.getTarget() instanceof WaterMobEntity)) {
                this.getTarget().remove();
            }
        }
    }

    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
        this.setTitanHealth(this.getMaxHealth());
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void die(DamageSource p_70645_1_) {
        final Entity entity = p_70645_1_.getEntity();
        final ArrayList listp = Lists.newArrayList(this.level.players());
        if (listp != null && !listp.isEmpty() && !this.level.isClientSide && entity != null && entity instanceof LivingEntity) {
            for (int i1 = 0; i1 < listp.size(); ++i1) {
                final PlayerEntity entityplayer = (PlayerEntity) listp.get(i1);
                if (this.getEntityData().get(t5) > 0.0f) {
                    entityplayer.sendMessage(new StringTextComponent(Objects.requireNonNull(this.getCustomName()).getString() + " has refused to let " + entity.getName().getString() + " cheat..."), this.getUUID());
                } else {
                    entityplayer.sendMessage(new StringTextComponent(Objects.requireNonNull(this.getCustomName()).getString() + " has been defeated by " + entity.getName().getString()), this.getUUID());
                }
            }
        }
        if (this.entityData.get(t5).floatValue() <= 0.0f) {
            LivingEntity LivingEntity2 = this.getKillCredit();
            if (this.deathScore >= 0 && LivingEntity2 != null && entity != null) {
                entity.killed((ServerWorld) this.level, this);
            }
            this.dead = true;
            this.getCombatTracker().recheckStatus();
            if (!this.level.isClientSide) {
                int i = 0;
                if (entity instanceof PlayerEntity) {
                    i = EnchantmentHelper.getMobLooting((LivingEntity) entity);
                }
                this.captureDrops().clear();
                int j = 0;
                int k = 0;
                if (!this.shouldDropExperience() || !this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) || this.lastHurtByPlayerTime <= 0 || (j = this.random.nextInt(200) - i) < 5) {
                    this.dropFewItems(this.lastHurtByPlayerTime > 0, j);
                    this.dropEquipment();
                    if (this.lastHurtByPlayerTime > 0) {
                        k = this.random.nextInt(200) - j;
                        if (k < 5) {
                            this.dropRareDrop((k <= 0) ? 1 : 0);
                        }
                    }
                }
                if (!ForgeHooks.onLivingDrops(this, p_70645_1_, this.captureDrops(), i, this.lastHurtByPlayerTime > 0)) {
                    for (ItemEntity item : this.captureDrops()) {
                        this.level.addFreshEntity(item);
                    }
                }
            }
            this.level.broadcastEntityEvent(this, (byte) 3);
        }
    }

    protected void dropRareDrop(int i) {
    }

    protected void jumpFromGround() {
        super.jumpFromGround();
        this.playSound(TitanSounds.titanSwing, 5.0f, 2.0f);
        double motionY = this.getDeltaMovement().y();
        motionY += 1.0;
        this.setDeltaMovement(this.getDeltaMovement().x(), motionY += 1.0, this.getDeltaMovement().z());
    }

    public int getParticleCount() {
        return 6;
    }

    public BasicParticleType getParticles() {
        switch (this.getTitanStatus()) {
            case GOD: {
                return ParticleTypes.FIREWORK;
            }
            case GREATER: {
                return ParticleTypes.ENCHANTED_HIT;
            }
            case AVERAGE: {
                return ParticleTypes.CRIT;
            }
        }
        return ParticleTypes.ENCHANT;
    }

    public int getThreashHold() {
        return (this.getTitanStatus() == EnumTitanStatus.GOD) ? 7100 : ((this.getTitanStatus() == EnumTitanStatus.GREATER) ? 1310 : ((this instanceof EntitySnowGolemTitan || this instanceof EntitySlimeTitan) ? 150 : 850));
    }

    public void collideWithEntities(EntityTitanPart part, List<Entity> p_70970_1_) {
        if (part != null && part.level != null && !this.getWaiting()) {
            final double d0 = (part.getBoundingBox().minX + part.getBoundingBox().maxX) / 2.0;
            final double d2 = (part.getBoundingBox().minZ + part.getBoundingBox().maxZ) / 2.0;
            for (final Object object : p_70970_1_) {
                final Entity entity = (Entity) object;
                final boolean leg = part.field_146032_b == "rightleg" || part.field_146032_b == "leftleg" || part.field_146032_b == "leg1" || part.field_146032_b == "leg2" || part.field_146032_b == "leg3" || part.field_146032_b == "leg4";
                if (this.canAttack(entity) && entity != null && !(entity instanceof EntityWebShot) && !(entity instanceof EntitySkeletonTitanGiantArrow) && !(entity instanceof WitherSkullEntity) && !(entity instanceof EntityTitanFireball) && !(entity instanceof EntityProtoBall) && !(entity instanceof EntityLightningBall) && !(entity instanceof EntityTitanPart) && !(entity instanceof EntityHarcadiumArrow) && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit)) {
                    final double d3 = entity.getX() - d0;
                    final double d4 = entity.getZ() - d2;
                    final double d5 = d3 * d3 + d4 * d4;
                    entity.push(d3 / d5 * (leg ? 5.0 : 1.5), leg ? 1.75 : 0.5, d4 / d5 * (leg ? 5.0 : 1.5));
                    if (!this.level.isClientSide && this.canAttack(entity) && entity.getY() <= part.getY() - part.height - 0.01) {
                        entity.hurt(DamageSource.thorns(this), 20.0f);
                    }
                    if (!(this instanceof EntitySkeletonTitan) || ((EntitySkeletonTitan) this).getSkeletonType() != 1 || !(entity instanceof LivingEntity)) {
                        continue;
                    }
                    ((LivingEntity) entity).addEffect(new EffectInstance(Effects.WITHER, 1200, 3));
                }
            }
        }
    }

    public int getRegenTime() {
        return 20;
    }

    public int getKnockbackAmount() {
        switch (this.getTitanStatus()) {
            case GOD: {
                return 24;
            }
            case GREATER: {
                return 16;
            }
            case AVERAGE: {
                return 8;
            }
            default: {
                return 4;
            }
        }
    }

    @Override
    public EnumTitanStatus getTitanStatus() {
        return EnumTitanStatus.LESSER;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    public boolean canBeAffected(EffectInstance p_70687_1_) {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (amount > (TitanConfig.NightmareMode ? 500.0f : 1000.0f) && !(this instanceof EntitySlimeTitan)) {
            amount = (TitanConfig.NightmareMode ? 500.0f : 1000.0f);
        }
        if (isOreSpawnBossToExempt(source.getEntity()) || source.getEntity() instanceof IronGolemEntity || source.getEntity() instanceof EnderDragonEntity || source.getEntity() instanceof WitherEntity) {
            this.playSound(TitanSounds.titanPunch, 50.0f, this.isBaby() ? 1.5f : 1.0f);
            amount *= 10.0f;
        }
        if (this.isInvulnerable() || source.getEntity() == null || amount <= 20.0f) {
            return false;
        }
        if (source.getEntity() instanceof EntitySnowGolemTitan && this instanceof EntitySnowGolemTitan) {
            return false;
        }
        if (source.getEntity() instanceof PlayerEntity && !this.canBeHurtByPlayer()) {
            return false;
        }
        if (entity != null && entity instanceof LivingEntity && ((this.getVehicle() != null && source.getEntity() == this.getVehicle()) || entity.isInvulnerable() || entity.getBbHeight() >= 6.0f || (((LivingEntity) entity).getArmorValue() > 24 && !((LivingEntity) entity).hasEffect(Effects.ABSORPTION)) || entity.isInvulnerable() || (((LivingEntity) entity).hasEffect(Effects.DAMAGE_BOOST) && ((LivingEntity) entity).getEffect(Effects.DAMAGE_BOOST).getAmplifier() > 255)) && !whiteListNoDamage(entity) && !(entity instanceof TameableEntity)) {
            return false;
        }
        if (source.isMagic() || source.isExplosion() || source.isFire() || source.getMsgId() == "inFire" || source.getMsgId() == "onFire" || source.getMsgId() == "lava" || source.getMsgId() == "inWall" || source.getMsgId() == "drown" || source.getMsgId() == "starve" || source.getMsgId() == "cactus" || source.getMsgId() == "fall" || source.getMsgId() == "generic" || source.getMsgId() == "outOfWorld" || source.getMsgId() == "magic" || source.getMsgId() == "wither" || source.getMsgId() == "anvil" || source.getMsgId() == "fallingBlock" || source.getMsgId() == "explosion.player" || source.getMsgId() == "explosion" || source.getMsgId() == "indirectMagic" && !(this instanceof EntitySlimeTitan)) {
            return false;
        }
        if (super.hurt(source, amount)) {
            if (entity != null && entity instanceof LivingEntity && this.animTick <= 12 && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
                this.setTarget((LivingEntity) entity);
                this.setLastHurtByMob((LivingEntity) entity);
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.HOSTILE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOSTILE_DEATH;
    }

    @OnlyIn(value = Dist.CLIENT)
    public void shakeNearbyPlayerCameras(double distance) {
        if (!this.level.players().isEmpty()) {
            for (int l1 = 0; l1 < this.level.players().size(); ++l1) {
                Entity entity = this.level.players().get(l1);
                if (!(entity instanceof PlayerEntity) || !(entity.distanceToSqr(this) < distance)) continue;
                this.level.broadcastEntityEvent(entity, (byte) 2);
                entity.invulnerableTime = 0;
            }
        }
        if (!(this.level instanceof ServerWorld)) {
            return;
        }
        for (Entity entity : ((ServerWorld) this.level).getAllEntities()) {
            if (!(entity instanceof LivingEntity) || entity instanceof EntityTitan || !(entity.distanceToSqr(this) < distance))
                continue;
            try {
                ObfuscationReflectionHelper.findField(Entity.class, new String[]{"hurt_timer"}.toString()).setInt(entity, 0);
            } catch (Exception e) {
                entity.invulnerableTime = 0;
            }
            entity.invulnerableTime = 0;
            this.level.broadcastEntityEvent(entity, (byte) 2);
        }
    }

    public void attackChoosenEntity(Entity damagedEntity, float damage, int knockbackAmount) {
        float at;
        if (this.getTitanStatus() == EnumTitanStatus.AVERAGE) {
            at = 20.0f;
        } else if (this.getTitanStatus() == EnumTitanStatus.GREATER) {
            at = 50.0f;
        } else if (this.getTitanStatus() == EnumTitanStatus.GOD) {
            at = 200.0f;
        } else {
            at = 10.0f;
        }
        if (this.level.getDifficulty() == Difficulty.EASY) {
            at *= 0.5f;
        }
        if (this.level.getDifficulty() == Difficulty.HARD) {
            at *= 2.0f;
        }
        if (TitanConfig.TotalDestructionMode) {
            at = Float.MAX_VALUE;
            damage = Float.MAX_VALUE;
        }
        damagedEntity.invulnerableTime = 0;
        if (!(damagedEntity instanceof EntityTitan) && damagedEntity.getBbHeight() < 6.0f && this.canAttack(damagedEntity)) {
            damagedEntity.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() + this.random.nextDouble(), this.getDeltaMovement().z());
            if (knockbackAmount > 0) {
                damagedEntity.push((double) (-MathHelper.sin(this.yBodyRot * (float) Math.PI / 180.0f) * (float) knockbackAmount) * 0.2, (double) knockbackAmount * 0.2, (double) (MathHelper.cos(this.yBodyRot * (float) Math.PI / 180.0f) * (float) knockbackAmount) * 0.2);
            }
        }
        if (damagedEntity != null && this.isAlive() && (!(damagedEntity instanceof EntityTitanPart) && !(damagedEntity instanceof ItemEntity) && !(damagedEntity instanceof ExperienceOrbEntity) && (!(damagedEntity instanceof ProjectileEntity && !(damagedEntity instanceof FireballEntity))) && !(damagedEntity instanceof FireballEntity))) {
            if (damagedEntity instanceof EnderCrystalEntity) {
                damagedEntity.hurt(new DamageSource("other").bypassArmor().bypassMagic().bypassInvul(), 100.0f);
            }
            if (damagedEntity instanceof EnderDragonEntity) {
                this.level.explode(null, damagedEntity.getX(), damagedEntity.getY(), damagedEntity.getZ(), 6.0f, false, Explosion.Mode.NONE);
            }
            final float rotationYawHead = this.yHeadRot;
            this.yRot = rotationYawHead;
            this.yBodyRot = rotationYawHead;
            if (EnchantmentHelper.getFireAspect(this) > 0) {
                damagedEntity.setSecondsOnFire(EnchantmentHelper.getFireAspect(this) * 100);
            }
            if (!(damagedEntity instanceof LivingEntity)) {
                damagedEntity.hurt(new DamageSource("other").bypassArmor().bypassMagic().bypassInvul(), damage);
                damagedEntity.removed = true;
            } else if (damagedEntity.isAlive()) {
                if (damagedEntity.getBbHeight() >= 6.0f || damagedEntity instanceof EntityTitan) {
                    damage *= (isOreSpawnBossToExempt(damagedEntity) ? 2.0f : 10.0f);
                    damagedEntity.playSound(TitanSounds.titanPunch, 50.0f, this.isBaby() ? 1.5f : 1.0f);
                }
                if (damagedEntity instanceof PlayerEntity && damagedEntity.createCommandSourceStack().getTextName() == "SuperGirlyGamer") {
                    damage *= 100.0f;
                    damagedEntity.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() + 10.0, this.getDeltaMovement().z());
                    damagedEntity.playSound(TitanSounds.titanPunch, 50.0f, 1.0f);
                }
                if (this.canAttack(damagedEntity) && !(this instanceof EntitySlimeTitan) && !(this instanceof EntitySnowGolemTitan) && !(this instanceof EntityGargoyleTitan) && !(this instanceof EntityIronGolemTitan)) {
                    this.damageBypassEntity(((LivingEntity)damagedEntity), DamageSourceExtra.causeSoulStealingDamage(this), at);
                    final int b0 = 1 + this.level.getDifficulty().getId();
                    //((LivingEntity) damagedEntity).addEffect(new PotionEffect(ClientProxy.electricJudgment.id, b0 * 20, 2));
                }
                damage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)damagedEntity).getMobType());
                knockbackAmount += EnchantmentHelper.getKnockbackBonus(this);
                if (!(damagedEntity instanceof EntityTitan) && this.canAttack(damagedEntity)) {
                    damagedEntity.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() + this.random.nextDouble(), this.getDeltaMovement().z());
                    if (knockbackAmount > 0) {
                        damagedEntity.push((double) (-MathHelper.sin(this.yBodyRot * (float) Math.PI / 180.0f) * (float) knockbackAmount) * 0.2, (double) knockbackAmount * 0.2, (double) (MathHelper.cos(this.yBodyRot * (float) Math.PI / 180.0f) * (float) knockbackAmount) * 0.2);
                    }
                }
                if (damagedEntity instanceof PlayerEntity) {
                    LivingEntity damagedPlayer = (LivingEntity) damagedEntity;
                    if (!this.level.isClientSide && !damagedPlayer.getMainHandItem().isEmpty() && damagedPlayer.getMainHandItem().isEnchanted() && ((PlayerEntity) damagedPlayer).getMainHandItem().getItem() != TitanItems.ultimaBlade) {
                        damagedPlayer.getMainHandItem().setTag(new CompoundNBT());
                        this.setTarget(damagedPlayer);
                    }
                    for (int i = 0; i < ((PlayerEntity) damagedPlayer).inventory.armor.size(); ++i) {
                        if (!((PlayerEntity) damagedPlayer).inventory.armor.get(i).isEmpty()) {
                            if (!(((PlayerEntity) damagedPlayer).inventory.armor.get(i).getItem() instanceof ItemTitanArmor)) {
                                ((PlayerEntity) damagedPlayer).inventory.armor.get(i).shrink(1);
                                ((PlayerEntity) damagedPlayer).inventory.armor.set(i, new ItemStack(null));
                                damagedPlayer.playSound(SoundEvents.ITEM_BREAK, 0.8f, 0.8f + this.level.random.nextFloat() * 0.4f);
                                this.damageBypassEntity((LivingEntity) damagedPlayer, new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), at);
                            } else {
                                ((PlayerEntity) damagedPlayer).inventory.armor.get(i).setDamageValue((int) (((PlayerEntity) damagedPlayer).inventory.armor.get(i).getDamageValue() + damage));
                            }
                        }
                    }
                    if ((((PlayerEntity) damagedPlayer).abilities.invulnerable || this.level.getDifficulty() == Difficulty.PEACEFUL)) {
                        damagedPlayer.hurt(DamageSourceExtra.causeArmorPiercingMobDamage(this).bypassMagic().bypassInvul(), at);
                        this.damageBypassEntity(damagedPlayer, new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), at);
                        this.damageBypassEntity(damagedPlayer, new DamageSource("other").bypassArmor().bypassMagic().bypassInvul(), at);
                        this.damageBypassEntity(damagedPlayer, DamageSource.OUT_OF_WORLD.bypassMagic(), at);
                    }
                }
                if ((damagedEntity.isInvulnerable() && !(damagedEntity instanceof EntityTitan) || ((LivingEntity)damagedEntity).getArmorValue() > 19 || damagedEntity instanceof PlayerEntity && ((PlayerEntity) damagedEntity).abilities.invulnerable || ((LivingEntity)damagedEntity).hasEffect(Effects.DAMAGE_RESISTANCE) && ((LivingEntity)damagedEntity).getEffect(Effects.DAMAGE_RESISTANCE).getAmplifier() > 4) && !(damagedEntity instanceof EntityTitan) && !(damagedEntity instanceof TameableEntity)) {
                    if (damagedEntity instanceof PlayerEntity) {
                        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.level.getServer() != null) {
                            this.level.getServer().setDifficulty(Difficulty.HARD, true);
                        }
                        damagedEntity.hurt(DamageSource.mobAttack(this).bypassArmor().bypassInvul().bypassMagic(), at);
                        damagedEntity.hurt(new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), at);
                    } else {
                        damagedEntity.hurt(new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), at);
                        this.damageBypassEntity(((LivingEntity)damagedEntity), new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), damage);
                    }
                } else if (this.getMobType() == CreatureAttribute.UNDEAD && ((LivingEntity)damagedEntity).getMobType() == CreatureAttribute.UNDEAD && !(((LivingEntity)damagedEntity) instanceof EntityTitan)) {
                    this.damageBypassEntity(((LivingEntity)damagedEntity), new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), damage);
                } else if (((LivingEntity)damagedEntity).getHealth() >= 6.0f && !(((LivingEntity)damagedEntity) instanceof TameableEntity) && !(((LivingEntity)damagedEntity) instanceof EntityTitan) && !(((LivingEntity)damagedEntity) instanceof EnderDragonEntity) || ((LivingEntity)damagedEntity).getArmorValue() > 9 && !(damagedEntity instanceof PlayerEntity) && !(damagedEntity instanceof EntityTitan) && !(damagedEntity instanceof EnderDragonEntity)) {
                    this.damageBypassEntity((LivingEntity) damagedEntity, new DamageSource("infinity").bypassArmor().bypassInvul().bypassMagic(), damage);
                } else {
                    damagedEntity.hurt(DamageSource.mobAttack(this), damage);
                }
            }
        }
    }

    private void damageBypassEntity(LivingEntity entity, DamageSource p_70665_1_, float p_70665_2_) {
        if (entity.isAlive()) {
            if (p_70665_2_ <= 0.0f) {
                return;
            }
            float f1 = p_70665_2_;
            p_70665_2_ = Math.max(p_70665_2_ - entity.getAbsorptionAmount(), 0.0f);
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() - f1 - p_70665_2_);
            if (p_70665_2_ != 0.0f) {
                if (entity instanceof CreatureEntity) {
                    EntityIronGolemTitan.addTitanTargetingTaskToEntity((CreatureEntity) entity);
                }
                float f2 = entity.getHealth();
                if (!entity.hurt(p_70665_1_, p_70665_2_)) {
                    entity.getEntityData().set(LivingEntity.DATA_HEALTH_ID, MathHelper.clamp(f2 - p_70665_2_, 0.0f, entity.getMaxHealth()));
                }
                entity.getCombatTracker().recordDamage(p_70665_1_, f2, p_70665_2_);
                entity.setAbsorptionAmount(entity.getAbsorptionAmount() - p_70665_2_);
                if (!(entity instanceof EntityTitan)) {
                    if (entity.getHealth() == 50.0f && entity.getBbWidth() == 15.0f) {
                        entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(0.0);
                        entity.hurt(DamageSourceExtra.causeAntiTitanDamage(this).bypassArmor().bypassMagic(), 40.0f);
                    }
                    if (!entity.isAlive() && p_70665_1_.getEntity() != null) {
                        entity.die(p_70665_1_);
                    }
                }
            }
        }
    }

    public boolean canBeHurtByPlayer() {
        return !this.isInvulnerable();
    }

    public boolean doHurtTarget(Entity p_70652_1_) {
        this.swing(Hand.MAIN_HAND);
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
        int i = this.getKnockbackAmount();
        if (TitanConfig.TotalDestructionMode) {
            f = Float.MAX_VALUE;
        }
        this.attackChoosenEntity(p_70652_1_, f, i);
        if (p_70652_1_ instanceof MobEntity) {
            ((MobEntity) p_70652_1_).setLastHurtByMob(this);
        }
        this.getLookControl().setLookAt(p_70652_1_, 180.0f, this.getMaxHeadXRot());
        return true;
    }

    protected boolean isValidLightLevel() {
        final int i = MathHelper.floor(this.getX());
        final int j = MathHelper.floor(this.getBoundingBox().minY);
        final int k = MathHelper.floor(this.getZ());
        if (this.level.getBrightness(LightType.SKY, new BlockPos(i, j, k)) > this.random.nextInt(32)) {
            return false;
        }
        int l = this.level.getBrightness(LightType.BLOCK, new BlockPos(i, j, k));

        if (this.level.isThundering()) {
            final int i2 = this.level.getMaxLocalRawBrightness(new BlockPos(i, j, k));
            int originalSubtractedSkylight = this.level.skyDarken;
            this.level.skyDarken = 10;
            l = this.level.getBrightness(LightType.BLOCK, new BlockPos(i, j, k));
            this.level.skyDarken = originalSubtractedSkylight;
        }
        return l <= this.random.nextInt(8);
    }

    public void kill() {
        if (this.getTitanStatus() != EnumTitanStatus.GOD) {
            this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getVoicePitch());
            this.removeAfterChangingDimensions();
        }
    }

    protected void tickDeath() {
    }

    protected void onTitanDeathUpdate() {
        if (this instanceof EntitySnowGolemTitan || this instanceof EntitySlimeTitan) {
            ++this.deathTime;
            this.destroyBlocksInAABB(this.getBoundingBox());
            if (this.deathTime == 20) {
                this.removeAfterChangingDimensions();
                for (int i = 0; i < 20; ++i) {
                    final double d2 = this.random.nextGaussian() * 0.02;
                    final double d3 = this.random.nextGaussian() * 0.02;
                    final double d4 = this.random.nextGaussian() * 0.02;
                    this.level.addParticle(ParticleTypes.POOF, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), this.getY() + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), d2, d3, d4);
                }
            }
        }
        this.entityData.set(t6, Float.valueOf(MathHelper.clamp(this.entityData.get(t5).floatValue(), 0.0f, this.getMaxHealth())));
        this.deathTicks = this.entityData.get(t5).floatValue() <= 0.0f ? ++this.deathTicks : 0;
        if (this.deathTicks == 1 && !this.level.isClientSide) {
            this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getVoicePitch());
            ArrayList listp = Lists.newArrayList(this.level.players());
            if (listp != null && !listp.isEmpty()) {
                for (int i1 = 0; i1 < listp.size(); ++i1) {
                    Entity entity = (Entity) listp.get(i1);
                    if (entity != null && !(entity instanceof PlayerEntity)) continue;
                }
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().x() * 0.0, this.getDeltaMovement().y(), this.getDeltaMovement().z() * 0.0);
        this.yRot = this.yHeadRot += 10.0f;
        this.yBodyRot = this.yHeadRot;
        this.xRot = 0.0f + (float) this.getInvulTime() / 4.8f + (0.01f + 0.01f * MathHelper.cos((float) this.tickCount * 0.25f)) * (float) Math.PI;
        this.setTarget(null);
        this.animateHurt();
        this.spawnAnim();
        if (this.deathTicks > 200 && !this.level.isClientSide) {
            this.setInvulTime(this.getInvulTime() + 10);
        }
        if (this.getInvulTime() >= this.getThreashHold()) {
            this.removeAfterChangingDimensions();
        }
    }

    public boolean isInvulnerable() {
        return this.getInvulTime() >= 1 || this.getWaiting() || this.animID == 13 || this.deathTicks > 0 || (this instanceof EntityWitherzilla && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void")) && this.getExtraPower() > 5) || super.isInvulnerable();
    }

    protected boolean teleportEntityRandomly(LivingEntity entity) {
        double d0 = this.getX() + (this.random.nextDouble() - 0.5) * (72.0 + (double) this.getBbWidth());
        double d1 = this.getY() - (double) this.getBbHeight() + (double) (this.getBbHeight() * 2.0f);
        double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * (72.0 + (double) this.getBbWidth());
        return this.teleportEntityTo(entity, d0, d1, d2);
    }

    protected boolean teleportEntityTo(LivingEntity entity, double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(entity, p_70825_1_, p_70825_3_, p_70825_5_);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        double d3 = this.getX();
        double d4 = this.getY();
        double d5 = this.getZ();
        entity.setPos(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        boolean flag = false;
        int i = MathHelper.floor(entity.getX());
        int j = MathHelper.floor(entity.getY());
        int k = MathHelper.floor(entity.getZ());
        if (this.level.getChunkSource().hasChunk(i, k)) {
            boolean flag1 = false;
            while (!flag1 && j > 0) {
                double posY;
                BlockState state = this.level.getBlockState(new BlockPos(i, j - 1, k));
                if (state.getMaterial().isSolid()) {
                    flag1 = true;
                    continue;
                }
                double d = posY = entity.getY();
                posY = d - 1.0;
                entity.setPos(entity.getX(), d, entity.getZ());
                --j;
            }
            if (flag1) {
                entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), this.yRot, this.xRot);
                if (this.level.noCollision(entity, entity.getBoundingBox()) && !this.level.containsAnyLiquid(entity.getBoundingBox())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            entity.moveTo(d3, d4, d5, this.yRot, this.xRot);
            return false;
        }
        return true;
    }

    public void retractMinionNumFromType(final EnumMinionType minionType) {
        if (minionType == EnumMinionType.SPECIAL) {
            --this.numSpecialMinions;
        } else if (minionType == EnumMinionType.PRIEST) {
            --this.numPriests;
        } else if (minionType == EnumMinionType.ZEALOT) {
            --this.numZealots;
        } else if (minionType == EnumMinionType.BISHOP) {
            --this.numBishop;
        } else if (minionType == EnumMinionType.TEMPLAR) {
            --this.numTemplar;
        } else if (minionType == EnumMinionType.LOYALIST) {
            --this.numMinions;
        }
    }

    public Vector3d collide(Vector3d vec) {
        boolean flag3;
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        ISelectionContext iselectioncontext = ISelectionContext.of(this);
        VoxelShape voxelshape = this.level.getWorldBorder().getCollisionShape();
        Stream<Object> stream = VoxelShapes.joinIsNotEmpty(voxelshape, VoxelShapes.create(axisalignedbb.deflate(1.0E-7)), IBooleanFunction.AND) ? Stream.empty() : Stream.of(voxelshape);
        Stream stream1 = this.level.getEntityCollisions(this, axisalignedbb.expandTowards(vec), p_233561_0_ -> true);
        ReuseableStream reuseablestream = new ReuseableStream(Stream.concat(stream1, stream));
        Vector3d vector3d = vec.lengthSqr() == 0.0 ? vec : EntityTitan.collideBoundingBoxHeuristically(this, vec, axisalignedbb, this.level, iselectioncontext, reuseablestream);
        boolean flag = vec.x != vector3d.x;
        boolean flag1 = vec.y != vector3d.y;
        boolean flag2 = vec.z != vector3d.z;
        boolean bl = flag3 = this.onGround || flag1 && vec.y < 0.0;
        if (this.maxUpStep > 0.0f && flag3 && (flag || flag2)) {
            Vector3d vector3d3;
            Vector3d vector3d1 = EntityTitan.collideBoundingBoxHeuristically(this, new Vector3d(vec.x, this.maxUpStep, vec.z), axisalignedbb, this.level, iselectioncontext, reuseablestream);
            Vector3d vector3d2 = EntityTitan.collideBoundingBoxHeuristically(this, new Vector3d(0.0, this.maxUpStep, 0.0), axisalignedbb.expandTowards(vec.x, 0.0, vec.z), this.level, iselectioncontext, reuseablestream);
            if (vector3d2.y < (double) this.maxUpStep && EntityTitan.getHorizontalDistanceSqr(vector3d3 = EntityTitan.collideBoundingBoxHeuristically(this, new Vector3d(vec.x, 0.0, vec.z), axisalignedbb.move(vector3d2), this.level, iselectioncontext, reuseablestream).add(vector3d2)) > EntityTitan.getHorizontalDistanceSqr(vector3d1)) {
                vector3d1 = vector3d3;
            }
            if (EntityTitan.getHorizontalDistanceSqr(vector3d1) > EntityTitan.getHorizontalDistanceSqr(vector3d)) {
                return vector3d1.add(EntityTitan.collideBoundingBoxHeuristically(this, new Vector3d(0.0, -vector3d1.y + vec.y, 0.0), axisalignedbb.move(vector3d1), this.level, iselectioncontext, reuseablestream));
            }
        }
        return vector3d;
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return entitySize;
    }

    public void setSize(float width, float height) {
        entitySize = EntitySize.fixed(width, height);
        this.refreshDimensions();
    }

    protected float getSoundPitch() {
        return 1.0f;
    }

    public boolean isBaby() {
        return false;
    }

    protected boolean getCanSpawnHere() {
        return true;
    }
}
