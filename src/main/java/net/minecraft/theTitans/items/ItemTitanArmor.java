package net.minecraft.theTitans.items;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.EntityFallingBlockTitan;
import net.minecraft.entity.titan.EntityHarcadiumArrow;
import net.minecraft.entity.titan.EntityTitan;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.server.management.OpList;
import net.minecraft.server.management.PlayerList;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemTitanArmor extends ArmorItem {
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private final TitanItems.Material material;
    AttributeModifier modifierHarcadiumHelmet;
    AttributeModifier modifierHarcadiumChestplate;
    AttributeModifier modifierHarcadiumLeggings;
    AttributeModifier modifierHarcadiumBoots;
    AttributeModifier modifierVoidHelmet;
    AttributeModifier modifierVoidChestplate;
    AttributeModifier modifierVoidLeggings;
    AttributeModifier modifierVoidBoots;
    AttributeModifier modifierAdminiumHelmet;
    AttributeModifier modifierAdminiumChestplate;
    AttributeModifier modifierAdminiumLeggings;
    AttributeModifier modifierAdminiumBoots;

    public ItemTitanArmor(IArmorMaterial p_i48534_1_, EquipmentSlotType p_i48534_2_, Properties p_i48534_3_) {
        super(p_i48534_1_, p_i48534_2_, p_i48534_3_.tab(TheTitans.titansTab));
        UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[p_i48534_2_.getIndex()];
        this.modifierHarcadiumHelmet = new AttributeModifier(uuid, "Helmet modifier", 50.0, AttributeModifier.Operation.ADDITION);
        this.modifierHarcadiumChestplate = new AttributeModifier(uuid, "Chestplate modifier", 80.0, AttributeModifier.Operation.ADDITION);
        this.modifierHarcadiumLeggings = new AttributeModifier(uuid, "Leggings modifier", 70.0, AttributeModifier.Operation.ADDITION);
        this.modifierHarcadiumBoots = new AttributeModifier(uuid, "Boots modifier", 40.0, AttributeModifier.Operation.ADDITION);
        this.modifierVoidHelmet = new AttributeModifier(uuid, "Helmet modifier", 250.0, AttributeModifier.Operation.ADDITION);
        this.modifierVoidChestplate = new AttributeModifier(uuid, "Chestplate modifier", 400.0, AttributeModifier.Operation.ADDITION);
        this.modifierVoidLeggings = new AttributeModifier(uuid, "Leggings modifier", 350.0, AttributeModifier.Operation.ADDITION);
        this.modifierVoidBoots = new AttributeModifier(uuid, "Boots modifier", 200.0, AttributeModifier.Operation.ADDITION);
        this.modifierAdminiumHelmet = new AttributeModifier(uuid, "Helmet modifier", 1250.0, AttributeModifier.Operation.ADDITION);
        this.modifierAdminiumChestplate = new AttributeModifier(uuid, "Chestplate modifier", 2000.0, AttributeModifier.Operation.ADDITION);
        this.modifierAdminiumLeggings = new AttributeModifier(uuid, "Leggings modifier", 1750.0, AttributeModifier.Operation.ADDITION);
        this.modifierAdminiumBoots = new AttributeModifier(uuid, "Boots modifier", 1000.0, AttributeModifier.Operation.ADDITION);
        this.material = (TitanItems.Material) p_i48534_1_;
    }

    public ItemTitanArmor(IArmorMaterial p_i48534_1_, EquipmentSlotType p_i48534_2_) {
        this(p_i48534_1_, p_i48534_2_, p_i48534_1_ == TitanItems.Material.Harcadium ? new Properties() : new Properties().fireResistant());
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        boolean flag = slot == EquipmentSlotType.LEGS;
        String harcadiumArmorSetLocation = "thetitans:textures/models/armor/harcadium_layer_" + (flag ? "2" : "1") + ".png";
        String absenceArmorSetLocation = "thetitans:textures/models/armor/absence_layer_" + (flag ? "2" : "1") + ".png";
        String adminiumArmorSetLocation = "thetitans:textures/models/armor/adminium_layer_" + (flag ? "2" : "1") + ".png";
        return this.material == TitanItems.Material.Harcadium ? harcadiumArmorSetLocation : this.material == TitanItems.Material.Absence ? absenceArmorSetLocation : adminiumArmorSetLocation;
    }

    @Override
    public void onArmorTick(ItemStack itemStack, World world, PlayerEntity player) {
        if (itemStack.getItem() == TitanItems.harcadiumHelmet) {
            this.effectPlayer(player, Effects.NIGHT_VISION, 0);
            this.effectPlayer(player, Effects.WATER_BREATHING, 0);
            this.removeEffect(player, Effects.BLINDNESS);
        }
        if (itemStack.getItem() == TitanItems.harcadiumChestplate) {
            this.effectPlayer(player, Effects.DIG_SPEED, 3);
            this.effectPlayer(player, Effects.DAMAGE_RESISTANCE, 3);
            this.effectPlayer(player, Effects.DAMAGE_BOOST, 9);
            this.effectPlayer(player, Effects.FIRE_RESISTANCE, 0);
            this.removeEffect(player, Effects.WEAKNESS);
            this.removeEffect(player, Effects.DIG_SLOWDOWN);
        }
        if (itemStack.getItem() == TitanItems.harcadiumLeggings) {
            this.effectPlayer(player, Effects.REGENERATION, 9);
            this.removeEffect(player, Effects.CONFUSION);
            this.removeEffect(player, Effects.HUNGER);
            this.removeEffect(player, Effects.POISON);
        }
        if (itemStack.getItem() == TitanItems.harcadiumBoots) {
            this.effectPlayer(player, Effects.JUMP, 3);
            this.effectPlayer(player, Effects.MOVEMENT_SPEED, 3);
            this.removeEffect(player, Effects.MOVEMENT_SLOWDOWN);
        }
        if (this.isWearingFullSet(player, TitanItems.harcadiumHelmet, TitanItems.harcadiumChestplate, TitanItems.harcadiumLeggings, TitanItems.harcadiumBoots)) {
            this.effectPlayerLong(player, Effects.ABSORPTION, 99);
            this.effectPlayer(player, Effects.SATURATION, 49);
            player.playSound(TitanSounds.harcadiumHum, 0.2f, 1.0f);
            //player.triggerAchievement(TitansAchievments.harcadiumArmor);
        }
        if (itemStack.getItem() == TitanItems.voidHelmet) {
            this.effectPlayer(player, Effects.NIGHT_VISION, 0);
            this.effectPlayer(player, Effects.WATER_BREATHING, 0);
            this.removeEffect(player, Effects.BLINDNESS);
            player.playSound(TitanSounds.harcadiumHum, 5.0f, 0.5f);
        }
        if (itemStack.getItem() == TitanItems.voidChestplate) {
            this.effectPlayer(player, Effects.DIG_SPEED, 99);
            this.effectPlayer(player, Effects.DAMAGE_RESISTANCE, 3);
            this.effectPlayer(player, Effects.DAMAGE_BOOST, 49);
            this.effectPlayer(player, Effects.FIRE_RESISTANCE, 0);
            this.removeEffect(player, Effects.WEAKNESS);
            this.removeEffect(player, Effects.DIG_SLOWDOWN);
            player.playSound(TitanSounds.harcadiumHum, 5.0f, 0.5f);
            if (player.isOnFire()) {
                player.setRemainingFireTicks(0);
            }
        }
        if (itemStack.getItem() == TitanItems.voidLeggings) {
            this.effectPlayer(player, Effects.REGENERATION, 199);
            this.removeEffect(player, Effects.CONFUSION);
            this.removeEffect(player, Effects.HUNGER);
            this.removeEffect(player, Effects.POISON);
            player.playSound(TitanSounds.harcadiumHum, 5.0f, 0.0f);
        }
        if (itemStack.getItem() == TitanItems.voidBoots) {
            this.effectPlayer(player, Effects.JUMP, 5);
            this.effectPlayer(player, Effects.MOVEMENT_SPEED, 19);
            this.removeEffect(player, Effects.DIG_SLOWDOWN);
            player.playSound(TitanSounds.harcadiumHum, 5.0f, 0.5f);
        }
        if (this.isWearingFullSet(player, TitanItems.voidHelmet, TitanItems.voidChestplate, TitanItems.voidLeggings, TitanItems.voidBoots)) {
            player.fallDistance *= 0.0f;
            this.effectPlayerLong(player, Effects.ABSORPTION, 199);
            this.effectPlayer(player, Effects.SATURATION, 99);
            //player.triggerAchievement(TitansAchievments.voidArmor);
            for (int i = 0; i < 4; ++i) {
                player.level.addParticle(ParticleTypes.SMOKE, player.getX() + (player.getRandom().nextDouble() - 0.5) * player.getBbWidth() * 2.0, player.getY() + player.getRandom().nextDouble() * player.getBbHeight(), player.getZ() + (player.getRandom().nextDouble() - 0.5) * player.getBbWidth() * 2.0, 0.0, 0.05, 0.0);
            }
            final List list11 = player.level.getEntities(player, player.getBoundingBox().inflate(4.0, 4.0, 4.0));
            if (list11 != null && !list11.isEmpty()) {
                for (int i2 = 0; i2 < list11.size(); ++i2) {
                    final Entity entity = (Entity) list11.get(i2);
                    if (entity != null && entity instanceof LivingEntity && !(entity instanceof EntityTitan) && !(entity instanceof GolemEntity) && !(entity instanceof TameableEntity) && !(entity instanceof VillagerEntity)) {
                        entity.hurt(DamageSource.OUT_OF_WORLD, 4.0f);
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.BLINDNESS, 5000, 1));
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.CONFUSION, 5000, 1));
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 5000, 9));
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.WITHER, 5000, 3));
                    }
                }
            }
        }
        if (this.material == TitanItems.Material.Adminium) {
            itemStack.setDamageValue(itemStack.getDamageValue() - 10);
            if (itemStack.getItem() == TitanItems.adminiumHelmet) {
                this.effectPlayer(player, Effects.NIGHT_VISION, 0);
                this.effectPlayer(player, Effects.WATER_BREATHING, 0);
                this.removeEffect(player, Effects.BLINDNESS);
                player.playSound(TitanSounds.harcadiumHum, 5.0f, 1.5f);
            }
            if (itemStack.getItem() == TitanItems.adminiumChestplate) {
                this.effectPlayer(player, Effects.DIG_SPEED, 99);
                this.effectPlayer(player, Effects.DAMAGE_RESISTANCE, 3);
                this.effectPlayer(player, Effects.DAMAGE_BOOST, 999);
                this.effectPlayer(player, Effects.FIRE_RESISTANCE, 0);
                this.removeEffect(player, Effects.WEAKNESS);
                this.removeEffect(player, Effects.DIG_SLOWDOWN);
                player.playSound(TitanSounds.harcadiumHum, 5.0f, 0.5f);
            }
            if (itemStack.getItem() == TitanItems.adminiumLeggings) {
                this.effectPlayer(player, Effects.REGENERATION, 199);
                this.removeEffect(player, Effects.CONFUSION);
                this.removeEffect(player, Effects.HUNGER);
                this.removeEffect(player, Effects.POISON);
                player.playSound(TitanSounds.harcadiumHum, 5.0f, 1.0f);
            }
            if (itemStack.getItem() == TitanItems.adminiumBoots) {
                this.effectPlayer(player, Effects.JUMP, 19);
                this.effectPlayer(player, Effects.MOVEMENT_SPEED, 39);
                this.removeEffect(player, Effects.MOVEMENT_SLOWDOWN);
                player.playSound(TitanSounds.harcadiumHum, 5.0f, 2.0f);
            }
            if (this.isWearingFullSet(player, TitanItems.adminiumHelmet, TitanItems.adminiumChestplate, TitanItems.adminiumLeggings, TitanItems.adminiumBoots)) {
                if (!player.level.isClientSide) {
                    MinecraftServer server = player.level.getServer();
                    PlayerList playerList = server.getPlayerList();
                    GameProfile profile = server.getProfileCache().get(player.getName().getString());
                    OpList opList = playerList.getOps();
                    OpEntry opEntry = new OpEntry(profile, server.getOperatorUserPermissionLevel(), false);
                    opList.add(opEntry);
                    playerList.sendPlayerPermissionLevel(server.getPlayerList().getPlayer(profile.getId()));
                }
                player.hurtDir = 40;
                player.setRemainingFireTicks(0);
                player.fallDistance *= 0.0f;
                this.effectPlayerLong(player, Effects.ABSORPTION, 399);
                this.effectPlayer(player, Effects.SATURATION, 199);
                if (player.isAlive()) {
                    player.setHealth((player.getHealth() < player.getMaxHealth() / 4.0f) ? (player.getHealth() + 50.0f) : (player.getHealth() + 10.0f));
                }
                //player.triggerAchievement(TitansAchievments.adminiumArmor);
                if (player.getY() <= -45.0) {
                    player.setPos(player.getX(), 200.0, player.getZ());
                }
                if (player.getDeltaMovement().y > 3.0 && !player.removed) {
                    player.setDeltaMovement(player.getDeltaMovement().x, 0.0, player.getDeltaMovement().z);
                }
                if (player.getHealth() < player.getMaxHealth()) {
                    this.effectPlayer(player, Effects.HEAL, 2);
                }
                if (player.getY() <= -45.0) {
                    BlockPos playerPos = player.blockPosition();
                    int x = playerPos.getX();
                    int z = playerPos.getZ();
                    int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
                    BlockPos topSolidOrLiquidBlock = new BlockPos(x, y, z);
                    player.setPos(player.getX(), topSolidOrLiquidBlock.getY(), player.getZ());
                }
                for (int i = 0; i < 2; ++i) {
                    player.level.addParticle(ParticleTypes.SMOKE, player.getX() + (player.getRandom().nextDouble() - 0.5) * player.getBbWidth(), player.getY() + player.getRandom().nextDouble() * player.getBbHeight(), player.getZ() + (player.getRandom().nextDouble() - 0.5) * player.getBbWidth(), 0.0, 0.0, 0.0);
                    player.level.addParticle(ParticleTypes.PORTAL, player.getX() + (player.getRandom().nextDouble() - 0.5) * player.getBbWidth(), player.getY() + player.getRandom().nextDouble() * player.getBbHeight(), player.getZ() + (player.getRandom().nextDouble() - 0.5) * player.getBbWidth(), (player.getRandom().nextDouble() - 0.5) * 2.0, -player.getRandom().nextDouble(), (player.getRandom().nextDouble() - 0.5) * 2.0);
                }
                final BlockState block = player.level.getBlockState(new BlockPos((int) player.getX(), (int) (player.getY() - 1.0), (int) player.getZ()));
                final List list = player.level.getEntities(player, player.getBoundingBox().inflate(32.0, 8.0, 32.0));
                if (!player.isOnGround() && block.getMaterial().isSolid()) {
                    if (!player.level.isClientSide && player.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        final int j = MathHelper.floor(player.getBoundingBox().minX - 3.0);
                        final int k = MathHelper.floor(player.getBoundingBox().minY - 1.0);
                        final int l = MathHelper.floor(player.getBoundingBox().minZ - 3.0);
                        final int m = MathHelper.floor(player.getBoundingBox().maxX + 3.0);
                        final int i2 = MathHelper.floor(player.getBoundingBox().maxY + 1.0);
                        final int j2 = MathHelper.floor(player.getBoundingBox().maxZ + 3.0);
                        for (int k2 = j; k2 <= m; ++k2) {
                            for (int l2 = k; l2 <= i2; ++l2) {
                                for (int i3 = l; i3 <= j2; ++i3) {
                                    final BlockState block2 = player.level.getBlockState(new BlockPos(k2, l2, i3));
                                    if (player.getBoundingBox() != null && player.level.hasChunksAt(k2, l2, i3, k2, l2, i3) && !block2.isAir(player.level, new BlockPos(k2, l2, i3)) && !player.level.isClientSide && block2.getBlock().getExplosionResistance() != 3600000.0F) {
                                        if (block2.getMaterial().isLiquid()) {
                                            player.level.removeBlock(new BlockPos(k2, l2, i3), false);
                                        } else if (player.getRandom().nextInt(2) == 0) {
                                            final EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(player.level, k2 + 0.5, l2 + 0.5, i3 + 0.5, player.level.getBlockState(new BlockPos(k2, l2, i3)));
                                            entityfallingblock.setPos(k2 + 0.5, l2 + 0.5, i3 + 0.5);
                                            final double d0 = (player.getBoundingBox().minX + player.getBoundingBox().maxX) / 2.0;
                                            final double d2 = (player.getBoundingBox().minZ + player.getBoundingBox().maxZ) / 2.0;
                                            final double d3 = entityfallingblock.getX() - d0;
                                            final double d4 = entityfallingblock.getZ() - d2;
                                            final double d5 = d3 * d3 + d4 * d4;
                                            entityfallingblock.push(d3 / d5 * 10.0, 2.0 + player.getRandom().nextGaussian(), d4 / d5 * 10.0);
                                            player.level.addFreshEntity(entityfallingblock);
                                            player.level.removeBlock(new BlockPos(k2, l2, i3), false);
                                        } else if (player.level.getNearestPlayer(player, 16.0) != null) {
                                            player.level.removeBlock(new BlockPos(k2, l2, i3), true);
                                        } else {
                                            player.level.removeBlock(new BlockPos(k2, l2, i3), false);
                                            dropBlockAsItem(block2.getBlock(), player.level, k2, l2, i3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    player.level.explode(player, player.getX(), player.getY() - 2.0, player.getZ(), 3.0f, player.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                    if (list != null && !list.isEmpty()) {
                        for (int i4 = 0; i4 < list.size(); ++i4) {
                            final Entity entity = (Entity) list.get(i4);
                            if (entity != null && !(entity instanceof EntityTitan) && !(entity instanceof GolemEntity) && !(entity instanceof TameableEntity) && !(entity instanceof VillagerEntity)) {
                                entity.hurt(new EntityDamageSource("explosion.player", player).setScalesWithDifficulty().setExplosion().bypassArmor(), 300.0f);
                                final Entity entity4 = entity;
                                entity4.push(0.0, 1.0, 0.0);
                                final Entity entity5 = entity;
                                entity5.setPos(entity5.getX(), entity5.getY() + 1, entity5.getZ());
                            }
                        }
                    }
                }
                final List list2 = player.level.getEntities(player, player.getBoundingBox().inflate(10.0, 10.0, 10.0));
                if (list2 != null && !list2.isEmpty()) {
                    for (int i5 = 0; i5 < list2.size(); ++i5) {
                        final Entity entity2 = (Entity) list2.get(i5);
                        if (entity2 != null && entity2.isAlive() && entity2.tickCount % 10 == 0 && entity2 instanceof LivingEntity && !(entity2 instanceof EntityTitan) && !(entity2 instanceof GolemEntity) && !(entity2 instanceof TameableEntity) && !(entity2 instanceof VillagerEntity)) {
                            entity2.hurt(EntityTitan.isOreSpawnBossToExempt(entity2) ? DamageSource.playerAttack(player).bypassArmor() : DamageSourceExtra.radiation, EntityTitan.isOreSpawnBossToExempt(entity2) ? 100.0f : 10.0f);
                            //((LivingEntity) entity2).addEffect(new EffectInstance(ClientProxy.creeperTitanRadiation.id, 5000, 1));
                        }
                    }
                }
                final List list3 = player.level.getEntities(player, player.getBoundingBox().inflate(48.0, 48.0, 48.0));
                if (list3 != null && !list3.isEmpty()) {
                    for (int i6 = 0; i6 < list3.size(); ++i6) {
                        final Entity entity3 = (Entity) list3.get(i6);
                        if (entity3 != null && !(entity3 instanceof PlayerEntity) && !(entity3 instanceof EntityTitan) && !(entity3 instanceof EntityHarcadiumArrow)) {
                            final double d6 = (player.getX() - entity3.getX()) / 48.0;
                            final double d7 = (player.getY() + 1.0 - entity3.getY()) / 48.0;
                            final double d8 = (player.getZ() - entity3.getZ()) / 48.0;
                            final double d9 = Math.sqrt(d6 * d6 + d7 * d7 + d8 * d8);
                            double d10 = 1.0 - d9;
                            if (d10 > 0.0) {
                                d10 *= d10;
                                final Entity entity7 = entity3;
                                entity7.push(d6 / d9 * d10 * 0.1, 0.0, 0.0);
                                final Entity entity8 = entity3;
                                entity8.push(0.0, d7 / d9 * d10 * 0.1, 0.0);
                                final Entity entity9 = entity3;
                                entity9.push(0.0, 0.0, d8 / d9 * d10 * 0.1);
                            }
                        }
                    }
                }
                player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
            } else {
                player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0);
            }
        }
    }

    private void dropBlockAsItem(Block block, World level, int p1474801, int p1474802, int p1474803) {
        ItemEntity it = new ItemEntity(level, p1474801, p1474802, p1474803, new ItemStack(block.asItem()));
        level.addFreshEntity(it);
        it.push(0, .14, 0);
    }

    private void removeEffect(final PlayerEntity player, final Effect potion) {
        if (player.getEffect(potion) != null) {
            player.removeEffect(potion);
            player.playSound(SoundEvents.FIRE_EXTINGUISH, 0.5f, 2.0f);
        }
    }

    private void effectPlayer(final PlayerEntity player, final Effect potion, final int amplifier) {
        if (player.getEffect(potion) == null || player.getEffect(potion).getDuration() <= 1) {
            player.addEffect(new EffectInstance(potion, 1, amplifier, false, true));
        }
    }

    private void effectPlayerLong(final PlayerEntity player, final Effect potion, final int amplifier) {
        if (player.getEffect(potion) == null || player.getEffect(potion).getDuration() <= 1) {
            player.addEffect(new EffectInstance(potion, 800, amplifier, false, true));
        }
    }

    private boolean isWearingFullSet(final PlayerEntity player, final Item helmet, final Item chestplate, final Item leggings, final Item boots) {
        return !player.inventory.getArmor(3).isEmpty() && player.inventory.getArmor(3).getItem() == helmet && !player.inventory.getArmor(2).isEmpty() && player.inventory.getArmor(2).getItem() == chestplate && !player.inventory.getArmor(1).isEmpty() && player.inventory.getArmor(1).getItem() == leggings && !player.inventory.getArmor(0).isEmpty() && player.inventory.getArmor(0).getItem() == boots;
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return this.material != TitanItems.Material.Harcadium;
    }

    @Override
    public Rarity getRarity(ItemStack p_77613_1_) {
        Rarity rarity = super.getRarity(p_77613_1_);
        switch (this.material) {
            case Absence:
                rarity = Rarity.RARE;
                break;
            case Adminium:
                rarity = TheTitans.godly;
                break;
        }
        return rarity;
    }
}
