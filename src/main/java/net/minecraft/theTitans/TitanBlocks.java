package net.minecraft.theTitans;

import com.zerwhit.annotations.EventRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.theTitans.blocks.*;
import net.minecraftforge.common.ToolType;

public class TitanBlocks {
    @EventRegistry(identifier = "copper_ore")
    public static Block copper_ore;
    @EventRegistry(identifier = "tin_ore")
    public static Block tin_ore;
    @EventRegistry(identifier = "chromium_ore")
    public static Block chromium_ore;
    @EventRegistry(identifier = "magnesium_ore")
    public static Block magnesium_ore;
    @EventRegistry(identifier = "lead_ore")
    public static Block lead_ore;
    @EventRegistry(identifier = "silver_ore")
    public static Block silver_ore;
    @EventRegistry(identifier = "platinum_ore")
    public static Block platinum_ore;
    @EventRegistry(identifier = "harcadium_ore")
    public static Block harcadium_ore;
    @EventRegistry(identifier = "harcadium_ore_end_stone")
    public static Block harcadium_ore_end_stone;
    @EventRegistry(identifier = "harcadium_ore_obsidian")
    public static Block harcadium_ore_obsidian;
    @EventRegistry(identifier = "void_ore")
    public static Block void_ore;
    @EventRegistry(identifier = "void_ore_end_stone")
    public static Block void_ore_end_stone;
    @EventRegistry(identifier = "void_ore_obsidian")
    public static Block void_ore_obsidian;
    @EventRegistry(identifier = "adamantium_ore")
    public static Block adamantium_ore;
    @EventRegistry(identifier = "nether_stone_ore")
    public static Block nether_stone_ore;
    @EventRegistry(identifier = "nether_coal_ore")
    public static Block nether_coal_ore;
    @EventRegistry(identifier = "nether_gold_ore")
    public static Block nether_gold_ore;
    @EventRegistry(identifier = "nether_diamond_ore")
    public static Block nether_diamond_ore;
    @EventRegistry(identifier = "harcadium_block")
    public static Block harcadium_block;
    @EventRegistry(identifier = "void_block")
    public static Block void_block;
    @EventRegistry(identifier = "bedrock_compact")
    public static Block bedrock_block;
    @EventRegistry(identifier = "malgrum")
    public static Block malgrumCrop;
    @EventRegistry(identifier = "pleasant_blade")
    public static Block pleasantBladeCrop;
    @EventRegistry(identifier = "magic_pumpkin")
    public static Block magic_pumpkin;
    @EventRegistry(identifier = "stoneperch")
    public static Block stoneperch;
    @EventRegistry(identifier = "sandstoneperch")
    public static Block sandstoneperch;
    @EventRegistry(identifier = "obsidianperch")
    public static Block obsidianperch;
    @EventRegistry(identifier = "goldperch")
    public static Block goldperch;
    @EventRegistry(identifier = "ironperch")
    public static Block ironperch;
    @EventRegistry(identifier = "endstoneperch")
    public static Block endstoneperch;
    @EventRegistry(identifier = "netherbrickperch")
    public static Block netherbrickperch;

    static {
        copper_ore = new BlockTitanOre(0, "copper_ore", 3.0f, 5.0f);
        tin_ore = new BlockTitanOre(0, "tin_ore", 3.0f, 5.0f);
        chromium_ore = new BlockTitanOre(1, "chromium_ore", 3.0f, 5.0f);
        magnesium_ore = new BlockTitanOre(1, "magnesium_ore", 3.0f, 5.0f);
        lead_ore = new BlockTitanOre(1, "lead_ore", 5.0f, 5.0f);
        silver_ore = new BlockTitanOre(1, "silver_ore", 5.0f, 10.0f);
        platinum_ore = new BlockTitanOre(2, "platinum_ore", 5.0f, 15.0f);
        nether_stone_ore = new BlockNetherStoneOre(Material.STONE);
        nether_coal_ore = new BlockNetherCoalOre(Material.STONE);
        nether_gold_ore = new BlockNetherGoldOre(Material.STONE);
        nether_diamond_ore = new BlockNetherDiamondOre(Material.STONE);
        bedrock_block = new BlockCompactBedrock(Material.STONE, "bedrock_compact");
        adamantium_ore = new BlockCustomNonSmeltingOre(0, "adamantium_ore", -1.0f, 1.0E9f, TitanItems.adamantium, 1000000000, 1000000000);
        harcadium_ore = new BlockHarcadiumOre(
                AbstractBlock.Properties
                        .of(Material.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(3)
                        .requiresCorrectToolForDrops()
                        .strength(50.0f, 2000.0f)
                        .lightLevel((block) -> (int) (15 * 0.2f))
                        .sound(SoundType.STONE)
        );
        harcadium_ore_end_stone = new BlockHarcadiumOre(
                AbstractBlock.Properties
                        .of(Material.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(3)
                        .requiresCorrectToolForDrops()
                        .strength(50.0f, 2000.0f)
                        .lightLevel((block) -> (int) (15 * 0.2f))
                        .sound(SoundType.STONE)
        );
        harcadium_ore_obsidian = new BlockHarcadiumOre(
                AbstractBlock.Properties
                        .of(Material.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(3)
                        .requiresCorrectToolForDrops()
                        .strength(50.0f, 2000.0f)
                        .lightLevel((block) -> (int) (15 * 0.2f))
                        .sound(SoundType.STONE)
        );
        harcadium_block = new BlockHarcadiumBlock(
                AbstractBlock.Properties
                        .of(Material.METAL)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(3)
                        .requiresCorrectToolForDrops()
                        .strength(100.0f, 18000.0f)
                        .lightLevel((block) -> 15)
                        .sound(SoundType.STONE)
        );
        void_ore = new BlockVoidOre(
                AbstractBlock.Properties
                        .of(Material.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(1000)
                        .requiresCorrectToolForDrops()
                        .strength(800.0f, 6000000.0f)
                        .sound(BlockVoidOre.soundTypeVoid)
        );
        void_ore_end_stone = new BlockVoidOre(
                AbstractBlock.Properties
                        .of(Material.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(1000)
                        .requiresCorrectToolForDrops()
                        .strength(800.0f, 6000000.0f)
                        .sound(BlockVoidOre.soundTypeVoid)
        );
        void_ore_obsidian = new BlockVoidOre(
                AbstractBlock.Properties
                        .of(Material.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(1000)
                        .requiresCorrectToolForDrops()
                        .strength(800.0f, 6000000.0f)
                        .sound(BlockVoidOre.soundTypeVoid)
        );
        void_block = new BlockVoidBlock(
                AbstractBlock.Properties
                        .of(Material.METAL)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(1000)
                        .requiresCorrectToolForDrops()
                        .strength(2400.0f, 1.8E7f)
                        .sound(BlockVoidOre.soundTypeVoid)
        );
        malgrumCrop = new BlockMalgrumCrop(AbstractBlock.Properties.of(Material.PLANT));
        //pleasantBladeCrop = new BlockPleasantBladeCrop();
    }
}
