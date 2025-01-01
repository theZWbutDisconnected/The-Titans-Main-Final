package net.minecraft.theTitans.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockCompactBedrock extends Block {
    public BlockCompactBedrock(Material material, String name) {
        super(AbstractBlock.Properties
                .of(material)
                .sound(SoundType.STONE)
                .harvestTool(ToolType.PICKAXE)
                .strength(-1.0f, 1.8E8f)
                .harvestLevel(10000)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return false;
    }
}
