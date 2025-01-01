package net.minecraft.theTitans.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class TitanConfig {
    public static boolean TitansFFAMode;
    public static boolean NightmareMode;
    public static boolean TotalDestructionMode;
    public static int SlimeTitanMinionSpawnrate = 5;
    public static int ZombieTitanMinionSpawnrate = 5;
    public static int SkeletonTitanMinionSpawnrate = 5;
    public static int GhastTitanMinionSpawnrate = 5;
    public static int WitherSkeletonTitanMinionSpawnrate = 5;
    public static int UltimaIronGolemMinionSpawnrate = 5;
    public static int WitherzillaMinionSpawnrate = 5;
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue TitansFFAModeBuilder;
    public static ForgeConfigSpec.BooleanValue NightmareModeBuilder;
    public static ForgeConfigSpec.BooleanValue TotalDestructionModeBuilder;
    public static ForgeConfigSpec.IntValue SlimeTitanMinionSpawnrateBuilder;
    public static ForgeConfigSpec.IntValue ZombieTitanMinionSpawnrateBuilder;
    public static ForgeConfigSpec.IntValue SkeletonTitanMinionSpawnrateBuilder;
    public static ForgeConfigSpec.IntValue GhastTitanMinionSpawnrateBuilder;
    public static ForgeConfigSpec.IntValue WitherSkeletonTitanMinionSpawnrateBuilder;
    public static ForgeConfigSpec.IntValue UltimaIronGolemMinionSpawnrateBuilder;
    public static ForgeConfigSpec.IntValue WitherzillaMinionSpawnrateBuilder;

    public static final void load() {
        buildConfig();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new TitanConfigGui(screen)
        );
    }

    public static final void buildConfig() {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        CLIENT_BUILDER.comment("Client settings")
                .push("general");
        CLIENT_BUILDER.pop();
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("Common settings")
                .push("general");
        TitansFFAModeBuilder = COMMON_BUILDER.comment("Enable Free For All Mode").define("TitansFFAMode", TitansFFAMode);
        NightmareModeBuilder = COMMON_BUILDER.comment("Enable Nightmare Mode").define("NightmareMode", NightmareMode);
        TotalDestructionModeBuilder = COMMON_BUILDER.comment("Enable Total Destruction Mode").define("TotalDestructionMode", TotalDestructionMode);
        SlimeTitanMinionSpawnrateBuilder = COMMON_BUILDER.comment("Set Slime Titan Minion Spawnrate").defineInRange("SlimeTitanMinionSpawnrate", SlimeTitanMinionSpawnrate, 1, 10);
        ZombieTitanMinionSpawnrateBuilder = COMMON_BUILDER.comment("Set Zombie Titan Minion Spawnrate").defineInRange("ZombieTitanMinionSpawnrate", ZombieTitanMinionSpawnrate, 1, 10);
        SkeletonTitanMinionSpawnrateBuilder = COMMON_BUILDER.comment("Set Skeleton Titan Minion Spawnrate").defineInRange("SkeletonTitanMinionSpawnrate", SkeletonTitanMinionSpawnrate, 1, 10);
        GhastTitanMinionSpawnrateBuilder = COMMON_BUILDER.comment("Set Ghast Titan Minion Spawnrate").defineInRange("GhastTitanMinionSpawnrate", GhastTitanMinionSpawnrate, 1, 10);
        WitherSkeletonTitanMinionSpawnrateBuilder = COMMON_BUILDER.comment("Set Wither Skeleton Titan Minion Spawnrate").defineInRange("WitherSkeletonTitanMinionSpawnrate", WitherSkeletonTitanMinionSpawnrate, 1, 10);
        UltimaIronGolemMinionSpawnrateBuilder = COMMON_BUILDER.comment("Set Ultima Iron Golem Titan Minion Spawnrate").defineInRange("UltimaIronGolemTitanMinionSpawnrate", UltimaIronGolemMinionSpawnrate, 1, 10);
        WitherzillaMinionSpawnrateBuilder = COMMON_BUILDER.comment("Set Witherzilla Minion Spawnrate").defineInRange("WitherzillaMinionSpawnrate", WitherzillaMinionSpawnrate, 1, 10);
        COMMON_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
