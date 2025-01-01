package net.minecraft.theTitans;

import net.minecraft.entity.Entity;
import net.minecraft.entity.titan.EntityHarcadiumArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class DamageSourceExtra extends DamageSource {
    public static DamageSource wip;
    public static DamageSource destroy;
    public static DamageSource lightningBolt;
    public static DamageSource mindCrush;
    public static DamageSource radiation;

    static {
        DamageSourceExtra.wip = new DamageSource("wip").bypassArmor().bypassInvul().bypassMagic();
        DamageSourceExtra.destroy = new DamageSource("outOfWorld").bypassArmor().bypassInvul().bypassMagic().setScalesWithDifficulty();
        DamageSourceExtra.lightningBolt = new DamageSource("lightningBolt").setIsFire().bypassArmor().bypassMagic();
        DamageSourceExtra.mindCrush = new DamageSource("mindcrush").bypassArmor().bypassMagic().bypassInvul();
        DamageSourceExtra.radiation = new DamageSource("radiation").bypassArmor().bypassMagic();
    }

    public DamageSourceExtra(final String damageTypeIn) {
        super(damageTypeIn);
    }

    public static DamageSource causeSoulStealingDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("soulSucking", p_92087_0_).bypassArmor().bypassMagic().bypassInvul().setMagic();
    }

    public static DamageSource causeSquishingDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("squash", p_92087_0_).bypassArmor().setScalesWithDifficulty();
    }

    public static DamageSource causeArmorPiercingMobDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("mob", p_92087_0_).bypassArmor().bypassMagic();
    }

    public static DamageSource causeAntiTitanDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("mob", p_92087_0_).bypassArmor();
    }

    public static DamageSource causeHarcadiumArrowDamage(final EntityHarcadiumArrow arrow, final Entity p_76353_1_) {
        return new IndirectEntityDamageSource("arrow", arrow, p_76353_1_).bypassInvul().bypassArmor();
    }

    public static DamageSource causeHomingSkullDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("arrow", p_92087_0_).bypassArmor();
    }

    public static DamageSource causeHomingSkullDamageVSEnderDragon(final Entity p_92087_0_) {
        return new EntityDamageSource("arrow", p_92087_0_).bypassArmor().setExplosion();
    }

    public static DamageSource causeVaroizationDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("vaporize", p_92087_0_).bypassArmor().setMagic();
    }

    public static DamageSource causeCreeperTitanExplosionDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("explosion.player", p_92087_0_).setScalesWithDifficulty().bypassArmor().setExplosion();
    }
}
