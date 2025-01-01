package net.minecraft.entity.titanminion;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.world.World;

public class EntityWitherzillaMinion extends WitherEntity {
    public EntityWitherzillaMinion(EntityType<? extends WitherEntity> p_i50226_1_, World p_i50226_2_) {
        super(p_i50226_1_, p_i50226_2_);
    }


    public void func_82206_m() {
        this.setInvulTime(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }

    private void setInvulTime(int i) {
    }
}
