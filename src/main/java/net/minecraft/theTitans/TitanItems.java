package net.minecraft.theTitans;

import com.zerwhit.annotations.EventRegistry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagRegistry;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.theTitans.items.*;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class TitanItems {
    @EventRegistry(identifier = "copper_ore")
    public static Item copper_ore;
    @EventRegistry(identifier = "tin_ore")
    public static Item tin_ore;
    @EventRegistry(identifier = "chromium_ore")
    public static Item chromium_ore;
    @EventRegistry(identifier = "magnesium_ore")
    public static Item magnesium_ore;
    @EventRegistry(identifier = "lead_ore")
    public static Item lead_ore;
    @EventRegistry(identifier = "silver_ore")
    public static Item silver_ore;
    @EventRegistry(identifier = "platinum_ore")
    public static Item platinum_ore;
    @EventRegistry(identifier = "harcadium_ore")
    public static Item harcadium_ore;
    @EventRegistry(identifier = "harcadium_ore_end_stone")
    public static Item harcadium_ore_end_stone;
    @EventRegistry(identifier = "harcadium_ore_obsidian")
    public static Item harcadium_ore_obsidian;
    @EventRegistry(identifier = "void_ore")
    public static Item void_ore;
    @EventRegistry(identifier = "void_ore_end_stone")
    public static Item void_ore_end_stone;
    @EventRegistry(identifier = "void_ore_obsidian")
    public static Item void_ore_obsidian;
    @EventRegistry(identifier = "adamantium_ore")
    public static Item adamantium_ore;
    @EventRegistry(identifier = "nether_stone_ore")
    public static Item nether_stone_ore;
    @EventRegistry(identifier = "nether_coal_ore")
    public static Item nether_coal_ore;
    @EventRegistry(identifier = "nether_gold_ore")
    public static Item nether_gold_ore;
    @EventRegistry(identifier = "nether_diamond_ore")
    public static Item nether_diamond_ore;
    @EventRegistry(identifier = "harcadium_block")
    public static Item harcadium_block;
    @EventRegistry(identifier = "void_block")
    public static Item void_block;
    @EventRegistry(identifier = "bedrock_compact")
    public static Item bedrock_block;
    @EventRegistry(identifier = "magic_pumpkin")
    public static Item magic_pumpkin;
    @EventRegistry(identifier = "stoneperch")
    public static Item stoneperch;
    @EventRegistry(identifier = "sandstoneperch")
    public static Item sandstoneperch;
    @EventRegistry(identifier = "obsidianperch")
    public static Item obsidianperch;
    @EventRegistry(identifier = "goldperch")
    public static Item goldperch;
    @EventRegistry(identifier = "ironperch")
    public static Item ironperch;
    @EventRegistry(identifier = "endstoneperch")
    public static Item endstoneperch;
    @EventRegistry(identifier = "netherbrickperch")
    public static Item netherbrickperch;
    @EventRegistry(identifier = "harcadium")
    public static Item harcadium;
    @EventRegistry(identifier = "harcadium_nugget")
    public static Item harcadiumNugget;
    @EventRegistry(identifier = "harcadium_wafer")
    public static Item harcadiumWafer;
    @EventRegistry(identifier = "harcadium_waflet")
    public static Item harcadiumWaflet;
    @EventRegistry(identifier = "harcadium_pickaxe")
    public static Item harcadiumPickaxe;
    @EventRegistry(identifier = "harcadium_axe")
    public static Item harcadiumAxe;
    @EventRegistry(identifier = "harcadium_spade")
    public static Item harcadiumSpade;
    @EventRegistry(identifier = "harcadium_hoe")
    public static Item harcadiumHoe;
    @EventRegistry(identifier = "harcadium_sword")
    public static Item harcadiumSword;
    @EventRegistry(identifier = "harcadium_helmet")
    public static Item harcadiumHelmet;
    @EventRegistry(identifier = "harcadium_chestplate")
    public static Item harcadiumChestplate;
    @EventRegistry(identifier = "harcadium_leggings")
    public static Item harcadiumLeggings;
    @EventRegistry(identifier = "harcadium_boots")
    public static Item harcadiumBoots;
    @EventRegistry(identifier = "growth_serum")
    public static Item growthSerum;
    @EventRegistry(identifier = "teleporter")
    public static Item teleporter;
    @EventRegistry(identifier = "teleporter2")
    public static Item teleporter2;
    @EventRegistry(identifier = "turret_good")
    public static Item goodTurret;
    @EventRegistry(identifier = "machine_gun_good")
    public static Item goodTurret2;
    @EventRegistry(identifier = "mortar_good")
    public static Item goodTurret3;
    @EventRegistry(identifier = "golden_potatoe")
    public static Item goldenPotatoe;
    @EventRegistry(identifier = "golden_bread")
    public static Item goldenCookie;
    @EventRegistry(identifier = "golden_cookie")
    public static Item goldenBread;
    @EventRegistry(identifier = "golden_melon")
    public static Item goldenMelon;
    @EventRegistry(identifier = "golden_pumpkin_pie")
    public static Item goldenPumpkinPie;
    @EventRegistry(identifier = "apple_diamond")
    public static Item diamondApple;
    @EventRegistry(identifier = "diamond_potatoe")
    public static Item diamondPotatoe;
    @EventRegistry(identifier = "diamond_cookie")
    public static Item diamondCookie;
    @EventRegistry(identifier = "diamond_bread")
    public static Item diamondBread;
    @EventRegistry(identifier = "diamond_melon")
    public static Item diamondMelon;
    @EventRegistry(identifier = "diamond_pumpkin_pie")
    public static Item diamondPumpkinPie;
    @EventRegistry(identifier = "enchanted_golden_potatoe")
    public static Item enchantedGoldenPotatoe;
    @EventRegistry(identifier = "enchanted_golden_bread")
    public static Item enchantedGoldenCookie;
    @EventRegistry(identifier = "enchanted_golden_cookie")
    public static Item enchantedGoldenBread;
    @EventRegistry(identifier = "enchanted_golden_melon")
    public static Item enchantedGoldenMelon;
    @EventRegistry(identifier = "enchanted_golden_pumpkin_pie")
    public static Item enchantedGoldenPumpkinPie;
    @EventRegistry(identifier = "enchanted_apple_diamond")
    public static Item enchantedDiamondApple;
    @EventRegistry(identifier = "enchanted_diamond_potatoe")
    public static Item enchantedDiamondPotatoe;
    @EventRegistry(identifier = "enchanted_diamond_cookie")
    public static Item enchantedDiamondCookie;
    @EventRegistry(identifier = "enchanted_diamond_bread")
    public static Item enchantedDiamondBread;
    @EventRegistry(identifier = "enchanted_diamond_melon")
    public static Item enchantedDiamondMelon;
    @EventRegistry(identifier = "enchanted_diamond_pumpkin_pie")
    public static Item enchantedDiamondPumpkinPie;
    @EventRegistry(identifier = "malgrum_fruit")
    public static Item malgrum;
    @EventRegistry(identifier = "malgrum_seeds")
    public static Item malgrumSeeds;
    @EventRegistry(identifier = "pleasant_blade_seed")
    public static Item pleasantBladeSeed;
    @EventRegistry(identifier = "pleasant_blade_leaf")
    public static Item pleasantBladeLeaf;
    @EventRegistry(identifier = "pleasant_blade_flower")
    public static Item pleasantBladeFlower;
    @EventRegistry(identifier = "pleasant_blade_brew")
    public static Item pleasantBladeBrew;
    @EventRegistry(identifier = "chaff")
    public static Item chaff;
    @EventRegistry(identifier = "diamond_string")
    public static Item diamondString;
    @EventRegistry(identifier = "harcadium_arrow")
    public static Item harcadiumArrow;
    @EventRegistry(identifier = "harcadium_bow")
    public static Item harcadiumBow;
    @EventRegistry(identifier = "witherskeletonspawn")
    public static Item witherSkeletonSpawner;
    @EventRegistry(identifier = "spawn_egg_omegafish")
    public static Item eggOmegafish;
    @EventRegistry(identifier = "spawn_egg_cave_spider_titan")
    public static Item eggCaveSpiderTitan;
    @EventRegistry(identifier = "spawn_egg_spider_titan")
    public static Item eggSpiderTitan;
    @EventRegistry(identifier = "spawn_egg_spider_jockey_titan")
    public static Item eggSpiderJockeyTitan;
    @EventRegistry(identifier = "spawn_egg_slime_titan")
    public static Item eggSlimeTitan;
    @EventRegistry(identifier = "spawn_egg_magma_cube_titan")
    public static Item eggMagmaCubeTitan;
    @EventRegistry(identifier = "spawn_egg_zombie_titan")
    public static Item eggZombieTitan;
    @EventRegistry(identifier = "spawn_egg_skeleton_titan")
    public static Item eggSkeletonTitan;
    @EventRegistry(identifier = "spawn_egg_creeper_titan")
    public static Item eggCreeperTitan;
    @EventRegistry(identifier = "spawn_egg_charged_creeper_titan")
    public static Item eggChargedCreeperTitan;
    @EventRegistry(identifier = "spawn_egg_zombie_pigman_titan")
    public static Item eggZombiePigmanTitan;
    @EventRegistry(identifier = "spawn_egg_blaze_titan")
    public static Item eggBlazeTitan;
    @EventRegistry(identifier = "spawn_egg_wither_skeleton_titan")
    public static Item eggWitherSkeletonTitan;
    @EventRegistry(identifier = "spawn_egg_wither_jockey_titan")
    public static Item eggWitherJockeyTitan;
    @EventRegistry(identifier = "spawn_egg_ghast_titan")
    public static Item eggGhastTitan;
    @EventRegistry(identifier = "spawn_egg_ender_colossus")
    public static Item eggEnderColossus;
    @EventRegistry(identifier = "spawn_egg_witherzilla")
    public static Item eggWitherzilla;
    @EventRegistry(identifier = "ultima_blade")
    public static Item ultimaBlade;
    @EventRegistry(identifier = "optima_axe")
    public static Item optimaAxe;
    @EventRegistry(identifier = "adamantium_sword")
    public static Item adamantiumSword;
    @EventRegistry(identifier = "spawn_egg_iron_golem_titan")
    public static Item eggUltimaIronGolemTitan;
    @EventRegistry(identifier = "spawn_egg_iron_golem_better")
    public static Item eggIronGolemBetter;
    @EventRegistry(identifier = "spawn_egg_gargoyle_titan")
    public static Item eggGargoyleTitan;
    @EventRegistry(identifier = "spawn_egg_snow_golem_titan")
    public static Item eggSnowGolemTitan;
    @EventRegistry(identifier = "adminium_helmet")
    public static Item adminiumHelmet;
    @EventRegistry(identifier = "adminium_chestplate")
    public static Item adminiumChestplate;
    @EventRegistry(identifier = "adminium_leggings")
    public static Item adminiumLeggings;
    @EventRegistry(identifier = "adminium_boots")
    public static Item adminiumBoots;
    @EventRegistry(identifier = "adminium_pickaxe")
    public static Item adminiumPickaxe;
    @EventRegistry(identifier = "adminium_axe")
    public static Item adminiumAxe;
    @EventRegistry(identifier = "adminium_spade")
    public static Item adminiumSpade;
    @EventRegistry(identifier = "adminium_hoe")
    public static Item adminiumHoe;
    @EventRegistry(identifier = "adminium_sword")
    public static Item adminiumSword;
    @EventRegistry(identifier = "void")
    public static Item voidItem;
    @EventRegistry(identifier = "void_helmet")
    public static Item voidHelmet;
    @EventRegistry(identifier = "void_chestplate")
    public static Item voidChestplate;
    @EventRegistry(identifier = "void_leggings")
    public static Item voidLeggings;
    @EventRegistry(identifier = "void_boots")
    public static Item voidBoots;
    @EventRegistry(identifier = "void_pickaxe")
    public static Item voidPickaxe;
    @EventRegistry(identifier = "void_axe")
    public static Item voidAxe;
    @EventRegistry(identifier = "void_spade")
    public static Item voidSpade;
    @EventRegistry(identifier = "void_hoe")
    public static Item voidHoe;
    @EventRegistry(identifier = "void_sword")
    public static Item voidSword;
    @EventRegistry(identifier = "copper_ingot")
    public static Item copperIngot;
    @EventRegistry(identifier = "copper_pickaxe")
    public static Item copperPickaxe;
    @EventRegistry(identifier = "copper_axe")
    public static Item copperAxe;
    @EventRegistry(identifier = "copper_spade")
    public static Item copperSpade;
    @EventRegistry(identifier = "copper_hoe")
    public static Item copperHoe;
    @EventRegistry(identifier = "copper_sword")
    public static Item copperSword;
    @EventRegistry(identifier = "copper_helmet")
    public static Item copperHelmet;
    @EventRegistry(identifier = "copper_chestplate")
    public static Item copperChestplate;
    @EventRegistry(identifier = "copper_leggings")
    public static Item copperLeggings;
    @EventRegistry(identifier = "copper_boots")
    public static Item copperBoots;
    @EventRegistry(identifier = "tin_ingot")
    public static Item tinIngot;
    @EventRegistry(identifier = "tin_pickaxe")
    public static Item tinPickaxe;
    @EventRegistry(identifier = "tin_axe")
    public static Item tinAxe;
    @EventRegistry(identifier = "tin_spade")
    public static Item tinSpade;
    @EventRegistry(identifier = "tin_hoe")
    public static Item tinHoe;
    @EventRegistry(identifier = "tin_sword")
    public static Item tinSword;
    @EventRegistry(identifier = "tin_helmet")
    public static Item tinHelmet;
    @EventRegistry(identifier = "tin_chestplate")
    public static Item tinChestplate;
    @EventRegistry(identifier = "tin_leggings")
    public static Item tinLeggings;
    @EventRegistry(identifier = "tin_boots")
    public static Item tinBoots;
    @EventRegistry(identifier = "bronze_ingot")
    public static Item bronzeIngot;
    @EventRegistry(identifier = "bronze_pickaxe")
    public static Item bronzePickaxe;
    @EventRegistry(identifier = "bronze_axe")
    public static Item bronzeAxe;
    @EventRegistry(identifier = "bronze_spade")
    public static Item bronzeSpade;
    @EventRegistry(identifier = "bronze_hoe")
    public static Item bronzeHoe;
    @EventRegistry(identifier = "bronze_sword")
    public static Item bronzeSword;
    @EventRegistry(identifier = "bronze_helmet")
    public static Item bronzeHelmet;
    @EventRegistry(identifier = "bronze_chestplate")
    public static Item bronzeChestplate;
    @EventRegistry(identifier = "bronze_leggings")
    public static Item bronzeLeggings;
    @EventRegistry(identifier = "bronze_boots")
    public static Item bronzeBoots;
    @EventRegistry(identifier = "steel_ingot")
    public static Item steelIngot;
    @EventRegistry(identifier = "steel_pickaxe")
    public static Item steelPickaxe;
    @EventRegistry(identifier = "steel_axe")
    public static Item steelAxe;
    @EventRegistry(identifier = "steel_spade")
    public static Item steelSpade;
    @EventRegistry(identifier = "steel_hoe")
    public static Item steelHoe;
    @EventRegistry(identifier = "steel_sword")
    public static Item steelSword;
    @EventRegistry(identifier = "steel_helmet")
    public static Item steelHelmet;
    @EventRegistry(identifier = "steel_chestplate")
    public static Item steelChestplate;
    @EventRegistry(identifier = "steel_leggings")
    public static Item steelLeggings;
    @EventRegistry(identifier = "steel_boots")
    public static Item steelBoots;
    @EventRegistry(identifier = "chromium_ingot")
    public static Item chromiumIngot;
    @EventRegistry(identifier = "magnesium_ingot")
    public static Item magnesiumIngot;
    @EventRegistry(identifier = "lead_ingot")
    public static Item leadIngot;
    @EventRegistry(identifier = "silver_ingot")
    public static Item silverIngot;
    @EventRegistry(identifier = "platinum_ingot")
    public static Item platinumIngot;
    @EventRegistry(identifier = "ruby")
    public static Item ruby;
    @EventRegistry(identifier = "onyx")
    public static Item onyx;
    @EventRegistry(identifier = "adamantium")
    public static Item adamantium;

    static {
        goldenPotatoe = new Item(new Item.Properties().food(TitanFoods.golden_potatoe).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        goldenBread = new Item(new Item.Properties().food(TitanFoods.golden_bread).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        goldenCookie = new Item(new Item.Properties().food(TitanFoods.golden_cookie).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        goldenMelon = new Item(new Item.Properties().food(TitanFoods.golden_melon).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        goldenPumpkinPie = new Item(new Item.Properties().food(TitanFoods.golden_pumpkin_pie).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        diamondApple = new Item(new Item.Properties().food(TitanFoods.apple_diamond).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        diamondPotatoe = new Item(new Item.Properties().food(TitanFoods.diamond_potatoe).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        diamondBread = new Item(new Item.Properties().food(TitanFoods.diamond_bread).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        diamondCookie = new Item(new Item.Properties().food(TitanFoods.diamond_cookie).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        diamondMelon = new Item(new Item.Properties().food(TitanFoods.diamond_melon).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        diamondPumpkinPie = new Item(new Item.Properties().food(TitanFoods.diamond_pumpkin_pie).rarity(Rarity.RARE).tab(ItemGroup.TAB_FOOD));
        enchantedGoldenPotatoe = new Item(new Item.Properties().food(TitanFoods.enchanted_golden_potatoe).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedGoldenBread = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_golden_bread).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedGoldenCookie = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_golden_cookie).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedGoldenMelon = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_golden_melon).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedGoldenPumpkinPie = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_golden_pumpkin_pie).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedDiamondApple = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_apple_diamond).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedDiamondPotatoe = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_diamond_potatoe).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedDiamondBread = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_diamond_bread).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedDiamondCookie = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_diamond_cookie).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedDiamondMelon = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.enchanted_diamond_melon).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        enchantedDiamondPumpkinPie = new EnchantedGoldenAppleItem(new Item.Properties().food(TitanFoods.diamond_pumpkin_pie).rarity(Rarity.EPIC).tab(ItemGroup.TAB_FOOD));
        copper_ore = new BlockItem(TitanBlocks.copper_ore, new Item.Properties().tab(TheTitans.titansTab));
        tin_ore = new BlockItem(TitanBlocks.tin_ore, new Item.Properties().tab(TheTitans.titansTab));
        chromium_ore = new BlockItem(TitanBlocks.chromium_ore, new Item.Properties().tab(TheTitans.titansTab));
        magnesium_ore = new BlockItem(TitanBlocks.magnesium_ore, new Item.Properties().tab(TheTitans.titansTab));
        lead_ore = new BlockItem(TitanBlocks.lead_ore, new Item.Properties().tab(TheTitans.titansTab));
        silver_ore = new BlockItem(TitanBlocks.silver_ore, new Item.Properties().tab(TheTitans.titansTab));
        platinum_ore = new BlockItem(TitanBlocks.platinum_ore, new Item.Properties().tab(TheTitans.titansTab));
        nether_stone_ore = new BlockItem(TitanBlocks.nether_stone_ore, new Item.Properties().tab(TheTitans.titansTab));
        nether_coal_ore = new BlockItem(TitanBlocks.nether_coal_ore, new Item.Properties().tab(TheTitans.titansTab));
        nether_gold_ore = new BlockItem(TitanBlocks.nether_gold_ore, new Item.Properties().tab(TheTitans.titansTab));
        nether_diamond_ore = new BlockItem(TitanBlocks.nether_diamond_ore, new Item.Properties().tab(TheTitans.titansTab));
        harcadium_block = new BlockItem(TitanBlocks.harcadium_block, new Item.Properties().tab(TheTitans.titansTab));
        harcadium_ore = new BlockItem(TitanBlocks.harcadium_ore, new Item.Properties().tab(TheTitans.titansTab));
        harcadium_ore_end_stone = new BlockItem(TitanBlocks.harcadium_ore_end_stone, new Item.Properties().tab(TheTitans.titansTab));
        harcadium_ore_obsidian = new BlockItem(TitanBlocks.harcadium_ore_obsidian, new Item.Properties().tab(TheTitans.titansTab));
        void_block = new BlockItem(TitanBlocks.void_block, new Item.Properties().tab(TheTitans.titansTab));
        void_ore = new BlockItem(TitanBlocks.void_ore, new Item.Properties().tab(TheTitans.titansTab));
        void_ore_end_stone = new BlockItem(TitanBlocks.void_ore_end_stone, new Item.Properties().tab(TheTitans.titansTab));
        void_ore_obsidian = new BlockItem(TitanBlocks.void_ore_obsidian, new Item.Properties().tab(TheTitans.titansTab));
        bedrock_block = new BlockItem(TitanBlocks.bedrock_block, new Item.Properties().tab(TheTitans.titansTab));
        adamantium_ore = new BlockItem(TitanBlocks.adamantium_ore, new Item.Properties().tab(TheTitans.titansTab));
        malgrumSeeds = new BlockItem(TitanBlocks.malgrumCrop, new Item.Properties().tab(TheTitans.titansTab));
        copperIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        bronzeIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        steelIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        tinIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        chromiumIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        magnesiumIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        leadIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        silverIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        platinumIngot = new Item(new Item.Properties().tab(TheTitans.titansTab));
        pleasantBladeLeaf = new Item(new Item.Properties().tab(TheTitans.titansTab));
        adamantium = new Item(new Item.Properties().tab(TheTitans.titansTab).fireResistant());
        ultimaBlade = new ItemUltimaBlade();
        optimaAxe = new ItemOptimaAxe();
        adminiumSword = new ItemAdminiumSword(Tier.Adminium);
        adminiumAxe = new ItemAdminiumAxe(Tier.Adminium);
        adminiumPickaxe = new ItemAdminiumPickaxe(Tier.Adminium);
        adminiumSpade = new ItemAdminiumSpade(Tier.Adminium);
        adminiumHoe = new ItemAdminiumHoe(Tier.Adminium);
        voidItem = new Item(new Item.Properties().tab(TheTitans.titansTab).fireResistant());
        voidSword = new ItemVoidSword(Tier.Absence);
        voidAxe = new ItemVoidAxe(Tier.Absence);
        voidPickaxe = new ItemVoidPickaxe(Tier.Absence);
        voidSpade = new ItemVoidSpade(Tier.Absence);
        voidHoe = new ItemVoidHoe(Tier.Absence);
        harcadium = new Item(new Item.Properties().tab(TheTitans.titansTab));
        harcadiumNugget = new Item(new Item.Properties().tab(TheTitans.titansTab));
        harcadiumWafer = new Item(new Item.Properties().tab(TheTitans.titansTab));
        harcadiumWaflet = new Item(new Item.Properties().tab(TheTitans.titansTab));
        harcadiumSword = new ItemHarcadiumSword(Tier.Harcadium);
        harcadiumAxe = new ItemHarcadiumAxe(Tier.Harcadium);
        harcadiumPickaxe = new ItemHarcadiumPickaxe(Tier.Harcadium);
        harcadiumSpade = new ItemHarcadiumSword(Tier.Harcadium);
        harcadiumHoe = new ItemHarcadiumHoe(Tier.Harcadium);
        adminiumHelmet = new ItemAdminiumHelmet(Material.Adminium);
        adminiumChestplate = new ItemAdminiumChestplate(Material.Adminium);
        adminiumLeggings = new ItemAdminiumLeggings(Material.Adminium);
        adminiumBoots = new ItemAdminiumBoots(Material.Adminium);
        voidHelmet = new ItemVoidHelmet(Material.Absence);
        voidChestplate = new ItemVoidChestplate(Material.Absence);
        voidLeggings = new ItemVoidLeggings(Material.Absence);
        voidBoots = new ItemVoidBoots(Material.Absence);
        harcadiumHelmet = new ItemHarcadiumHelmet(Material.Harcadium);
        harcadiumChestplate = new ItemHarcadiumChestplate(Material.Harcadium);
        harcadiumLeggings = new ItemHarcadiumLeggings(Material.Harcadium);
        harcadiumBoots = new ItemHarcadiumBoots(Material.Harcadium);
        eggSlimeTitan = new ItemTitanEgg(RenderTheTitans.slimeTitan);
        eggZombieTitan = new ItemTitanEgg(RenderTheTitans.zombieTitan);
        eggSkeletonTitan = new ItemTitanEgg(RenderTheTitans.skeletonTitan);
        eggWitherSkeletonTitan = new ItemTitanEgg(RenderTheTitans.witherSkeletonTitan);
        eggGhastTitan = new ItemTitanEgg(RenderTheTitans.ghastTitan);
        eggUltimaIronGolemTitan = new ItemTitanEgg(RenderTheTitans.ironGolemTitan);
        eggWitherzilla = new ItemTitanEgg(RenderTheTitans.witherzilla);
    }

    public enum Tier implements IItemTier {
        Ultima(Integer.MAX_VALUE, 2, 9999999.0f, 999996.0f, 0, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Optima(Integer.MAX_VALUE, 2, 9999999.0f, 999996.0f, 0, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Adamantium(Integer.MAX_VALUE, 2, 9999999.0f, 1.0E37f, 0, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Adminium(Integer.MAX_VALUE, 1000000000, 1.0E9f, 1.0E9f, 60, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Absence(100000000, 1000000, 4000.0f, 1496.0f, 40, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Harcadium(1000, 75000, 120.0f, 46.0f, 30, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Steel(3, 900, 8.0f, 3.0f, 10, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Bronze(2, 200, 6.0f, 2.0f, 14, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Tin(1, 100, 3.0f, 1.0f, 18, () -> {
            return Ingredient.of(Tags.Tool);
        }),
        Copper(0, 80, 3.0f, 0.0f, 12, () -> {
            return Ingredient.of(Tags.Tool);
        });

        private final int harvestLevel;
        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int enchantability;
        private final LazyValue<Ingredient> repairMaterial;

        Tier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
            this.harvestLevel = harvestLevel;
            this.maxUses = maxUses;
            this.efficiency = efficiency;
            this.attackDamage = attackDamage;
            this.enchantability = enchantability;
            this.repairMaterial = new LazyValue<>(repairMaterial);
        }

        @Override
        public int getUses() {
            return this.maxUses;
        }

        @Override
        public float getSpeed() {
            return this.efficiency;
        }

        @Override
        public float getAttackDamageBonus() {
            return this.attackDamage;
        }

        @Override
        public int getLevel() {
            return this.harvestLevel;
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantability;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairMaterial.get();
        }
    }

    public enum Material implements IArmorMaterial {
        Harcadium("Harcadium", 10000, new int[]{9, 15, 12, 8}, 30, 4.0f, 0.2f, () -> {
            return Ingredient.of(TitanItems.harcadium);
        }),
        Absence("absence", 100000, new int[]{11, 17, 13, 9}, 50, 6.0f, 0.5f, () -> {
            return Ingredient.of(TitanItems.voidItem);
        }),
        Adminium("adminium", 100000000, new int[]{100000, 100000, 100000, 100000}, 60, 10.0f, 1.0f, () -> {
            return Ingredient.of(Items.BEDROCK);
        });

        private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
        private final String name;
        private final int durabilityMultiplier;
        private final int[] slotProtections;
        private final int enchantmentValue;
        private final float toughness;
        private final float knockbackResistance;
        private final LazyValue<Ingredient> repairIngredient;

        Material(String p_i231593_3_, int p_i231593_4_, int[] p_i231593_5_, int p_i231593_6_, float p_i231593_8_, float p_i231593_9_, Supplier<Ingredient> p_i231593_10_) {
            this.name = p_i231593_3_;
            this.durabilityMultiplier = p_i231593_4_;
            this.slotProtections = p_i231593_5_;
            this.enchantmentValue = p_i231593_6_;
            this.toughness = p_i231593_8_;
            this.knockbackResistance = p_i231593_9_;
            this.repairIngredient = new LazyValue<>(p_i231593_10_);
        }

        public int getDurabilityForSlot(EquipmentSlotType p_200896_1_) {
            return HEALTH_PER_SLOT[p_200896_1_.getIndex()] * this.durabilityMultiplier;
        }

        public int getDefenseForSlot(EquipmentSlotType p_200902_1_) {
            return this.slotProtections[p_200902_1_.getIndex()];
        }

        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        public SoundEvent getEquipSound() {
            return null;
        }

        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }

        @OnlyIn(Dist.CLIENT)
        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }

        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }

    public static final class Tags {
        private static final TagRegistry<Item> HELPER = TagRegistryManager.create(new ResourceLocation("titan_items"), ITagCollectionSupplier::getItems);
        public static final ITag.INamedTag<Item> Tool = bind("tool");

        public static ITag.INamedTag<Item> bind(String p_199901_0_) {
            return HELPER.bind(p_199901_0_);
        }
    }
}
