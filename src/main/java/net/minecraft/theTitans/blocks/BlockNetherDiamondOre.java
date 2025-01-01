package net.minecraft.theTitans.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockNetherDiamondOre extends OreBlock {
    public BlockNetherDiamondOre(Material material) {
        super(AbstractBlock.Properties
                .of(material)
                .sound(SoundType.STONE)
                .strength(3.0f, 5.0f)
                .harvestLevel(0)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return MathHelper.nextInt(new Random(), 3, 7);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> stack = new ArrayList<>();
        stack.add(new ItemStack(Items.COAL));
        return stack;
    }
}
