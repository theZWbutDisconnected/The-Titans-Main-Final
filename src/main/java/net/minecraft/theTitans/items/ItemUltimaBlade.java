package net.minecraft.theTitans.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.titan.EntityGammaLightning;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.theTitans.render.items.RenderUltimaBlade;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ItemUltimaBlade extends ItemTitanSword {
    public ItemUltimaBlade() {
        super(TitanItems.Tier.Ultima, new Properties().setISTER(() -> RenderUltimaBlade::new));
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack p_getToolTypes_1_) {
        Set<ToolType> set = new HashSet<>();
        set.add(ToolType.PICKAXE);
        set.add(ToolType.AXE);
        set.add(ToolType.HOE);
        set.add(ToolType.SHOVEL);
        return set;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return !stack.hasTag() ? 0 : stack.getTag().getInt("DamageAntiCheat");
    }

    public void setDamageEx(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("DamageAntiCheat", Math.max(0, damage));
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        if (p_77624_1_.getDamageValue() > 0) {
            p_77624_3_.add(ITextComponent.nullToEmpty("\u00A73\u00A7lThe ultimate weapon."));
            p_77624_3_.add(ITextComponent.nullToEmpty("\u00A73\u00A7lCuts through all defences, even hacks."));
            p_77624_3_.add(ITextComponent.nullToEmpty("\u00A73\u00A7lNormally owned by Regnator"));
        } else {
            p_77624_3_.add(ITextComponent.nullToEmpty("\u00A7l\u00A7k\u00A73\u00A7lThe ultimate weapon."));
            p_77624_3_.add(ITextComponent.nullToEmpty("\u00A7l\u00A7k\u00A73\u00A7lCuts through all defences, even hacks."));
            p_77624_3_.add(ITextComponent.nullToEmpty("\u00A7l\u00A7k\u00A73\u00A7lNormally owned by Regnator"));
        }
    }

    @Override
    public boolean mineBlock(ItemStack p_179218_1_, World p_179218_2_, BlockState p_179218_3_, BlockPos p_179218_4_, LivingEntity p_179218_5_) {
        return true;
    }

    private void giveAdvice(final PlayerEntity player) {
        final List list1 = player.level.getEntities(player, player.getBoundingBox().inflate(16.0, 16.0, 16.0));
        if (list1 != null && !list1.isEmpty()) {
            for (int i1 = 0; i1 < list1.size(); ++i1) {
                final Entity entity = (Entity) list1.get(i1);
                if (entity instanceof IMob || entity instanceof PlayerEntity) {
                    if (player.canSee(entity)) {
                        player.sendMessage(new StringTextComponent("Ultima Blade: \u00A7l\u00A7oThe " + entity.getName().getString() + " is " + player.distanceTo(entity) + "\u00A7l\u00A7o blocks away from you."), UUID.randomUUID());
                    } else {
                        player.sendMessage(new StringTextComponent("Ultima Blade: \u00A7l\u00A7oAn unseen mob is located out of your \u00A7l\u00A7osight " + player.distanceTo(entity) + " blocks away from you."), UUID.randomUUID());
                        player.sendMessage(new StringTextComponent("Ultima Blade: \u00A7l\u00A7oThe mob's health: " + ((LivingEntity) entity).getHealth() + "/" + ((LivingEntity) entity).getMaxHealth()), UUID.randomUUID());
                        player.sendMessage(new StringTextComponent("Ultima Blade: \u00A7l\u00A7oThe mob's name: " + entity.getName().getString()), UUID.randomUUID());
                        player.playSound(SoundEvents.WITHER_HURT, 10.0f, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2f + 0.5f);
                    }
                }
            }
        }
    }

    private void offerAdvice(final PlayerEntity player) {
        final int y = MathHelper.floor(player.position().y);
        final int x = MathHelper.floor(player.position().x);
        final int z = MathHelper.floor(player.position().z);
        for (int l1 = -8; l1 <= 8; ++l1) {
            for (int i2 = -8; i2 <= 8; ++i2) {
                for (int j = -8; j <= 8; ++j) {
                    final int j2 = x + l1;
                    final int k = y + j;
                    final int m = z + i2;
                    final Block block = player.level.getBlockState(new BlockPos(j2, k, m)).getBlock();
                    if (block instanceof OreBlock) {
                        player.sendMessage(new StringTextComponent("Ultima Blade: \u00A7l\u00A7oI sense a " + block.getName().getString() + "\u00A7l\u00A7o within atleast " + MathHelper.sqrt(player.distanceToSqr(j2, k, m)) + " blocks of you."), UUID.randomUUID());
                        player.playSound(SoundEvents.WITHER_HURT, 10.0f, 0.6f);
                    }
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        if (p_77659_2_.level.isClientSide) {
            if (p_77659_2_.isCrouching()) {
                this.giveAdvice(p_77659_2_);
            } else {
                this.offerAdvice(p_77659_2_);
            }
        }
        if (p_77659_2_.inventory.contains(new ItemStack(TitanItems.optimaAxe))) {
            final ServerWorld worldserver = Minecraft.getInstance().getSingleplayerServer().getLevel(p_77659_2_.level.dimension());
            worldserver.setRainLevel(9999999);
            worldserver.setThunderLevel(1000000);
            final List list = p_77659_2_.level.getEntities(p_77659_2_, p_77659_2_.getBoundingBox().inflate(200.0, 100.0, 200.0));
            if (list != null && !list.isEmpty()) {
                for (int i1 = 0; i1 < list.size(); ++i1) {
                    final Entity entity = (Entity) list.get(i1);
                    if (entity != null && entity.isAlive() && entity instanceof LivingEntity && !(entity instanceof EntityTitan) && entity != p_77659_2_) {
                        //((LivingEntity) entity).addEffect(new EffectInstance(ClientProxy.electricJudgment.id, Integer.MAX_VALUE, 19));
                        entity.lerpMotion(-MathHelper.sin(p_77659_2_.yRot * 3.1415927f / 180.0f) * 1.25f, 1.0, MathHelper.cos(p_77659_2_.yRot * 3.1415927f / 180.0f) * 1.25f);
                        entity.hurt(DamageSourceExtra.lightningBolt, 49.0f);
                        p_77659_2_.level.addFreshEntity(new EntityGammaLightning(p_77659_2_.level, entity.position().x, entity.position().y + entity.getBbHeight(), entity.position().z, p_77659_2_.getRandom().nextFloat(), p_77659_2_.getRandom().nextFloat(), p_77659_2_.getRandom().nextFloat()));
                        entity.hurt(DamageSource.playerAttack(p_77659_2_).bypassArmor().bypassMagic(), 49.0f);
                        if (entity instanceof LivingEntity && !(entity instanceof EntityTitan) && (entity.getBbHeight() >= 6.0f || entity.isInvulnerable())) {
                            entity.remove();
                            entity.getEntityData().set(LivingEntity.DATA_HEALTH_ID, MathHelper.clamp(0.0f, 0.0f, ((LivingEntity) entity).getMaxHealth()));
                            entity.hurt(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                        }
                    }
                }
            }
        }
        return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    @Override
    public boolean hurtEnemy(ItemStack p_77644_1_, LivingEntity target, LivingEntity attacker) {
        attacker.level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(true, attacker.level.getServer());
        final float extradamage = EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), target.getMobType());
        final int knockbackAmount = EnchantmentHelper.getKnockbackBonus(attacker);
        if (target != null) {
            target.setRemainingFireTicks(Integer.MAX_VALUE);
            //target.addPotionEffect(new PotionEffect(ClientProxy.electricJudgment.id, Integer.MAX_VALUE, 19));
            target.hurtDuration = 0;
            if (target instanceof EntityTitan && (((EntityTitan) target).canBeHurtByPlayer())) {
                if (attacker instanceof PlayerEntity) {
                    target.hurt(DamageSource.playerAttack((PlayerEntity) attacker), this.func_150931_i());
                }
                ((EntityTitan) target).setTitanHealth(target.getHealth() - 3000.0f - extradamage);
                target.playSound(TitanSounds.titanPunch, 10.0f, 1.0f);
                ((EntityTitan) target).setTarget(attacker);
                ((EntityTitan) target).addTitanVelocity(-MathHelper.sin(attacker.yRot * 3.1415927f / 180.0f) * 2.0f + knockbackAmount, 0.5 + knockbackAmount, MathHelper.cos(attacker.yRot * 3.1415927f / 180.0f) * 2.0f + knockbackAmount);
            } else if (!(target instanceof EntityTitan)) {
                Label_0380:
                {
                    if (target.getBbHeight() != 50.0f || target.getBbWidth() != 15.0f) {
                        if (!EntityTitan.isOreSpawnBossToExempt(target)) {
                            break Label_0380;
                        }
                    }
                    try {
                        ObfuscationReflectionHelper.findField(Entity.class, new String[]{"hurt_timer"}.toString()).setInt(target, 0);
                    } catch (Exception e) {
                        target.hurtDuration = 0;
                    }
                    final double originalHealth = target.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
                    target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(0.0);
                    target.hurt(DamageSourceExtra.causeAntiTitanDamage(attacker).bypassArmor().bypassMagic(), 40.0f);
                    //target.addPotionEffect(new PotionEffect(ClientProxy.death.id, Integer.MAX_VALUE, 19));
                    ++target.deathTime;
                    target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(originalHealth);
                }
                target.getEntityData().set(LivingEntity.DATA_HEALTH_ID, MathHelper.clamp(target.getHealth() - this.func_150931_i() - extradamage, 0.0f, target.getMaxHealth()));
                target.playSound(TitanSounds.slashFlesh, 10.0f, 1.0f);
                target.setDeltaMovement(-MathHelper.sin(attacker.yRot * 3.1415927f / 180.0f) * 6.0f + knockbackAmount, 6.0 + knockbackAmount, MathHelper.cos(attacker.yRot * 3.1415927f / 180.0f) * 6.0f + knockbackAmount);
                if (!target.isAlive()) {
                    target.die(DamageSource.MAGIC);
                    target.level.broadcastEntityEvent(target, (byte) 3);
                }
            }
        }
        return true;
    }

    @Override
    public Rarity getRarity(ItemStack p_77613_1_) {
        return TheTitans.godly;
    }

    @Override
    public boolean isFoil(ItemStack p_77616_1_) {
        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        final int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, player);
        if (sharpness <= 0) {
            stack.enchant(Enchantments.SHARPNESS, 100);
        }
        final int smite = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, player);
        if (smite <= 0) {
            stack.enchant(Enchantments.SMITE, 100);
        }
        final int bug = EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, player);
        if (bug <= 0) {
            stack.enchant(Enchantments.BANE_OF_ARTHROPODS, 100);
        }
        final int fire = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, player);
        if (fire <= 0) {
            stack.enchant(Enchantments.FIRE_ASPECT, 100);
        }
        final int titanslaying = EnchantmentHelper.getEnchantmentLevel(Enchantments.MOB_LOOTING, player);
        if (titanslaying <= 0) {
            stack.enchant(Enchantments.MOB_LOOTING, 100);
        }
    }

    @Override
    public void inventoryTick(final ItemStack stack, final World worldIn, final Entity entityIn, final int itemSlot, final boolean isSelected) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        } else {
            stack.getTag().putBoolean("Unbreakable", true);
            if (stack.getTagElement("DamageAntiCheat") == null)
                stack.getTag().putInt("DamageAntiCheat", getMaxDamage());
        }
        entityIn.clearFire();
        if (entityIn instanceof PlayerEntity && !worldIn.isClientSide) {
            if (stack.getDamageValue() == 0 && entityIn.tickCount % 2 == 0) {
                entityIn.sendMessage(new StringTextComponent("Ultima Blade: You are unworthy of my power."), UUID.randomUUID());
                ((PlayerEntity) entityIn).inventory.removeItem(stack);
                entityIn.sendMessage(new StringTextComponent("\u00A77\u00A7lYou are unworthy of this weapon."), UUID.randomUUID());
            } else {
                //((PlayerEntity) entityIn).triggerAchievement(TitansAchievments.ultimaBlade);
                if (((PlayerEntity) entityIn).inventory.contains(new ItemStack(TitanItems.optimaAxe))) {
                    final ServerWorld worldserver = entityIn.level.getServer().getLevel(entityIn.level.dimension());
                    worldserver.setRainLevel(9999999);
                    worldserver.setThunderLevel(1000000);
                    if (((PlayerEntity) entityIn).getRandom().nextInt(5) == 0) {
                        for (int l = 0; l < 5; ++l) {
                            final int i = MathHelper.floor(entityIn.position().x + (((PlayerEntity) entityIn).getRandom().nextInt() * 200 - 100));
                            final int j = MathHelper.floor(entityIn.position().y + (((PlayerEntity) entityIn).getRandom().nextInt() * 100 - 50));
                            final int k = MathHelper.floor(entityIn.position().z + (((PlayerEntity) entityIn).getRandom().nextInt() * 200 - 100));
                            final EntityGammaLightning entitylightning = new EntityGammaLightning(entityIn.level, i, j, k, ((PlayerEntity) entityIn).getRandom().nextFloat(), ((PlayerEntity) entityIn).getRandom().nextFloat(), ((PlayerEntity) entityIn).getRandom().nextFloat());
                            //if (World.doesBlockHaveSolidTopSurface(entityIn.level, i, j - 1, k) && entityIn.level.checkNoEntityCollision(entitylightning.boundingBox) && entityIn.level.getCollidingBoundingBoxes(entitylightning, entitylightning.boundingBox).isEmpty() && !entityIn.level.isAnyLiquid(entitylightning.boundingBox)) {
                            entityIn.level.addFreshEntity(entitylightning);
                            //}
                        }
                    }
                    final List list = entityIn.level.getEntities(entityIn, entityIn.getBoundingBox().inflate(200.0, 100.0, 200.0));
                    if (list != null && !list.isEmpty()) {
                        for (int i2 = 0; i2 < list.size(); ++i2) {
                            final Entity entity = (Entity) list.get(i2);
                            if (entity != null && entity.isAlive() && entity instanceof LivingEntity && !(entity instanceof EntityTitan) && ((PlayerEntity) entityIn).getRandom().nextInt(60) == 0 && entity != entityIn) {
                                entity.lerpMotion(-MathHelper.sin(entityIn.yRot * 3.1415927f / 180.0f) * 1.25f, 1.0, MathHelper.cos(entityIn.yRot * 3.1415927f / 180.0f) * 1.25f);
                                entity.hurt(DamageSourceExtra.lightningBolt, 49.0f);
                                entityIn.level.addFreshEntity(new EntityGammaLightning(entityIn.level, entity.position().x, entity.position().y + entity.getBbHeight(), entity.position().z, ((PlayerEntity) entityIn).getRandom().nextFloat(), ((PlayerEntity) entityIn).getRandom().nextFloat(), ((PlayerEntity) entityIn).getRandom().nextFloat()));
                                LightningBoltEntity light = EntityType.LIGHTNING_BOLT.create(entityIn.level);
                                light.setPos(entity.position().x, entity.position().y + entity.getBbHeight(), entity.position().z);
                                entityIn.level.addFreshEntity(light);
                                entity.hurt(DamageSource.playerAttack((PlayerEntity) entityIn).bypassArmor().bypassMagic(), 49.0f);
                            }
                        }
                    }
                }
            }
        }
        if (entityIn.position().y < -45.0) {
            entityIn.lerpMotion(0.0, 10.0, 0.0);
        }
        stack.setHoverName(ITextComponent.nullToEmpty("\u00A7lThe Ultima Blade"));
        for (int m = 0; m < 3; ++m) {
            entityIn.level.addParticle(ParticleTypes.FIREWORK, entityIn.position().x + (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * entityIn.getBbWidth(), entityIn.position().y + ((LivingEntity) entityIn).getRandom().nextDouble() * entityIn.getBbHeight() + 1.62f, entityIn.position().z + (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * entityIn.getBbWidth(), (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * 2.0, 1.0, (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * 2.0);
            entityIn.level.addParticle(ParticleTypes.POOF, entityIn.position().x + (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * entityIn.getBbWidth(), entityIn.position().y + ((LivingEntity) entityIn).getRandom().nextDouble() * entityIn.getBbHeight() + 1.62f, entityIn.position().z + (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * entityIn.getBbWidth(), (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * 2.0, 1.0, (((LivingEntity) entityIn).getRandom().nextDouble() - 0.5) * 2.0);
        }
        if (entityIn instanceof PlayerEntity && entityIn != null) {
            if (((PlayerEntity) entityIn).isBlocking()) {
                final List list2 = entityIn.level.getEntities(entityIn, entityIn.getBoundingBox().inflate(16.0, 16.0, 16.0));
                if (list2 != null && !list2.isEmpty()) {
                    for (int i3 = 0; i3 < list2.size(); ++i3) {
                        final Entity entity2 = (Entity) list2.get(i3);
                        if (entity2 != null && entityIn.distanceTo(entity2) < 100.0 && (entity2 instanceof ArrowEntity || entity2 instanceof FireballEntity || entity2 instanceof ThrowableEntity || entity2 instanceof TNTEntity)) {
                            entity2.level.explode(entityIn, entity2.position().x, entity2.position().y, entity2.position().z, entity2.getBbWidth(), false, Explosion.Mode.NONE);
                            entity2.remove();
                        }
                    }
                }
            }
            ((PlayerEntity) entityIn).heal(((PlayerEntity) entityIn).getMaxHealth());
            if (((PlayerEntity) entityIn).getEffect(Effects.ABSORPTION) == null || ((PlayerEntity) entityIn).getEffect(Effects.ABSORPTION).getDuration() <= 1) {
                ((PlayerEntity) entityIn).addEffect(new EffectInstance(Effects.ABSORPTION, 5, 249, false, true));
            }
        }
    }

    @Override
    public boolean onEntitySwing(final ItemStack stack, final LivingEntity entityLiving) {
        this.onUsingTick(stack, entityLiving, 0);
        if (!entityLiving.level.isClientSide && entityLiving instanceof PlayerEntity && entityLiving.isCrouching()) {
            final List list = entityLiving.level.getEntities(entityLiving, entityLiving.getBoundingBox().inflate(64.0, 64.0, 64.0));
            if (list != null && !list.isEmpty()) {
                for (int i1 = 0; i1 < list.size(); ++i1) {
                    final Entity entity = (Entity) list.get(i1);
                    if (entity != null && !(entity instanceof EntityTitan)) {
                        entity.playSound(TitanSounds.titanPunch, 2.0f, 0.5f + entityLiving.getRandom().nextFloat() * 0.25f);
                        entity.playSound(SoundEvents.FIRE_EXTINGUISH, 2.0f, 2.0f);
                        entity.hurt(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                        if (entity instanceof LivingEntity) {
                            this.hurtEnemy(stack, (LivingEntity) entity, entityLiving);
                        }
                    }
                }
            }
        }
        if (!entityLiving.level.isClientSide && entityLiving instanceof PlayerEntity) {
            for (int i2 = 0; i2 < 24; ++i2) {
                final Vector3d vec3 = entityLiving.getLookAngle();
                final double dx = vec3.x * i2;
                final double dy = entityLiving.getEyeHeight() + vec3.y * i2;
                final double dz = vec3.z * i2;
                final int y = MathHelper.floor(entityLiving.position().y + dy);
                final int x = MathHelper.floor(entityLiving.position().x + dx);
                final int z = MathHelper.floor(entityLiving.position().z + dz);
                if (!entityLiving.level.getBlockState(new BlockPos(x, y, z)).isAir()) {
                    entityLiving.level.levelEvent(null, 1012, new BlockPos(x, y, z), 0);
                }
                if (!entityLiving.level.isClientSide && !entityLiving.level.getBlockState(new BlockPos(x, y, z)).isAir()) {
                    final Block block = entityLiving.level.getBlockState(new BlockPos(x, y, z)).getBlock();
                    entityLiving.level.levelEvent(2001, new BlockPos(x, y, z), Block.getId(block.defaultBlockState()));
                    final ItemEntity entityitem = new ItemEntity(entityLiving.level, x, y, z, new ItemStack(block.asItem()));
                    entityLiving.level.addFreshEntity(entityitem);
                    entityLiving.level.removeBlock(new BlockPos(x, y, z), true);
                    entityitem.lerpMotion(-MathHelper.sin(entityLiving.yRot * 3.1415927f / 180.0f), 0.75, MathHelper.cos(entityLiving.yRot * 3.1415927f / 180.0f));
                }
            }
        }
        entityLiving.playSound(TitanSounds.titanSwing, 10.0f, 1.0f);
        for (int i2 = 0; i2 < 24; ++i2) {
            final Vector3d vec3 = entityLiving.getLookAngle();
            final double dx = vec3.x * i2;
            final double dy = entityLiving.getEyeHeight() + vec3.y * i2;
            final double dz = vec3.z * i2;
            final List list2 = entityLiving.level.getEntities(entityLiving, entityLiving.getBoundingBox().inflate(3.0, 3.0, 3.0).move(dx, dy, dz));
            if (list2 != null && !list2.isEmpty()) {
                for (int i3 = 0; i3 < list2.size(); ++i3) {
                    final Entity entity2 = (Entity) list2.get(i3);
                    if (entity2 instanceof TNTEntity && !entity2.isInvulnerable()) {
                        entity2.level.explode(entityLiving, entity2.position().x, entity2.position().y, entity2.position().z, 4.0f, false, Explosion.Mode.NONE);
                        entity2.remove();
                    }
                    if (entity2 instanceof FireballEntity && !entity2.isInvulnerable()) {
                        entity2.level.explode(entityLiving, entity2.position().x, entity2.position().y, entity2.position().z, 0.0f, false, Explosion.Mode.NONE);
                        entity2.remove();
                    }
                    if (entity2 instanceof LivingEntity && entity2.isAlive() && entity2 != entityLiving) {
                        entity2.setRemainingFireTicks(Integer.MAX_VALUE);
                        entityLiving.level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(true, entityLiving.level.getServer());
                        final float extradamage = EnchantmentHelper.getDamageBonus(entityLiving.getMainHandItem(), ((LivingEntity) entity2).getMobType());
                        final int knockbackAmount = EnchantmentHelper.getKnockbackBonus(entityLiving);
                        if (entity2 != null) {
                            entity2.hurt(DamageSourceExtra.causeAntiTitanDamage(entityLiving), 20000.0f + extradamage);
                            ((LivingEntity) entity2).hurtDuration = 0;
                            this.hurtEnemy(stack, (LivingEntity) entity2, entityLiving);
                            if (entity2 instanceof EntityTitan) {
                                ((EntityTitan) entity2).setTarget(entityLiving);
                                if (!((EntityTitan) entity2).canBeHurtByPlayer() && entityLiving instanceof PlayerEntity) {
                                    entityLiving.sendMessage(new StringTextComponent(entity2.getName().getString() + ": Quit flailing that giant sword at me."), UUID.randomUUID());
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
