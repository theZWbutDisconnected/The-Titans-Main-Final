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

public class WorldOreGeneration
{
	private static BiomeLoadingEvent event;
	public WorldOreGeneration(BiomeLoadingEvent e) {

		event = e;
	}
	
	public void biomeGenerate() {
		this.generateOre(TitanBlocks.copper_ore, 8, 0, 256);
		this.generateOre(TitanBlocks.tin_ore, 8, 0, 12);
		this.generateOre(TitanBlocks.chromium_ore, 7, 0, 12);
		this.generateOre(TitanBlocks.magnesium_ore, 7, 0, 12);
		this.generateOre(TitanBlocks.lead_ore, 6, 0, 12);
		this.generateOre(TitanBlocks.silver_ore, 6, 0, 12);
		this.generateOre(TitanBlocks.platinum_ore, 5, 0, 12);
		this.generateOre(TitanBlocks.nether_coal_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 11, 0, 512);
		this.generateOre(TitanBlocks.nether_stone_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 11, 0, 512);
		this.generateOre(TitanBlocks.nether_gold_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 6, 0, 512);
		this.generateOre(TitanBlocks.nether_diamond_ore, OreFeatureConfig.FillerBlockType.NETHERRACK, 6, 0, 512);
		this.generateOre(TitanBlocks.harcadium_ore, 3, 0, 12);
		this.generateOre(TitanBlocks.harcadium_ore_end_stone, TitanOreFilterBlock.END_STONE, 5, 0, 512);
		this.generateOre(TitanBlocks.harcadium_ore_obsidian, TitanOreFilterBlock.OBSIDIAN, 4, 0, 512);
		this.generateOre(TitanBlocks.void_ore, 1, 0, 6);
		this.generateOre(TitanBlocks.void_ore_end_stone, TitanOreFilterBlock.END_STONE, 3, 0, 512);
		this.generateOre(TitanBlocks.void_ore_obsidian, TitanOreFilterBlock.OBSIDIAN, 2, 0, 512);
		this.generateOre(TitanBlocks.adamantium_ore, TitanOreFilterBlock.OBSIDIAN, 1, 0, 512);
	}
	
	public void generateOre(Block block, RuleTest filter, int size, int min, int max) {
		OreFeatureConfig conf = new OreFeatureConfig(filter, block.defaultBlockState(), size);
		ConfiguredPlacement<TopSolidRangeConfig> cpm = Placement.RANGE.configured(new TopSolidRangeConfig(min, min, max));
		ConfiguredFeature<?,?> cf = oreFeature(block, conf, cpm, size);
		event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, cf);
	}
	
	public void generateOre(Block block, int size, int min, int max) {
		this.generateOre(block, OreFeatureConfig.FillerBlockType.NATURAL_STONE, size, min, max);
	}
	
	public static ConfiguredFeature<?,?> oreFeature(Block block, OreFeatureConfig conf, ConfiguredPlacement cpm, int size) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, block.getRegistryName(), Feature.ORE.configured(conf).decorated(cpm).squared().count(size));
	}
	
	public static final class TitanOreFilterBlock {
		public static final RuleTest END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
		public static final RuleTest OBSIDIAN = new BlockMatchRuleTest(Blocks.OBSIDIAN);
	}
}
