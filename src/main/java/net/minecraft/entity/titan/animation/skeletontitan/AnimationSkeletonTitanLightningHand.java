package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.titan.EntitySkeletonTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationSkeletonTitanLightningHand extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanLightningHand(final EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 12;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 70;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
    }
}
