package net.minecraft.theTitans.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TitanSounds;

public class ItemVoidAxe extends ItemTitanAxe {
    public ItemVoidAxe(IItemTier p_i48460_1_) {
        super(p_i48460_1_);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.setDamageValue(1);
        if (target != null && (target.getBbHeight() >= 7.0f || target instanceof EntityTitan || !target.isOnGround())) {
            target.hurt(DamageSourceExtra.causeAntiTitanDamage(attacker), 2500.0f);
            target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
        }
        return true;
    }
}
