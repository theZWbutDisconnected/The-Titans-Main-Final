package net.minecraft.theTitans.items;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ItemAdminiumPickaxe extends ItemTitanPickaxe {
    public ItemAdminiumPickaxe(IItemTier tier) {
        super(tier);
    }

    public static BlockRayTraceResult raytraceFromEntity(World world, PlayerEntity player, boolean wut, double range) {
        final float f = 1.0f;
        final float f2 = player.xRotO + (player.xRot - player.xRotO) * f;
        final float f3 = player.yRotO + (player.yRot - player.yRotO) * f;
        final double d0 = player.xo + (player.getX() - player.xo) * f;
        double d2 = player.yo + (player.getY() - player.yo) * f;
        if (!world.isClientSide && player instanceof PlayerEntity) {
            d2 += 1.62;
        }
        final double d3 = player.zo + (player.getZ() - player.zo) * f;
        final Vector3d vec3 = new Vector3d(d0, d2, d3);
        final float f4 = MathHelper.cos(-f3 * 0.017453292f - 3.1415927f);
        final float f5 = MathHelper.sin(-f3 * 0.017453292f - 3.1415927f);
        final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
        final float f7 = MathHelper.sin(-f2 * 0.017453292f);
        final float f8 = f5 * f6;
        final float f9 = f4 * f6;
        final double d4 = range;
        final Vector3d vec4 = vec3.add(f8 * d4, f7 * d4, f9 * d4);
        return world.clip(new RayTraceContext(vec3, vec4, RayTraceContext.BlockMode.OUTLINE, wut ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, player));
    }

    @Override
    public boolean hurtEnemy(final ItemStack stack, final LivingEntity target, final LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (p_226874_1_) -> {
            p_226874_1_.broadcastBreakEvent(attacker.getUsedItemHand());
        });
        if (target != null && (target.getBbHeight() >= 6.0f || target instanceof EntityTitan || !target.isOnGround())) {
            target.hurt(DamageSourceExtra.causeAntiTitanDamage(attacker), 3.0E9f);
            target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onEntitySwing(final ItemStack stack, final LivingEntity entityLiving) {
        final BlockRayTraceResult raycast = raytraceFromEntity(entityLiving.level, (PlayerEntity) entityLiving, true, 6.0);
        if (!entityLiving.level.isClientSide && raycast != null) {
            final Block block = entityLiving.level.getBlockState(new BlockPos(raycast.getBlockPos().getX(), raycast.getBlockPos().getY(), raycast.getBlockPos().getZ())).getBlock();
            entityLiving.level.levelEvent(2001, new BlockPos(raycast.getBlockPos().getX(), raycast.getBlockPos().getY(), raycast.getBlockPos().getZ()), Block.getId(block.defaultBlockState()));
            final ItemEntity entityitem = new ItemEntity(entityLiving.level, raycast.getBlockPos().getX(), raycast.getBlockPos().getY(), raycast.getBlockPos().getZ(), new ItemStack(Item.byBlock(block), 1));
            entityLiving.level.addFreshEntity(entityitem);
            entityLiving.level.removeBlock(new BlockPos(raycast.getBlockPos().getX(), raycast.getBlockPos().getY(), raycast.getBlockPos().getZ()), true);
        }
        return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public void setDamage(final ItemStack stack, final int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    public Rarity getRarity(final ItemStack stack) {
        return TheTitans.godly;
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }
}
