package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.titan.EntitySkeletonTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationSkeletonTitanStun extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanStun(final EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 8;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 540;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= 540 && super.continueExecuting();
    }
}
