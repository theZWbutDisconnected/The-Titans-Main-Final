package net.minecraft.theTitans.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class BlockTitanBlock extends Block {

    public BlockTitanBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> stack = new ArrayList<>();
        stack.add(new ItemStack(this));
        return stack;
    }
}
