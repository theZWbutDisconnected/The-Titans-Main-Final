package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntitySkeletonTitan;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationSkeletonTitanAttack1 extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanAttack1(final EntitySkeletonTitan test) {
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
        return 100;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 20 && this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
        if (this.entity.getAnimTick() == 58) {
            final float d0 = (this.entity.getSkeletonType() == 1) ? 16.0f : 8.0f;
            final float f3 = this.entity.yBodyRot * 3.1415927f / 180.0f;
            final float f4 = MathHelper.sin(f3);
            final float f5 = MathHelper.cos(f3);
            this.entity.level.explode(this.entity, this.entity.getX() - f4 * d0, this.entity.getY(), this.entity.getZ() + f5 * d0, 5.0f, false, Explosion.Mode.NONE);
            final float f6 = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.entity.getKnockbackAmount();
            this.entity.collideWithEntities(this.entity.pelvis, this.entity.level.getEntities(this.entity, this.entity.pelvis.getBoundingBox().inflate(16.0, 8.0, 16.0)));
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(32.0, 2.0, 32.0).move(-(f4 * d0), -8.0, f5 * d0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1.getClass())) {
                        this.entity.attackChoosenEntity(entity1, f6, i);
                    }
                }
            }
            if (this.entity.getSkeletonType() == 1) {
                this.entity.playSound(TitanSounds.titanStrike, 20.0f, 0.9f);
            } else {
                this.entity.playSound(TitanSounds.titanStrike, 20.0f, 1.0f);
            }
        }
    }
}
