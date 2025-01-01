package net.minecraft.entity.titan;

import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.titanminion.EntityGiantZombieBetter;
import net.minecraft.entity.titanminion.EntityZombieMinion;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityProtoBall extends ThrowableEntity {
    private EntitySize entitySize;

    public EntityProtoBall(EntityType<? extends EntityProtoBall> p_i50163_1_, World p_i50163_2_) {
        super(p_i50163_1_, p_i50163_2_);
        this.setSize(3.0f, 3.0f);
        this.push(0.0, 0.25, 0.0);
    }

    public EntityProtoBall(World p_i50163_2_, MobEntity entityTitan) {
        super(RenderTheTitans.protoBall, entityTitan, p_i50163_2_);
        this.setSize(3.0f, 3.0f);
        this.push(0.0, 0.25, 0.0);
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
    public void tick() {
        super.tick();
        for (int i = 0; i < 15; ++i) {
            final float f = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
            final float f2 = (this.random.nextFloat() - 0.5f) * this.getBbHeight();
            final float f3 = (this.random.nextFloat() - 0.5f) * this.getBbWidth();
            this.level.addParticle(ParticleTypes.EXPLOSION, this.getX() + f, this.getY() + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
            this.level.addParticle(ParticleTypes.POOF, this.getX() + f, this.getY() + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
            this.level.addParticle(ParticleTypes.FIREWORK, this.getX() + f, this.getY() + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
            this.level.addParticle(ParticleTypes.SMOKE, this.getX() + f, this.getY() + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
            this.level.addParticle(ParticleTypes.LAVA, this.getX() + f, this.getY() + f2, this.getZ() + f3, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHit(RayTraceResult p_70184_1_) {
        RayTraceResult.Type raytraceresult$type = p_70184_1_.getType();
        Entity entityHit = null;
        BlockPos blockHit = null;
        if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
            entityHit = ((EntityRayTraceResult) p_70184_1_).getEntity();
        } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
            blockHit = ((BlockRayTraceResult) p_70184_1_).getBlockPos();
        }
        if (entityHit != this.getOwner() || ((entityHit instanceof EntityTitanPart) && ((EntityTitanPart) entityHit).entityDragonObj != this.getOwner())) {
            if (!this.level.isClientSide) {
                if (this.getOwner() != null && this.getOwner() instanceof EntityTitan && entityHit != null && entityHit instanceof MobEntity) {
                    ((EntityTitan) this.getOwner()).attackChoosenEntity(entityHit, 75.0f, 2);
                }
            /*if (this.getOwner() != null && this.getOwner() instanceof EntityPigZombieTitan) {
                if (this.random.nextInt(5) == 0) {
                    final EntityGhastGuard entitychicken = new EntityGhastGuard(this.level);
                    entitychicken.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                    this.level.addFreshEntity(entitychicken);
                    entitychicken.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                    final EntityGhastGuard entityGhastGuard = entitychicken;
                    ++entityGhastGuard.motionY;
                    final boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                    this.level.explode(entitychicken, entitychicken.getX(), entitychicken.getY() + 6.0, entitychicken.getZ(), 12.0f, false, flag);
                    entitychicken.master = (MobEntity) this.getOwner();
                } else {
                    switch (this.random.nextInt(4)) {
                        case 0 -> {
                            for (int l1 = 0; l1 <= 5; ++l1) {
                                final EntityPigZombieMinion entitychicken2 = new EntityPigZombieMinion(this.level);
                                entitychicken2.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                this.level.addFreshEntity(entitychicken2);
                                entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                entitychicken2.setMinionType(3);
                                final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                this.level.explode(entitychicken2, entitychicken2.getX(), entitychicken2.getY() + 2.0, entitychicken2.getZ(), 6.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                entitychicken2.master = (MobEntity) this.getOwner();
                            }
                        }
                        case 1 -> {
                            for (int l1 = 0; l1 <= 10; ++l1) {
                                final EntityPigZombieMinion entitychicken2 = new EntityPigZombieMinion(this.level);
                                entitychicken2.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                this.level.addFreshEntity(entitychicken2);
                                entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                entitychicken2.setMinionType(2);
                                final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                this.level.explode(entitychicken2, entitychicken2.getX(), entitychicken2.getY() + 2.0, entitychicken2.getZ(), 4.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                entitychicken2.master = (MobEntity) this.getOwner();
                            }
                        }
                        case 2 -> {
                            for (int l1 = 0; l1 <= 20; ++l1) {
                                final EntityPigZombieMinion entitychicken2 = new EntityPigZombieMinion(this.level);
                                entitychicken2.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                this.level.addFreshEntity(entitychicken2);
                                entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                entitychicken2.setMinionType(1);
                                final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                this.level.explode(entitychicken2, entitychicken2.getX(), entitychicken2.getY() + 2.0, entitychicken2.getZ(), 3.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                entitychicken2.master = (MobEntity) this.getOwner();
                            }
                        }
                        case 3 -> {
                            for (int l1 = 0; l1 <= 40; ++l1) {
                                final EntityPigZombieMinion entitychicken2 = new EntityPigZombieMinion(this.level);
                                entitychicken2.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                this.level.addFreshEntity(entitychicken2);
                                entitychicken2.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                entitychicken2.setMinionType(0);
                                final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                this.level.explode(entitychicken2, entitychicken2.getX(), entitychicken2.getY() + 2.0, entitychicken2.getZ(), 2.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                entitychicken2.master = (MobEntity) this.getOwner();
                            }
                        }
                    }
                }
            }*/
                if (this.getOwner() != null && this.getOwner() instanceof EntityZombieTitan) {
                    if (this.random.nextInt(5) == 0) {
                        final EntityGiantZombieBetter entitychicken3 = new EntityGiantZombieBetter(this.level);
                        entitychicken3.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                        this.level.addFreshEntity(entitychicken3);
                        entitychicken3.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                        final boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                        this.level.explode(entitychicken3, entitychicken3.getX(), entitychicken3.getY() + 6.0, entitychicken3.getZ(), 12.0f, false, flag ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                        entitychicken3.master = (MobEntity) this.getOwner();
                    } else {
                        switch (this.random.nextInt(4)) {
                            case 0 -> {
                                for (int l1 = 0; l1 <= 5; ++l1) {
                                    final EntityZombieMinion entitychicken4 = new EntityZombieMinion(this.level);
                                    entitychicken4.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                    this.level.addFreshEntity(entitychicken4);
                                    entitychicken4.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                    entitychicken4.setMinionType(3);
                                    final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                    this.level.explode(entitychicken4, entitychicken4.getX(), entitychicken4.getY() + 2.0, entitychicken4.getZ(), 6.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                    entitychicken4.master = (MobEntity) this.getOwner();
                                }
                            }
                            case 1 -> {
                                for (int l1 = 0; l1 <= 10; ++l1) {
                                    final EntityZombieMinion entitychicken4 = new EntityZombieMinion(this.level);
                                    entitychicken4.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                    this.level.addFreshEntity(entitychicken4);
                                    entitychicken4.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                    entitychicken4.setMinionType(2);
                                    final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                    this.level.explode(entitychicken4, entitychicken4.getX(), entitychicken4.getY() + 2.0, entitychicken4.getZ(), 4.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                    entitychicken4.master = (MobEntity) this.getOwner();
                                }
                            }
                            case 2 -> {
                                for (int l1 = 0; l1 <= 20; ++l1) {
                                    final EntityZombieMinion entitychicken4 = new EntityZombieMinion(this.level);
                                    entitychicken4.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                    this.level.addFreshEntity(entitychicken4);
                                    entitychicken4.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                    entitychicken4.setMinionType(1);
                                    final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                    this.level.explode(entitychicken4, entitychicken4.getX(), entitychicken4.getY() + 2.0, entitychicken4.getZ(), 3.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                    entitychicken4.master = (MobEntity) this.getOwner();
                                }
                            }
                            case 3 -> {
                                for (int l1 = 0; l1 <= 40; ++l1) {
                                    final EntityZombieMinion entitychicken4 = new EntityZombieMinion(this.level);
                                    entitychicken4.moveTo(this.getX(), this.getY(), this.getZ(), -this.yRot, -this.xRot);
                                    this.level.addFreshEntity(entitychicken4);
                                    entitychicken4.finalizeSpawn(level.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                                    entitychicken4.setMinionType(0);
                                    final boolean flag2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                                    this.level.explode(entitychicken4, entitychicken4.getX(), entitychicken4.getY() + 2.0, entitychicken4.getZ(), 2.0f, false, flag2 ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                                    entitychicken4.master = (MobEntity) this.getOwner();
                                }
                            }
                        }
                    }
                }
            }
            this.removeAfterChangingDimensions();
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
