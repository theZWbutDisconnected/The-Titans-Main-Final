package net.minecraft.entity.titan;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.titan.ai.EntityAINearestTargetTitan;
import net.minecraft.entity.titanminion.EntityWitherzillaMinion;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ProfileBanEntry;
import net.minecraft.tags.ITag;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanBlocks;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.theTitans.items.ItemUltimaBlade;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityWitherzilla extends EntityTitan implements IRangedAttackMob {
    private static final Predicate<Entity> attackEntitySelector;
    private static final DataParameter<Integer> t17 = EntityDataManager.defineId(EntityWitherzilla.class, DataSerializers.INT);
    private static final DataParameter<Integer> t18 = EntityDataManager.defineId(EntityWitherzilla.class, DataSerializers.INT);
    private static final DataParameter<Integer> t19 = EntityDataManager.defineId(EntityWitherzilla.class, DataSerializers.INT);

    static {
        attackEntitySelector = new Predicate<Entity>() {
            public boolean apply(final Entity p_180027_1_) {
                return !(p_180027_1_ instanceof EntityWitherzilla) && !(p_180027_1_ instanceof EntityTitanSpirit) && !(p_180027_1_ instanceof EntityWitherzillaMinion) && !(p_180027_1_ instanceof EntityWitherTurret) && !(p_180027_1_ instanceof EntityWitherTurretGround) && !(p_180027_1_ instanceof EntityWitherTurretMortar);
            }
        };
    }

    private final float[] field_82220_d;
    private final float[] field_82221_e;
    private final float[] field_82217_f;
    private final float[] field_82218_g;
    private final int[] field_82223_h;
    private final int[] field_82224_i;
    public int affectTicks;
    ArrayList allPlayerList;
    private int blockBreakCounter;
    private int attackTimer;
    private int omegacounter;
    private int livingSoundTime;

    public EntityWitherzilla(EntityType<? extends EntityTitan> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
        this.field_82220_d = new float[2];
        this.field_82221_e = new float[2];
        this.field_82217_f = new float[2];
        this.field_82218_g = new float[2];
        this.field_82223_h = new int[2];
        this.field_82224_i = new int[2];
        this.allPlayerList = Lists.newArrayList(this.level.players());
        this.setSize(64.0f, 224.0f);
        this.noPhysics = true;
        this.xpReward = 5000000;
        this.playSound(TitanSounds.witherzillaSpawn, Float.MAX_VALUE, 1.0f);
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return EntityTitan.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, Double.MAX_VALUE)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void applyEntityAI() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(0, new EntityAINearestTargetTitan(this, LivingEntity.class, 0, false, false, EntityWitherzilla.attackEntitySelector));
    }

    @Override
    public int getMinionCap() {
        return 1000;
    }

    @Override
    public boolean alreadyHasAName() {
        return true;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float p_70069_1_, float damageMultiplier) {
        return false;
    }

    @Override
    public int getMinionSpawnRate() {
        return TitanConfig.WitherzillaMinionSpawnrate;
    }

    @Override
    public int getParticleCount() {
        return 100;
    }

    @Override
    public EnumTitanStatus getTitanStatus() {
        return EnumTitanStatus.GOD;
    }

    @Override
    public float getRenderSizeModifier() {
        return this.isInOmegaForm() ? 128.0f : 64.0f;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(t17, 0);
        entityData.define(t18, 0);
        entityData.define(t19, 0);
    }

    public int getTalkInterval2() {
        return 20;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.level.getProfiler().push("zillaBaseTick");
        if (this.isArmored() && this.isAlive() && this.random.nextInt(50) < this.livingSoundTime++) {
            this.livingSoundTime = -30;
            this.playLivingSound2();
        }
        this.level.getProfiler().pop();
    }

    public void playLivingSound2() {
        final SoundEvent s = this.getAmbientSound();
        if (s != null) {
            this.playSound(s, this.getSoundVolume(), 0.9f);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TitanSounds.witherzillaLiving;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return TitanSounds.witherzillaGrunt;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TitanSounds.witherzillaDeath;
    }

    @Override
    public boolean canBeHurtByPlayer() {
        return this.level.getDifficulty() != Difficulty.PEACEFUL && !this.isInvulnerable();
    }

    private void doLightningAttackTo(Entity entity) {
        if (entity != null && entity.isAlive() && !(entity instanceof EntityTitanPart)) {
            if (entity instanceof PlayerEntity) {
                entity.sendMessage(new StringTextComponent("\u00A7k" + this.random.nextInt(1234567890)), this.getUUID());
            }
            if (entity instanceof LivingEntity && !(entity instanceof EntityTitan) && (entity.getBbHeight() >= 6.0f || entity.isInvulnerable() || entity instanceof EntityEnderColossusCrystal)) {
                ((LivingEntity) entity).die(DamageSource.OUT_OF_WORLD);
                entity.remove();
                ((LivingEntity) entity).setHealth(0.0f);
                entity.hurt(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
            }
            if (entity != null && entity instanceof LivingEntity && (this.affectTicks >= 600 || entity.getBbHeight() >= 6.0f) && !(entity instanceof EntityTitan)) {
                ((LivingEntity) entity).setHealth(0.0f);
                for (int i = 0; i < 50; ++i) {
                    this.attackChoosenEntity(entity, 2.14748365E9f, 0);
                }
            }
            if (entity instanceof EntityTitan) {
                ((EntityTitan) entity).setInvulTime(((EntityTitan) entity).getInvulTime() - 20);
            } else {
                entity.push(0.0, 0.5, 0.0);
            }
            this.attackChoosenEntity(entity, 20.0f, 0);
            if (entity instanceof MobEntity) {
                ((LivingEntity) entity).setLastHurtByMob(this);
            }
            if (this.random.nextInt(5) == 0) {
                this.level.addFreshEntity(new EntityGammaLightning(this.level, this.getX(), this.getY() + this.random.nextFloat() * this.getEyeHeight(), this.getZ(), this.random.nextFloat(), this.random.nextFloat(), this.random.nextFloat()));
                this.level.addFreshEntity(new EntityGammaLightning(this.level, entity.getX(), entity.getY(), entity.getZ(), this.random.nextFloat(), this.random.nextFloat(), this.random.nextFloat()));
            }
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).hurtDuration = 1;
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity p_70652_1_) {
        if (p_70652_1_.getBbHeight() >= 6.0f || p_70652_1_ instanceof EntityTitan) {
            final float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.getKnockbackAmount();
            for (int it = 0; it < 10; ++it) {
                this.attackChoosenEntity(p_70652_1_, f, 2);
                if (p_70652_1_ instanceof EntityTitan && ((EntityTitan) p_70652_1_).getInvulTime() > 1) {
                    ((EntityTitan) p_70652_1_).setInvulTime(((EntityTitan) p_70652_1_).getInvulTime() - 20);
                }
            }
            return super.doHurtTarget(p_70652_1_);
        }
        return false;
    }

    @Override
    protected void outOfWorld() {
        this.level.levelEvent(1013, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
        this.teleportRandomly(this.level.dimension() == TheTitans.voidLand || this.level.dimension().getRegistryName() == new ResourceLocation("end"));
    }

    @Override
    public void setTarget(LivingEntity p_70624_1_) {
        if (this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void") && p_70624_1_ != null && this.getX() == 0.0 && this.getY() == 200.0 && this.getZ() == 0.0) {
            p_70624_1_ = null;
        } else {
            super.setTarget(p_70624_1_);
        }
    }

    @Override
    public void aiStep() {
        if (tickCount < 10) {
            this.entityData.set(DATA_HEALTH_ID, this.getMaxHealth());
            this.entityData.set(t5, this.getMaxHealth());
            this.entityData.set(t6, this.getMaxHealth());
        }
        if (!(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
            if (!this.level.isClientSide) {
                final ServerWorld worldinfo = this.level.getServer().getLevel(this.level.dimension());
                if (this.getTarget() != null && this.getTarget() instanceof EntityEnderColossus) {
                    worldinfo.setRainLevel(0);
                    worldinfo.setThunderLevel(0);
                } else {
                    worldinfo.setRainLevel(9999999);
                    worldinfo.setThunderLevel(1000000);
                }
            }
        }
        if (!(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
            if (this.random.nextInt(2) == 0) {
                for (int l = 0; l < 2/*0*/; ++l) {
                    final int i = MathHelper.floor(this.getX());
                    final int j = MathHelper.floor(this.getY());
                    final int k = MathHelper.floor(this.getZ());
                    final int i2 = i + MathHelper.nextInt(this.random, 10, 100) * MathHelper.nextInt(this.random, -1, 1);
                    final int j2 = j + MathHelper.nextInt(this.random, 10, 100) * MathHelper.nextInt(this.random, -1, 1);
                    final int k2 = k + MathHelper.nextInt(this.random, 10, 100) * MathHelper.nextInt(this.random, -1, 1);
                    final EntityGammaLightning entitylightning = new EntityGammaLightning(this.level, i2, j2, k2, this.random.nextFloat(), this.random.nextFloat(), this.random.nextFloat());
                    boolean hasSolidTopSurface = this.level.getBlockState(new BlockPos(i2, j2 - 1, k2)).isSolidRender(this.level, new BlockPos(i2, j2 - 1, k2));
                    if (this.random.nextInt(5) == 0 && hasSolidTopSurface && this.level.getEntitiesOfClass(Entity.class, entitylightning.getBoundingBox()).isEmpty()) {
                        this.level.addFreshEntity(entitylightning);
                    }
                }
            }
        } else {
            if (this.distanceToSqr(0.0, 200.0, 0.0) > 50000.0) {
                this.setPos(0.0, 200.0, 0.0);
            }
            final ArrayList listp = Lists.newArrayList(this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1000.0)));
            if (!this.level.isClientSide && listp != null && !listp.isEmpty() && this.random.nextInt(4) == 0) {
                for (int i3 = 0; i3 < listp.size(); ++i3) {
                    final Entity entity = (Entity) listp.get(i3);
                    if (entity != null && entity.isAlive()) {
                        if (entity instanceof EntityWitherTurret && !((EntityWitherTurret) entity).isPlayerCreated()) {
                            this.setPos(0.0, 200.0, 0.0);
                            final float renderYawOffset = 0.0f;
                            this.yHeadRot = renderYawOffset;
                            this.yRot = renderYawOffset;
                            this.yBodyRot = renderYawOffset;
                            this.setTarget(null);
                        }
                        if (entity instanceof EntityWitherTurretGround && !((EntityWitherTurretGround) entity).isPlayerCreated()) {
                            this.setPos(0.0, 200.0, 0.0);
                            final float renderYawOffset2 = 0.0f;
                            this.yHeadRot = renderYawOffset2;
                            this.yRot = renderYawOffset2;
                            this.yBodyRot = renderYawOffset2;
                            this.setTarget(null);
                        }
                        if (entity instanceof EntityWitherTurretMortar && !((EntityWitherTurretMortar) entity).isPlayerCreated()) {
                            this.setPos(0.0, 200.0, 0.0);
                            final float renderYawOffset3 = 0.0f;
                            this.yHeadRot = renderYawOffset3;
                            this.yRot = renderYawOffset3;
                            this.yBodyRot = renderYawOffset3;
                            this.setTarget(null);
                        }
                    }
                }
            }
        }
        this.setInvisible(this.affectTicks >= 800);
        --this.omegacounter;
        this.setSize((this.omegacounter > 0) ? 128.0f : 64.0f, (this.omegacounter > 0) ? 448.0f : 224.0f);
        if (!this.level.isClientSide) {
            this.level.getServer().getLevel(this.level.dimension()).setDayTime(18000L);
        }
        final ArrayList listp = Lists.newArrayList(this.level.players());
        if (listp != null && !listp.isEmpty() && this.random.nextInt(4) == 0) {
            for (int i3 = 0; i3 < listp.size(); ++i3) {
                final Entity entity = (Entity) listp.get(i3);
                if (entity != null && entity instanceof PlayerEntity && this.random.nextInt(100) == 0) {
                    this.playAmbientSound();
                    if (!(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
                        entity.hurt(new DamageSource("generic").bypassInvul().bypassArmor().bypassMagic(), 10.0f);
                        entity.sendMessage(new TranslationTextComponent("dialog.witherzilla.taunt." + this.random.nextInt(6)), this.getUUID());
                    } else {
                        entity.sendMessage(new TranslationTextComponent("dialog.witherzilla.plead." + this.random.nextInt(6)), this.getUUID());
                    }
                }
            }
        }
        final List list = this.level.getLoadedEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1000.0D));
        if (list != null && !list.isEmpty() && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
            for (int i4 = 0; i4 < list.size(); ++i4) {
                final Entity entity2 = (Entity) list.get(i4);
                if (entity2 != null && entity2.isAlive() && this.getTarget() != null && entity2 instanceof EntityWitherzillaMinion) {
                    ((EntityWitherzillaMinion) entity2).setTarget(this.getTarget());
                }
                if (entity2 != null && entity2.isAlive() && !(entity2 instanceof EntityWitherzillaMinion) && !(entity2 instanceof WitherSkullEntity) && !(entity2 instanceof EntityWitherTurret) && !(entity2 instanceof EntityWitherTurretGround) && !(entity2 instanceof EntityWitherTurretMortar) && !(entity2 instanceof EntityWitherzillaMinion) && !(entity2 instanceof EntityTitan) && !(entity2 instanceof EntityTitanSpirit) && !(entity2 instanceof PlayerEntity)) {
                    if (this.getInvulTime() > 1) {
                        if (entity2 instanceof LivingEntity) {
                            if (entity2 instanceof MobEntity) {
                                ((MobEntity) entity2).getNavigation().recomputePath();
                            }
                            entity2.setDeltaMovement(entity2.getDeltaMovement().x, 0.05 - this.getDeltaMovement().y * 0.2, entity2.getDeltaMovement().z);
                            ((LivingEntity) entity2).hurtDuration = (int) this.random.nextGaussian() * 20;
                            ((LivingEntity) entity2).zza = (float) this.random.nextGaussian();
                            ((LivingEntity) entity2).xxa = (float) this.random.nextGaussian();
                            final LivingEntity entityLivingBase = (LivingEntity) entity2;
                            final LivingEntity entityLivingBase2 = (LivingEntity) entity2;
                            final LivingEntity entityLivingBase3 = (LivingEntity) entity2;
                            final float renderYawOffset4 = entityLivingBase3.yHeadRot + (float) this.random.nextGaussian() * 10.0f;
                            entityLivingBase3.yHeadRot = renderYawOffset4;
                            entityLivingBase2.yRot = renderYawOffset4;
                            entityLivingBase.yBodyRot = renderYawOffset4;
                        } else {
                            entity2.setDeltaMovement(this.random.nextGaussian() * 0.5, this.random.nextGaussian() * 0.5, this.random.nextGaussian() * 0.5);
                            final Entity entity4 = entity2;
                            entity4.yRot += 10.0f;
                            entity2.hurtMarked = true;
                        }
                    } else {
                        this.doLightningAttackTo(entity2);
                    }
                }
            }
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Double.MAX_VALUE);
        if (TitanConfig.NightmareMode) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(2.0E7 + this.getExtraPower() * 2.0E7);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0E7 + this.getExtraPower() * 1.0E7);
        }
        if (this.tickCount % 3 + this.random.nextInt(3) == 0 || this.getInvulTime() >= 1000) {
            this.setCustomName(new StringTextComponent("\u00A7kRegnator"));
        } else {
            this.setCustomName(new StringTextComponent("\u00A7l" + new TranslationTextComponent("entity.WitherBossTitan.name").getString()));
        }
        if (this.getTarget() != null && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void")) && (this.affectTicks >= 600 || (this.getTarget().getBbHeight() > 6.0f && !(this.getTarget() instanceof EntityTitan)))) {
            this.doLightningAttackTo(this.getTarget());
            if (this.isInOmegaForm()) {
                for (int i4 = 0; i4 < 19; ++i4) {
                    this.doLightningAttackTo(this.getTarget());
                    this.getTarget().hurt(DamageSource.OUT_OF_WORLD, 1.0f);
                }
            }
        }
        ++this.affectTicks;
        if (this.affectTicks >= 1010) {
            this.affectTicks = 0;
        }
        if (this.getTarget() != null && !this.getTarget().isAlive()) {
            this.setTarget(null);
        }
        if (this.numMinions < this.getMinionCap() && this.random.nextInt(this.getMinionSpawnRate()) == 0 && this.getHealth() > 0.0f && !this.level.isClientSide) {
            final EntityWitherzillaMinion entitychicken = new EntityWitherzillaMinion(EntityType.WITHER, this.level);
            entitychicken.moveTo(this.getX(), this.getY() + this.getEyeHeight(), this.getZ(), this.yRot, 0.0f);
            entitychicken.func_82206_m();
            this.level.addFreshEntity(entitychicken);
            entitychicken.push(-MathHelper.sin(this.yHeadRot * 3.1415927f / 180.0f) * 3.0f, 0.0, MathHelper.cos(this.yHeadRot * 3.1415927f / 180.0f) * 3.0f);
            ++this.numMinions;
        }
        if (this.random.nextInt(120) == 0 && this.getTarget() != null && !this.level.isClientSide) {
            if (TitanConfig.NightmareMode) {
                this.level.explode(this, this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 14.0f, true, Explosion.Mode.BREAK);
            } else {
                this.level.explode(this, this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 7.0f, false, this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
            }
        }
        if (!this.isArmored()) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.1, this.getDeltaMovement().z);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.9, this.getDeltaMovement().z);
        }
        if (!this.level.isClientSide && this.getWatchedTargetId(0) > 0) {
            final Entity entity = this.level.getEntity(this.getWatchedTargetId(0));
            if (entity != null) {
                final double d0 = entity.getX() - this.getX();
                final double d2 = entity.getY() + (this.isArmored() ? 2.0 : 12.0) - this.getY();
                final double d3 = entity.getZ() - this.getZ();
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                this.getLookControl().setLookAt(entity, 180.0f, 40.0f);
                if (entity instanceof LivingEntity && d4 < 10000.0) {
                    this.performRangedAttack((LivingEntity) entity, 0.0f);
                }
                if (d4 > 64.0) {
                    final double d5 = MathHelper.sqrt(d4);
                    this.pushSelf(d0 / d5 * 1.5 - this.getDeltaMovement().x, d2 / d5 * 1.5 - this.getDeltaMovement().y, d3 / d5 * 1.5 - this.getDeltaMovement().z);
                    final float n = (float) Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 57.295776f - 90.0f;
                    this.yRot = n;
                    this.yBodyRot = n;
                }
            }
        }
        super.aiStep();
        for (int m = 0; m < 2; ++m) {
            this.field_82218_g[m] = this.field_82221_e[m];
            this.field_82217_f[m] = this.field_82220_d[m];
        }
        for (int m = 0; m < 2; ++m) {
            int j3 = this.getWatchedTargetId(m + 1);
            Entity entity3 = null;
            if (j3 > 0) {
                entity3 = this.level.getEntity(j3);
            }
            if (entity3 != null) {
                final double d2 = this.func_82214_u(m + 1);
                final double d6 = this.func_82208_v(m + 1);
                final double d7 = this.func_82213_w(m + 1);
                final double d8 = entity3.getX() - d2;
                final double d9 = entity3.getY() + entity3.getEyeHeight() - d6;
                final double d10 = entity3.getZ() - d7;
                final double d11 = MathHelper.sqrt(d8 * d8 + d10 * d10);
                final float f = (float) (Math.atan2(d10, d8) * 180.0 / 3.141592653589793) - 90.0f;
                final float f2 = (float) (-(Math.atan2(d9, d11) * 180.0 / 3.141592653589793));
                this.field_82220_d[m] = this.func_82204_b(this.field_82220_d[m], f2, 5.0f);
                this.field_82221_e[m] = this.func_82204_b(this.field_82221_e[m], f, 5.0f);
            } else {
                if (this.random.nextInt(120) == 0) {
                    for (j3 = 0; j3 < 36; ++j3) {
                        this.field_82220_d[m] = this.func_82204_b(this.field_82220_d[m], this.random.nextFloat() * 360.0f - 180.0f, 5.0f);
                    }
                }
                if (this.random.nextInt(120) == 0) {
                    for (j3 = 0; j3 < 36; ++j3) {
                        this.field_82221_e[m] = this.func_82204_b(this.field_82221_e[m], this.random.nextFloat() * 360.0f - 180.0f, 5.0f);
                    }
                }
            }
        }
        this.shouldParticlesBeUpward = false;
        final boolean flag = this.isArmored();
        for (int j3 = 0; j3 < 3; ++j3) {
            final double d12 = this.func_82214_u(j3);
            final double d13 = this.func_82208_v(j3);
            final double d14 = this.func_82213_w(j3);
            for (int j4 = 0; j4 < 15; ++j4) {
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, d12 + this.random.nextGaussian() * 32.0, d13 + this.random.nextGaussian() * 32.0, d14 + this.random.nextGaussian() * 32.0, 0.0, 0.0, 0.0);
            }
            if (flag && this.level.random.nextInt(4) == 0) {
                this.level.addParticle(ParticleTypes.EFFECT, d12 + this.random.nextGaussian() * 32.0, d13 + this.random.nextGaussian() * 32.0, d14 + this.random.nextGaussian() * 32.0, 0.699999988079071, 0.699999988079071, 0.5);
            }
        }
        if (this.getInvulTime() > 0) {
            for (int j3 = 0; j3 < 3; ++j3) {
                this.level.addParticle(ParticleTypes.EFFECT, this.getX() + this.random.nextGaussian() * 32.0, this.getY() + this.random.nextFloat() * 210.0f, this.getZ() + this.random.nextGaussian() * 32.0, 0.699999988079071, 0.699999988079071, 0.8999999761581421);
            }
        }
        if (this.level.isClientSide) {
            for (int i5 = 0; i5 < this.getParticleCount(); ++i5) {
                this.level.addParticle(this.getParticles(), this.getX() + (this.random.nextDouble() - 0.5) * (this.getBbWidth() * 3.0), this.getY() + this.random.nextDouble() * 210.0, this.getZ() + (this.random.nextDouble() - 0.5) * (this.getBbWidth() * 3.0), 0.0, 0.5, 0.0);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        --this.attackTimer;
        if (this.getInvulTime() > 0) {
            if (0 == 0) {
                this.heal(this.getMaxHealth() / 800.0f);
            }
            final int i = this.getInvulTime() - 1;
            this.setInvulTime(i);
            if (i <= 0) {
                this.blockBreakCounter = 1;
                this.level.levelEvent(1013, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
                if (this.allPlayerList != null && !this.allPlayerList.isEmpty()) {
                    for (int i2 = 0; i2 < this.allPlayerList.size(); ++i2) {
                        final Entity entity = (Entity) this.allPlayerList.get(i2);
                        if (entity instanceof PlayerEntity) {
                            this.level.playSound(null, new BlockPos(entity.getX(), entity.getY(), entity.getZ()), TitanSounds.witherzillaSpawn, SoundCategory.MASTER, 10.0f, 1.0f);
                        }
                    }
                }
            }
        } else {
            super.customServerAiStep();
            if (!(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
                this.omegacounter = 600;
            }
            if (this.tickCount % 400 == 0 && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
                final PlayerEntity entity2 = this.level.getNearestPlayer(this, -1.0);
                if (this.allPlayerList != null && !this.allPlayerList.isEmpty() && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void")) && this.getTarget() == null && this.level.dimension().getRegistryName() == entity2.level.dimension().getRegistryName()) {
                    for (int i2 = 0; i2 < this.allPlayerList.size(); ++i2) {
                        this.teleportToEntity(entity2, true);
                        if (!entity2.abilities.invulnerable) {
                            this.setTarget(entity2);
                        }
                    }
                }
                if (this.deathTicks <= 0 && this.allPlayerList != null && !this.allPlayerList.isEmpty() && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void")) && this.random.nextInt(20) == 0 && this.getTarget() != null && this.getTarget() == entity2 && this.isArmored()) {
                    for (int i2 = 0; i2 < this.allPlayerList.size(); ++i2) {
                        if (!this.level.isClientSide) {
                            final MinecraftServer minecraftserver = this.level.getServer();
                            final GameProfile gameprofile = minecraftserver.getProfileCache().get(entity2.getName().getString());
                            final ServerPlayerEntity entityplayermp = minecraftserver.getPlayerList().getPlayerByName(entity2.getName().getString());
                            if (entityplayermp != null && entity2.getName().getString() != "Umbrella_Ghast") {
                                this.attackChoosenEntity(entity2, 2.14748365E9f, 0);
                                entity2.remove();
                                final ProfileBanEntry userlistbansentry = new ProfileBanEntry(gameprofile, null, entity2.getName().getString(), null, null);
                                minecraftserver.getPlayerList().getBans().add(userlistbansentry);
                                entityplayermp.connection.disconnect(new StringTextComponent("You've been banned from this server by Witherzilla for being annoying."));
                            }
                        }
                    }
                }
            }
            if (this.getTarget() != null && this.canAttack() && this.getTarget() instanceof LivingEntity) {
                final double d0 = this.distanceToSqr(this.getTarget());
                if (d0 < this.getBbWidth() * this.getBbWidth() + this.getTarget().getBbWidth() * this.getTarget().getBbWidth() + 6000.0) {
                    this.swing(Hand.MAIN_HAND);
                    this.doHurtTarget(this.getTarget());
                }
            }
            if (this.getTarget() != null) {
                final ArrayList listp = Lists.newArrayList(this.level.players());
                if (listp != null && !listp.isEmpty()) {
                    for (int i2 = 0; i2 < listp.size(); ++i2) {
                        final Entity entity = (Entity) listp.get(i2);
                        if (entity != null && entity instanceof PlayerEntity && this.getTarget() instanceof EntityWitherzilla) {
                            entity.sendMessage(new StringTextComponent("\u00A7l\u00A7kRegnator: There's another me. This is a paradox!"), this.getUUID());
                        }
                    }
                }
            }
            if (this.getY() <= 0.0) {
                this.teleportRandomly(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void") || this.level.dimension().getRegistryName() == new ResourceLocation("the_end"));
            }
            final List list = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1000.0));
            if (list != null && !list.isEmpty()) {
                for (int i2 = 0; i2 < list.size(); ++i2) {
                    final Entity entity = (Entity) list.get(i2);
                    if (!(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void")) && entity != null && entity instanceof PlayerEntity && ((PlayerEntity) entity).getMainHandItem() != ItemStack.EMPTY && ((PlayerEntity) entity).getMainHandItem().getItem() != TitanItems.ultimaBlade) {
                        ((PlayerEntity) entity).getMainHandItem().setDamageValue(((PlayerEntity) entity).getMainHandItem().getMaxDamage());
                        ((PlayerEntity) entity).getMainHandItem().setCount(0);
                        ((PlayerEntity) entity).getMainHandItem().getStack().shrink(((PlayerEntity) entity).getMainHandItem().getStack().getCount());
                        this.doLightningAttackTo(entity);
                    }
                    if (entity instanceof AgeableEntity || entity instanceof AmbientEntity || entity instanceof WaterMobEntity) {
                        list.remove(entity);
                    }
                }
            }
            for (int j = 1; j < 3; ++j) {
                if (this.tickCount >= this.field_82223_h[j - 1] && this.getTarget() != null) {
                    this.field_82223_h[j - 1] = this.tickCount + this.random.nextInt(20);
                    final int k2 = j - 1;
                    final int l2 = this.field_82224_i[j - 1];
                    this.field_82224_i[k2] = this.field_82224_i[j - 1] + 1;
                    if (l2 > 15) {
                        for (int i3 = 0; i3 < 100; ++i3) {
                            final float f = 100.0f;
                            final float f2 = 20.0f;
                            final double d2 = MathHelper.nextInt(this.random, (int) (this.getX() - f), (int) (this.getX() + f));
                            final double d3 = MathHelper.nextInt(this.random, (int) (this.getY() - f2), (int) (this.getY() + f2));
                            final double d4 = MathHelper.nextInt(this.random, (int) (this.getZ() - f), (int) (this.getZ() + f));
                            this.launchWitherSkullToCoords(j + 1, d2, d3, d4, true);
                        }
                        this.field_82224_i[j - 1] = 0;
                    }
                    final int i4 = this.getWatchedTargetId(j);
                    if (i4 > 0) {
                        final Entity entity3 = this.level.getEntity(i4);
                        if (entity3 != null && entity3.isAlive()) {
                            this.launchWitherSkullToEntity(j + 1, (LivingEntity) entity3);
                            this.field_82223_h[j - 1] = this.tickCount;
                            this.field_82224_i[j - 1] = 0;
                        } else {
                            this.func_82211_c(j, 0);
                        }
                    } else if (this.getTarget() != null && this.getTarget().isAlive()) {
                        this.func_82211_c(j, this.getTarget().getId());
                    } else {
                        this.func_82211_c(j, 0);
                    }
                }
            }
            if (this.getTarget() != null) {
                this.func_82211_c(0, this.getTarget().getId());
            } else {
                this.func_82211_c(0, 0);
            }
            if (this.blockBreakCounter > 0) {
                --this.blockBreakCounter;
                if (this.blockBreakCounter == 0 && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    final int j = MathHelper.floor(this.getY());
                    final int i5 = MathHelper.floor(this.getX());
                    final int j2 = MathHelper.floor(this.getZ());
                    boolean flag = false;
                    for (int l3 = -32; l3 <= 32; ++l3) {
                        for (int i6 = -32; i6 <= 32; ++i6) {
                            for (int m = -32; m <= 246; ++m) {
                                final int j3 = i5 + l3;
                                final int k3 = j + m;
                                final int l4 = j2 + i6;
                                final Block block = this.level.getBlockState(new BlockPos(j3, k3, l4)).getBlock();
                                if (!block.defaultBlockState().isAir(this.level, new BlockPos(j3, k3, l4)) && block.getExplosionResistance() != -1.0f) {
                                    flag = (this.level.removeBlock(new BlockPos(j3, k3, l4), false) || flag);
                                }
                            }
                        }
                    }
                    if (flag) {
                        this.destroyBlocksInAABB(this.getBoundingBox());
                        this.level.levelEvent(null, 1012, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
                    }
                }
            }
        }
    }

    @Override
    public void kill() {
        final ArrayList list11 = Lists.newArrayList(this.level.players());
        if (list11 != null && !list11.isEmpty()) {
            for (int i1 = 0; i1 < list11.size(); ++i1) {
                final Entity entity = (Entity) list11.get(i1);
                if (entity != null && entity instanceof PlayerEntity) {
                    entity.sendMessage(new TranslationTextComponent("dialog.witherzilla.killattempt"), this.getUUID());
                }
            }
        }
    }

    private double func_82214_u(final int p_82214_1_) {
        return this.getX();
    }

    private double func_82208_v(final int p_82208_1_) {
        return this.getY() + 12.0;
    }

    private double func_82213_w(final int p_82213_1_) {
        return this.getZ();
    }

    private float func_82204_b(final float p_82204_1_, final float p_82204_2_, final float p_82204_3_) {
        float f3 = MathHelper.wrapDegrees(p_82204_2_ - p_82204_1_);
        if (f3 > p_82204_3_) {
            f3 = p_82204_3_;
        }
        if (f3 < -p_82204_3_) {
            f3 = -p_82204_3_;
        }
        return p_82204_1_ + f3;
    }

    private void launchWitherSkullToEntity(final int p_82216_1_, final LivingEntity p_82216_2_) {
        if (p_82216_2_ instanceof EntityTitan || p_82216_2_.getBbHeight() >= 6.0f) {
            final double d0 = this.distanceToSqr(p_82216_2_);
            if (d0 < 1000.0 && this.attackTimer <= 0) {
                this.attackTimer = 10;
                this.doHurtTarget(p_82216_2_);
            }
        } else {
            this.launchWitherSkullToCoords(p_82216_1_, p_82216_2_.getX(), p_82216_2_.getY() + p_82216_2_.getEyeHeight() * 0.5, p_82216_2_.getZ(), p_82216_1_ == 0 && this.random.nextFloat() < 0.001f);
            p_82216_2_.hurt(DamageSource.mobAttack(this), 21.0f);
            p_82216_2_.hurtDuration = 0;
        }
        this.launchWitherSkullToCoords(p_82216_1_, p_82216_2_.getX(), p_82216_2_.getY() + p_82216_2_.getEyeHeight() * 0.5, p_82216_2_.getZ(), p_82216_1_ == 0 && this.random.nextFloat() < 0.001f);
    }

    private void launchWitherSkullToCoords(final int p_82209_1_, final double p_82209_2_, final double p_82209_4_, final double p_82209_6_, final boolean p_82209_8_) {
        final double d3 = this.func_82214_u(p_82209_1_);
        final double d4 = this.func_82208_v(p_82209_1_);
        final double d5 = this.func_82213_w(p_82209_1_);
        final double d6 = p_82209_2_ - d3;
        final double d7 = p_82209_4_ - d4;
        final double d8 = p_82209_6_ - d5;
        final WitherSkullEntity entitywitherskull = new WitherSkullEntity(this.level, this, d6, d7, d8);
        if (p_82209_8_) {
            entitywitherskull.setInvulnerable(true);
        }
        entitywitherskull.setPos(d3, d4, d5);
        this.level.playSound(null, new BlockPos(d3, d4, d5), SoundEvents.WITHER_SHOOT, SoundCategory.MASTER, 3.0f, 0.8f);
        this.level.addFreshEntity(entitywitherskull);
    }

    public void performRangedAttack(final LivingEntity p_82196_1_, final float p_82196_2_) {
        if (p_82196_1_ instanceof EntityTitan || p_82196_1_.getBbHeight() >= 6.0f) {
            final double d0 = this.distanceToSqr(p_82196_1_);
            if (d0 < 1000.0 && this.attackTimer <= 0) {
                this.attackTimer = 1 + this.random.nextInt(9);
                this.doHurtTarget(p_82196_1_);
            }
        } else {
            this.launchWitherSkullToEntity(0, p_82196_1_);
        }
    }

    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        if (this.random.nextInt(10) == 0 && !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"))) {
            this.omegacounter = 600;
        }
        if (this.isInvulnerable() || this.level.getDifficulty() == Difficulty.PEACEFUL || this.getExtraPower() > 5) {
            return false;
        }
        if (source.getEntity() instanceof EntityWitherzillaMinion || (source.isExplosion() && !(source.getEntity() instanceof EntityWitherTurret))) {
            return false;
        }
        if (this.isArmored() && !(source.getEntity() instanceof PlayerEntity) && !(source.getEntity() instanceof EntityTitan)) {
            if (source.getEntity() != null && this.random.nextInt(10) == 0) {
                this.teleportToEntity(source.getEntity(), false);
            }
            return false;
        }
        if (this.blockBreakCounter <= 0) {
            this.blockBreakCounter = 1;
        }
        ++this.tickCount;
        return super.hurt(source, amount);
    }

    public boolean attackWitherzillaFrom(final DamageSource source, final float amount) {
        if (source.getEntity() instanceof EntityWitherzillaMinion || source.getEntity() instanceof EntityWitherzilla || (source.isExplosion() && !(source.getEntity() instanceof EntityWitherTurret))) {
            return false;
        }
        if (this.isArmored() && !(source.getEntity() instanceof PlayerEntity) && !(source.getEntity() instanceof EntityTitan)) {
            return false;
        }
        if (this.blockBreakCounter <= 0) {
            this.blockBreakCounter = 1;
        }
        return super.hurt(source, amount);
    }

    public int getWatchedTargetId(final int p_82203_1_) {
        return switch (p_82203_1_) {
            case 0 -> this.entityData.get(t17);
            case 1 -> this.entityData.get(t18);
            case 2 -> this.entityData.get(t19);
            default -> 0;
        };
    }

    public void func_82211_c(final int p_82211_1_, final int p_82211_2_) {
        DataParameter data = switch (p_82211_1_) {
            case 0 -> t17;
            case 1 -> t18;
            case 2 -> t19;
            default -> throw new IllegalStateException("Unexpected value: " + p_82211_1_);
        };
        this.entityData.set(data, p_82211_2_);
    }

    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }

    public float getHeadYRot(int p_82207_1_) {
        return this.field_82221_e[p_82207_1_];
    }

    public float getHeadXRot(int p_82210_1_) {
        return this.field_82220_d[p_82210_1_];
    }

    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (this.deathTicks > 0) {
            for (int x = 0; x < 250; ++x) {
                final EntityXPBomb entitylargefireball = new EntityXPBomb(this.level, this.getX(), this.getY() + 4.0, this.getZ());
                entitylargefireball.setPos(this.getX(), this.getY() + 4.0, this.getZ());
                final EntityXPBomb entityXPBomb = entitylargefireball;
                entityXPBomb.push(0.0, 1.0, 0.0);
                entitylargefireball.setXPCount(32000);
                this.level.addFreshEntity(entitylargefireball);
            }
            Item it = null;
            Block bl = null;
            Iterator ilist = Registry.ITEM.iterator();
            int icount = 0;
            while (ilist.hasNext()) {
                it = (Item) ilist.next();
                if (it != null) {
                    ++icount;
                }
            }
            int j = 0;
            while (j < 256) {
                int k;
                for (k = 1 + this.random.nextInt(icount), ilist = Registry.ITEM.iterator(); k > 0 && ilist.hasNext(); --k) {
                    it = (Item) ilist.next();
                }
                if (it != null && it != TitanItems.ultimaBlade) {
                    ++j;
                    final ItemEntity var3 = new ItemEntity(this.level, this.getX() + (this.random.nextDouble() - 0.5) * 12.0, this.getY() + 12.0 + (this.random.nextDouble() - 0.5) * 12.0, this.getZ() + (this.random.nextDouble() - 0.5) * 12.0, new ItemStack(it, 1));
                    this.level.addFreshEntity(var3);
                }
            }
            Iterator blist = Registry.BLOCK.iterator();
            int bcount = 0;
            while (blist.hasNext()) {
                bl = (Block) blist.next();
                if (bl != null) {
                    ++bcount;
                }
            }
            int i = 0;
            while (i < 256) {
                int l;
                for (l = 1 + this.random.nextInt(bcount), blist = Registry.BLOCK.iterator(); l > 0 && blist.hasNext(); --l) {
                    bl = (Block) blist.next();
                }
                if (bl != null) {
                    ++i;
                    final ItemEntity var4 = new ItemEntity(this.level, this.getX() + (this.random.nextDouble() - 0.5) * 12.0, this.getY() + 12.0 + (this.random.nextDouble() - 0.5) * 12.0, this.getZ() + (this.random.nextDouble() - 0.5) * 12.0, new ItemStack(Item.byBlock(bl), 1));
                    this.level.addFreshEntity(var4);
                }
            }
            for (int m = 0; m < 1024; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.COAL_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 512; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.IRON_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 512; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.GOLD_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 256; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.EMERALD_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 256; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.DIAMOND_BLOCK));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 128; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(TitanBlocks.harcadium_block));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 128; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(TitanBlocks.void_block));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 128; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(TitanBlocks.bedrock_block));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
            for (int m = 0; m < 256; ++m) {
                final ItemEntity entityitem = new ItemEntity(this.level, this.getX() + (this.random.nextFloat() * 12.0f - 6.0f), this.getY() + 10.0 + this.random.nextFloat() * 10.0f, this.getZ() + (this.random.nextFloat() * 12.0f - 6.0f), new ItemStack(Blocks.DRAGON_EGG));
                entityitem.setPickUpDelay(40);
                this.level.addFreshEntity(entityitem);
            }
        }
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    protected float getSoundVolume() {
        return 1000.0f;
    }

    @Override
    public float getBrightness() {
        return 1.0f;
    }

    @Override
    public boolean addEffect(EffectInstance p_70690_1_) {
        return false;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public int getThreashHold() {
        return 210;
    }

    @Override
    protected void inactDeathAction() {
        if (!this.level.isClientSide) {
            this.createBeaconPortal(MathHelper.floor(this.getX()), MathHelper.floor(this.getZ()));
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.dropFewItems(true, 0);
                this.dropEquipment();
                //this.dropRareDrop(1);
            }
            final EntityTitanSpirit entitytitan = new EntityTitanSpirit(this.level);
            entitytitan.moveTo(this.getX(), this.getY() + 48.0, this.getZ(), this.yRot, 0.0f);
            this.level.addFreshEntity(entitytitan);
            entitytitan.setVesselHunting(true);
            entitytitan.setSpiritType(12);
        }
    }

    @Override
    protected void onTitanDeathUpdate() {
        this.entityData.set(t6, MathHelper.clamp(this.entityData.get(t5), 0.0f, this.getMaxHealth()));
        this.dead = false;
        if (this.entityData.get(t5) <= 0.0f) {
            ++this.deathTicks;
        }
        if (this.deathTicks > 180) {
            final float f = (this.random.nextFloat() - 0.5f) * 24.0f;
            final float f2 = (this.random.nextFloat() - 0.5f) * 80.0f;
            final float f3 = (this.random.nextFloat() - 0.5f) * 24.0f;
            this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + f, this.getY() + 2.0 + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
        }
        if (this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void")) {
            this.moveTo(0.0, 120.0, 0.0, (float) (this.deathTicks * 10), 0.0f);
        }
        if (!this.level.isClientSide && this.deathTicks == 1) {
            this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
            this.level.levelEvent(1018, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
            this.level.levelEvent(1018, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
            this.level.levelEvent(1018, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
            this.level.levelEvent(1018, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
            this.level.getServer().setDifficulty(Difficulty.PEACEFUL, true);
            final ArrayList listp = Lists.newArrayList(this.level.players());
            if (listp != null && !listp.isEmpty()) {
                for (int i1 = 0; i1 < listp.size(); ++i1) {
                    final Entity entity = (Entity) listp.get(i1);
                    if (entity != null && entity instanceof PlayerEntity) {
                        //((PlayerEntity) entity).triggerAchievement(this.getAchievement());
                        //((PlayerEntity) entity).triggerAchievement(AchievementList.field_150964_J);
                        final ItemStack item = new ItemStack(TitanItems.ultimaBlade, 1);
                        ((ItemUltimaBlade)item.getItem()).setDamageEx(item, 1);
                        final ItemEntity entityitem = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), item);
                        entityitem.setPickUpDelay(20);
                        this.level.addFreshEntity(entityitem);
                        this.playAmbientSound();
                        if (this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void")) {
                            entity.sendMessage(new TranslationTextComponent("dialog.witherzilla.death"), this.getUUID());
                        } else {
                            entity.sendMessage(new TranslationTextComponent("dialog.witherzilla.defeat"), this.getUUID());
                        }
                    }
                }
            }
        }
        if (this.deathTicks >= 200) {
            this.setInvulTime(2000);
        }
        if (this.deathTicks >= 400) {
            this.removeAfterChangingDimensions();
        }
    }

    private void createBeaconPortal(final int p_70975_1_, final int p_70975_2_) {
        final byte b0 = 64;
        final byte b2 = 4;
        for (int k = b0 - 1; k <= b0 + 32; ++k) {
            for (int l = p_70975_1_ - b2; l <= p_70975_1_ + b2; ++l) {
                for (int i1 = p_70975_2_ - b2; i1 <= p_70975_2_ + b2; ++i1) {
                    final double d0 = l - p_70975_1_;
                    final double d2 = i1 - p_70975_2_;
                    final double d3 = d0 * d0 + d2 * d2;
                    if (d3 <= (b2 - 0.5) * (b2 - 0.5)) {
                        if (k < b0) {
                            if (d3 <= (b2 - 1 - 0.5) * (b2 - 1 - 0.5)) {
                                this.level.setBlock(new BlockPos(l, k, i1), Blocks.BEDROCK.defaultBlockState(), 0, 3);
                            }
                        } else if (k > b0) {
                            this.level.setBlock(new BlockPos(l, k, i1), Blocks.AIR.defaultBlockState(), 0, 3);
                        } else if (d3 > (b2 - 1 - 0.5) * (b2 - 1 - 0.5)) {
                            this.level.setBlock(new BlockPos(l, k, i1), Blocks.BEDROCK.defaultBlockState(), 0, 3);
                        } else {
                            this.level.setBlock(new BlockPos(l, k, i1), Blocks.END_PORTAL.defaultBlockState(), 0, 3);
                        }
                    }
                }
            }
        }
        this.level.setBlock(new BlockPos(p_70975_1_, b0, p_70975_2_), Blocks.BEDROCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 1, p_70975_2_), Blocks.BEDROCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 2, p_70975_2_), Blocks.BEDROCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ - 1, b0 + 2, p_70975_2_), Blocks.TORCH.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ + 1, b0 + 2, p_70975_2_), Blocks.TORCH.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 2, p_70975_2_ - 1), Blocks.TORCH.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 2, p_70975_2_ + 1), Blocks.TORCH.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 3, p_70975_2_), Blocks.BEDROCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 4, p_70975_2_), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ + 1, b0 + 4, p_70975_2_ + 1), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ + 1, b0 + 4, p_70975_2_), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ + 1, b0 + 4, p_70975_2_ - 1), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ - 1, b0 + 4, p_70975_2_ + 1), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ - 1, b0 + 4, p_70975_2_), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_ - 1, b0 + 4, p_70975_2_ - 1), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 4, p_70975_2_ + 1), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 4, p_70975_2_ - 1), Blocks.DIAMOND_BLOCK.defaultBlockState(), 0, 3);
        this.level.setBlock(new BlockPos(p_70975_1_, b0 + 5, p_70975_2_), Blocks.BEACON.defaultBlockState(), 0, 3);
    }

    @Override
    public void collideWithEntities(EntityTitanPart part, List<Entity> p_70970_1_) {
    }

    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return this.isInOmegaForm() ? 380.8f : 190.4f;
    }

    protected boolean teleportRandomly(final boolean bool) {
        final double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        final double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        if (bool) {
            return this.teleportInstanceTo(0.0, 200.0, 0.0);
        }
        return this.teleportInstanceTo(d0, 200.0, d2);
    }

    protected boolean teleportToEntity(final Entity p_70816_1_, final boolean bool) {
        Vector3d vec3 = new Vector3d(this.getX() - p_70816_1_.getX(), this.getBoundingBox().minY + this.getBbHeight() / 2.0f - p_70816_1_.getY() + p_70816_1_.getEyeHeight(), this.getZ() - p_70816_1_.getZ());
        vec3 = vec3.normalize();
        final double d0 = 32.0;
        final double d2 = this.getX() + (this.random.nextDouble() - 0.5) * 16.0 - vec3.x * d0;
        final double d3 = this.getY() + (this.random.nextInt(16) - 8) - vec3.y * d0;
        final double d4 = this.getZ() + (this.random.nextDouble() - 0.5) * 16.0 - vec3.z * d0;
        if (bool) {
            this.doLightningAttackTo(p_70816_1_);
            return this.teleportInstanceTo(p_70816_1_.getX(), p_70816_1_.getY(), p_70816_1_.getZ());
        }
        return this.teleportInstanceTo(d2, d3, d4);
    }

    protected boolean teleportInstanceTo(final double p_70825_1_, final double p_70825_3_, final double p_70825_5_) {
        final EnderTeleportEvent event = new EnderTeleportEvent(this, p_70825_1_, p_70825_3_, p_70825_5_, 0.0f);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        if (!this.level.isClientSide) {
            this.setPos(p_70825_1_, p_70825_3_, p_70825_5_);
        }
        return true;
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    public boolean isInOmegaForm() {
        return !(this.level.dimension().getRegistryName() == new ResourceLocation(TheTitans.modid, "provider_void"));
    }

    //Fuck u stupid collision push
    @Override
    public boolean updateFluidHeightAndDoFluidPushing(ITag<Fluid> p_210500_1_, double p_210500_2_) {
        return false;
    }
}
