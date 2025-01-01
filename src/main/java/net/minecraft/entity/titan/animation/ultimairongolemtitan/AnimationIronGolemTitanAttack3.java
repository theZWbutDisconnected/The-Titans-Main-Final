package net.minecraft.entity.titan.animation.ultimairongolemtitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntityIronGolemTitan;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.SoundEvents;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationIronGolemTitanAttack3 extends AIAnimation {
    private final EntityIronGolemTitan entity;

    public AnimationIronGolemTitanAttack3(final EntityIronGolemTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 8;
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
        return this.entity.animTick <= 60 && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 24 && this.entity.getTarget() != null) {
            this.entity.lookAt(this.entity.getTarget(), 180.0f, 180.0f);
        }
        if (this.entity.getAnimTick() == 26) {
            this.entity.playSound(TitanSounds.titanSwing, 100.0f, 1.0f);
            this.entity.playSound(SoundEvents.IRON_GOLEM_ATTACK, 100.0f, 0.5f);
            this.entity.playSound(SoundEvents.IRON_GOLEM_ATTACK, 100.0f, 0.5f);
        }
        if (this.entity.getAnimTick() == 28 && this.entity.getTarget() != null) {
            final float f = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.entity.getKnockbackAmount();
            this.entity.doHurtTarget(this.entity.getTarget());
            this.entity.attackChoosenEntity(this.entity.getTarget(), f, i);
            final List list11 = this.entity.level.getEntities(this.entity.getTarget(), this.entity.getTarget().getBoundingBox().inflate(6.0, 2.0, 6.0));
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
