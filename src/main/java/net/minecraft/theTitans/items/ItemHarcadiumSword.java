package net.minecraft.theTitans.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.entity.titan.EntityTitanPart;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

import java.util.List;

public class ItemHarcadiumSword extends ItemTitanSword {
    public ItemHarcadiumSword(IItemTier p_i48460_1_) {
        super(p_i48460_1_);
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.BOW;
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
                final Entity target = (Entity) list1.get(i11);
                if (target != p_77615_3_ && (target instanceof LivingEntity || target instanceof EntityTitanPart)) {
                    target.hurt(DamageSourceExtra.causeAntiTitanDamage(p_77615_3_), 3000.0f * f);
                    target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                    target.playSound(TitanSounds.slashFlesh, 2.0f, 1.25f);
                }
            }
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.setDamageValue(1);
        if (target != null) {
            target.playSound(TitanSounds.slashFlesh, 2.0f, 1.3f + target.getRandom().nextFloat() * 0.5f);
            if (target.getBbHeight() >= 6.0f || target instanceof EntityTitan || !target.isOnGround()) {
                target.hurt(DamageSourceExtra.causeAntiTitanDamage(attacker), 500.0f);
                target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
            }
        }
        return true;
    }
}
