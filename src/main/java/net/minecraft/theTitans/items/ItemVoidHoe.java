package net.minecraft.theTitans.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.SoundEvents;

public class ItemVoidHoe extends ItemTitanHoe {
    public ItemVoidHoe(IItemTier p_i48460_1_) {
        super(p_i48460_1_);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.setDamageValue(1);
        if (target != null && target instanceof LivingEntity) {
            if (target instanceof LivingEntity && target.level.isClientSide) {
                for (int i = 0; i < 20; ++i) {
                    double d0 = target.getRandom().nextGaussian() * 0.02D;
                    double d1 = target.getRandom().nextGaussian() * 0.02D;
                    double d2 = target.getRandom().nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    target.level.addParticle(ParticleTypes.POOF,
                            target.getX() + (double) (target.getRandom().nextFloat() * target.getBbWidth() * 2.0F) - (double) target.getBbWidth() - d0 * d3,
                            target.getY() + (double) (target.getRandom().nextFloat() * target.getBbHeight()) - d1 * d3,
                            target.getZ() + (double) (target.getRandom().nextFloat() * target.getBbWidth() * 2.0F) - (double) target.getBbWidth() - d2 * d3,
                            d0, d1, d2
                    );
                }
            }
            if (target.getBbHeight() >= 6.0f || target instanceof EntityTitan || !target.isOnGround()) {
                target.hurt(DamageSourceExtra.causeAntiTitanDamage(attacker), 5000.0f);
                target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
            } else if (!(target instanceof PlayerEntity) && !(target instanceof GolemEntity)) {
                attacker.level.getChunk(target.xChunk, target.zChunk).removeEntity(target);
                target.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0f, 0.5f);
                attacker.addEffect(new EffectInstance(Effects.SATURATION, 1, 3, false, true));
                attacker.getAttribute(Attributes.MAX_HEALTH).setBaseValue(attacker.getMaxHealth() + target.getMaxHealth());
                attacker.heal(target.getMaxHealth());
                if (target instanceof MobEntity && target.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
                    attacker.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((float) attacker.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() + (float) target.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue());
                }
            }
        }
        return true;
    }
}
