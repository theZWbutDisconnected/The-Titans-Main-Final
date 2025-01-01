//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thehippomaster.AnimationAPI;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public abstract class AIAnimation extends Goal {
    private final IAnimatedEntity animatedEntity;

    public AIAnimation(IAnimatedEntity entity) {
        this.animatedEntity = entity;
    }

    public abstract int getAnimID();

    public LivingEntity getEntity() {
        return (LivingEntity) this.animatedEntity;
    }

    public abstract boolean isAutomatic();

    public abstract int getDuration();

    public boolean shouldAnimate() {
        return false;
    }

    @Override
    public boolean canUse() {
        return shouldExecute();
    }

    @Override
    public void start() {
        startExecuting();
    }

    @Override
    public void tick() {
        updateTask();
    }

    @Override
    public boolean canContinueToUse() {
        return continueExecuting();
    }

    @Override
    public void stop() {
        resetTask();
    }

    /**
     * The old methods calling
     */
    public boolean shouldExecute() {
        if (this.isAutomatic()) {
            return this.animatedEntity.getAnimID() == this.getAnimID();
        } else {
            return this.shouldAnimate();
        }
    }

    public void startExecuting() {
        if (!this.isAutomatic()) {
            AnimationAPI.sendAnimPacket(this.animatedEntity, this.getAnimID());
        }
        this.animatedEntity.setAnimTick(0);
    }

    public boolean continueExecuting() {
        return this.animatedEntity.getAnimTick() < this.getDuration() && this.getEntity().tickCount > 0;
    }

    public void updateTask() {
    }

    public void resetTask() {
        AnimationAPI.sendAnimPacket(this.animatedEntity, 0);
    }
}
