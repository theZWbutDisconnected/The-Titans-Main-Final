package net.minecraft.entity.titanminion;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.entity.titan.EntityTitanSpirit;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.entity.titan.ITitan;
import net.minecraft.entity.titan.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.titan.ai.EntityAIAvoidEntity;
import net.minecraft.entity.titan.ai.EntityAIBreakDoorMinion;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

public class EntityZombieMinion extends ZombieEntity implements IRangedAttackMob, ITemplar {
    private static final DataParameter<Boolean> t13 = EntityDataManager.defineId(EntityZombieMinion.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> t19 = EntityDataManager.defineId(EntityZombieMinion.class, DataSerializers.INT);
    private final RangedBowAttackGoal<EntityZombieMinion> aiArrowAttack;
    public MobEntity master;
    public int randomSoundDelay;
    public LivingEntity entityToHeal;
    public int deathTicks;
    private int attackPattern;
    private float heightOffset;
    private int heightOffsetUpdateTime;

    public EntityZombieMinion(EntityType<? extends ZombieEntity> p_i48549_1_, World p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
        this.aiArrowAttack = new RangedBowAttackGoal(this, 1.0, 10, 64.0f);
        this.heightOffset = 0.5f;
        for (int i = 0; i < this.armorDropChances.length; ++i) {
            this.armorDropChances[i] = 0.2f;
        }
        this.setSize(0.5f, 1.95f);
        setCanBreakDoors(true);
        this.goalSelector.addGoal(0, new EntityAIAvoidEntity<>(this, WitherSkullEntity.class, 2.0f, 1.2, 1.75));
        this.goalSelector.addGoal(0, new EntityAIAvoidEntity<>(this, EntityTitanSpirit.class, 48.0f, 1.5, 1.5));
        this.goalSelector.addGoal(0, new EntityAIBreakDoorMinion(this));
        this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 1.2));
        this.goalSelector.addGoal(1, new RestrictSunGoal(this));
        this.goalSelector.addGoal(1, new FleeSunGoal(this, 1.2));
        this.goalSelector.addGoal(1, new EntityAIAttackOnCollide(this, 1.0, true));
        this.goalSelector.addGoal(0, new EntityAIFindEntityNearestInjuredAlly(this));
        if (TitanConfig.TitansFFAMode) {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.ZombieTitanSorter));
        } else {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SearchForAThingToKill));
        }
    }

    public EntityZombieMinion(World p_i48549_2_) {
        this(RenderTheTitans.zombieMinion, p_i48549_2_);
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return ZombieEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }    private EntitySize entitySize = this.setSize(0.5f, 1.95f);

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return entitySize;
    }

    public EntitySize setSize(float width, float height) {
        entitySize = EntitySize.fixed(width, height);
        this.refreshDimensions();
        return entitySize;
    }

    public boolean isVillager() {
        return entityData.get(t13);
    }

    public void setVillager(boolean b) {
        entityData.set(t13, b);
    }

    private void dropFewItems(boolean b, int i) {
    }

    @Override
    protected void removeAfterChangingDimensions() {
        super.removeAfterChangingDimensions();
        if (this.master != null && this.master instanceof EntityTitan) {
            ((EntityTitan) this.master).retractMinionNumFromType(this.getMinionType());
        }
    }

    public void setCombatTask() {
        this.goalSelector.removeGoal(this.aiArrowAttack);
        if (this.attackPattern == 0 && this.getMinionTypeInt() == 4) {
            this.goalSelector.addGoal(0, this.aiArrowAttack);
        }
    }

    protected SoundEvent getAmbientSound() {
        if (this.getMinionTypeInt() == 4) {
            this.playSound(SoundEvents.ZOMBIE_AMBIENT, this.getSoundVolume(), this.getSoundPitch() - 0.5f);
        }
        return (this.getMinionTypeInt() == 4) ? TitanSounds.titanZombieLiving : SoundEvents.ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource d) {
        if (this.getMinionTypeInt() == 4) {
            this.playSound(SoundEvents.ZOMBIE_HURT, this.getSoundVolume(), this.getSoundPitch() - 0.5f);
        }
        return (this.getMinionTypeInt() == 4) ? TitanSounds.titanZombieGrunt : SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        if (this.getMinionTypeInt() == 4) {
            this.playSound(SoundEvents.ZOMBIE_DEATH, this.getSoundVolume(), this.getSoundPitch() - 0.5f);
        }
        return (this.getMinionTypeInt() == 4) ? TitanSounds.titanZombieDeath : SoundEvents.ZOMBIE_DEATH;
    }

    protected float getSoundPitch() {
        return (this.getMinionTypeInt() == 4) ? (1.0f + 0.2f) : 1.0f;
    }

    public int getArmorValue() {
        int i = 2;
        for (final ItemStack itemstack : this.getArmorSlots()) {
            if (itemstack != null && itemstack.getItem() instanceof ArmorItem) {
                final int l = ((ArmorItem) itemstack.getItem()).getDefense();
                i += l;
            }
        }
        switch (this.getMinionTypeInt()) {
            case 1: {
                i += 2;
                break;
            }
            case 2: {
                i += 15;
                break;
            }
            case 3: {
                i += 18;
                break;
            }
            case 4: {
                i += 22;
                break;
            }
        }
        if (i > 24) {
            i = 24;
        }
        return i;
    }

    protected float getDamageAfterMagicAbsorb(final DamageSource p_70672_1_, float p_70672_2_) {
        if (this.getMinionTypeInt() == 4) {
            p_70672_2_ = super.getDamageAfterMagicAbsorb(p_70672_1_, p_70672_2_);
            if (p_70672_1_.getEntity() == this) {
                p_70672_2_ = 0.0f;
            }
            if (p_70672_1_.isMagic()) {
                p_70672_2_ *= (float) 0.15;
            }
            return p_70672_2_;
        }
        return super.getDamageAfterMagicAbsorb(p_70672_1_, p_70672_2_);
    }

    @Override
    public ITextComponent getName() {
        switch (this.getMinionTypeInt()) {
            case 1: {
                return new TranslationTextComponent("entity.ZombiePriest.name");
            }
            case 2: {
                return new TranslationTextComponent("entity.ZombieZealot.name");
            }
            case 3: {
                return new TranslationTextComponent("entity.ZombieBishop.name");
            }
            case 4: {
                return new TranslationTextComponent("entity.ZombieTemplar.name");
            }
            default: {
                return new TranslationTextComponent("entity.ZombieLoyalist.name");
            }
        }
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        Object p_110161_1_2 = p_213386_4_;
        final float f = p_213386_2_.getSpecialMultiplier();
        this.setCanPickUpLoot(true);
        if (p_110161_1_2 == null) {
            p_110161_1_2 = new GroupData(this.level.random.nextFloat() < ((this.level.getDifficulty() == Difficulty.NORMAL) ? 0.05f : ((this.level.getDifficulty() == Difficulty.HARD) ? 0.25f : 0.005f)), this.level.random.nextFloat() < 0.1f, null);
        }
        if (p_110161_1_2 instanceof GroupData groupdata) {
            if (groupdata.field_142046_b) {
                this.setVillager(true);
            }
            if (groupdata.field_142048_a) {
                this.setBaby(true);
            }
        }
        this.setCanBreakDoors(true);
        this.addRandomArmor();
        this.enchantEquipment();
        if (this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.1, AttributeModifier.Operation.ADDITION));
        final double d0 = this.random.nextDouble() * 1.5 * f;
        if (d0 > 1.0) {
            this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (this.random.nextFloat() < f * (this.getMinionTypeInt() * 0.1f + 0.1f)) {
            this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25 + 0.5, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0 + 4.0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (!this.level.isClientSide) {
            this.setMinionType(0);
            if (this.random.nextInt(2) == 0) {
                this.setMinionType(1);
                if (this.random.nextInt(2) == 0) {
                    this.setMinionType(2);
                    if (this.random.nextInt(2) == 0) {
                        this.setMinionType(3);
                        if (this.random.nextInt(2) == 0) {
                            this.setMinionType(4);
                        }
                    }
                }
            }
        }
        this.getAttribute(Attributes.FOLLOW_RANGE).addTransientModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian(), AttributeModifier.Operation.MULTIPLY_BASE));
        return p_213386_4_;
    }

    private void enchantEquipment() {

    }

    private void addRandomArmor() {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(t13, Boolean.valueOf(false));
        getEntityData().define(t19, Integer.valueOf(0));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putInt("MinionType", this.getMinionTypeInt());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        this.setMinionType(tagCompund.getInt("MinionType"));
    }

    public int getMinionTypeInt() {
        return this.getEntityData().get(t19);
    }

    public EnumMinionType getMinionType() {
        switch (this.getMinionTypeInt()) {
            case 1: {
                return EnumMinionType.PRIEST;
            }
            case 2: {
                return EnumMinionType.ZEALOT;
            }
            case 3: {
                return EnumMinionType.BISHOP;
            }
            case 4: {
                return EnumMinionType.TEMPLAR;
            }
            default: {
                return EnumMinionType.LOYALIST;
            }
        }
    }

    public void setMinionType(final int miniontype) {
        this.getEntityData().set(t19, miniontype);
        if (miniontype == 1) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
            this.goalSelector.addGoal(0, new EntityAIFindEntityNearestInjuredAlly(this));
            this.setHealth(40.0f);
            this.xpReward = 15;
        } else if (miniontype == 2) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(180.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10.0);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
            this.setHealth(180.0f);
            this.xpReward = 100;
        } else if (miniontype == 3) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(400.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15.0);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.375);
            this.fireImmune();
            this.setHealth(400.0f);
            this.xpReward = 200;
        } else if (miniontype == 4) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1800.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(30.0);
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
            this.fireImmune();
            this.setHealth(1800.0f);
            this.xpReward = 1000;
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0);
            this.setHealth(26.0f);
            this.xpReward = 7;
        }
    }

    @Override
    public void aiStep() {
        if (this.getMinionTypeInt() == 1) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
            this.xpReward = 15;
        } else if (this.getMinionTypeInt() == 2) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(180.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10.0);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
            this.xpReward = 100;
        } else if (this.getMinionTypeInt() == 3) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(400.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15.0);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.375);
            this.fireImmune();
            this.xpReward = 200;
        } else if (this.getMinionTypeInt() == 4) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1800.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(30.0);
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
            this.fireImmune();
            this.xpReward = 1000;
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0);
            this.xpReward = 7;
        }
        if (this.isAlive() || this.getMinionTypeInt() != 4) {
            super.aiStep();
        }
        if (this.getMinionTypeInt() == 3) {
            if (this.random.nextInt(120) == 0 && this.master == null && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                final EntityZombieMinion entitychicken = new EntityZombieMinion(this.level);
                entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.SPAWNER, null, null);
                entitychicken.setMinionType(0);
                entitychicken.setBaby(this.isBaby());
                entitychicken.setVillager(this.isVillager());
                this.level.addFreshEntity(entitychicken);
            }
            if (this.random.nextInt(240) == 0 && this.master == null && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                final EntityZombieMinion entitychicken = new EntityZombieMinion(this.level);
                entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.SPAWNER, null, null);
                entitychicken.setMinionType(1);
                entitychicken.setBaby(this.isBaby());
                entitychicken.setVillager(this.isVillager());
                this.level.addFreshEntity(entitychicken);
            }
        }
        if (this.getMinionTypeInt() == 4) {
            if (this.tickCount % 40 == 0) {
                this.heal(1.0f);
            }
            if (this.level.random.nextInt(150) == 1) {
                this.heal(2.0f);
            }
            if (this.level.random.nextInt(100) == 1 && this.getHealth() < this.getMaxHealth() * 0.75) {
                this.heal(2.0f);
            }
            if (this.level.random.nextInt(35) == 1 && this.getHealth() < this.getMaxHealth() * 0.5) {
                this.heal(5.0f);
            }
            if (this.level.random.nextInt(30) == 1 && this.getHealth() < this.getMaxHealth() * 0.25) {
                this.heal(5.0f);
            }
            if (this.level.random.nextInt(30) == 1 && this.getHealth() < this.getMaxHealth() * 0.05) {
                this.heal(200.0f);
            }
            if (!this.onGround && this.getDeltaMovement().y < 0.0) {
                this.setDeltaMovement(getDeltaMovement().x, this.getDeltaMovement().y * 0.6, getDeltaMovement().z);
            }
            if (this.master == null && this.getHealth() > 0.0f && !this.level.isClientSide && this.level.getDifficulty() != Difficulty.PEACEFUL) {
                if (this.random.nextInt(60) == 0) {
                    final EntityZombieMinion entitychicken = new EntityZombieMinion(this.level);
                    entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken.setMinionType(0);
                    entitychicken.setBaby(this.isBaby());
                    entitychicken.setVillager(this.isVillager());
                    this.level.addFreshEntity(entitychicken);
                }
                if (this.random.nextInt(120) == 0) {
                    final EntityZombieMinion entitychicken = new EntityZombieMinion(this.level);
                    entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken.setMinionType(1);
                    entitychicken.setBaby(this.isBaby());
                    entitychicken.setVillager(this.isVillager());
                    this.level.addFreshEntity(entitychicken);
                }
                if (this.random.nextInt(240) == 0) {
                    final EntityZombieMinion entitychicken = new EntityZombieMinion(this.level);
                    entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.SPAWNER, null, null);
                    entitychicken.setMinionType(2);
                    entitychicken.setBaby(this.isBaby());
                    entitychicken.setVillager(this.isVillager());
                    this.level.addFreshEntity(entitychicken);
                }
            }
            if (this.level.isClientSide && !this.onGround) {
                for (int i = 0; i < 3; ++i) {
                    this.level.addParticle(ParticleTypes.POOF, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                }
            }
            if (this.random.nextInt(60) == 0 && this.getTarget() != null) {
                this.setCombatTask();
                if (!this.onGround) {
                    this.attackPattern = 0;
                } else {
                    this.attackPattern = 1;
                }
            }
            --this.heightOffsetUpdateTime;
            if (this.heightOffsetUpdateTime <= 0) {
                this.jumpFromGround();
                this.heightOffsetUpdateTime = 100;
                this.heightOffset = 0.5f + (float) this.random.nextGaussian() * 3.0f;
                this.attackPattern = 0;
            }
            final LivingEntity entitylivingbase = this.getTarget();
            if (this.attackPattern == 0 && entitylivingbase != null && !this.level.isClientSide) {
                if (entitylivingbase.getY() + entitylivingbase.getEyeHeight() > this.getY() + this.getEyeHeight() + this.heightOffset) {
                    this.push(0.0, 0.4 - this.getDeltaMovement().y, 0.0);
                    this.hasImpulse = true;
                }
                this.getLookControl().setLookAt(entitylivingbase, 180.0f, 40.0f);
                final double d0 = entitylivingbase.getX() - this.getX();
                final double d2 = entitylivingbase.getZ() - this.getZ();
                final double d3 = d0 * d0 + d2 * d2;
                if (d3 > entitylivingbase.getBbWidth() * entitylivingbase.getBbWidth() + this.getBbWidth() * this.getBbWidth() + 16.0) {
                    final double d4 = MathHelper.sqrt(d3);
                    this.push(d0 / d4 * 0.6 - this.getDeltaMovement().x, 0.0, d2 / d4 * 0.6 - this.getDeltaMovement().z);
                }
            }
            if (this.isAlive() && !this.level.isClientSide && this.random.nextInt(1000) == 0 && this.getTarget() != null && this.getHealth() < this.getMaxHealth() / 2.0f && this.master == null) {
                for (int j = 0; j < 16; ++j) {
                    this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(), 0.0, 0.0, 0.0);
                }
                this.playSound(TitanSounds.titanLand, 10000.0f, 1.0f);
                this.TransformEntity(this);
            }
            if (this.onGround) {
                this.hasImpulse = false;
            }
            final List list11 = this.level.getEntities(this, this.getBoundingBox().inflate(8.0, 8.0, 8.0));
            if (!this.level.isClientSide && list11 != null && !list11.isEmpty() && (this.tickCount + this.getId()) % ((this.getHealth() < this.getMaxHealth() / 2.0f) ? 40 : 160) == 0) {
                this.level.explode(this, this.getX(), this.getY(), this.getZ(), 8.0f, Explosion.Mode.NONE);
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity = (Entity) list11.get(i2);
                    if (entity != null && entity instanceof LivingEntity && this.canAttack((LivingEntity) entity)) {
                        final Entity entity3 = entity;
                        entity3.push(0.0, this.random.nextDouble(), 0.0);
                        //((LivingEntity) entity).addEffect(new EffectInstance(ClientProxy.electricJudgment.id, 10, 1));
                        this.level.levelEvent(null, 1017, new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()), 0);
                    }
                }
            }
        }
        if (this.getMinionTypeInt() == 2 && this.getTarget() != null) {
            final double d5 = this.distanceToSqr(this.getTarget());
            if (d5 < 0.8) {
                this.doHurtTarget(this.getTarget());
            }
            if (this.random.nextInt(40) == 0 && this.onGround && d5 < 256.0 && this.getTarget().getY() > this.getY() + 3.0) {
                this.addEffect(new EffectInstance(Effects.JUMP, 60, 7));
                this.lookAt(this.getTarget(), 180.0f, 180.0f);
                final double d6 = this.getTarget().getX() - this.getX();
                final double d7 = this.getTarget().getZ() - this.getZ();
                final float f2 = MathHelper.sqrt(d6 * d6 + d7 * d7);
                this.jumpFromGround();
                this.push(d6 / f2 * 0.75 * 0.75 + this.getDeltaMovement().x * 0.75, 0.0, d7 / f2 * 0.75 * 0.75 + this.getDeltaMovement().z * 0.75);
            }
        }
        if (this.getMinionTypeInt() == 1 && this.tickCount % 40 == 0 && this.entityToHeal != null) {
            if (this.entityToHeal.getHealth() < this.entityToHeal.getMaxHealth()) {
                this.swing(Hand.MAIN_HAND);
                this.lookAt(this.entityToHeal, 180.0f, (float) this.getMaxHeadXRot());
                this.entityToHeal.heal(4.0f);
                this.playSound(SoundEvents.WITHER_SHOOT, 1.0f, 3.0f);
            } else {
                this.entityToHeal = null;
            }
        }
        if (this.getTarget() != null && !this.getTarget().isAlive()) {
            this.setTarget(null);
        }
        if (this.master != null) {
            if (this.master.getTarget() != null && this.master instanceof EntityZombieTitan && ((EntityZombieTitan) this.master).animID == 11 && ((EntityZombieTitan) this.master).animTick > 80) {
                this.getMoveControl().setWantedPosition(this.master.getTarget().getX(), this.master.getTarget().getY(), this.master.getTarget().getZ(), 10.0);
            }
            if (this.master.getTarget() != null) {
                if (this.master.getTarget().getBbHeight() < 6.0f) {
                    this.setTarget(this.master.getTarget());
                } else {
                    this.getLookControl().setLookAt(this.master.getTarget(), 10.0f, 40.0f);
                }
            }
            if (this.tickCount % 10 == 0 && this.master.getTarget() != null && this.master.getTarget() instanceof VillagerEntity) {
                if (this.distanceToSqr(this.master.getTarget()) > 256.0) {
                    this.getMoveControl().setWantedPosition(this.master.getTarget().getX(), this.master.getTarget().getY(), this.master.getTarget().getZ(), 1.0);
                } else {
                    this.getNavigation().moveTo(this.master.getTarget(), 1.0);
                }
            } else if (this.distanceToSqr(this.master) > 2304.0) {
                this.getMoveControl().setWantedPosition(this.master.getX(), this.master.getY(), this.master.getZ(), 2.0);
            }
        } else {
            final List list12 = this.level.getEntities(this, this.getBoundingBox().inflate(100.0, 100.0, 100.0));
            if (list12 != null && !list12.isEmpty()) {
                for (int i3 = 0; i3 < list12.size(); ++i3) {
                    final Entity entity2 = (Entity) list12.get(i3);
                    if (entity2 != null && entity2 instanceof EntityZombieTitan) {
                        this.master = (MobEntity) entity2;
                    }
                }
            }
        }
    }

    @Override
    public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
        if (this.deathTicks > 0) {
            super.move(p_213315_1_, new Vector3d(0.0, 0.10000000149011612, 0.0));
        } else {
            super.move(p_213315_1_, p_213315_2_);
        }
    }

    @Override
    protected void tickDeath() {
        if (this.getMinionTypeInt() == 4) {
            --this.tickCount;
            ++this.deathTicks;
            if (this.master != null) {
                final double mx = this.getX() - this.master.getX();
                final double my = this.getY() + this.getEyeHeight() - (this.master.getY() + this.master.getEyeHeight());
                final double mz = this.getZ() - this.master.getZ();
                final short short1 = (short) (this.distanceTo(this.master) * 2.0f);
                for (int f = 0; f < short1; ++f) {
                    final double d9 = f / (short1 - 1.0);
                    final double d10 = this.getX() + mx * -d9;
                    final double d11 = this.getY() + this.getEyeHeight() + my * -d9;
                    final double d12 = this.getZ() + mz * -d9;
                    this.level.addParticle(ParticleTypes.FIREWORK, d10, d11, d12, this.master.getDeltaMovement().x, this.master.getDeltaMovement().y + 0.2, this.master.getDeltaMovement().z);
                }
            }
            if (!this.level.isClientSide) {
                if (this.deathTicks > 150 && this.deathTicks % 5 == 0) {
                    this.dropFewItems(true, 0);
                }
                if (this.deathTicks == 1) {
                    this.level.levelEvent(1018, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
                }
            }
            if (this.deathTicks >= 180 && this.deathTicks <= 200) {
                final float f2 = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
                final float f3 = (this.random.nextFloat() - 0.5f) * this.getBbHeight();
                final float f4 = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + f2, this.getY() + this.getEyeHeight() + f3, this.getZ() + f4, 0.0, 0.0, 0.0);
            }
            this.move(MoverType.SELF, new Vector3d(0.0, 0.10000000149011612, 0.0));
            final float f2 = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
            final float f3 = (this.random.nextFloat() - 0.5f) * this.getBbHeight();
            final float f4 = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
            this.level.addParticle(ParticleTypes.POOF, this.getX() + f2, this.getY() + this.getEyeHeight() + f3, this.getZ() + f4, 0.0, 0.0, 0.0);
            this.level.addParticle(ParticleTypes.LAVA, this.getX() + f2, this.getY() + this.getEyeHeight() + f3, this.getZ() + f4, this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian());
            if (this.deathTicks == 200 && !this.level.isClientSide) {
                if (this.master != null) {
                    this.master.heal(this.master.getMaxHealth() / 100.0f);
                    for (int i = 0; i < 100; ++i) {
                        final double d13 = this.random.nextGaussian() * 0.02;
                        final double d14 = this.random.nextGaussian() * 0.02;
                        final double d15 = this.random.nextGaussian() * 0.02;
                        this.level.addParticle(ParticleTypes.POOF, this.master.getX() + this.random.nextFloat() * this.master.getBbWidth() * 2.0f - this.master.getBbWidth(), this.master.getY() + this.random.nextFloat() * this.master.getBbHeight(), this.master.getZ() + this.random.nextFloat() * this.master.getBbWidth() * 2.0f - this.master.getBbWidth(), d13, d14, d15);
                    }
                }
                int i = this.xpReward;
                while (i > 0) {
                    final int j = ExperienceOrbEntity.getExperienceValue(i);
                    i -= j;
                    this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), j));
                }
                this.removeAfterChangingDimensions();
            }
        } else {
            super.tickDeath();
        }
    }

    @Override
    public void killed(ServerWorld p_241847_1_, LivingEntity p_70074_1_) {
        if (p_70074_1_ instanceof VillagerEntity) {
            final EntityZombieMinion entityzombie = new EntityZombieMinion(this.level);
            entityzombie.copyPosition(p_70074_1_);
            this.level.getChunk(p_70074_1_.xChunk, p_70074_1_.zChunk).removeEntity(p_70074_1_);
            entityzombie.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
            entityzombie.setMinionType(this.getMinionTypeInt());
            entityzombie.setVillager(true);
            if (p_70074_1_.isBaby()) {
                entityzombie.setBaby(true);
            }
            this.level.addFreshEntity(entityzombie);
            this.level.levelEvent(null, 1016, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
        }
    }

    public void TransformEntity(final Entity entity) {
        entity.level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 18.0f, true, entity.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
        final EntityZombieTitan entitytitan = new EntityZombieTitan(RenderTheTitans.zombieTitan, entity.level);
        entitytitan.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, 0.0f);
        entitytitan.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
        entity.remove();
        entitytitan.func_82206_m();
        entity.level.addFreshEntity(entitytitan);
        if (this.isBaby()) {
            entitytitan.setBaby(true);
        }
        if (this.isVillager()) {
            entitytitan.setVillager(true);
        }
    }

    public void performRangedAttack(final LivingEntity p_82196_1_, final float p_82196_2_) {
        this.swing(Hand.MAIN_HAND);
        if (this.distanceToSqr(p_82196_1_) < p_82196_1_.getBbWidth() * p_82196_1_.getBbWidth() + 36.0) {
            this.doHurtTarget(p_82196_1_);
        } else {
            switch (this.random.nextInt(4)) {
                case 0: {
                    final ArrowEntity entityarrow = new ArrowEntity(this.level, this);
                    entityarrow.setCritArrow(true);
                    entityarrow.setBaseDamage(p_82196_2_ * 2.0f + this.random.nextGaussian() * 0.25 + this.level.getDifficulty().getId() * 0.11f);
                    Vector3d look = this.getLookAngle();
                    entityarrow.shoot(look.x, look.y, look.z, 1.6f, 1.0f);
                    this.playSound(SoundEvents.ARROW_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
                    this.level.addFreshEntity(entityarrow);
                    break;
                }
                case 1: {
                    ItemStack potionStack = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HARMING);
                    PotionEntity potionEntity = new PotionEntity(level, this);
                    potionEntity.setItem(potionStack);

                    if (p_82196_1_.getMobType() == CreatureAttribute.UNDEAD) {
                        potionStack = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HEALING);
                        potionEntity.setItem(potionStack);
                    }

                    double d0 = p_82196_1_.getY() + p_82196_1_.getEyeHeight() - 1.1;
                    double d1 = p_82196_1_.getX() - this.getX();
                    double d2 = d0 - potionEntity.getY();
                    double d3 = p_82196_1_.getZ() - this.getZ();
                    float f = MathHelper.sqrt(d1 * d1 + d3 * d3);

                    potionEntity.shoot(d1, d2 + f * 0.2F, d3, 1.6F, 1.0F);
                    this.level.addFreshEntity(potionEntity);
                    break;
                }
                case 2: {
                    final double d5 = this.distanceToSqr(p_82196_1_);
                    final double d6 = p_82196_1_.getX() - this.getX();
                    final double d7 = p_82196_1_.getBoundingBox().minY + p_82196_1_.getBbHeight() / 2.0f - (this.getY() + p_82196_1_.getBbHeight() / 2.0f);
                    final double d8 = p_82196_1_.getZ() - this.getZ();
                    final float f2 = MathHelper.sqrt(MathHelper.sqrt(d5)) * 0.1f;
                    this.level.levelEvent(null, 1009, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
                    final SmallFireballEntity entitysmallfireball = new SmallFireballEntity(this.level, this, d6 + this.getRandom().nextGaussian() * f2, d7, d8 + this.getRandom().nextGaussian() * f2);
                    entitysmallfireball.setPos(entitysmallfireball.getX(), getY() + 1.6, entitysmallfireball.getZ());
                    this.level.addFreshEntity(entitysmallfireball);
                    break;
                }
                case 3: {
                    this.playSound(SoundEvents.ZOMBIE_HURT, 1.0f, 0.5f);
                    this.playSound(SoundEvents.ZOMBIE_HURT, 1.0f, 0.5f);
                    this.playSound(SoundEvents.ZOMBIE_HURT, 1.0f, 0.5f);
                    this.playSound(SoundEvents.ZOMBIE_HURT, 1.0f, 0.5f);
                    this.level.playSound(null, new BlockPos(p_82196_1_.getX(), p_82196_1_.getY(), p_82196_1_.getZ()), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0f, (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f);
                    for (int i = 0; i < 256; ++i) {
                        final ItemEntity entityitem = new ItemEntity(
                                p_82196_1_.level,
                                p_82196_1_.getX(),
                                p_82196_1_.getY(),
                                p_82196_1_.getZ(),
                                new ItemStack(Items.ROTTEN_FLESH, 1)
                        );
                        entityitem.setPickUpDelay(6000);
                        entityitem.lifespan = 40 + this.random.nextInt(20);
                    }
                    p_82196_1_.addEffect(new EffectInstance(Effects.HUNGER, 100, 2));
                    p_82196_1_.hurt(DamageSource.STARVE, 5.0f);
                    p_82196_1_.hurtDuration = 0;
                    break;
                }
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

    class GroupData implements ILivingEntityData {
        public boolean field_142048_a;
        public boolean field_142046_b;

        private GroupData(final boolean p_i2348_2_, final boolean p_i2348_3_) {
            this.field_142048_a = false;
            this.field_142046_b = false;
            this.field_142048_a = p_i2348_2_;
            this.field_142046_b = p_i2348_3_;
        }

        GroupData(final boolean p_i2349_2_, final boolean p_i2349_3_, final Object p_i2349_4_) {
            this(p_i2349_2_, p_i2349_3_);
        }
    }

    public class EntityAIFindEntityNearestInjuredAlly extends Goal {
        private final EntityZombieMinion field_179434_b;
        private LivingEntity field_179433_e;

        public EntityAIFindEntityNearestInjuredAlly(final EntityZombieMinion entityCaveSpiderPriest) {
            this.field_179434_b = entityCaveSpiderPriest;
        }

        public boolean canUse() {
            if (!this.field_179434_b.isAlive()) {
                return false;
            }
            if (this.field_179434_b.getMinionType() != EnumMinionType.PRIEST) {
                return false;
            }
            if (this.field_179433_e != null) {
                return false;
            }
            final double d0 = this.func_179431_f();
            final List list = this.field_179434_b.level.getEntitiesOfClass(EntityZombieMinion.class, this.field_179434_b.getBoundingBox().inflate(d0, d0, d0));
            if (list.isEmpty()) {
                return false;
            }
            for (int i = 0; i < list.size(); ++i) {
                final EntityZombieMinion entity = (EntityZombieMinion) list.get(i);
                if (entity.getHealth() < entity.getMaxHealth() && entity.isAlive()) {
                    this.field_179433_e = entity;
                }
            }
            return true;
        }

        public boolean continueExecuting() {
            final LivingEntity entitylivingbase = this.field_179434_b.entityToHeal;
            if (entitylivingbase == null) {
                return false;
            }
            if (!entitylivingbase.isAlive()) {
                this.field_179433_e = null;
                return false;
            }
            if (entitylivingbase.getHealth() >= entitylivingbase.getMaxHealth()) {
                this.field_179433_e = null;
                return false;
            }
            final double d0 = this.func_179431_f();
            return this.field_179434_b.distanceToSqr(entitylivingbase) <= d0 * d0;
        }

        public void start() {
            this.field_179434_b.entityToHeal = this.field_179433_e;
            super.start();
        }

        public void stop() {
            this.field_179434_b.entityToHeal = null;
            this.field_179433_e = null;
            super.stop();
        }

        public void updateTask() {
            if (this.field_179434_b.entityToHeal != null && this.field_179434_b.distanceTo(this.field_179434_b.entityToHeal) > 16.0) {
                this.field_179434_b.getNavigation().moveTo(this.field_179434_b.entityToHeal, 1.0);
                this.field_179434_b.getLookControl().setLookAt(this.field_179434_b.entityToHeal, 10.0f, (float) this.field_179434_b.getMaxHeadXRot());
            }
        }

        protected double func_179431_f() {
            return 32.0;
        }
    }




}
