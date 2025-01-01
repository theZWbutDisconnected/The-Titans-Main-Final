package net.minecraft.entity.titan.ai;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.InteractDoorGoal;
import net.minecraft.entity.titanminion.EntityCreeperMinion;
import net.minecraft.entity.titanminion.EnumMinionType;
import net.minecraft.entity.titanminion.IMinion;
import net.minecraft.item.AxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class EntityAIBreakDoorMinion extends InteractDoorGoal {
    private int breakingTime;
    private int field_75358_j;

    public EntityAIBreakDoorMinion(final MobEntity p_i1618_1_) {
        super(p_i1618_1_);
        this.field_75358_j = -1;
    }

    public boolean canUse() {
        return super.canUse() && this.mob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && !DoorBlock.isWoodenDoor(this.mob.level, new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()));
    }

    public void start() {
        super.start();
        this.breakingTime = 0;
    }

    public boolean continueExecuting() {
        final double d0 = this.mob.distanceToSqr(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ());
        return this.breakingTime <= 240 && !DoorBlock.isWoodenDoor(this.mob.level, new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ())) && d0 < 4.0;
    }

    public void stop() {
        super.stop();
        this.mob.level.destroyBlock(new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()), true, this.mob, -1);
    }

    public void tick() {
        super.tick();
        this.mob.getLookControl().setLookAt(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ(), 180.0f, 30.0f);
        final int dam = (int) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
        if (this.mob instanceof EntityCreeperMinion && ((EntityCreeperMinion) this.mob).getMinionTypeInt() != 3) {
            ((EntityCreeperMinion) this.mob).ignite();
        } else if (this.mob.tickCount % 20 == 0) {
            this.breakingTime += dam;
            if (!this.mob.getMainHandItem().isEmpty()) {
                this.breakingTime += dam;
                if (this.mob.getMainHandItem().getItem() instanceof AxeItem) {
                    this.breakingTime += dam * 3;
                }
            }
            this.mob.swing(Hand.MAIN_HAND);
            this.mob.level.levelEvent(1010, new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()), 0);
        }
        final int i = (int) (this.breakingTime / 240.0f * 10.0f);
        if (i != this.field_75358_j) {
            this.mob.level.destroyBlock(new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()), true, this.mob, i);
            this.field_75358_j = i;
        }
        if (this.breakingTime >= 240 || (this.mob instanceof IMinion && ((IMinion) this.mob).getMinionType() == EnumMinionType.TEMPLAR)) {
            this.mob.level.setBlock(new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()), Blocks.AIR.defaultBlockState(), 0, 3);
            this.mob.level.levelEvent(1012, new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()), 0);
            this.mob.level.levelEvent(2001, new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()), Block.getId(this.mob.level.getBlockState(new BlockPos(this.doorPos.getX(), this.doorPos.getY(), this.doorPos.getZ()))));
        }
    }
}
