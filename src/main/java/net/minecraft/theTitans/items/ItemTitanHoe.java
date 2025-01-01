package net.minecraft.theTitans.items;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemTitanHoe extends ItemTitanTool {
    protected static final Map<Block, BlockState> TILLABLES = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(), Blocks.GRASS_PATH, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()));
    private static final Set<Block> DIGGABLES = ImmutableSet.of(Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.HAY_BLOCK, Blocks.DRIED_KELP_BLOCK, Blocks.TARGET, Blocks.SHROOMLIGHT, Blocks.SPONGE, Blocks.WET_SPONGE, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES);

    public ItemTitanHoe(IItemTier p_i48469_1_, Properties p_i48469_4_) {
        super(p_i48469_1_, DIGGABLES, p_i48469_4_.tab(TheTitans.titansTab).addToolType(ToolType.HOE, p_i48469_1_.getLevel()));
    }

    public ItemTitanHoe(IItemTier p_i48469_1_) {
        this(p_i48469_1_, new Properties());
    }

    @javax.annotation.Nullable
    public static BlockState getHoeTillingState(BlockState originalState) {
        return TILLABLES.get(originalState.getBlock());
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        Set<ToolType> set = new HashSet<>();
        set.add(ToolType.HOE);
        return set;
    }

    public ActionResultType useOn(ItemUseContext p_195939_1_) {
        World world = p_195939_1_.getLevel();
        BlockPos blockpos = p_195939_1_.getClickedPos();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(p_195939_1_);
        if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        if (p_195939_1_.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
            BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, p_195939_1_.getPlayer(), p_195939_1_.getItemInHand(), net.minecraftforge.common.ToolType.HOE);
            if (blockstate != null) {
                PlayerEntity playerentity = p_195939_1_.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide) {
                    world.setBlock(blockpos, blockstate, 11);
                    if (playerentity != null) {
                        p_195939_1_.getItemInHand().hurtAndBreak(1, playerentity, (p_220043_1_) -> {
                            p_220043_1_.broadcastBreakEvent(p_195939_1_.getHand());
                        });
                    }
                }

                return ActionResultType.sidedSuccess(world.isClientSide);
            }
        }

        return ActionResultType.PASS;
    }
}
