package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.titan.EntitySkeletonTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationSkeletonTitanRangedAttack2 extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanRangedAttack2(final EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 11;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 120;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && this.entity.getSkeletonType() != 1 && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
    }
}
