package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntitySkeletonTitan;
import net.minecraft.theTitans.TitanSounds;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationSkeletonTitanAttack2 extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanAttack2(final EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 6;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 150;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 60 && this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
        if (this.entity.getAnimTick() == 60 || this.entity.getAnimTick() == 104) {
            final float f = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = 0;
            this.entity.collideWithEntities(this.entity.pelvis, this.entity.level.getEntities(this.entity, this.entity.pelvis.getBoundingBox().inflate(16.0, 8.0, 16.0)));
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(32.0, 8.0, 32.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1.getClass())) {
                        final Entity entity2 = entity1;
                        entity2.setDeltaMovement(entity2.getDeltaMovement().x, 1.0f + this.entity.getRandom().nextFloat() + this.entity.getRandom().nextFloat(), entity2.getDeltaMovement().z);
                        this.entity.attackChoosenEntity(entity1, f, i);
                    }
                }
            }
            this.entity.shakeNearbyPlayerCameras(20000.0);
            this.entity.playSound(TitanSounds.titanSlam, 100.0f, 1.0f);
            this.entity.playSound(TitanSounds.titanPress, 100.0f, 1.0f);
        }
    }
}
