package net.minecraft.theTitans;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CreeperEntity;

import java.util.Comparator;

public class TargetingSorter implements Comparator<Entity> {
    private final Entity theEntity;

    public TargetingSorter(final Entity par2Entity) {
        this.theEntity = par2Entity;
    }

    public int compareDistanceSq(final Entity par1Entity, final Entity par2Entity) {
        double weight = 0.0;
        double var3 = this.theEntity.distanceToSqr(par1Entity);
        if (par1Entity instanceof CreeperEntity) {
            var3 /= 2.0;
        }
        weight = par1Entity.getBbHeight() * par1Entity.getBbWidth();
        if (weight > 1.0) {
            var3 /= weight;
        }
        double var4 = this.theEntity.distanceToSqr(par2Entity);
        if (par2Entity instanceof CreeperEntity) {
            var4 /= 2.0;
        }
        weight = par2Entity.getBbHeight() * par2Entity.getBbWidth();
        if (weight > 1.0) {
            var4 /= weight;
        }
        return Double.compare(var3, var4);
    }

    @Override
    public int compare(final Entity par1Obj, final Entity par2Obj) {
        return this.compareDistanceSq(par1Obj, par2Obj);
    }
}
