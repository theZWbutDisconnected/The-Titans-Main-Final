package net.minecraft.entity.titan;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.titanminion.EntityCreeperMinion;
import net.minecraft.entity.titanminion.EntityZombieMinion;
import net.minecraft.entity.titanminion.EnumMinionType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityTitanSpirit extends LivingEntity {
    public int spiritType;
    public int spiritNameID;
    public float tonnage;
    public boolean isSearchingForVessel;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private LivingEntity prevEntity;
    private EntitySize entitySize;

    public EntityTitanSpirit(EntityType<? extends EntityTitanSpirit> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
        this.setSize(8.0f, 8.0f);
        noPhysics = true;
        fireImmune();
        noCulling = true;
        this.playSound(TitanSounds.titanBirth, 10000.0f, 2.0f);
    }

    public EntityTitanSpirit(World p_i48576_2_) {
        this(RenderTheTitans.titanSpirit, p_i48576_2_);
    }

    public static AttributeModifierMap.MutableAttribute applyEntityAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.MAX_HEALTH, Double.MAX_VALUE);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return entitySize;
    }

    public void setSize(float width, float height) {
        entitySize = EntitySize.fixed(width, height);
        this.refreshDimensions();
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        this.setTonnage(tagCompund.getFloat("Tonnage"));
        this.setSpiritType(tagCompund.getInt("SpiritType"));
        this.setSpiritNameID(tagCompund.getInt("SpiritNameID"));
        this.setVesselHunting(tagCompund.getBoolean("ShouldHuntForVessels"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putFloat("Tonnage", this.getTonnage());
        tagCompound.putInt("SpiritType", this.getSpiritType());
        tagCompound.putInt("SpiritNameID", this.getSpiritNameID());
        tagCompound.putBoolean("ShouldHuntForVessels", this.isVesselHunting());
    }

    @Override
    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    public void travel(Vector3d p_213352_1_) {
        if (this.isInWater()) {
            this.moveRelative(0.02f, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().x * 0.800000011920929, this.getDeltaMovement().y * 0.800000011920929, this.getDeltaMovement().z * 0.800000011920929);
        } else if (this.isInLava()) {
            this.moveRelative(0.02f, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().x * 0.5, this.getDeltaMovement().y * 0.5, this.getDeltaMovement().z * 0.5);
        } else {
            float f2 = 0.91f;
            if (this.onGround) {
                f2 = this.level.getBlockState(new BlockPos(MathHelper.floor(this.getX()), MathHelper.floor(this.getBoundingBox().minY) - 1, MathHelper.floor(this.getZ()))).getBlock().getFriction() * 0.91f;
            }
            final float f3 = 0.16277136f / (f2 * f2 * f2);
            this.moveRelative(this.onGround ? (0.1f * f3) : 0.02f, p_213352_1_);
            f2 = 0.91f;
            if (this.onGround) {
                f2 = this.level.getBlockState(new BlockPos(MathHelper.floor(this.getX()), MathHelper.floor(this.getBoundingBox().minY) - 1, MathHelper.floor(this.getZ()))).getBlock().getFriction() * 0.91f;
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().x * f2, this.getDeltaMovement().y * f2, this.getDeltaMovement().z * f2);
        }
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

    public boolean isVesselHunting() {
        return this.isSearchingForVessel;
    }

    public void setVesselHunting(final boolean p_70819_1_) {
        this.isSearchingForVessel = p_70819_1_;
    }

    public int getSpiritType() {
        return this.spiritType;
    }

    public void setSpiritType(final int p_82215_1_) {
        this.spiritType = p_82215_1_;
    }

    public int getSpiritNameID() {
        return this.spiritNameID;
    }

    public void setSpiritNameID(final int p_82215_1_) {
        this.spiritNameID = p_82215_1_;
    }

    public float getTonnage() {
        return this.tonnage;
    }

    public void setTonnage(final float p_82215_1_) {
        this.tonnage = p_82215_1_;
    }

    public float getMaxTonnage() {
        switch (this.getSpiritType()) {
            case 1: {
                return 8000.0f;
            }
            case 2: {
                return 12000.0f;
            }
            case 3: {
                return 16000.0f;
            }
            case 4: {
                return 20000.0f;
            }
            case 5: {
                return 100000.0f;
            }
            case 6: {
                return 20000.0f;
            }
            case 7: {
                return 25000.0f;
            }
            case 8: {
                return 20000.0f;
            }
            case 9: {
                return 40000.0f;
            }
            case 10: {
                return 150000.0f;
            }
            case 11: {
                return 200000.0f;
            }
            case 12: {
                return 1.0E7f;
            }
            default: {
                return 1.0f;
            }
        }
    }

    public ITextComponent getName() {
        switch (this.getSpiritType()) {
            case 1: {
                return new TranslationTextComponent("entity.SilverfishTitan.name");
            }
            case 2: {
                return new TranslationTextComponent("entity.CaveSpiderTitan.name");
            }
            case 3: {
                return new TranslationTextComponent("entity.SpiderTitan.name");
            }
            case 4: {
                return new TranslationTextComponent("entity.SkeletonTitan.name");
            }
            case 5: {
                return new TranslationTextComponent("entity.WitherSkeletonTitan.name");
            }
            case 6: {
                return new TranslationTextComponent("entity.ZombieTitan.name");
            }
            case 7: {
                return new TranslationTextComponent("entity.CreeperTitan.name");
            }
            case 8: {
                return new TranslationTextComponent("entity.PigZombieTitan.name");
            }
            case 9: {
                return new TranslationTextComponent("entity.BlazeTitan.name");
            }
            case 10: {
                return new TranslationTextComponent("entity.EndermanTitan.realname");
            }
            case 11: {
                return new TranslationTextComponent("entity.GhastTitan.name");
            }
            case 12: {
                return ITextComponent.nullToEmpty("\u00A7kRegnator");
            }
            default: {
                return new TranslationTextComponent("entity.TitanSpirit.name");
            }
        }
    }

    @Override
    protected void removeAfterChangingDimensions() {
        if (!this.level.isClientSide) {
            if (this.getSpiritType() == 6) {
                this.level.explode(this, this.getX(), this.getY(), this.getZ(), 18.0f, true, this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                final EntityZombieTitan zombietitan = new EntityZombieTitan(RenderTheTitans.zombieTitan, this.level);
                zombietitan.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                zombietitan.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                zombietitan.func_82206_m();
                this.level.addFreshEntity(zombietitan);
                if (this.prevEntity != null) {
                    zombietitan.setBaby(this.prevEntity.isBaby());
                    zombietitan.setVillager(((EntityZombieMinion) this.prevEntity).isVillager());
                }
            }
            final ArrayList listp = Lists.newArrayList(this.level.players());
            if (this.getSpiritType() != 12 && listp != null && !listp.isEmpty() && !this.level.isClientSide) {
                for (int i1 = 0; i1 < listp.size(); ++i1) {
                    final Entity entity2 = (Entity) listp.get(i1);
                    if (entity2 != null && entity2 instanceof PlayerEntity) {
                        this.playAmbientSound();
                        entity2.sendMessage(new StringTextComponent(this.getName().getString() + ": Now to return to where we left off, " + entity2.getName().getString() + "."), this.getUUID());
                    }
                }
            }
        }
        super.removeAfterChangingDimensions();
    }

    @Override
    public void aiStep() {
        if (this.getTonnage() >= this.getMaxTonnage()) {
            this.removeAfterChangingDimensions();
        }
        if (this.getTonnage() < 0.0f) {
            this.setTonnage(0.0f);
        } else {
            this.setTonnage(this.getTonnage() - 1.0f);
        }
        final List list = this.level.getEntities(this, this.getBoundingBox().inflate(32.0, 32.0, 32.0));
        if (this.isVesselHunting() && list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = (Entity) list.get(i);
                if (entity != null && entity.isAlive() && entity instanceof LivingEntity && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit) && !(entity instanceof EntityTitanPart)) {
                    if (this.tickCount + this.getId() % 40 == 0) {
                        entity.hurt(DamageSourceExtra.causeSoulStealingDamage(this), 2.0f);
                    }
                    final double speed = entity.isCrouching() ? 0.2 : 0.4;
                    final double mx = this.getX() - entity.getX();
                    final double my = this.getY() + 4.0 - (entity.getY() + 1.0);
                    final double mz = this.getZ() - entity.getZ();
                    final float f2 = MathHelper.sqrt(mx * mx + my * my + mz * mz);
                    entity.push(mx / f2 * speed * speed, my / f2 * speed * speed, mz / f2 * speed * speed);
                    final short short1 = (short) this.distanceTo(entity);
                    for (int f3 = 0; f3 < short1; ++f3) {
                        final double d9 = f3 / (short1 - 1.0);
                        final double d10 = this.getX() + mx * -d9;
                        final double d11 = this.getY() + 4.0 + my * -d9;
                        final double d12 = this.getZ() + mz * -d9;
                        this.level.addParticle(ParticleTypes.FIREWORK, d10, d11, d12, entity.getDeltaMovement().x, entity.getDeltaMovement().y, entity.getDeltaMovement().z);
                    }
                }
                if (entity != null && entity instanceof ItemEntity) {
                    final double mx2 = this.getX() - entity.getX();
                    final double my2 = this.getY() + 4.0 - entity.getY();
                    final double mz2 = this.getZ() - entity.getZ();
                    final float f4 = MathHelper.sqrt(mx2 * mx2 + my2 * my2 + mz2 * mz2);
                    entity.push(mx2 / f4 * 0.3 * 0.3, my2 / f4 * 0.3 * 0.3, mz2 / f4 * 0.3 * 0.3);
                }
                if (entity != null && entity instanceof TNTEntity) {
                    final double mx2 = this.getX() - entity.getX();
                    final double my2 = this.getY() + 4.0 - entity.getY();
                    final double mz2 = this.getZ() - entity.getZ();
                    final float f4 = MathHelper.sqrt(mx2 * mx2 + my2 * my2 + mz2 * mz2);
                    entity.push(mx2 / f4 * 0.3 * 0.3, my2 / f4 * 0.3 * 0.3, mz2 / f4 * 0.3 * 0.3);
                }
                if (entity != null && entity instanceof FallingBlockEntity) {
                    final float f5 = (entity.tickCount + entity.getId()) * 3.1415927f * -0.5f;
                    final double mx3 = this.getX() + MathHelper.cos(f5) * 16.0f - entity.getX();
                    final double my3 = this.getY() + 4.0 - entity.getY();
                    final double mz3 = this.getZ() + MathHelper.sin(f5) * 16.0f - entity.getZ();
                    final float f6 = MathHelper.sqrt(mx3 * mx3 + my3 * my3 + mz3 * mz3);
                    entity.push(mx3 / f6 * 0.4 * 0.4, my3 / f6 * 0.4 * 0.4, mz3 / f6 * 0.4 * 0.4);
                    final ArrayList<Entity> arraylist = new ArrayList(this.level.getEntities(entity, entity.getBoundingBox()));
                    final boolean flag = ((FallingBlockEntity) entity).getBlockState().getBlock() == Blocks.ANVIL;
                    final DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                    for (final Entity entity2 : arraylist) {
                        entity2.hurt(damagesource, 20.0f);
                    }
                }
                if (entity != null && entity instanceof FireballEntity) {
                    final double mx2 = this.getX() - entity.getX();
                    final double my2 = this.getY() + 4.0 - entity.getY();
                    final double mz2 = this.getZ() - entity.getZ();
                    final float f4 = MathHelper.sqrt(mx2 * mx2 + my2 * my2 + mz2 * mz2);
                    entity.push(mx2 / f4 * 0.3 * 0.3, my2 / f4 * 0.3 * 0.3, mz2 / f4 * 0.3 * 0.3);
                }
            }
        }
        if (this.spiritNameID <= 0) {
            this.spiritNameID = 1;
        }
        super.aiStep();
        if (this.getY() <= -45.0) {
            this.setPos(this.getX(), 0.0, this.getZ());
        }
        this.setHealth(Float.MAX_VALUE);
        this.deathTime = 0;
        for (int i = 0; i < 30; ++i) {
            final float f7 = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
            final float f8 = (this.random.nextFloat() - 0.5f) * this.getBbHeight();
            final float f9 = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
            this.level.addParticle(ParticleTypes.POOF, this.getX() + f7, this.getY() + 4.0 + f8, this.getZ() + f9, 0.0, 0.0, 0.0);
            this.level.addParticle(ParticleTypes.EXPLOSION, this.getX() + f7, this.getY() + 2.0 + f8, this.getZ() + f9, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
            this.level.addParticle(ParticleTypes.FIREWORK, this.getX() + f7, this.getY() + 4.0 + f8, this.getZ() + f9, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
        }
        final List theBoundingBox = this.level.getEntities(this, this.getBoundingBox());
        if (theBoundingBox != null && !theBoundingBox.isEmpty()) {
            for (int i2 = 0; i2 < theBoundingBox.size(); ++i2) {
                final Entity entity3 = (Entity) theBoundingBox.get(i2);
                if (entity3 != null) {
                    if (this.spiritType == 6 && entity3 instanceof EntityZombieMinion && ((EntityZombieMinion) entity3).getMinionType() == EnumMinionType.TEMPLAR) {
                        this.prevEntity = (LivingEntity) entity3;
                        this.refreshDimensions();
                    }
                    if (this.spiritType == 7 && entity3 instanceof EntityCreeperMinion && ((EntityCreeperMinion) entity3).getMinionType() == EnumMinionType.TEMPLAR) {
                        this.prevEntity = (LivingEntity) entity3;
                        this.refreshDimensions();
                    }
                    if (this.spiritType == 12 && entity3 instanceof PlayerEntity) {
                        this.prevEntity = (LivingEntity) entity3;
                        this.refreshDimensions();
                    }
                    if (entity3 instanceof LivingEntity) {
                        entity3.hurt(DamageSourceExtra.causeSoulStealingDamage(this), 100.0f);
                        ((LivingEntity) entity3).addEffect(new EffectInstance(Effects.WITHER, 300, 3));
                    }
                    if (entity3 instanceof ItemEntity && !((ItemEntity) entity3).hasPickUpDelay() && ((ItemEntity) entity3).getItem().getItem() != Items.NETHER_STAR) {
                        entity3.hurt(DamageSourceExtra.causeSoulStealingDamage(this), 100.0f);
                    }
                    if (entity3 instanceof EnderCrystalEntity) {
                        entity3.hurt(DamageSourceExtra.causeSoulStealingDamage(this), 100.0f);
                    }
                }
            }
        }
    }

    @Override
    protected void serverAiStep() {
        super.serverAiStep();
        if (this.waypointY <= 0.0) {
            this.waypointY = 0.0;
        }
        if (this.waypointY > 255.0) {
            this.waypointY = 255.0;
        }
        if (this.isVesselHunting() && this.tickCount % 20 == 0 && this.random.nextInt(5) == 0) {
            final PlayerEntity player = this.level.getNearestPlayer(this, 256.0);
            if (player != null) {
                this.waypointX = player.getX() + (this.random.nextFloat() * 2.0f - 1.0f) * 32.0f;
                this.waypointY = player.getY() + 32.0 + (this.random.nextFloat() * 2.0f - 1.0f) * 16.0f;
                this.waypointZ = player.getZ() + (this.random.nextFloat() * 2.0f - 1.0f) * 32.0f;
            } else {
                this.waypointX = this.getX() + (this.random.nextFloat() * 2.0f - 1.0f) * 32.0f;
                this.waypointY = this.getY() + (this.random.nextFloat() * 2.0f - 1.0f) * 32.0f;
                this.waypointZ = this.getZ() + (this.random.nextFloat() * 2.0f - 1.0f) * 32.0f;
            }
        }
        final double d0 = this.waypointX - this.getX();
        final double d2 = this.waypointY - this.getY();
        final double d3 = this.waypointZ - this.getZ();
        double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        d4 = MathHelper.sqrt(d4);
        if (this.isVesselHunting() && this.distanceToSqr(this.waypointX, this.waypointY, this.waypointZ) > 40000.0) {
            this.waypointX = this.getX();
            this.waypointY = this.getY();
            this.waypointZ = this.getZ();
        }
        if (this.distanceToSqr(this.waypointX, this.waypointY, this.waypointZ) > 4.0) {
            this.push(d0 / d4 * 0.15, d2 / d4 * 0.15, d3 / d4 * 0.15);
        }
        if (!this.isVesselHunting()) {
            this.waypointX = this.getX() + (this.random.nextFloat() * 2.0f - 1.0f) * 128.0f;
            this.waypointY = 255.0;
            this.waypointZ = this.getZ() + (this.random.nextFloat() * 2.0f - 1.0f) * 128.0f;
            if (this.getY() >= 254.0) {
                this.setVesselHunting(true);
                if (!this.level.isClientSide) {
                    if (this.random.nextInt(2) == 0 || this.getSpiritType() == 12) {
                        this.level.levelEvent(1013, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), 0);
                        final ArrayList listp = Lists.newArrayList(this.level.players());
                        if (listp != null && !listp.isEmpty() && !this.level.isClientSide) {
                            for (int i1 = 0; i1 < listp.size(); ++i1) {
                                final Entity entity = (Entity) listp.get(i1);
                                if (entity != null && entity instanceof PlayerEntity) {
                                    this.playAmbientSound();
                                    entity.sendMessage(new StringTextComponent(this.getName().getString() + ": I always come back, " + entity.getName().getString() + "."), this.getUUID());
                                }
                            }
                        }
                    } else {
                        this.setPos(this.getX() + (this.random.nextFloat() * 2.0f - 1.0f) * 1024.0f, 250.0, this.getZ() + (this.random.nextFloat() * 2.0f - 1.0f) * 1024.0f);
                    }
                }
            }
        } else {
            final List list = this.level.getEntities(this, this.getBoundingBox().inflate(256.0, 256.0, 256.0));
            if (list != null && !list.isEmpty()) {
                for (int i1 = 0; i1 < list.size(); ++i1) {
                    final Entity entity = (Entity) list.get(i1);
                    if (entity != null) {
                        if (this.spiritType == 6 && entity instanceof EntityZombieMinion && ((EntityZombieMinion) entity).getMinionType() == EnumMinionType.TEMPLAR) {
                            ((MobEntity) entity).setTarget(this);
                            this.waypointX = entity.getX();
                            this.waypointY = entity.getY();
                            this.waypointZ = entity.getZ();
                        }
                        if (this.spiritType == 7 && entity instanceof EntityCreeperMinion && ((EntityCreeperMinion) entity).getMinionType() == EnumMinionType.TEMPLAR) {
                            ((MobEntity) entity).setTarget(this);
                            this.waypointX = entity.getX();
                            this.waypointY = entity.getY();
                            this.waypointZ = entity.getZ();
                        }
                        if (this.spiritType == 12 && entity instanceof PlayerEntity) {
                            this.waypointX = entity.getX();
                            this.waypointY = entity.getY();
                            this.waypointZ = entity.getZ();
                        }
                    }
                }
            }
        }
    }

    public void playAmbientSound() {
        String soundName = this.getAmbientSound();
        Field name;
        SoundEvent soundevent = null;
        try {
            name = TitanSounds.class.getDeclaredField(soundName);
            name.setAccessible(true);
            soundevent = (SoundEvent) name.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
        }
    }

    protected String getAmbientSound() {
        switch (this.getSpiritType()) {
            case 1: {
                return "titanSilverfishLiving";
            }
            case 2: {
                return "titanSpiderLiving";
            }
            case 3: {
                return "titanSpiderLiving";
            }
            case 4: {
                return "titanSkeletonLiving";
            }
            case 5: {
                return "titanWitherSkeletonLiving";
            }
            case 6: {
                return "titanZombieLiving";
            }
            case 7: {
                return "titanCreeperLiving";
            }
            case 8: {
                return "titanPigZombieLiving";
            }
            case 9: {
                return "titanBlazeBreathe";
            }
            case 10: {
                return "titanEnderColossusRoar";
            }
            case 11: {
                return "titanGhastLiving";
            }
            case 12: {
                return "witherzillaLiving";
            }
            default: {
                return "mob.wither/idle";
            }
        }
    }

    @Override
    public void kill() {
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType p_184582_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType p_184201_1_, ItemStack p_184201_2_) {

    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }
}
