package net.minecraft.entity.titan.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class EntityAIAvoidEntity<T extends Entity> extends Goal {
    protected final CreatureEntity mob;
    protected final float maxDist;
    protected final PathNavigator pathNav;
    protected final Class<T> avoidClass;
    protected final Predicate<LivingEntity> avoidPredicate;
    protected final Predicate<LivingEntity> predicateOnAvoidEntity;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    private final EntityPredicate avoidEntityTargeting;
    protected T toAvoid;
    protected Path path;

    public EntityAIAvoidEntity(CreatureEntity p_i46404_1_, Class<T> p_i46404_2_, float p_i46404_3_, double p_i46404_4_, double p_i46404_6_) {
        this(p_i46404_1_, p_i46404_2_, (p_200828_0_) -> {
            return true;
        }, p_i46404_3_, p_i46404_4_, p_i46404_6_, EntityPredicates.NO_CREATIVE_OR_SPECTATOR::test);
    }

    public EntityAIAvoidEntity(CreatureEntity p_i48859_1_, Class<T> p_i48859_2_, Predicate<LivingEntity> p_i48859_3_, float p_i48859_4_, double p_i48859_5_, double p_i48859_7_, Predicate<LivingEntity> p_i48859_9_) {
        this.mob = p_i48859_1_;
        this.avoidClass = p_i48859_2_;
        this.avoidPredicate = p_i48859_3_;
        this.maxDist = p_i48859_4_;
        this.walkSpeedModifier = p_i48859_5_;
        this.sprintSpeedModifier = p_i48859_7_;
        this.predicateOnAvoidEntity = p_i48859_9_;
        this.pathNav = p_i48859_1_.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.avoidEntityTargeting = (new EntityPredicate()).range(p_i48859_4_).selector(p_i48859_9_.and(p_i48859_3_));
    }

    public EntityAIAvoidEntity(CreatureEntity p_i48860_1_, Class<T> p_i48860_2_, float p_i48860_3_, double p_i48860_4_, double p_i48860_6_, Predicate<LivingEntity> p_i48860_8_) {
        this(p_i48860_1_, p_i48860_2_, (p_203782_0_) -> {
            return true;
        }, p_i48860_3_, p_i48860_4_, p_i48860_6_, p_i48860_8_);
    }

    public boolean canUse() {
        this.toAvoid = getNearestLoadedEntity(this.avoidClass, this.avoidEntityTargeting, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.maxDist, 3.0D, this.maxDist));
        if (this.toAvoid == null) {
            return false;
        } else {
            Vector3d vector3d = RandomPositionGenerator.getPosAvoid(this.mob, 16, 7, this.toAvoid.position());
            if (vector3d == null) {
                return false;
            } else if (this.toAvoid.distanceToSqr(vector3d.x, vector3d.y, vector3d.z) < this.toAvoid.distanceToSqr(this.mob)) {
                return false;
            } else {
                this.path = this.pathNav.createPath(vector3d.x, vector3d.y, vector3d.z, 0);
                return this.path != null;
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    public void start() {
        this.pathNav.moveTo(this.path, this.walkSpeedModifier);
    }

    public void stop() {
        this.toAvoid = null;
    }

    public void tick() {
        if (this.mob.distanceToSqr(this.toAvoid) < 49.0D) {
            this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
        } else {
            this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
        }

    }

    @Nullable
    <T extends Entity> T getNearestLoadedEntity(Class<? extends T> p_225318_1_, EntityPredicate p_225318_2_, @Nullable LivingEntity p_225318_3_, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB p_225318_10_) {
        return this.getNearestEntity(this.mob.level.getLoadedEntitiesOfClass(p_225318_1_, p_225318_10_, null), p_225318_2_, p_225318_3_, p_225318_4_, p_225318_6_, p_225318_8_);
    }

    @Nullable
    <T extends Entity> T getNearestEntity(List<? extends T> p_217361_1_, EntityPredicate p_217361_2_, @Nullable LivingEntity p_217361_3_, double p_217361_4_, double p_217361_6_, double p_217361_8_) {
        double d0 = -1.0D;
        T t = null;

        for (T t1 : p_217361_1_) {
            double d1 = t1.distanceToSqr(p_217361_4_, p_217361_6_, p_217361_8_);
            if (d0 == -1.0D || d1 < d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }
}