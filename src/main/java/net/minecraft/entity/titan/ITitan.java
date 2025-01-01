package net.minecraft.entity.titan;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titanminion.*;

public interface ITitan {
    Predicate<Entity> SearchForAThingToKill = new Predicate<Entity>() {
        public boolean apply(final Entity p_179983_1_) {
            return p_179983_1_ != null && !(p_179983_1_ instanceof EntityTitanSpirit) && (!(p_179983_1_ instanceof EntityTitan) || p_179983_1_ instanceof EntitySnowGolemTitan || p_179983_1_ instanceof EntityIronGolemTitan || p_179983_1_ instanceof EntityGargoyleTitan || p_179983_1_ instanceof EntityWitherzilla) && !(p_179983_1_ instanceof IMinion) && (!(p_179983_1_ instanceof PlayerEntity) || !((PlayerEntity) p_179983_1_).isCreative()) && !(p_179983_1_ instanceof ZombieEntity) && !(p_179983_1_ instanceof SkeletonEntity) && !(p_179983_1_ instanceof SpiderEntity) && !(p_179983_1_ instanceof CreeperEntity) && !(p_179983_1_ instanceof EndermanEntity) && !(p_179983_1_ instanceof BlazeEntity) && !(p_179983_1_ instanceof GhastEntity) && !(p_179983_1_ instanceof WitherEntity) && !(p_179983_1_ instanceof EnderDragonEntity) && !(p_179983_1_ instanceof SilverfishEntity) && !(p_179983_1_ instanceof SlimeEntity) && !(p_179983_1_ instanceof EntityGiantZombieBetter) && p_179983_1_.isAlive();
        }
    };
    //    Predicate<Entity> BlazeTitanSorter = new Predicate<Entity>() {
//        public boolean apply(final Entity p_179983_1_) {
//            return !(p_179983_1_ instanceof EntityBlazeMinion) && !(p_179983_1_ instanceof EntityBlazeTitan);
//        }
//    };
//    Predicate<Entity> CaveSpiderTitanSorter = new Predicate<Entity>() {
//        public boolean apply(final Entity p_179983_1_) {
//            return !(p_179983_1_ instanceof EntityCaveSpiderMinion) && !(p_179983_1_ instanceof EntityCaveSpiderTitan);
//        }
//    };
//    Predicate<Entity> CreeperTitanSorter = new Predicate<Entity>() {
//        public boolean apply(final Entity p_179983_1_) {
//            return (!(p_179983_1_ instanceof EntityCreeperMinion) && !(p_179983_1_ instanceof EntityCreeperTitan)) || (Loader.isModLoaded("MutantCreatures") && !(p_179983_1_ instanceof MutantCreeper));
//        }
//    };
//    Predicate<Entity> EnderColossusSorter = new Predicate<Entity>() {
//        public boolean apply(final Entity p_179983_1_) {
//            return (!(p_179983_1_ instanceof EntityEndermanMinion) && !(p_179983_1_ instanceof EntityEnderColossus) && !(p_179983_1_ instanceof EntityDragon) && !(p_179983_1_ instanceof EntityDragonMinion) && !(p_179983_1_ instanceof EntityEnderColossusCrystal)) || (Loader.isModLoaded("MutantCreatures") && !(p_179983_1_ instanceof MutantEnderman));
//        }
//    };
    Predicate<Entity> GhastTitanSorter = new Predicate<Entity>() {
        public boolean apply(final Entity p_179983_1_) {
            return !(p_179983_1_ instanceof EntityGhastMinion) && !(p_179983_1_ instanceof EntityGhastTitan);
        }
    };
    Predicate<Entity> MagmaCubeTitanSorter = new Predicate<Entity>() {
        public boolean apply(final Entity p_179983_1_) {
            return !(p_179983_1_ instanceof MagmaCubeEntity) && (!(p_179983_1_ instanceof EntityMagmaCubeTitan) || p_179983_1_ instanceof EntitySlimeTitan);
        }
    };
//    Predicate<Entity> PigZombieTitanSorter = new Predicate<Entity>() {
//        public boolean apply(final Entity p_179983_1_) {
//            return !(p_179983_1_ instanceof EntityPigZombieMinion) && !(p_179983_1_ instanceof EntityGhastGuard) && !(p_179983_1_ instanceof EntityPigZombieTitan);
//        }
//    };
//    Predicate<Entity> SilverfishTitanSorter = new Predicate<Entity>() {
//        public boolean apply(final Entity p_179983_1_) {
//            return !(p_179983_1_ instanceof EntitySilverfishMinion) && !(p_179983_1_ instanceof EntitySilverfishTitan);
//        }
//    };
    Predicate<Entity> SkeletonTitanSorter = new Predicate<Entity>() {
        public boolean apply(final Entity p_179983_1_) {
            return (!(p_179983_1_ instanceof EntitySkeletonMinion) && !(p_179983_1_ instanceof EntitySkeletonTitan) && !(p_179983_1_ instanceof EntityWitherMinion)) || !(p_179983_1_.getClass().getName().contains("MutantSkeleton"));
        }
    };
    Predicate<Entity> SlimeTitanSorter = new Predicate<Entity>() {
        public boolean apply(final Entity p_179983_1_) {
            return (!(p_179983_1_ instanceof SlimeEntity) || p_179983_1_ instanceof MagmaCubeEntity) && (!(p_179983_1_ instanceof EntitySlimeTitan) || p_179983_1_ instanceof EntityMagmaCubeTitan);
        }
    };
//    Predicate<Entity> SpiderTitanSorter = new Predicate<Entity>() {
//        public boolean apply(final Entity p_179983_1_) {
//            return !(p_179983_1_ instanceof EntitySpiderMinion) && (!(p_179983_1_ instanceof EntitySpiderTitan) || p_179983_1_ instanceof EntityCaveSpiderTitan);
//        }
//    };
    Predicate<Entity> ZombieTitanSorter = new Predicate<Entity>() {
        public boolean apply(final Entity p_179983_1_) {
            return (!(p_179983_1_ instanceof EntityZombieMinion) && !(p_179983_1_ instanceof EntityZombieTitan) && !(p_179983_1_ instanceof EntityGiantZombieBetter) || !p_179983_1_.getClass().getName().contains("MutantZombie"));
        }
    };

    EnumTitanStatus getTitanStatus();
}
