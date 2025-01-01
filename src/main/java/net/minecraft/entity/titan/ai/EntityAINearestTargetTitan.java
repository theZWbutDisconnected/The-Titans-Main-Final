package net.minecraft.entity.titan.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.EntityTitan;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class EntityAINearestTargetTitan
        extends TargetGoal {
    protected final Class<? extends Entity> targetClass;
    protected final Sorter theNearestAttackableTargetSorter;
    protected Predicate<Entity> targetEntitySelector;
    protected LivingEntity targetEntity;

    public EntityAINearestTargetTitan(CreatureEntity p_i1663_1_, Class<? extends Entity> p_i1663_2_, int p_i1663_3_, boolean p_i1663_4_) {
        this(p_i1663_1_, p_i1663_2_, p_i1663_3_, p_i1663_4_, false);
    }

    public EntityAINearestTargetTitan(CreatureEntity p_i1664_1_, Class<? extends Entity> p_i1664_2_, int p_i1664_3_, boolean p_i1664_4_, boolean p_i1664_5_) {
        this(p_i1664_1_, p_i1664_2_, p_i1664_3_, p_i1664_4_, p_i1664_5_, null);
    }

    public EntityAINearestTargetTitan(CreatureEntity p_i1665_1_, Class<? extends Entity> p_i1665_2_, int p_i1665_3_, boolean p_i1665_4_, boolean p_i1665_5_, final Predicate<Entity> p_i1665_6_) {
        super(p_i1665_1_, p_i1665_4_, p_i1665_5_);
        this.targetClass = p_i1665_2_;
        this.theNearestAttackableTargetSorter = new Sorter(p_i1665_1_);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = new Predicate<Entity>() {
            private final EntityPredicate TARGET_ENTITY_SELECTOR = new EntityPredicate().allowUnseeable().ignoreInvisibilityTesting();

            @Override
            public boolean apply(Entity p_82704_1_) {
                return p_82704_1_ instanceof LivingEntity && ((p_i1665_6_ == null || p_i1665_6_.apply(p_82704_1_)) && EntityAINearestTargetTitan.this.canAttack((LivingEntity) p_82704_1_, this.TARGET_ENTITY_SELECTOR));
            }
        };
    }

    public boolean canUse() {
        double d0 = this.getFollowDistance();
        List list = this.mob.level.getEntitiesOfClass(this.targetClass, this.mob.getBoundingBox().inflate(d0, d0, d0), this.targetEntitySelector);
        Collections.sort(list, this.theNearestAttackableTargetSorter);
        if (list.isEmpty()) {
            return false;
        }
        this.targetEntity = (LivingEntity) list.get(0);
        return true;
    }

    public void start() {
        if (!this.mob.level.isClientSide) {
            this.mob.setTarget(this.targetEntity);
        }
        super.start();
    }

    protected boolean canAttack(LivingEntity p_75296_1_, EntityPredicate targetPredicate) {
        if (p_75296_1_ instanceof EntityTitan) {
            return true;
        }
        if (p_75296_1_ == null) {
            return false;
        }
        if (p_75296_1_ == this.mob) {
            return false;
        }
        if (!p_75296_1_.isAlive()) {
            return false;
        }
        if (!this.mob.canAttack(p_75296_1_)) {
            return false;
        }
        if (p_75296_1_ instanceof PlayerEntity && p_75296_1_.createCommandSourceStack().getTextName() == "Boom337317") {
            return false;
        }
        if ((this.mob instanceof EntityTitan || this.mob instanceof MobEntity) && p_75296_1_ instanceof PlayerEntity && p_75296_1_.createCommandSourceStack().getTextName() == "Umbrella_Ghast") {
            return false;
        }
        if (p_75296_1_ instanceof EntityTitan && (((EntityTitan) p_75296_1_).getInvulTime() > 0 || ((EntityTitan) p_75296_1_).getWaiting() || ((EntityTitan) p_75296_1_).animID == 13)) {
            return false;
        }
        if (this.mob instanceof EntityTitan && this.mob.getLastHurtByMob() != null) {
            return false;
        }
        return super.canAttack(p_75296_1_, targetPredicate);
    }

    public static class Sorter
            implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity p_i1662_1_) {
            this.theEntity = p_i1662_1_;
        }

        public int isCompare(Entity p_compare_1_, Entity p_compare_2_) {
            double d1;
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            return d0 > (d1 = this.theEntity.distanceToSqr(p_compare_2_)) ? 1 : (d0 < d1 ? -1 : 0);
        }

        @Override
        public int compare(Entity o1, Entity o2) {
            return this.isCompare(o1, o2);
        }
    }
}

