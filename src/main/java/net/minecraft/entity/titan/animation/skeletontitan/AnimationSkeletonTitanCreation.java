package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.titan.EntitySkeletonTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationSkeletonTitanCreation extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanCreation(final EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 13;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 700;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= 700 && super.continueExecuting();
    }
}
