package net.minecraft.theTitans;

import com.zerwhit.annotations.EventRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.titan.*;
import net.minecraft.entity.titanminion.EntityGiantZombieBetter;
import net.minecraft.entity.titanminion.EntityZombieMinion;

public class RenderTheTitans {
    @EventRegistry(priority = -101, identifier = "generic.max_health")
    public static Attribute MAX_HEALTH;
    @EventRegistry(priority = -100, identifier = "generic.attack_damage")
    public static Attribute ATTACK_DAMAGE;
    @EventRegistry(priority = -5, identifier = "growth_serum")
    public static EntityType<EntityGrowthSerum> growthSerum;
    @EventRegistry(priority = -4, identifier = "fireball")
    public static EntityType<EntityTitanFireball> titanFireball;
    @EventRegistry(priority = -4, identifier = "proto_ball")
    public static EntityType<EntityProtoBall> protoBall;
    @EventRegistry(priority = -3, identifier = "titan_falling_block")
    public static EntityType<EntityFallingBlockTitan> titanFallingBlock;
    @EventRegistry(priority = -2, identifier = "titan_spirit")
    public static EntityType<EntityTitanSpirit> titanSpirit;
    @EventRegistry(priority = -1, identifier = "gamma_lighting")
    public static EntityType<EntityGammaLightning> gammaLightingBolt;
    @EventRegistry(identifier = "slime_titan")
    public static EntityType<EntitySlimeTitan> slimeTitan;
    @EventRegistry(identifier = "zombie_titan")
    public static EntityType<EntityZombieTitan> zombieTitan;
    @EventRegistry(identifier = "skeleton_titan")
    public static EntityType<EntitySkeletonTitan> skeletonTitan;
    @EventRegistry(identifier = "wither_skeleton_titan")
    public static EntityType<EntitySkeletonTitan> witherSkeletonTitan;
    @EventRegistry(identifier = "ghast_titan")
    public static EntityType<EntityGhastTitan> ghastTitan;
    @EventRegistry(identifier = "ultima_iron_golem_titan")
    public static EntityType<EntityIronGolemTitan> ironGolemTitan;
    @EventRegistry(identifier = "witherzilla")
    public static EntityType<EntityWitherzilla> witherzilla;
    @EventRegistry(priority = 100, identifier = "giant_better")
    public static EntityType<EntityGiantZombieBetter> giantZombie;
    @EventRegistry(priority = 101, identifier = "zombie_minion")
    public static EntityType<EntityZombieMinion> zombieMinion;

    static {
        MAX_HEALTH = new RangedAttribute("attribute.name.generic.max_health", 20.0D, 1.0D, Double.MAX_VALUE).setSyncable(true);
        ATTACK_DAMAGE = new RangedAttribute("attribute.name.generic.attack_damage", 2.0D, 1.0D, Double.MAX_VALUE).setSyncable(true);
		growthSerum = EntityType.Builder
			    .<EntityGrowthSerum>of(EntityGrowthSerum::new, EntityClassification.MISC)
		        .fireImmune()
		    	.sized(0.5F, 0.5F)
			    .clientTrackingRange(4)
			    .updateInterval(20)
			    .build("growth_serum");
        titanFireball = EntityType.Builder
                .<EntityTitanFireball>of(EntityTitanFireball::new, EntityClassification.MISC)
                .fireImmune()
                .sized(1.0F, 1.0F)
                .clientTrackingRange(4)
                .updateInterval(20)
                .build("fireball");
        protoBall = EntityType.Builder
                .<EntityProtoBall>of(EntityProtoBall::new, EntityClassification.MISC)
                .fireImmune()
                .sized(3.0F, 3.0F)
                .clientTrackingRange(4)
                .updateInterval(20)
                .build("proto_ball");
        titanFallingBlock = EntityType.Builder
                .<EntityFallingBlockTitan>of(EntityFallingBlockTitan::new, EntityClassification.MISC)
                .fireImmune()
                .sized(0.5F, 0.5F)
                .clientTrackingRange(4)
                .updateInterval(20)
                .build("titan_falling_block");
        titanSpirit = EntityType.Builder
                .<EntityTitanSpirit>of(EntityTitanSpirit::new, EntityClassification.MISC)
                .sized(8.0f, 8.0f)
                .fireImmune()
                .build("titan_spirit");
        gammaLightingBolt = EntityType.Builder
                .<EntityGammaLightning>of(EntityGammaLightning::new, EntityClassification.MISC)
                .noSave()
                .sized(3.0f, 3.0f)
                .fireImmune()
                .updateInterval(Integer.MAX_VALUE)
                .build("gamma_lighting");
        slimeTitan = EntityType.Builder
                .of(EntitySlimeTitan::new, EntityClassification.MISC)
                .sized(8.0f, 8.0f)
                .fireImmune()
                .build("slime_titan");
        zombieTitan = EntityType.Builder
                .of((type, worldIn) -> new EntityZombieTitan(type, worldIn), EntityClassification.MISC)
                .sized(8.0f, 32.0f)
                .fireImmune()
			    .build("zombie_titan");
        skeletonTitan = EntityType.Builder
			    .<EntitySkeletonTitan>of((type, worldIn) -> new EntitySkeletonTitan(type, worldIn), EntityClassification.MISC)
	        	.sized(8.0f, 32.0f)
			    .fireImmune()
			    .build("skeleton_titan");
        witherSkeletonTitan = EntityType.Builder
			    .<EntitySkeletonTitan>of((type, worldIn) -> new EntitySkeletonTitan(type, worldIn, true), EntityClassification.MISC)
		        .sized(14.0f, 56.0f)
			    .fireImmune()
			    .build("wither_skeleton_titan");
        ghastTitan = EntityType.Builder
                .of(EntityGhastTitan::new, EntityClassification.MISC)
                .sized(110.0f, 110.0f)
                .fireImmune()
                .build("ghast_titan");
        ironGolemTitan = EntityType.Builder
                .of(EntityIronGolemTitan::new, EntityClassification.MISC)
                .sized(24.0f, 64.0f)
                .fireImmune()
                .build("ultima_iron_golem_titan");
        witherzilla = EntityType.Builder
                .of(EntityWitherzilla::new, EntityClassification.MISC)
                .sized(0.6f, 1.8f)
                .fireImmune()
                .build("witherzilla");
        giantZombie = EntityType.Builder
                .<EntityGiantZombieBetter>of(EntityGiantZombieBetter::new, EntityClassification.MISC)
                .sized(4.0f, 16.0f)
                .fireImmune()
                .build("giant_better");
        zombieMinion = EntityType.Builder
                .<EntityZombieMinion>of(EntityZombieMinion::new, EntityClassification.MISC)
                .sized(0.6f, 1.8f)
                .fireImmune()
                .build("zombie_minion");
    }
}
