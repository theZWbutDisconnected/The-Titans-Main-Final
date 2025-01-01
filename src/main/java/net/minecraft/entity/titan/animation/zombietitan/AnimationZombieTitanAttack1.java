package net.minecraft.entity.titan.animation.zombietitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.math.vector.Vector3d;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationZombieTitanAttack1 extends AIAnimation {
    private final EntityZombieTitan entity;

    public AnimationZombieTitanAttack1(final EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 3;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 70;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 30 && this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
        if (this.entity.getAnimTick() == 28) {
            this.entity.playSound(TitanSounds.titanSwing, 10.0f, 1.0f);
        }
        if (this.entity.getAnimTick() == 32) {
            final double d8 = 12.0;
            final Vector3d vec3 = this.entity.getLookAngle();
            final double dx = vec3.x * d8;
            final double dz = vec3.z * d8;
            final float f = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.entity.getKnockbackAmount();
            this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(16.0, 8.0, 16.0)));
            this.entity.shakeNearbyPlayerCameras(20000.0);
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(32.0, 2.0, 32.0).move(dx, 0.0, dz));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1)) {
                        this.entity.attackChoosenEntity(entity1, f, i);
                    }
                }
            }
            this.entity.playSound(TitanSounds.titanStrike, 20.0f, 1.0f);
        }
    }
}
