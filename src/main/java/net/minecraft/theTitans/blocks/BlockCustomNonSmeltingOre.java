package net.minecraft.theTitans.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.theTitans.TitanBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCustomNonSmeltingOre extends BlockTitanOre {
    private final Item droppingItem;
    private final int minXP;
    private final int maxXP;
    private final Random rand;

    public BlockCustomNonSmeltingOre(final int harvestLevel, final String name, final float hardness, final float resistance, final Item item, final int minxp, final int maxxp) {
        super(harvestLevel, name, hardness, resistance);
        this.rand = new Random();
        this.droppingItem = item;
        this.minXP = minxp;
        this.maxXP = maxxp;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> stack = new ArrayList<>();
        stack.add(new ItemStack(this.droppingItem));
        return stack;
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return MathHelper.nextInt(this.rand, this.minXP, this.maxXP);
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return (this != TitanBlocks.adamantium_ore && super.canEntityDestroy(state, world, pos, entity));
    }
}
