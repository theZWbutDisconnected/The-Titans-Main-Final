package net.minecraft.theTitans.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockNetherStoneOre extends OreBlock {
    public BlockNetherStoneOre(Material material) {
        super(AbstractBlock.Properties
                .of(material)
                .sound(SoundType.STONE)
                .strength(1.5f, 10.0f)
                .harvestLevel(0)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return MathHelper.nextInt(new Random(), 0, 1);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> stack = new ArrayList<>();
        stack.add(new ItemStack(Blocks.COBBLESTONE));
        return stack;
    }
}
