package net.minecraft.entity.titan.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.util.EntityPredicates;

import java.util.EnumSet;

public class EntityAITitanWatchClosest
        extends Goal {
    protected final EntityPredicate field_220716_e;
    private final EntityTitan theWatcher;
    private final float maxDistanceForPlayer;
    private final float field_75331_e;
    private final Class<? extends LivingEntity> watchedClass;
    protected Entity closestEntity;
    private int lookTime;

    public EntityAITitanWatchClosest(EntityTitan p_i1631_1_, Class<? extends LivingEntity> p_i1631_2_, float p_i1631_3_) {
        this.theWatcher = p_i1631_1_;
        this.watchedClass = p_i1631_2_;
        this.maxDistanceForPlayer = p_i1631_3_;
        this.field_75331_e = 0.05f;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        this.field_220716_e = this.watchedClass == PlayerEntity.class ? new EntityPredicate().range(p_i1631_3_).allowSameTeam().allowInvulnerable().allowNonAttackable().selector(target -> EntityPredicates.notRiding(p_i1631_1_).test(target)) : new EntityPredicate().range(p_i1631_3_).allowSameTeam().allowInvulnerable().allowNonAttackable();
    }

    public EntityAITitanWatchClosest(EntityTitan p_i1632_1_, Class<? extends LivingEntity> p_i1632_2_, float p_i1632_3_, float p_i1632_4_) {
        this.theWatcher = p_i1632_1_;
        this.watchedClass = p_i1632_2_;
        this.maxDistanceForPlayer = p_i1632_3_;
        this.field_75331_e = p_i1632_4_;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        this.field_220716_e = this.watchedClass == PlayerEntity.class ? new EntityPredicate().range(p_i1632_3_).allowSameTeam().allowInvulnerable().allowNonAttackable().selector(target -> EntityPredicates.notRiding(p_i1632_1_).test(target)) : new EntityPredicate().range(p_i1632_3_).allowSameTeam().allowInvulnerable().allowNonAttackable();
    }

    public boolean canUse() {
        if (this.theWatcher.getRandom().nextFloat() >= this.field_75331_e) {
            return false;
        }
        this.closestEntity = this.watchedClass == PlayerEntity.class ? this.theWatcher.level.getNearestPlayer(this.theWatcher, this.maxDistanceForPlayer) : this.theWatcher.level.getNearestLoadedEntity(this.watchedClass, this.field_220716_e, this.theWatcher, this.theWatcher.getX(), this.theWatcher.getY(), this.theWatcher.getZ(), this.theWatcher.getBoundingBox().inflate(this.maxDistanceForPlayer, this.maxDistanceForPlayer, this.maxDistanceForPlayer));
        return !this.theWatcher.getWaiting() && this.theWatcher.animID != 13 && this.theWatcher.getTarget() == null && this.closestEntity != null;
    }

    public boolean canContinueToUse() {
        return this.closestEntity.isAlive() && this.lookTime > 0;
    }

    public void start() {
        this.lookTime = 40 + this.theWatcher.getRandom().nextInt(40);
        if (this.closestEntity instanceof GiantEntity) {
            ((GiantEntity) this.closestEntity).lookAt(this.theWatcher, 10.0f, 40.0f);
        }
    }

    public void stop() {
        this.closestEntity = null;
    }

    public void tick() {
        this.theWatcher.getLookControl().setLookAt(this.closestEntity.getX(), this.closestEntity.getY() + (double) this.closestEntity.getEyeHeight(), this.closestEntity.getZ(), 5.0f, this.theWatcher.getMaxHeadXRot());
        --this.lookTime;
    }
}

