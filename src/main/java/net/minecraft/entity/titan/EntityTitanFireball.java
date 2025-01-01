package net.minecraft.entity.titan;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityTitanFireball extends AbstractFireballEntity {
    public static final DataParameter<Integer> t21 = EntityDataManager.defineId(EntityTitanFireball.class, DataSerializers.INT);
    public float explosionRadius;
    public float impactDamage;
    public boolean canCauseFires;
    public EntitySize entitySize;
    public EntityTitanFireball(EntityType<EntityTitanFireball> p_i50163_1_, World p_i50163_2_) {
        super(p_i50163_1_, p_i50163_2_);
    }

    public EntityTitanFireball(final World worldIn, final LivingEntity p_i1771_2_, final double p_i1771_3_, final double p_i1771_5_, final double p_i1771_7_) {
        super(RenderTheTitans.titanFireball, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_, worldIn);
    }

    public EntityTitanFireball(final World worldIn, final double p_i1772_2_, final double p_i1772_4_, final double p_i1772_6_, final double p_i1772_8_, final double p_i1772_10_, final double p_i1772_12_) {
        super(RenderTheTitans.titanFireball, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_, worldIn);
    }

    public EntityTitanFireball(final World worldIn, final LivingEntity p_i1771_2_, final double p_i1771_3_, final double p_i1771_5_, final double p_i1771_7_, final int id) {
        this(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.setFireballID(id);
    }

    public void onHitEntity(EntityRayTraceResult p_213868_1_) {
        Entity movingObject = p_213868_1_.getEntity();
        float f;
        if (TitanConfig.NightmareMode) {
            f = this.impactDamage * 3.0f;
        } else {
            f = this.impactDamage;
        }
        if (movingObject != null && this.getOwner() != null && this.getOwner() instanceof LivingEntity && ((LivingEntity) this.getOwner()).canAttack((LivingEntity) movingObject)) {
            if (this.getOwner() instanceof EntityTitan) {
                ((EntityTitan) this.getOwner()).attackChoosenEntity(movingObject, f, 3);
                ((EntityTitan) this.getOwner()).destroyBlocksInAABB(this.getBoundingBox().inflate(this.explosionRadius - this.getBbWidth() + 1.0, this.explosionRadius - this.getBbWidth() + 1.0, this.explosionRadius - this.getBbWidth() + 1.0));
            }
            this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0f, (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f);
            this.removeAfterChangingDimensions();
        }
    }

    @Override
    protected void onHit(RayTraceResult hit) {
        if (hit.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult movingObject = (EntityRayTraceResult) hit;
            movingObject.hitInfo = movingObject.getEntity();
            float f;
            if (TitanConfig.NightmareMode) {
                f = this.impactDamage * 3.0f;
            } else {
                f = this.impactDamage;
            }
            if (!this.level.isClientSide) {
                if (movingObject.hitInfo != null && movingObject.hitInfo instanceof FireballEntity) {
                    return;
                }
                if (movingObject.hitInfo != null) {
                    if (this.getOwner() != null && this.getOwner() instanceof LivingEntity && ((EntityTitan) this.getOwner()).canAttack((Entity) movingObject.hitInfo) && this.getOwner() != movingObject.hitInfo) {
                        if (this.getOwner() instanceof EntityTitan) {
                            ((EntityTitan) this.getOwner()).attackChoosenEntity((Entity) movingObject.hitInfo, f, 3);
                            ((EntityTitan) this.getOwner()).destroyBlocksInAABB(this.getBoundingBox().inflate(this.explosionRadius - this.getBbWidth() + 1.0, this.explosionRadius - this.getBbWidth() + 1.0, this.explosionRadius - this.getBbWidth() + 1.0));
                        } else {
                            if (movingObject.hitInfo instanceof EntityTitanPart) {
                                ((EntityTitanPart)movingObject.hitInfo).hurt(DamageSource.mobAttack((LivingEntity) this.getOwner()), f);
                            }
                        }
                        this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0f, (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f);
                        this.removeAfterChangingDimensions();
                    }
                } else {
                    if (this.getOwner() != null && this.getOwner() instanceof EntityTitan && this.getFireballID() != 6) {
                        ((EntityTitan) this.getOwner()).destroyBlocksInAABB(this.getBoundingBox().inflate(this.explosionRadius - this.getBbWidth() + 1.0, this.explosionRadius - this.getBbWidth() + 1.0, this.explosionRadius - this.getBbWidth() + 1.0));
                    }
                    if (!this.level.isClientSide && this.getFireballID() == 6) {
                        for (int l = 0; l < 128; ++l) {
                            final int i = MathHelper.floor(this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth());
                            final int j = MathHelper.floor(this.getY());
                            final int k = MathHelper.floor(this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth());
                            if (this.level.getBlockState(new BlockPos(i, j, k)).getMaterial() == Material.AIR && Blocks.SNOW.canBeReplacedByLeaves(this.level.getBlockState(new BlockPos(i, j - 1, k)), this.level, new BlockPos(i, j, k))) {
                                this.level.setBlock(new BlockPos(i, j, k), Blocks.SNOW.defaultBlockState(), 0, 3);
                            }
                        }
                    }
                    this.level.explode((this.getOwner() != null) ? this.getOwner() : this, this.getX(), this.getY(), this.getZ(), this.explosionRadius, this.canCauseFires && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING), this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                    this.removeAfterChangingDimensions();
                }
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean hurt(final DamageSource source, final float amount) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(t21, 0);
    }

    public int getFireballID() {
        return this.getEntityData().get(t21);
    }

    public void setFireballID(final int p_82215_1_) {
        this.getEntityData().set(t21, p_82215_1_);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT p_70014_1_) {
        super.addAdditionalSaveData(p_70014_1_);
        p_70014_1_.putInt("FireballID", this.getFireballID());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        this.setFireballID(p_70037_1_.getInt("FireballID"));
    }

    @Override
    protected boolean shouldBurn() {
        return this.canCauseFires;
    }

    @Override
    public boolean isOnFire() {
        return this.canCauseFires;
    }

    @Override
    public void setRemainingFireTicks(int p_241209_1_) {
        if (this.canCauseFires) {
            super.setRemainingFireTicks(p_241209_1_);
        }
    }

    public void baseTick() {
        if (this.getOwner() != null) {
            if (this.getOwner() instanceof EntityGhastTitan) {
                this.setFireballID(0);
            }
            if (this.getOwner() instanceof EntityCreeperTitan) {
                this.setFireballID(1);
            }
            if (this.getOwner() instanceof EntityBlazeTitan) {
                this.setFireballID(2);
            }
            if (this.getOwner() instanceof EntityPigZombieTitan) {
                this.setFireballID(3);
            }
            if (this.getOwner() instanceof EntityEnderColossus) {
                this.setFireballID(4);
            }
            if (this.getOwner() instanceof EntityIronGolemTitan) {
                this.setFireballID(5);
            }
            if (this.getOwner() instanceof EntitySnowGolemTitan) {
                this.setFireballID(6);
            }
        }
        switch (this.getFireballID()) {
            case 1 -> {
                this.setSize(1.5f, 1.5f);
                this.impactDamage = 200.0f;
                this.canCauseFires = false;
                this.explosionRadius = 3.0f;
            }
            case 2 -> {
                this.setSize(2.0f, 2.0f);
                this.impactDamage = 600.0f;
                this.canCauseFires = true;
                this.explosionRadius = 3.0f;
            }
            case 3 -> {
                this.setSize(1.5f, 1.5f);
                this.impactDamage = 300.0f;
                this.canCauseFires = true;
                this.explosionRadius = 4.0f;
            }
            case 4 -> {
                this.setSize(4.0f, 4.0f);
                this.impactDamage = 1500.0f;
                this.canCauseFires = false;
                this.explosionRadius = 8.0f;
            }
            case 5 -> {
                this.setSize(6.0f, 6.0f);
                this.impactDamage = 10000.0f;
                this.canCauseFires = false;
                this.explosionRadius = 12.0f;
            }
            case 6 -> {
                this.setSize(2.0f, 2.0f);
                this.impactDamage = 60.0f;
                this.canCauseFires = false;
                this.explosionRadius = 1.0f;
            }
            default -> {
                this.setSize(6.0f, 6.0f);
                this.impactDamage = 10000.0f;
                this.canCauseFires = true;
                this.explosionRadius = 12.0f;
            }
        }
        super.baseTick();
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return entitySize;
    }

    public void setSize(float width, float height) {
        entitySize = EntitySize.fixed(width, height);
        this.refreshDimensions();
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
