package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.titan.EntitySkeletonTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationSkeletonTitanDeath extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanDeath(final EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 10;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 2000;
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.deathTicks > 0 && !this.entity.isAlive() && super.shouldExecute();
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.deathTicks > 0 && !this.entity.isAlive() && super.continueExecuting();
    }
}
