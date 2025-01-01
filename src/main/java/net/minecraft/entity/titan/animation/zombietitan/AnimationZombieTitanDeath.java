package net.minecraft.entity.titan.animation.zombietitan;

import net.minecraft.entity.titan.EntityZombieTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationZombieTitanDeath extends AIAnimation {
    private final EntityZombieTitan entity;

    public AnimationZombieTitanDeath(final EntityZombieTitan test) {
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
