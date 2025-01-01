package net.minecraft.entity.titanminion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.world.World;

public class EntityCreeperMinion extends CreeperEntity implements ITemplar {
    public EntityCreeperMinion(EntityType<? extends CreeperEntity> p_i50213_1_, World p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
    }

    @Override
    public EnumMinionType getMinionType() {
        return null;
    }

    @Override
    public void TransformEntity(Entity p0) {

    }

    public int getMinionTypeInt() {
        return 0;
    }
}
