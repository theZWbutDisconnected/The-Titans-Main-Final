package net.minecraft.theTitans.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockMalgrumCrop extends CropsBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f), Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f), Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f), Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f), Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f), Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f), Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f), Block.box(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f)};

    public BlockMalgrumCrop(Properties p_i48421_1_) {
        super(p_i48421_1_);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE_BY_AGE[p_220053_1_.getValue(this.getAgeProperty())];
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return TitanItems.malgrumSeeds;
    }

    public void randomTick(BlockState p_225542_1_, ServerWorld p_225542_2_, BlockPos p_225542_3_, Random p_225542_4_) {
        super.randomTick(p_225542_1_, p_225542_2_, p_225542_3_, p_225542_4_);
        if (p_225542_2_.getRawBrightness(p_225542_3_, 0) >= 9) {
            int i = this.getAge(p_225542_1_);
            if (i < this.getMaxAge()) {
                if (p_225542_4_.nextInt(2 * (1 + i)) == 0) {
                    p_225542_2_.setBlock(p_225542_3_, this.getStateForAge(i + 1), 2);
                }
            }
        }

    }
}
