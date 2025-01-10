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

public class WorldOreGeneration
{
	public BiomeLoadingEvent event;
	public WorldOreGeneration() {}
	
	public final void biomeGenerate(final BiomeLoadingEvent e) {
		this.event = e;
		this.generateOre(TitanBlocks.harcadium_ore, 3, 0, 32);
	}
	
	public void generateOre(Block block, int size, int min, int max) {
		OreFeatureConfig conf = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, block.defaultBlockState(), size);
		ConfiguredPlacement<TopSolidRangeConfig> cpm = Placement.RANGE.configured(new TopSolidRangeConfig(min, min, max));
		ConfiguredFeature<?,?> cf = oreFeature(block, conf, cpm, size);
		event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, cf);
	}
	
	public static ConfiguredFeature<?,?> oreFeature(Block block, OreFeatureConfig conf, ConfiguredPlacement cpm, int size) {
		return Registry.register(WorldGenRegistries.NOISE_GENERATOR_SETTINGS, block.getRegistryName(), Feature.ORE.configured(conf).decorated(cpm).squared().count(size));
	}
}
