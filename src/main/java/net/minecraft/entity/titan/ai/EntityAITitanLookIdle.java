package net.minecraft.entity.titan.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.titan.EntityTitan;

import java.util.EnumSet;

public class EntityAITitanLookIdle
        extends Goal {
    private final EntityTitan idleEntity;
    private double lookX;
    private double lookZ;
    private int idleTime;

    public EntityAITitanLookIdle(EntityTitan p_i1647_1_) {
        this.idleEntity = p_i1647_1_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return !this.idleEntity.getWaiting() && this.idleEntity.animID == 0 && this.idleEntity.getTarget() == null && this.idleEntity.getRandom().nextFloat() < 0.1f;
    }

    public boolean canContinueToUse() {
        return this.idleTime >= 0;
    }

    public void start() {
        double d0 = 24.0 * this.idleEntity.getRandom().nextDouble();
        this.lookX = Math.cos(d0);
        this.lookZ = Math.sin(d0);
        this.idleTime = 80 + this.idleEntity.getRandom().nextInt(40);
    }

    public void tick() {
        --this.idleTime;
        this.idleEntity.getLookControl().setLookAt(this.idleEntity.getX() + this.lookX, this.idleEntity.getY() + (double) this.idleEntity.getEyeHeight(), this.idleEntity.getZ() + this.lookZ, 2.0f, this.idleEntity.getMaxHeadXRot());
    }
}
