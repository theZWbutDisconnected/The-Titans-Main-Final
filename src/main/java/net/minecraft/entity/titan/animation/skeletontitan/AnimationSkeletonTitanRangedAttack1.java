package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.titan.EntitySkeletonTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationSkeletonTitanRangedAttack1 extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanRangedAttack1(final EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 5;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 400;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && this.entity.getSkeletonType() != 1 && (this.entity.getTarget() == null || this.entity.distanceToSqr(this.entity.getTarget()) >= 800.0) && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
    }
}
