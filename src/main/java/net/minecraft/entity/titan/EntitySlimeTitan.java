package net.minecraft.entity.titan;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.item.Item;
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
import net.minecraft.potion.Potion;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class EntitySlimeTitan extends EntityTitan {
    public static final DataParameter<Byte> t16 = EntityDataManager.defineId(EntitySlimeTitan.class, DataSerializers.BYTE);
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    public boolean doubleJumped;
    private boolean field_175452_bi;
    private int slimeJumpDelay;
    public EntitySlimeTitan(EntityType<? extends EntityTitan> type, World worldIn) {
        super(type, worldIn);
        this.setSize(8.0f * this.getSlimeSize(), 8.0f * this.getSlimeSize());
        this.slimeJumpDelay = 0;
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    @Override
    public boolean canAttack() {
        return true;
    }

    @Override
    protected void applyEntityAI() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        if (TitanConfig.TitansFFAMode) {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SlimeTitanSorter));
        } else {
            this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, ITitan.SearchForAThingToKill));
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        final LivingEntity entity = this.getTarget();
        if (this.onGround && this.slimeJumpDelay-- <= 0 && this.getInvulTime() <= 0) {
            this.slimeJumpDelay = this.getJumpDelay();
            if (entity != null) {
                this.slimeJumpDelay /= 3;
                this.getLookControl().setLookAt(entity, 180.0f, 60.0f);
            }
            this.jumpFromGround();
            if (this.makesSoundOnJump()) {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) * 0.8f);
            }
            this.zza = (float) (2 * this.getSlimeSize());
            this.travel(new Vector3d(this.xxa, this.yya, this.zza));
        } else {
            this.jumping = false;
            if (this.onGround) {
                final float n = 0.0f;
                this.zza = n;
                this.xxa = n;
            }
        }
    }

    @Override
    public boolean canAttack(Entity p_70686_1_) {
        return (p_70686_1_ instanceof MagmaCubeEntity || p_70686_1_ instanceof SlimeEntity) && (p_70686_1_ instanceof EntityMagmaCubeTitan || !(p_70686_1_ instanceof EntitySlimeTitan));
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.random.nextInt(20) == 0 && this.level.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public int getMinionSpawnRate() {
        return TitanConfig.SlimeTitanMinionSpawnrate;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(t16, (byte) 1);
    }

    @Override
    public int getParticleCount() {
        return 4;
    }

    @Override
    public BasicParticleType getParticles() {
        return this.func_180487_n();
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return EntityTitan.applyEntityAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    public int getSlimeSize() {
        return this.entityData.get(t16);
    }

    protected void setSlimeSize(final int p_70799_1_) {
        this.entityData.set(t16, (byte) p_70799_1_);
        this.setSize(8.0f * p_70799_1_, 8.0f * p_70799_1_);
        this.setPos(this.getX(), this.getY(), this.getZ());
        if (TitanConfig.NightmareMode) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(p_70799_1_ * 2000.0);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(p_70799_1_ * 1000.0);
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getAttackStrength());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5f + 0.1f * p_70799_1_);
        this.setTitanHealth(this.getMaxHealth());
        if (this instanceof EntityMagmaCubeTitan) {
            this.xpReward = p_70799_1_ * 3000;
        } else {
            this.xpReward = p_70799_1_ * 1000;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putInt("Size", this.getSlimeSize() - 1);
        tagCompound.putBoolean("wasOnGround", this.field_175452_bi);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        int i = tagCompund.getInt("Size");
        if (i < 0) {
            i = 0;
        }
        this.setSlimeSize(i + 1);
        this.field_175452_bi = tagCompund.getBoolean("wasOnGround");
    }

    protected BasicParticleType func_180487_n() {
        return ParticleTypes.ITEM_SLIME;
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.SLIME_JUMP;
    }

    protected BasicParticleType getSlimeParticle() {
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5f;
        this.prevSquishFactor = this.squishFactor;
        final boolean flag = this.onGround;
        super.move(p_213315_1_, p_213315_2_);
        if (this.onGround && !flag) {
            for (int i = this.getSlimeSize(), j = 0; j < i * 32; ++j) {
                final float f = this.random.nextFloat() * 3.1415927f * 2.0f;
                final float f2 = this.random.nextFloat() * 8.0f + 8.0f;
                final float f3 = MathHelper.sin(f) * i * 0.5f * f2;
                final float f4 = MathHelper.cos(f) * i * 0.5f * f2;
                this.level.addParticle(this.getSlimeParticle(), this.getX() + f3, this.getBoundingBox().minY, this.getZ() + f4, 0.0, 0.0, 0.0);
            }
            if (this.makesSoundOnLand()) {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            }
            this.squishAmount = -0.5f;
        } else if (!this.onGround && flag) {
            this.squishAmount = 1.0f;
        }
        this.alterSquishAmount();
    }

    @Override
    public boolean shouldMove() {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.yBodyRot = this.yRot;
        if (!this.isAlive()) {
            if (this instanceof EntityMagmaCubeTitan) {
                this.squishAmount = 1.0f;
            } else {
                this.squishAmount = -0.5f;
            }
        }
        this.destroyBlocksInAABB(this.getBoundingBox().move(this.getDeltaMovement().x, (this.getDeltaMovement().y > 0.0) ? this.getDeltaMovement().y : 0.0, this.getDeltaMovement().z));
        final List list = this.level.getEntities(this, this.getBoundingBox().inflate(0.1, 0.1, 0.1));
        if (list != null && !list.isEmpty() && this.isAlive()) {
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = (Entity) list.get(i);
                if (entity != null && this.canAttack(entity)) {
                    this.func_175451_e((LivingEntity) entity);
                }
            }
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getAttackStrength());
        this.meleeTitan = true;
        if (this.createProtoInstance() instanceof MagmaCubeEntity) {
            this.setCustomName(new TranslationTextComponent("entity.LavaSlimeTitan.name"));
            this.flyingSpeed = 0.75f;
            final List list2 = this.level.getEntities(this, this.getBoundingBox().inflate(128.0, 128.0, 128.0));
            if (list2 != null && !list2.isEmpty()) {
                for (int i2 = 0; i2 < list2.size(); ++i2) {
                    final Entity entity2 = (Entity) list2.get(i2);
                    if (entity2 != null && entity2 instanceof MagmaCubeEntity && this.getTarget() != null) {
                        ((MagmaCubeEntity) entity2).lookAt(this.getTarget(), 180.0f, 180.0f);
                        if (entity2.isOnGround() || entity2.isInWater() || entity2.isInLava()) {
                            entity2.setDeltaMovement(entity2.getDeltaMovement().x, 0.6f + ((MagmaCubeEntity) entity2).getSize() * 0.1f, entity2.getDeltaMovement().z);
                        }
                        ((MagmaCubeEntity) entity2).setZza(2.0f);
                    }
                }
            }
        } else {
            this.setCustomName(new TranslationTextComponent("entity.SlimeTitan.name"));
            this.flyingSpeed = 0.5f;
            final List list2 = this.level.getEntities(this, this.getBoundingBox().inflate(128.0, 128.0, 128.0));
            if (list2 != null && !list2.isEmpty()) {
                for (int i2 = 0; i2 < list2.size(); ++i2) {
                    final Entity entity2 = (Entity) list2.get(i2);
                    if (entity2 != null && entity2 instanceof SlimeEntity && !(entity2 instanceof MagmaCubeEntity) && this.getTarget() != null) {
                        ((SlimeEntity) entity2).lookAt(this.getTarget(), 180.0f, 180.0f);
                        if (entity2.isOnGround() || entity2.isInWater() || entity2.isInLava()) {
                            entity2.setDeltaMovement(entity2.getDeltaMovement().x, 0.5f, entity2.getDeltaMovement().z);
                        }
                        ((SlimeEntity) entity2).setZza(2.0f);
                    }
                }
            }
        }
        if (this.getTarget() != null) {
            this.lookAt(this.getTarget(), 180.0f, 40.0f);
        }
        if ((this.getTarget() == null && this.doubleJumped) || this.onGround) {
            this.doubleJumped = false;
        }
        if (this.getTarget() != null && !this.doubleJumped && this.random.nextInt(100) == 0 && !this.onGround) {
            this.squishAmount = 2.0f;
            this.jumpFromGround();
            this.doubleJumped = true;
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
        if (this.random.nextInt(this.getMinionSpawnRate()) == 0 && this.getHealth() > 0.0f && !this.level.isClientSide) {
            final SlimeEntity entitychicken = this.createProtoInstance();
            entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
            this.level.addFreshEntity(entitychicken);
            entitychicken.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 40, 4, false, true));
            entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
        }
        final int j = this.getSlimeSize();
        this.setSize(8.0f * j, 8.0f * j);
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.85f;
    }

    protected int getJumpDelay() {
        return this.random.nextInt(40) + 20;
    }

    protected EntitySlimeTitan createInstance() {
        return new EntitySlimeTitan(RenderTheTitans.slimeTitan, this.level);
    }

    protected SlimeEntity createProtoInstance() {
        return new SlimeEntity(EntityType.SLIME, this.level);
    }

    @Override
    public void push(Entity entityIn) {
        if (entityIn instanceof EntityTitan) {
            super.push(entityIn);
        }
        if (this.canAttack(entityIn)) {
            final int i = this.getSlimeSize();
            if (this.tickCount % 5 == 0 && this.isAlive()) {
                final float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
                final int i2 = this.getKnockbackAmount();
                final float rotationYawHead = this.yHeadRot;
                this.yRot = rotationYawHead;
                this.yBodyRot = rotationYawHead;
                this.attackChoosenEntity(entityIn, f, i2);
                if (!entityIn.isAlive() && !(entityIn instanceof EntityTitan)) {
                    this.playSound(SoundEvents.SLIME_ATTACK, 100.0f, 0.5f);
                    if (entityIn instanceof LivingEntity) {
                        this.heal(((LivingEntity) entityIn).getMaxHealth());
                    }
                    entityIn.removed = true;
                    if (!this.level.isClientSide) {
                        final SlimeEntity entitychicken = this.createProtoInstance();
                        entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
                        entitychicken.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 100, 4, false, true));
                        entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                        this.level.addFreshEntity(entitychicken);
                    }
                }
            }
        }
    }

    @Override
    public boolean canCollideWith(Entity p_241849_1_) {
        return super.canCollideWith(p_241849_1_);
    }

    protected void func_175451_e(final LivingEntity p_175451_1_) {
    }

    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return 0.625f * this.getBbHeight();
    }

    protected int getAttackStrength() {
        if (TitanConfig.NightmareMode) {
            return this.getSlimeSize() * 90;
        }
        return this.getSlimeSize() * 30;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    protected Item getDropItem() {
        return Items.SLIME_BALL;
    }

    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (this.deathTicks > 0) {
            for (int x = 0; x < this.getSlimeSize(); ++x) {
                final EntityXPBomb entitylargefireball = new EntityXPBomb(this.level, this.getX(), this.getY() + 4.0, this.getZ());
                entitylargefireball.setPos(this.getX(), this.getY() + 4.0, this.getZ());
                final EntityXPBomb entityXPBomb = entitylargefireball;
                entityXPBomb.push(0.0, 0.5, 0.0);
                entitylargefireball.setXPCount((this instanceof EntityMagmaCubeTitan) ? 2000 : 1000);
                this.level.addFreshEntity(entitylargefireball);
            }
            if (this.getSlimeSize() <= 1) {
                for (int l = 0; l < 64 + this.random.nextInt(64 + p_70628_2_) + p_70628_2_; ++l) {
                    final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(this.getDropItem()));
                    entityitem.setPickUpDelay(40);
                    this.level.addFreshEntity(entityitem);
                }
                for (int l = 0; l < 16 + this.random.nextInt(16 + p_70628_2_) + p_70628_2_; ++l) {
                    final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.COAL));
                    entityitem.setPickUpDelay(40);
                    this.level.addFreshEntity(entityitem);
                }
                for (int l = 0; l < this.random.nextInt(8 + p_70628_2_); ++l) {
                    final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.EMERALD));
                    entityitem.setPickUpDelay(40);
                    this.level.addFreshEntity(entityitem);
                }
                for (int l = 0; l < this.random.nextInt(8 + p_70628_2_); ++l) {
                    final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Items.DIAMOND));
                    entityitem.setPickUpDelay(40);
                    this.level.addFreshEntity(entityitem);
                }
                for (int l = 0; l < this.random.nextInt(4 + p_70628_2_); ++l) {
                    final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(TitanItems.harcadiumWaflet));
                    entityitem.setPickUpDelay(40);
                    this.level.addFreshEntity(entityitem);
                }
            }
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    public int getMaxHeadXRot() {
        return 30;
    }

    protected boolean makesSoundOnJump() {
        return true;
    }

    protected boolean makesSoundOnLand() {
        return true;
    }

    @Override
    protected void jumpFromGround() {
        this.setDeltaMovement(this.getDeltaMovement().x, 1.5 + this.getSlimeSize() * 0.2f, this.getDeltaMovement().z);
        this.hasImpulse = true;
        if (this.getTarget() != null) {
            final double d01 = this.getTarget().getX() - this.getX();
            final double d2 = this.getTarget().getZ() - this.getZ();
            final float f21 = MathHelper.sqrt(d01 * d01 + d2 * d2);
            final double hor = 1.5 + this.getSlimeSize() * 0.25f;
            this.setDeltaMovement(d01 / f21 * hor * hor + this.getDeltaMovement().x * hor, this.getDeltaMovement().y, d2 / f21 * hor * hor + this.getDeltaMovement().z * hor);
        }
    }

    @Override
    public boolean causeFallDamage(float p_70069_1_, float damageMultiplier) {
        this.onGround = true;
        this.hasImpulse = false;
        if (!this.level.isClientSide && this.random.nextInt(5) == 0 && this.getTarget() == null) {
            final float renderYawOffset = this.random.nextFloat() * 360.0f;
            this.yHeadRot = renderYawOffset;
            this.yRot = renderYawOffset;
            this.yBodyRot = renderYawOffset;
        }
        p_70069_1_ = ForgeHooks.onLivingFall(this, p_70069_1_, damageMultiplier)[0];
        if (p_70069_1_ <= 0.0f) {
            return false;
        }
        final EffectInstance potioneffect = this.getEffect(Effects.JUMP);
        final float f1 = (potioneffect != null) ? ((float) (potioneffect.getAmplifier() + 1)) : 0.0f;
        final int i = MathHelper.ceil(p_70069_1_ - 12.0f - f1);
        if (i > 0) {
            this.playSound(TitanSounds.titanSlam, 5.0f * this.getSlimeSize(), 2.0f - this.getSlimeSize() / 4);
            this.playSound(SoundEvents.SLIME_DEATH, 5.0f * this.getSlimeSize(), 2.0f - this.getSlimeSize() / 8);
            final List list11 = this.level.getEntities(this, this.getBoundingBox().inflate(6.0 * this.getSlimeSize(), 6.0 * this.getSlimeSize(), 6.0 * this.getSlimeSize()));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity = (Entity) list11.get(i2);
                    if (this.canAttack(entity) && this.distanceToSqr(entity) <= this.getBbWidth() * this.getBbWidth() + this.getBbWidth() * this.getBbWidth()) {
                        final float f2 = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
                        final int i3 = this.getKnockbackAmount();
                        this.attackChoosenEntity(entity, f2, i3);
                        final double d0 = this.getBoundingBox().minX + this.getBoundingBox().maxX;
                        final double d2 = this.getBoundingBox().minZ + this.getBoundingBox().maxZ;
                        final double d3 = entity.getX() - d0;
                        final double d4 = entity.getZ() - d2;
                        final double d5 = d3 * d3 + d4 * d4;
                        if (this.doubleJumped) {
                            entity.push(d3 / d5 * 16.0, 2.0, d4 / d5 * 16.0);
                        } else {
                            entity.push(d3 / d5 * 8.0, 1.0, d4 / d5 * 8.0);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        if (!this.level.isClientSide) {
            final SlimeEntity entitychicken = this.createProtoInstance();
            entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
            entitychicken.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 100, 4, false, true));
            entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            this.level.addFreshEntity(entitychicken);
        }
        if (this.isInvulnerable()) {
            return false;
        }
        Label_0126:
        {
            if (this instanceof EntityMagmaCubeTitan) {
                if (!(source.getEntity() instanceof EntityMagmaCubeTitan)) {
                    break Label_0126;
                }
            } else if (!(source.getEntity() instanceof EntitySlimeTitan) || source.getEntity() instanceof EntityMagmaCubeTitan) {
                break Label_0126;
            }
            return false;
        }
        final Entity entity = source.getEntity();
        if (entity instanceof LivingEntity && !this.isInvulnerable() && amount > 25.0f) {
            final List list = this.level.getEntities(this, this.getBoundingBox().inflate(100.0, 100.0, 100.0));
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = (Entity) list.get(i);
                Label_0267:
                {
                    if (this instanceof EntityMagmaCubeTitan) {
                        if (!(entity2 instanceof EntityMagmaCubeTitan)) {
                            break Label_0267;
                        }
                    } else if (!(entity2 instanceof EntitySlimeTitan) || entity2 instanceof EntityMagmaCubeTitan) {
                        break Label_0267;
                    }
                    final EntitySlimeTitan entitypigzombie = (EntitySlimeTitan) entity2;
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
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
        int i = this.random.nextInt(3);
        if (i < 2 && this.random.nextFloat() < 0.5f) {
            ++i;
        }
        final int j = 1 << i;
        this.setSlimeSize(j);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void inactDeathAction() {
        this.level.explode(this, this.getX(), this.getY() + 3.0, this.getZ(), 0.0f, false, Explosion.Mode.NONE);
        if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.dropFewItems(true, 0);
            this.dropEquipment();
            //this.dropRareDrop(1);
        }
        final int i = this.getSlimeSize();
        if (!this.level.isClientSide) {
            for (int i2 = 0; i2 < 8 * this.getSlimeSize() + this.level.random.nextInt(8 * this.getSlimeSize()); ++i2) {
                final SlimeEntity entitychicken = this.createProtoInstance();
                entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(entitychicken.blockPosition()), SpawnReason.SPAWNER, null, null);
                entitychicken.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 100, 4, false, true));
                entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                this.level.addFreshEntity(entitychicken);
            }
        }
        if (!this.level.isClientSide && i > 1) {
            for (int j = 2 + this.random.nextInt(3), k = 0; k < j; ++k) {
                final float f = (k % 2 - 0.5f) * i / 4.0f;
                final float f2 = (k / 2 - 0.5f) * i / 4.0f;
                final EntitySlimeTitan entityslime = this.createInstance();
                if (this.hasCustomName()) {
                    entityslime.setCustomName(this.getCustomName());
                }
                if (this.isPersistenceRequired()) {
                    entityslime.setPersistenceRequired();
                }
                entityslime.setSlimeSize(i / 2);
                entityslime.moveTo(this.getX() + f, this.getY() + 0.5, this.getZ() + f2, this.random.nextFloat() * 360.0f, 0.0f);
                this.level.addFreshEntity(entityslime);
            }
        }
    }
}
