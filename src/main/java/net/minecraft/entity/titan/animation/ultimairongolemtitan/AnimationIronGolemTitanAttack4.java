package net.minecraft.entity.titan.animation.ultimairongolemtitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntityIronGolemTitan;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationIronGolemTitanAttack4 extends AIAnimation {
    private final EntityIronGolemTitan entity;

    public AnimationIronGolemTitanAttack4(final EntityIronGolemTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 9;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 90;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= 90 && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 40 && this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
        if (this.entity.getAnimTick() == 44) {
            this.entity.playSound(TitanSounds.titanSwing, 100.0f, 1.0f);
            this.entity.playSound(SoundEvents.IRON_GOLEM_ATTACK, 100.0f, 0.5f);
        }
        if (this.entity.getAnimTick() == 48) {
            this.entity.shakeNearbyPlayerCameras(20000.0);
            final double d8 = 24.0;
            final Vector3d vec3 = this.entity.getLookAngle();
            final double dx = vec3.x * d8;
            final double dz = vec3.z * d8;
            final float f = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.entity.getKnockbackAmount();
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(32.0, 4.0, 32.0).move(dx, -2.0, dz));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1)) {
                        this.entity.doHurtTarget(entity1);
                        entity1.push(-MathHelper.sin(this.entity.yHeadRot * 3.1415927f / 180.0f) * 3.0f, 0.0, MathHelper.cos(this.entity.yHeadRot * 3.1415927f / 180.0f) * 3.0f);
                    }
                }
            }
            this.entity.playSound(TitanSounds.titanSlam, 20.0f, 1.0f);
            this.entity.playSound(TitanSounds.titanStep, 20.0f, 1.0f);
            this.entity.playSound(TitanSounds.titanStep, 20.0f, 1.0f);
            this.entity.playSound(TitanSounds.titanStep, 20.0f, 1.0f);
            this.entity.playSound(TitanSounds.titanStep, 20.0f, 1.0f);
        }
    }
}
