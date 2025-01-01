package net.minecraft.entity.titan.animation.zombietitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.DamageSource;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationZombieTitanRoar extends AIAnimation {
    private final EntityZombieTitan entity;

    public AnimationZombieTitanRoar(final EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 11;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 200;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() < 20 && this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
        if (this.entity.getAnimTick() >= 20) {
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(64.0, 64.0, 64.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i1 = 0; i1 < list11.size(); ++i1) {
                    final Entity entity1 = (Entity) list11.get(i1);
                    if (this.entity.canAttack(entity1) && entity1 instanceof LivingEntity) {
                        ((LivingEntity) entity1).hurtDuration = 0;
                    }
                    if (!this.entity.level.isClientSide && entity1 instanceof LivingEntity && !this.entity.canAttack((LivingEntity) entity1)) {
                        ((LivingEntity) entity1).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 20, 4));
                    }
                }
            }
        }
        if (this.entity.getAnimTick() == 20) {
            this.entity.playSound(TitanSounds.titanZombieRoar, 1000.0f, 1.0f);
            this.entity.collideWithEntities(this.entity.head, this.entity.level.getEntities(this.entity, this.entity.head.getBoundingBox().inflate(64.0, 64.0, 64.0)));
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(64.0, 64.0, 64.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i1 = 0; i1 < list11.size(); ++i1) {
                    final Entity entity1 = (Entity) list11.get(i1);
                    if (this.entity.canAttack(entity1)) {
                        entity1.hurt(DamageSource.thorns(this.entity), 40.0f);
                    }
                }
            }
        }
    }
}
