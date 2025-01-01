package net.minecraft.entity.titan;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.world.World;

public class EntityWitherTurret extends WitherEntity {
    public EntityWitherTurret(EntityType<? extends WitherEntity> p_i50226_1_, World p_i50226_2_) {
        super(p_i50226_1_, p_i50226_2_);
    }

    public boolean isPlayerCreated() {
        return false;
    }
}
