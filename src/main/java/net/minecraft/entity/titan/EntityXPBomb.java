package net.minecraft.entity.titan;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.world.World;

public class EntityXPBomb extends ExperienceOrbEntity {
    public EntityXPBomb(World p_i1585_1_, double p_i1585_2_, double p_i1585_4_, double p_i1585_6_) {
        super(p_i1585_1_, p_i1585_2_, p_i1585_4_, p_i1585_6_, 0);
    }

    public void setXPCount(int i) {
        this.value = i;
    }
}
