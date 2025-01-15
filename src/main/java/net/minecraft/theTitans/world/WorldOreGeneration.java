package net.minecraft.theTitans.world;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraft.block.Block;
import net.minecraft.theTitans.TitanBlocks;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.biome.Biome;
import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.server.ServerWorld;

public class WorldOreGeneration
{
	private static BiomeLoadingEvent event;
	public WorldOreGeneration(BiomeLoadingEvent e) {
		event = e;
	}
	
	public void biomeGenerate() {
		if (event.getCategory().getName() == "nether") {
			this.generateOre(TitanBlocks.nether_coal_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 24, 20, 256);
			this.generateOre(TitanBlocks.nether_stone_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 16, 20, 256);
			this.generateOre(TitanBlocks.nether_gold_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 16, 2, 256);
			this.generateOre(TitanBlocks.nether_diamond_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 7, 1, 256);
		} else if (event.getCategory().getName() == "the_end") {
			this.generateOre(TitanBlocks.harcadium_ore_end_stone, TitanOreFilterBlock.END_STONE, 16, 30, 128);
			this.generateOre(TitanBlocks.void_ore_end_stone, TitanOreFilterBlock.END_STONE, 7, 10, 128);
		} else if (event.getCategory().getName() == "the_void") {
			this.generateOre(Blocks.OBSIDIAN, TitanOreFilterBlock.BEDROCK, 32, 20, 128);
		} else if (event.getCategory().getName() == "nowhere") {
			this.generateOre(TitanBlocks.harcadium_ore_obsidian, TitanOreFilterBlock.OBSIDIAN, 20, 60, 256);
			this.generateOre(TitanBlocks.void_ore_obsidian, TitanOreFilterBlock.OBSIDIAN, 15, 20, 256);
			this.generateOre(Blocks.COAL_ORE, TitanOreFilterBlock.OBSIDIAN, 16, 1, 64);
			this.generateOre(Blocks.IRON_ORE, TitanOreFilterBlock.OBSIDIAN, 8, 1, 64);
			this.generateOre(Blocks.GOLD_ORE, TitanOreFilterBlock.OBSIDIAN, 8, 1, 64);
			this.generateOre(Blocks.DIAMOND_ORE, TitanOreFilterBlock.OBSIDIAN, 7, 1, 64);
			this.generateOre(Blocks.REDSTONE_ORE, TitanOreFilterBlock.OBSIDIAN, 7, 1, 64);
			this.generateOre(Blocks.LAPIS_ORE, TitanOreFilterBlock.OBSIDIAN, 6, 1, 64);
			this.generateOre(TitanBlocks.adamantium_ore, TitanOreFilterBlock.OBSIDIAN, 1, 1, 128);
		} else {
			this.generateOre(TitanBlocks.void_ore, 2, 1, 8);
			this.generateOre(TitanBlocks.harcadium_ore, 4, 1, 12);
			this.generateOre(TitanBlocks.copper_ore, 16, 0, 128);
			this.generateOre(TitanBlocks.tin_ore, 16, 0, 128);
			this.generateOre(TitanBlocks.chromium_ore, 9, 0, 48);
			this.generateOre(TitanBlocks.magnesium_ore, 9, 0, 48);
			this.generateOre(TitanBlocks.lead_ore, 9, 0, 48);
			this.generateOre(TitanBlocks.silver_ore, 8, 0, 32);
			this.generateOre(TitanBlocks.platinum_ore, 7, 0, 28);
			this.generateOre(Blocks.COAL_BLOCK, 16, 8, 128);
			this.generateOre(Blocks.IRON_BLOCK, 8, 6, 64);
			this.generateOre(Blocks.GOLD_BLOCK, 8, 4, 32);
			this.generateOre(Blocks.DIAMOND_BLOCK, 7, 6, 16);
			this.generateOre(Blocks.EMERALD_BLOCK, 7, 6, 16);
			this.generateOre(Blocks.REDSTONE_BLOCK, 7, 4, 32);
			this.generateOre(Blocks.LAPIS_BLOCK, 6, 3, 16);
			this.generateOre(Blocks.COAL_ORE, 32, 8, 128);
			this.generateOre(Blocks.IRON_ORE, 24, 6, 64);
			this.generateOre(Blocks.GOLD_ORE, 24, 4, 32);
			this.generateOre(Blocks.DIAMOND_ORE, 18, 6, 16);
			this.generateOre(Blocks.EMERALD_ORE, 18, 6, 16);
			this.generateOre(Blocks.REDSTONE_ORE, 18, 4, 32);
			this.generateOre(Blocks.LAPIS_ORE, 16, 3, 16);
		}
	}
	
	public void generateOre(Block block, RuleTest filter, int size, int min, int max) {
		OreFeatureConfig conf = new OreFeatureConfig(filter, block.defaultBlockState(), (int)(size * 0.5));
		ConfiguredPlacement<TopSolidRangeConfig> cpm = Placement.RANGE.configured(new TopSolidRangeConfig(min, min, max));
		ConfiguredFeature<?,?> cf = oreFeature(block, conf, cpm, size);
		event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, cf);
	}
	/*
	public void generateOre(Block block, RuleTest filter, int size, int min, int max) {
		Random rand = new Random();
		rand.setSeed(((ServerWorld)(Minecraft.getInstance().level.getServer().getAllLevels()[0])).getSeed());
		this generateOre(block, filter, size, min, max, rand.nextInt(16));
	}
	*/
	public void generateOre(Block block, int size, int min, int max) {
		this.generateOre(block, OreFeatureConfig.FillerBlockType.NATURAL_STONE, size, min, max);
	}
	
	public static ConfiguredFeature<?,?> oreFeature(Block block, OreFeatureConfig conf, ConfiguredPlacement cpm, int size) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, block.getRegistryName(), Feature.ORE.configured(conf).decorated(cpm).squared().count(size));
	}
	
	public static final class TitanOreFilterBlock {
		public static final RuleTest END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
		public static final RuleTest OBSIDIAN = new BlockMatchRuleTest(Blocks.OBSIDIAN);
		public static final RuleTest BEDROCK = new BlockMatchRuleTest(Blocks.BEDROCK);
	}
}
