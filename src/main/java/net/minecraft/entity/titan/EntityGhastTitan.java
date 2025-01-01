package net.minecraft.entity.titan;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.entity.titan.ai.EntityAITitanLookIdle;
import net.minecraft.entity.titanminion.EntityGhastMinion;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.tags.ITag;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class EntityGhastTitan extends EntityTitan {
    public static final DataParameter<Byte> t16 = EntityDataManager.defineId(EntityGhastTitan.class, DataSerializers.BYTE);
    private final int explosionStrength;
    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    public int prevAttackCounter;
    public int attackCounter;
    private int aggroCooldown;
    public EntityGhastTitan(EntityType<? extends EntityTitan> type, World worldIn) {
        super(type, worldIn);
        this.explosionStrength = 5;
        this.shouldParticlesBeUpward = true;
        this.noPhysics = true;
        this.setSize(110.0f, 110.0f);
        this.xpReward = 750000;
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
        this.goalSelector.removeGoal(new EntityAITitanLookIdle(this));
    }

    @Override
    protected void applyEntityAI() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        if (TitanConfig.TitansFFAMode) {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.GhastTitanSorter));
        } else {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SearchForAThingToKill));
        }
    }

    public float getStandingEyeHeight(Pose pose, EntitySize p_213348_2_) {
        return 60.0f;
    }

    @Override
    public int getMinionCap() {
        return 120;
    }

    @Override
    public int getPriestCap() {
        return 60;
    }

    @Override
    public int getZealotCap() {
        return 30;
    }

    @Override
    public int getBishopCap() {
        return 15;
    }

    @Override
    public int getTemplarCap() {
        return 8;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_110182_bF() {
        return this.entityData.get(t16) != 0;
    }

    public boolean canAttack(final Entity p_70686_1_) {
        return !(p_70686_1_ instanceof EntityGhastMinion) && !(p_70686_1_ instanceof EntityGhastTitan) && !(p_70686_1_ instanceof EntityTitanFireball);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.random.nextInt(250) == 0 && this.level.getDifficulty() != Difficulty.PEACEFUL && super.getCanSpawnHere();
    }

    @Override
    public int getMinionSpawnRate() {
        return TitanConfig.GhastTitanMinionSpawnrate;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(t16, (byte) 0);
    }

    @Override
    public int getParticleCount() {
        return 100;
    }

    @Override
    public BasicParticleType getParticles() {
        return ParticleTypes.LARGE_SMOKE;
    }

    @Override
    public int getRegenTime() {
        return 5;
    }

    @Override
    public EnumTitanStatus getTitanStatus() {
        return EnumTitanStatus.GREATER;
    }

    protected SoundEvent getAmbientSound() {
        return TitanSounds.titanGhastLiving;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TitanSounds.titanGhastGrunt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TitanSounds.titanGhastDeath;
    }

    public float getBrightness() {
        return 1.0f;
    }

    @Override
    public float getSpeed() {
        return (float) (0.5 + this.getExtraPower() * 0.001);
    }

    @Override
    public boolean causeFallDamage(float p_70069_1_, float damageMultiplier) {
        return false;
    }

    @Override
    protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
        return 0;
    }

    @Override
    public void travel(Vector3d p_213352_1_) {
        this.move(MoverType.SELF, new Vector3d(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z));
        this.setDeltaMovement(this.getDeltaMovement().x * 0.9100000262260437, this.getDeltaMovement().y * 0.9100000262260437, this.getDeltaMovement().z * 0.9100000262260437);
        this.animationSpeedOld = this.animationSpeed;
        final double d1 = this.getX() - this.xo;
        final double d2 = this.getZ() - this.zo;
        float f4 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0f;
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        this.animationSpeed += (f4 - this.animationSpeed) * 0.4f;
        this.animationPosition += this.animationSpeed;
    }

    @Override
    public void aiStep() {
        this.setSize(110.0f, 110.0f);
        for (int i = 0; i < 90; ++i) {
            final double d0 = this.getX() + (this.random.nextFloat() * 90.0f - 45.0f);
            final double d2 = this.getY() + this.random.nextFloat() * 30.0f;
            final double d3 = this.getZ() + (this.random.nextFloat() * 90.0f - 45.0f);
            if (!this.level.isClientSide && this.level.getBlockState(new BlockPos((int) d0, (int) d2 + (int) this.getEyeHeight(), (int) d3)).getMaterial() != Material.AIR) {
                this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
            }
        }
        final PlayerEntity entity = this.level.getNearestPlayer(this, -1.0);
        if (entity instanceof PlayerEntity && entity != null && entity == this.getTarget() && this.getTarget() != null) {
            entity.setRemainingFireTicks(50);
            if (this.random.nextInt(200) == 0 && this.getTarget() != null && this.getHealth() <= this.getMaxHealth() / 100.0f) {
                entity.hurt(DamageSourceExtra.ON_FIRE.bypassMagic().bypassInvul(), Float.MAX_VALUE);
            }
            if (entity.getAbsorptionAmount() <= 0.0f && this.tickCount % 10 == 0) {
                entity.hurt(DamageSourceExtra.ON_FIRE.bypassMagic().bypassInvul(), 12.0f);
                entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, 9));
                if (entity.getHealth() <= 5.0f) {
                    entity.addEffect(new EffectInstance(Effects.BLINDNESS, 400, 1));
                }
            } else if (entity.getAbsorptionAmount() >= 0.0f && this.tickCount % 20 == 0) {
                entity.hurt(DamageSourceExtra.ON_FIRE.bypassMagic().bypassInvul(), 12.0f);
            }
        }
        if (TitanConfig.NightmareMode) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(9000.0 + this.getExtraPower() * 1800);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(400000.0 + this.getExtraPower() * 60000);
        } else {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3000.0 + this.getExtraPower() * 600);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200000.0 + this.getExtraPower() * 30000);
        }
        this.setCustomName(new TranslationTextComponent("entity.GhastTitan.name"));
        if (this.numMinions < this.getMinionCap() && (this.random.nextInt(this.getMinionSpawnRate()) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            final EntityGhastMinion entitychicken = new EntityGhastMinion(this.level);
            entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.teleportEntityRandomly(entitychicken);
            entitychicken.setMinionType(0);
            this.level.addFreshEntity(entitychicken);
            ++this.numMinions;
        }
        if (this.numPriests < this.getPriestCap() && (this.random.nextInt(this.getMinionSpawnRate() * 2) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            final EntityGhastMinion entitychicken = new EntityGhastMinion(this.level);
            entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.teleportEntityRandomly(entitychicken);
            entitychicken.setMinionType(1);
            this.level.addFreshEntity(entitychicken);
            ++this.numPriests;
        }
        if (this.numZealots < this.getZealotCap() && (this.random.nextInt(this.getMinionSpawnRate() * 4) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            final EntityGhastMinion entitychicken = new EntityGhastMinion(this.level);
            entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.teleportEntityRandomly(entitychicken);
            entitychicken.setMinionType(2);
            this.level.addFreshEntity(entitychicken);
            ++this.numZealots;
        }
        if (this.numBishop < this.getBishopCap() && (this.random.nextInt(this.getMinionSpawnRate() * 8) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            final EntityGhastMinion entitychicken = new EntityGhastMinion(this.level);
            entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.teleportEntityRandomly(entitychicken);
            entitychicken.setMinionType(3);
            this.level.addFreshEntity(entitychicken);
            ++this.numTemplar;
        }
        if (this.numTemplar < this.getTemplarCap() && (this.random.nextInt(this.getMinionSpawnRate() * 16) == 0 || this.getInvulTime() > 1) && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            final EntityGhastMinion entitychicken = new EntityGhastMinion(this.level);
            entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.teleportEntityRandomly(entitychicken);
            entitychicken.setMinionType(4);
            this.level.addFreshEntity(entitychicken);
            ++this.numTemplar;
        }
        if (this.level.isClientSide) {
            for (int i2 = 0; i2 < this.getParticleCount(); ++i2) {
                this.level.addParticle(this.getParticles(), this.getX() + (this.random.nextDouble() - 0.5) * 96.0, this.getY() + this.random.nextDouble() * 96.0, this.getZ() + (this.random.nextDouble() - 0.5) * 96.0, 0.0, 0.5, 0.0);
            }
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        this.prevAttackCounter = this.attackCounter;
        final double d0 = this.waypointX - this.getX();
        final double d2 = this.waypointY - this.getY();
        final double d3 = this.waypointZ - this.getZ();
        double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        if (d4 < 36.0 || d4 > 40000.0) {
            if (this.getTarget() != null) {
                this.waypointX = this.getTarget().getX() + (this.random.nextDouble() * 2.0 - 1.0) * 96.0;
                this.waypointY = 160.0 + (this.random.nextDouble() * 2.0 - 1.0) * 32.0;
                this.waypointZ = this.getTarget().getZ() + (this.random.nextDouble() * 2.0 - 1.0) * 96.0;
            } else {
                final PlayerEntity player = this.level.getNearestPlayer(this, 512.0);
                if (player != null) {
                    this.waypointX = player.getX() + (this.random.nextDouble() * 2.0 - 1.0) * 96.0;
                    this.waypointY = 160.0 + (this.random.nextDouble() * 2.0 - 1.0) * 32.0;
                    this.waypointZ = player.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * 96.0;
                } else {
                    this.waypointX = this.getX() + (this.random.nextDouble() * 2.0 - 1.0) * 96.0;
                    this.waypointY = 160.0 + (this.random.nextDouble() * 2.0 - 1.0) * 32.0;
                    this.waypointZ = this.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * 96.0;
                }
            }
        }
        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.random.nextInt(5) + 2;
            d4 = MathHelper.sqrt(d4);
            this.pushSelf(d0 / d4 * 0.3, d2 / d4 * 0.3, d3 / d4 * 0.3);
        }
        final double d5 = 1024.0;
        final float rotationYawHead = this.yHeadRot;
        this.yRot = rotationYawHead;
        this.yBodyRot = rotationYawHead;
        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < d5 * d5) {
            this.getLookControl().setLookAt(this.getTarget(), 180.0f, 180.0f);
            final double d6 = 50.0;
            final Vector3d vec3 = this.getLookAngle();
            final double px = this.getX() + vec3.x * d6;
            final double py = this.getY() + vec3.y * d6 + 10.0;
            final double pz = this.getZ() + vec3.z * d6;
            final double d7 = this.getTarget().getX() - px;
            final double d8 = this.getTarget().getY() - py;
            final double d9 = this.getTarget().getZ() - pz;
//            if (this.getSensing().canSee(this.getTarget())){
                if (this.attackCounter == 10) {
                    this.playSound(TitanSounds.titanGhastCharge, Float.MAX_VALUE, this.getSoundPitch());
                }
                ++this.attackCounter;
                if (this.attackCounter >= 50) {
                    this.lookAt(this.getTarget(), 180.0f, 180.0f);
                    if (this.getTarget() instanceof EntityTitan || this.getTarget().getBbHeight() >= 6.0f) {
                        this.attackChoosenEntity(this.getTarget(), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue(), this.getKnockbackAmount());
                    }
                    this.playSound(TitanSounds.titanGhastFireball, Float.MAX_VALUE, 1.0f);
                    final EntityTitanFireball entitysmallfireball = new EntityTitanFireball(this.level, this, d7 + this.getRandom().nextGaussian() * 16.0, d8, d9 + this.getRandom().nextGaussian() * 16.0);
                    entitysmallfireball.setPos(px, py, pz);
                    this.level.addFreshEntity(entitysmallfireball);
                    if (this.attackCounter >= 100) {
                        this.attackCounter = -80;
                    }
                }
//            } else if (this.attackCounter > 0) {
//                --this.attackCounter;
//            }
        } else {
            if (this.getTarget() == null) {
                final float renderYawOffset = -(float) Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * 180.0f / 3.1415927f;
                this.yHeadRot = renderYawOffset;
                this.yRot = renderYawOffset;
                this.yBodyRot = renderYawOffset;
            }
            if (this.attackCounter > 0) {
                --this.attackCounter;
            }
        }
        if (!this.level.isClientSide) {
            final byte b1 = this.entityData.get(t16);
            final byte b2 = (byte) ((this.attackCounter > 20) ? 1 : 0);
            if (b1 != b2) {
                this.entityData.set(t16, b2);
            }
        }
        super.customServerAiStep();
    }
    public int getMaxHeadXRot() {
        return 180;
    }

    protected Item getDropItem() {
        return Items.BLAZE_ROD;
    }

    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (this.deathTicks > 0) {
            for (int x = 0; x < 80; ++x) {
                final EntityXPBomb entitylargefireball = new EntityXPBomb(this.level, this.getX(), this.getY() + 4.0, this.getZ());
                entitylargefireball.setPos(this.getX(), this.getY() + 4.0, this.getZ());
                final EntityXPBomb entityXPBomb = entitylargefireball;
                entityXPBomb.push(0.0, 1.0, 0.0);
                entitylargefireball.setXPCount(26000);
                this.level.addFreshEntity(entitylargefireball);
            }
            for (int l = 0; l < 512 + this.random.nextInt(512 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.GUNPOWDER));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 512 + this.random.nextInt(512 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.GHAST_TEAR));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 256 + this.random.nextInt(256 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.COAL));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 256 + this.random.nextInt(256 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.IRON_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 256 + this.random.nextInt(256 + p_70628_2_) + p_70628_2_; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.GOLD_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 128 + this.random.nextInt(128 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.EMERALD_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 128 + this.random.nextInt(128 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.DIAMOND_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 64 + this.random.nextInt(64 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(TitanItems.harcadium));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 32 + this.random.nextInt(32 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(TitanItems.voidItem));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 16 + this.random.nextInt(16 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.BEDROCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 64 + this.random.nextInt(64 + p_70628_2_); ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.DRAGON_EGG));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int l = 0; l < 64; ++l) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.BREWING_STAND));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerable()) {
            return false;
        }
        if (source.getEntity() instanceof EntityGhastMinion || source.getEntity() instanceof EntityGhastTitan) {
            return false;
        }
        if (source.isFire()) {
            this.heal(amount);
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void inactDeathAction() {
        if (!this.level.isClientSide) {
            this.playSound(SoundEvents.GHAST_DEATH, this.getSoundVolume(), this.getSoundPitch());
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.dropFewItems(true, 0);
                this.dropEquipment();
                //this.dropRareDrop(1);
            }
            final EntityTitanSpirit entitytitan = new EntityTitanSpirit(this.level);
            entitytitan.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.level.addFreshEntity(entitytitan);
            entitytitan.setVesselHunting(false);
            entitytitan.setSpiritType(11);
        }
    }

    //Fuck u stupid collision push
    @Override
    public boolean updateFluidHeightAndDoFluidPushing(ITag<Fluid> p_210500_1_, double p_210500_2_) {
        return false;
    }
}
