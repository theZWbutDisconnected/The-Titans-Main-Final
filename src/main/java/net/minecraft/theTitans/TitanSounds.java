package net.minecraft.theTitans;

import com.zerwhit.annotations.EventRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TitanSounds {
    @EventRegistry(identifier = "harcadium_hum")
    public static SoundEvent harcadiumHum = registry("harcadium_hum");
    @EventRegistry(identifier = "harcadium_block_hum")
    public static SoundEvent harcadiumBlockHum = registry("harcadium_block_hum");
    @EventRegistry(identifier = "sword_drag")
    public static SoundEvent swordDrag = registry("sword_drag");
    @EventRegistry(identifier = "slash_flesh")
    public static SoundEvent slashFlesh = registry("slash_flesh");
    @EventRegistry(identifier = "titanpunch")
    public static SoundEvent titanPunch = registry("titanpunch");
    @EventRegistry(identifier = "titan_swing")
    public static SoundEvent titanSwing = registry("titan_swing");
    @EventRegistry(identifier = "titan_strike")
    public static SoundEvent titanStrike = registry("titan_strike");
    @EventRegistry(identifier = "titan_slam")
    public static SoundEvent titanSlam = registry("titan_slam");
    @EventRegistry(identifier = "titan_press")
    public static SoundEvent titanPress = registry("titan_press");
    @EventRegistry(identifier = "titansummonminion")
    public static SoundEvent titanSummonMinion = registry("titansummonminion");
    @EventRegistry(identifier = "titan_rumble")
    public static SoundEvent titanRumble = registry("titan_rumble");
    @EventRegistry(identifier = "titan_quake")
    public static SoundEvent titanQuake = registry("titan_quake");
    @EventRegistry(identifier = "titan_fall")
    public static SoundEvent titanFall = registry("titan_fall");
    @EventRegistry(identifier = "ground_smash")
    public static SoundEvent groundSmash = registry("ground_smash");
    @EventRegistry(identifier = "distant_large_fall")
    public static SoundEvent distantLargeFall = registry("distant_large_fall");
    @EventRegistry(identifier = "titan_step")
    public static SoundEvent titanStep = registry("titan_step");
    @EventRegistry(identifier = "lightning_charge")
    public static SoundEvent lightningCharge = registry("lightning_charge");
    @EventRegistry(identifier = "lightning_throw")
    public static SoundEvent lightningThrow = registry("lightning_throw");
    @EventRegistry(identifier = "titanland")
    public static SoundEvent titanLand = registry("titanland");
    @EventRegistry(identifier = "titan_birth")
    public static SoundEvent titanBirth = registry("titan_birth");
    @EventRegistry(identifier = "titan_zombie_roar")
    public static SoundEvent titanZombieRoar = registry("titan_zombie_roar");
    @EventRegistry(identifier = "titan_zombie_living")
    public static SoundEvent titanZombieLiving = registry("titan_zombie_living");
    @EventRegistry(identifier = "titan_zombie_grunt")
    public static SoundEvent titanZombieGrunt = registry("titan_zombie_grunt");
    @EventRegistry(identifier = "titan_zombie_death")
    public static SoundEvent titanZombieDeath = registry("titan_zombie_death");
    @EventRegistry(identifier = "titan_zombie_creation")
    public static SoundEvent titanZombieCreation = registry("titan_zombie_creation");
    @EventRegistry(identifier = "titan_skeleton_living")
    public static SoundEvent titanSkeletonLiving = registry("titan_skeleton_living");
    @EventRegistry(identifier = "titan_skeleton_get_up")
    public static SoundEvent titanSkeletonGetUp = registry("titan_skeleton_get_up");
    @EventRegistry(identifier = "titan_skeleton_awaken")
    public static SoundEvent titanSkeletonAwaken = registry("titan_skeleton_awaken");
    @EventRegistry(identifier = "titan_skeleton_begin_move")
    public static SoundEvent titanSkeletonBeginMove = registry("titan_skeleton_begin_move");
    @EventRegistry(identifier = "titan_skeleton_grunt")
    public static SoundEvent titanSkeletonGrunt = registry("titan_skeleton_grunt");
    @EventRegistry(identifier = "titan_skeleton_death")
    public static SoundEvent titanSkeletonDeath = registry("titan_skeleton_death");
    @EventRegistry(identifier = "titan_ghast_living")
    public static SoundEvent titanGhastLiving = registry("titan_ghast_living");
    @EventRegistry(identifier = "titan_ghast_grunt")
    public static SoundEvent titanGhastGrunt = registry("titan_ghast_grunt");
    @EventRegistry(identifier = "titan_ghast_death")
    public static SoundEvent titanGhastDeath = registry("titan_ghast_death");
    @EventRegistry(identifier = "titan_ghast_charge")
    public static SoundEvent titanGhastCharge = registry("titan_ghast_charge");
    @EventRegistry(identifier = "titan_ghast_fireball")
    public static SoundEvent titanGhastFireball = registry("titan_ghast_fireball");
    @EventRegistry(identifier = "titan_wither_skeleton_living")
    public static SoundEvent titanWitherSkeletonLiving = registry("titan_wither_skeleton_living");
    @EventRegistry(identifier = "titan_wither_skeleton_grunt")
    public static SoundEvent titanWitherSkeletonGrunt = registry("titan_wither_skeleton_grunt");
    @EventRegistry(identifier = "titan_wither_skeleton_death")
    public static SoundEvent titanWitherSkeletonDeath = registry("titan_wither_skeleton_death");
    @EventRegistry(identifier = "witherzilla_spawn")
    public static SoundEvent witherzillaSpawn = registry("witherzilla_spawn");
    @EventRegistry(identifier = "witherzilla_living")
    public static SoundEvent witherzillaLiving = registry("witherzilla_living");
    @EventRegistry(identifier = "witherzilla_grunt")
    public static SoundEvent witherzillaGrunt = registry("witherzilla_grunt");
    @EventRegistry(identifier = "witherzilla_death")
    public static SoundEvent witherzillaDeath = registry("witherzilla_death");

    private static SoundEvent registry(String location) {
        return new SoundEvent(new ResourceLocation(TheTitans.modid, location));
    }
}
