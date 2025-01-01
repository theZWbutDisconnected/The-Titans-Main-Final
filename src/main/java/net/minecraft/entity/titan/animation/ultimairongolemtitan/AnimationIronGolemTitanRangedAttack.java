package net.minecraft.entity.titan.animation.ultimairongolemtitan;

import net.minecraft.entity.titan.EntityIronGolemTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationIronGolemTitanRangedAttack extends AIAnimation {
    private final EntityIronGolemTitan entity;

    public AnimationIronGolemTitanRangedAttack(final EntityIronGolemTitan test) {
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
        return 80;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
    }
}
