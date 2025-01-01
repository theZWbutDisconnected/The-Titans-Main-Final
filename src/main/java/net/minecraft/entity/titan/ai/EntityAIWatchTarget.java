package net.minecraft.entity.titan.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EntityAIWatchTarget
        extends Goal {
    private final MobEntity theWatcher;
    private final float maxDistance;
    protected Entity targetEntity;
    private int lookTime;

    public EntityAIWatchTarget(MobEntity watcher, float dist) {
        this.theWatcher = watcher;
        this.maxDistance = dist;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    public boolean canUse() {
        if (this.theWatcher.getTarget() == null) {
            return false;
        }
        this.targetEntity = this.theWatcher.getTarget();
        return true;
    }

    public boolean canContinueToUse() {
        return this.targetEntity.isAlive() && (!(this.theWatcher.distanceToSqr(this.targetEntity) > (double) (this.maxDistance * this.maxDistance)) && this.lookTime > 0);
    }

    public void start() {
        this.lookTime = 120 + this.theWatcher.getRandom().nextInt(40);
    }

    public void stop() {
        this.targetEntity = null;
    }

    public void tick() {
        this.theWatcher.getLookControl().setLookAt(this.targetEntity.getX(), this.targetEntity.getY() + (double) this.targetEntity.getEyeHeight(), this.targetEntity.getZ(), 10.0f, this.theWatcher.getMaxHeadXRot());
        --this.lookTime;
    }
}
