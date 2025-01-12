package net.minecraft.theTitans.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;

public class BlockVoidOre extends OreBlock {
    public static final SoundType soundTypeVoid;

    static {
        soundTypeVoid = new SoundType(1.0F, 0.5F, SoundEvents.STONE_BREAK, SoundEvents.STONE_STEP, SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);
    }

    public BlockVoidOre(Properties p_i48357_1_) {
        super(p_i48357_1_);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> stack = new ArrayList<>();
        stack.add(new ItemStack(TitanItems.voidItem));
        return stack;
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return MathHelper.nextInt(new Random(), 12000, 12000);
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity) {
		if (!(
		collidingEntity instanceof ExperienceOrbEntity || 
		(collidingEntity instanceof ItemEntity && ((ItemEntity)collidingEntity).getItem().getItem() == TitanItems.voidItem)
		)) {
            collidingEntity.hurt(DamageSource.OUT_OF_WORLD, 4.0f);
            collidingEntity.setDeltaMovement(collidingEntity.getDeltaMovement().x * 0.2, collidingEntity.getDeltaMovement().y, collidingEntity.getDeltaMovement().z * 0.2);
            if (collidingEntity instanceof PlayerEntity) {
               ((PlayerEntity) collidingEntity).getFoodData().addExhaustion(0.2f);
            }
            if (collidingEntity instanceof LivingEntity && ((LivingEntity) collidingEntity).getRandom().nextInt(10) == 0) {
                collidingEntity.setRemainingFireTicks(40);
            }
            if (collidingEntity instanceof LivingEntity && ((LivingEntity) collidingEntity).getRandom().nextInt(60) == 0) {
                ((LivingEntity) collidingEntity).addEffect(new EffectInstance(Effects.WITHER, 160, 1));
            }
		}
        return super.collisionExtendsVertically(state, world, pos, collidingEntity);
    }

    @Override
    public void playerDestroy(World p_180657_1_, PlayerEntity p_180657_2_, BlockPos p_180657_3_, BlockState p_180657_4_, @Nullable TileEntity p_180657_5_, ItemStack p_180657_6_) {
        p_180657_2_.awardStat(Stats.BLOCK_MINED.get(this));
        p_180657_2_.causeFoodExhaustion(0.005F);
        p_180657_1_.explode(null, p_180657_2_.getX(), p_180657_2_.getY(), p_180657_2_.getZ(), 1.0f, Explosion.Mode.BREAK);
        dropResources(p_180657_4_, p_180657_1_, p_180657_3_, p_180657_5_, p_180657_2_, p_180657_6_);
    }

    @Override
    public void animateTick(BlockState p_225534_1_, World p_149734_1_, BlockPos p_225534_3_, Random p_149734_5_) {
        this.func_150186_m(p_149734_1_, p_225534_3_.getX(), p_225534_3_.getY(), p_225534_3_.getZ());
        if (p_149734_5_.nextInt(10) == 0) {
            p_149734_1_.playSound(null, p_225534_3_.getX() + 0.5f, p_225534_3_.getY() + 0.5f, p_225534_3_.getZ() + 0.5f, TitanSounds.harcadiumBlockHum, SoundCategory.NEUTRAL, 2.0f, 1.0f);
        }
    }

    private void func_150186_m(final World p_150186_1_, final int p_150186_2_, final int p_150186_3_, final int p_150186_4_) {
        final Random random = p_150186_1_.random;
        final double d0 = 0.0625;
        for (Direction direction : Direction.values()) {
            BlockPos blockpos = new BlockPos(p_150186_2_, p_150186_3_, p_150186_4_).relative(direction);
            if (!p_150186_1_.getBlockState(blockpos).isSolidRender(p_150186_1_, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX() : (double) random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY() : (double) random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ() : (double) random.nextFloat();
                p_150186_1_.addParticle(ParticleTypes.SMOKE, (double) p_150186_2_ + d1, (double) p_150186_3_ + d2, (double) p_150186_4_ + d3, 0.0D, 0.0D, 0.0D);
                p_150186_1_.addParticle(ParticleTypes.LARGE_SMOKE, (double) p_150186_2_ + d1, (double) p_150186_3_ + d2, (double) p_150186_4_ + d3, 0.0D, 0.0D, 0.0D);
                p_150186_1_.addParticle(ParticleTypes.SMOKE, (double) p_150186_2_ + d1, (double) p_150186_3_ + d2, (double) p_150186_4_ + d3, 0.0D, 0.0D, 0.0D);
                p_150186_1_.addParticle(ParticleTypes.SMOKE, (double) p_150186_2_ + d1, (double) p_150186_3_ + d2, (double) p_150186_4_ + d3, 0.0D, 0.0D, 0.0D);
                p_150186_1_.addParticle(ParticleTypes.SMOKE, (double) p_150186_2_ + d1, (double) p_150186_3_ + d2, (double) p_150186_4_ + d3, 0.0D, 0.0D, 0.0D);
                p_150186_1_.addParticle(ParticleTypes.SMOKE, (double) p_150186_2_ + d1, (double) p_150186_3_ + d2, (double) p_150186_4_ + d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
