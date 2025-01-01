package net.minecraft.entity.titan.animation.zombietitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.theTitans.TitanSounds;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationZombieTitanAttack5 extends AIAnimation {
    private final EntityZombieTitan entity;

    public AnimationZombieTitanAttack5(final EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 4;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 60;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 36 && this.entity.getTarget() != null) {
            this.entity.lookAt(this.entity.getTarget(), 180.0f, 180.0f);
        }
        if (this.entity.getAnimTick() == 30 && this.entity.getTarget() != null) {
            this.entity.playSound(TitanSounds.titanSwing, 5.0f, 1.0f);
            final float f = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.entity.getKnockbackAmount() * 20;
            final LivingEntity attackTarget = this.entity.getTarget();
            attackTarget.lerpMotion(0.0, 2.0, 0.0);
            this.entity.collideWithEntities(this.entity.head, this.entity.level.getEntities(this.entity, this.entity.head.getBoundingBox().inflate(3.0, 3.0, 3.0)));
            this.entity.attackChoosenEntity(this.entity.getTarget(), f, i);
            final List list11 = this.entity.level.getEntities(this.entity.getTarget(), this.entity.getTarget().getBoundingBox().inflate(3.0, 3.0, 3.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1)) {
                        this.entity.attackChoosenEntity(entity1, f, i);
                    }
                }
            }
        }
    }
}
