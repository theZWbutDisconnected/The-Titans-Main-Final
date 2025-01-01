package net.minecraft.entity.titan.animation.ultimairongolemtitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntityIronGolemTitan;
import net.minecraft.theTitans.TitanSounds;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationIronGolemTitanAttack2 extends AIAnimation {
    private final EntityIronGolemTitan entity;

    public AnimationIronGolemTitanAttack2(final EntityIronGolemTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 7;
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
        return this.entity.animTick <= 150 && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 60 && this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
        if (this.entity.getAnimTick() == 60 || this.entity.getAnimTick() == 104) {
            this.entity.shakeNearbyPlayerCameras(20000.0);
            final float f = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = 0;
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(48.0, 16.0, 48.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1)) {
                        final Entity entity2 = entity1;
                        entity2.push(0.0, 2.0 + this.entity.getRandom().nextDouble() + this.entity.getRandom().nextDouble(), 0.0);
                        this.entity.doHurtTarget(entity1);
                    }
                }
            }
            this.entity.playSound(TitanSounds.titanSlam, 20.0f, 1.0f);
        }
    }
}
