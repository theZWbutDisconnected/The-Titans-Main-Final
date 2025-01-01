package net.minecraft.theTitans.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.entity.titan.EntityTitanPart;
import net.minecraft.entity.titan.EntityWitherzilla;
import net.minecraft.item.*;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

public class ItemAdminiumSword extends ItemTitanSword {
    public ItemAdminiumSword(IItemTier tier) {
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
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.BOW;
    }

    @Override
    public boolean hurtEnemy(final ItemStack stack, final LivingEntity target, final LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (p_226874_1_) -> {
            p_226874_1_.broadcastBreakEvent(attacker.getUsedItemHand());
        });
        if (target != null && !target.level.isClientSide) {
            target.lerpMotion(0.0, 1.0, 0.0);
            target.hurtDuration = 0;
            if (target.getBbHeight() < 6.0f && !(target instanceof EntityTitan)) {
                if (target.isOnGround()) {
                    return true;
                }
            }
            try {
                ObfuscationReflectionHelper.findField(Entity.class, new String[]{"recentlyHit"}.toString()).setInt(target, 100);
            } catch (Exception e) {
                target.hurtDuration = 0;
            }
            try {
                ObfuscationReflectionHelper.findField(Entity.class, new String[]{"hurt_timer"}.toString()).setInt(target, 100);
                target.hurtDuration = 0;
            } catch (Exception e) {
                target.hurtDuration = 0;
            }
            target.setHealth(target.getHealth() - 5.0E9f);
            target.hurt(DamageSourceExtra.causeAntiTitanDamage(attacker), 5.0E9f);
            target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
            if (target.getBbHeight() == 50.0f && target.getBbWidth() == 15.0f) {
                target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(0.0);
                target.hurt(DamageSourceExtra.causeAntiTitanDamage(attacker), 40.0f);
                target.handleEntityEvent((byte) 3);
                //target.addEffect(new PotionEffect(ClientProxy.death.id, Integer.MAX_VALUE, 19));
                ++target.deathTime;
            }
            if (target instanceof EntityTitan && ((EntityTitan) target).canBeHurtByPlayer() && !(target instanceof EntityWitherzilla) && ((EntityTitan) target).getInvulTime() < 1) {
                target.level.explode(null, target.getX(), target.getY() + target.getBbHeight() * 0.5f, target.getZ(), 7.0f, false, Explosion.Mode.NONE);
                target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                ((EntityTitan) target).setTitanHealth(target.getHealth() - 1000.0f);
            }
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    @Override
    public void releaseUsing(ItemStack p_77615_1_, World p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_) {
        int j = this.getUseDuration(p_77615_1_) - p_77615_4_;
        final ArrowLooseEvent event = new ArrowLooseEvent((PlayerEntity) p_77615_3_, p_77615_1_, p_77615_2_, j, false);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }
        j = event.getCharge();
        float f = j / 60.0f;
        f = (f * f + f * 2.0f) / 3.0f;
        if (f < 0.1) {
            return;
        }
        if (f > 1.0f) {
            f = 1.0f;
        }
        p_77615_3_.playSound(TitanSounds.titanSwing, 1.0f, 2.0f);
        p_77615_3_.swing(p_77615_3_.getUsedItemHand());
        final Vector3d vec3 = p_77615_3_.getLookAngle();
        final double dx = vec3.x * 4.0;
        final double dy = p_77615_3_.getEyeHeight() + vec3.y * 4.0;
        final double dz = vec3.z * 4.0;
        final List list1 = p_77615_3_.level.getEntities(p_77615_3_, p_77615_3_.getBoundingBox().inflate(4.0, 4.0, 4.0).move(dx, dy, dz));
        if (list1 != null && !list1.isEmpty()) {
            for (int i11 = 0; i11 < list1.size(); ++i11) {
                final Entity entity1 = (Entity) list1.get(i11);
                if (entity1 != p_77615_3_ && (entity1 instanceof LivingEntity || entity1 instanceof EntityTitanPart || entity1 instanceof EntityTitan)) {
                    final Entity entity2 = entity1;
                    entity2.lerpMotion(0.0, 1.0, 0.0);
                    try {
                        ObfuscationReflectionHelper.findField(Entity.class, new String[]{"recentlyHit"}.toString()).setInt(entity1, 100);
                    } catch (Exception ex) {
                    }
                    entity1.hurt(DamageSourceExtra.causeAntiTitanDamage(p_77615_3_), 1.0E10f * f);
                    if (entity1 instanceof LivingEntity) {
                        ((LivingEntity) entity1).setHealth(((LivingEntity) entity1).getHealth() - 2000.0f * f);
                    }
                    entity1.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                    if (entity1 instanceof EntityTitan && ((EntityTitan) entity1).canBeHurtByPlayer() && !(entity1 instanceof EntityWitherzilla) && ((EntityTitan) entity1).getInvulTime() < 1) {
                        entity1.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                        ((EntityTitan) entity1).setTitanHealth(((EntityTitan) entity1).getHealth() - 2000.0f * f);
                    }
                }
            }
        }
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
