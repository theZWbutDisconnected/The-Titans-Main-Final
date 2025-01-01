package net.minecraft.entity.titan.animation.skeletontitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.EntitySkeletonTitan;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import thehippomaster.AnimationAPI.AIAnimation;

import java.util.List;

public class AnimationSkeletonTitanAttack3 extends AIAnimation {
    private final EntitySkeletonTitan entity;

    public AnimationSkeletonTitanAttack3(final EntitySkeletonTitan test) {
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
        return 260;
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.animTick <= this.getDuration() && !this.entity.isStunned && this.entity.level.getBlockState(new BlockPos((int) this.entity.getX(), (int) this.entity.getY() - 1, (int) this.entity.getZ())).getBlock().getExplosionResistance() <= 500.0f && super.continueExecuting();
    }

    public void updateTask() {
        final EntitySkeletonTitan entity2 = this.entity;
        final EntitySkeletonTitan entity3 = this.entity;
        final float rotationYawHead = this.entity.yHeadRot;
        entity3.yRot = rotationYawHead;
        entity2.yBodyRot = rotationYawHead;
        if (this.entity.getAnimTick() < 10 && this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 180.0f, 40.0f);
        }
        if (this.entity.getAnimTick() == 160 && this.entity.getSkeletonType() == 1) {
            this.entity.playSound(TitanSounds.swordDrag, 10.0f, 1.0f);
        }
        if (this.entity.getAnimTick() == 90) {
            this.entity.shakeNearbyPlayerCameras(20000.0);
            this.entity.playSound(TitanSounds.titanSlam, 100.0f, 1.0f);
            this.entity.playSound(TitanSounds.titanPress, 100.0f, 1.0f);
            final float d0 = (this.entity.getSkeletonType() == 1) ? 64.0f : 32.0f;
            final float f3 = this.entity.yBodyRot * 3.1415927f / 180.0f;
            final float f4 = MathHelper.sin(f3);
            final float f5 = MathHelper.cos(f3);
            final float f6 = (float) this.entity.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
            final int i = this.entity.getKnockbackAmount();
            this.entity.collideWithEntities(this.entity.pelvis, this.entity.level.getEntities(this.entity, this.entity.pelvis.getBoundingBox().inflate(16.0, 8.0, 16.0)));
            final List list11 = this.entity.level.getEntities(this.entity, this.entity.getBoundingBox().inflate(32.0, 2.0, 32.0).move(-(f4 * d0), -8.0, f5 * d0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity1 = (Entity) list11.get(i2);
                    if (this.entity.canAttack(entity1.getClass())) {
                        this.entity.attackChoosenEntity(entity1, f6 * 15.0f, i);
                    }
                }
            }
        }
    }
}
