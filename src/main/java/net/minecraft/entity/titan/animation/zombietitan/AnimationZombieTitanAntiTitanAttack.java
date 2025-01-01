package net.minecraft.entity.titan.animation.zombietitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.theTitans.TitanSounds;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationZombieTitanAntiTitanAttack extends AIAnimation {
    private final EntityZombieTitan entity;

    public AnimationZombieTitanAntiTitanAttack(final EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    @Override
    public int getAnimID() {
        return 1;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public int getDuration() {
        return 30;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && super.continueExecuting();
    }

    public void updateTask() {
        if (this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 5.0f, 40.0f);
        }
        if (this.entity.getAnimTick() == 12 && this.entity.getTarget() != null) {
            this.entity.shakeNearbyPlayerCameras(20000.0);
            final float f = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.entity.getKnockbackAmount();
            this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(48.0, 48.0, 48.0)));
            this.entity.attackChoosenEntity(this.entity.getTarget(), f, i);
            this.entity.attackChoosenEntity(this.entity.getTarget(), f, i);
            this.entity.attackChoosenEntity(this.entity.getTarget(), f, i);
            this.entity.attackChoosenEntity(this.entity.getTarget(), f, i);
            if (this.entity.isArmored()) {
                this.entity.playSound(TitanSounds.titanStrike, 20.0f, 1.0f);
                this.entity.playSound(TitanSounds.titanStrike, 20.0f, 1.05f);
                this.entity.playSound(TitanSounds.titanStrike, 20.0f, 1.1f);
                this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(26.0, 26.0, 26.0)));
                this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(26.0, 26.0, 26.0)));
                this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(26.0, 26.0, 26.0)));
            }
            final List list11 = this.entity.level.getEntities(this.entity.getTarget(), this.entity.getTarget().getBoundingBox().inflate(8.0, 8.0, 8.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1)) {
                        this.entity.attackChoosenEntity(entity1, f, i);
                        this.entity.attackChoosenEntity(entity1, f, i);
                        this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(6.0, 6.0, 6.0)));
                        this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(6.0, 6.0, 6.0)));
                        this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(6.0, 6.0, 6.0)));
                        this.entity.collideWithEntities(this.entity.body, this.entity.level.getEntities(this.entity, this.entity.body.getBoundingBox().inflate(6.0, 6.0, 6.0)));
                    }
                }
            }
        }
    }
}
