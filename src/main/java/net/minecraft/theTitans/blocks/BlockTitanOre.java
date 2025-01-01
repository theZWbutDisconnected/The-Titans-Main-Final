package net.minecraft.theTitans.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.List;

public class BlockTitanOre extends OreBlock {
    private final String name;

    public BlockTitanOre(final int harvestLevel, final String name, final float hardness, final float resistance) {
        super(AbstractBlock.Properties
                .of(Material.PISTON)
                .sound(SoundType.STONE)
                .harvestTool(ToolType.PICKAXE)
                .strength(hardness, resistance)
                .harvestLevel(harvestLevel)
                .requiresCorrectToolForDrops()
        );
        this.name = name;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> stack = new ArrayList<>();
        stack.add(new ItemStack(this));
        return stack;
    }

}
