package net.minecraft.entity.titanminion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.world.World;

public class EntityGhastMinion extends GhastEntity {
    public EntityGhastMinion(EntityType<? extends GhastEntity> p_i50206_1_, World p_i50206_2_) {
        super(p_i50206_1_, p_i50206_2_);
    }
    public EntityGhastMinion(World p_i50206_2_) {
        super(EntityType.GHAST, p_i50206_2_);
    }

    public void setMinionType(int i) {
    }
}
