package net.minecraft.entity.titan.animation.zombietitan;

import net.minecraft.entity.titan.EntityZombieTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationZombieTitanLightningAttack extends AIAnimation {
    private final EntityZombieTitan entity;

    public AnimationZombieTitanLightningAttack(final EntityZombieTitan test) {
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
        return 110;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 0.0f);
        }
    }
}
