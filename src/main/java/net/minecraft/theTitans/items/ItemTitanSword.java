package net.minecraft.theTitans.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.item.UseAction;
import net.minecraft.tags.BlockTags;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemTitanSword extends TieredItem implements IVanishable {
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemTitanSword(IItemTier p_i48460_1_, Properties p_i48460_4_) {
        super(p_i48460_1_, p_i48460_4_.tab(TheTitans.titansTab));
        this.attackDamage = 4.0f + p_i48460_1_.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(RenderTheTitans.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", p_i48460_1_.getSpeed(), AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public ItemTitanSword(IItemTier p_i48460_1_) {
        this(p_i48460_1_, new Properties());
    }

    public float getDamage() {
        return this.attackDamage;
    }

    public float func_150931_i() {
        return (float) ((AttributeModifier) this.defaultModifiers.get(RenderTheTitans.ATTACK_DAMAGE).toArray()[0]).getAmount();
    }

    public boolean canAttackBlock(BlockState p_195938_1_, World p_195938_2_, BlockPos p_195938_3_, PlayerEntity p_195938_4_) {
        return !p_195938_4_.isCreative();
    }

    public float getDestroySpeed(ItemStack p_150893_1_, BlockState p_150893_2_) {
        if (p_150893_2_.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            Material material = p_150893_2_.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !p_150893_2_.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }

    public boolean hurtEnemy(ItemStack p_77644_1_, LivingEntity p_77644_2_, LivingEntity p_77644_3_) {
        p_77644_1_.hurtAndBreak(1, p_77644_3_, (p_220045_0_) -> {
            p_220045_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack p_179218_1_, World p_179218_2_, BlockState p_179218_3_, BlockPos p_179218_4_, LivingEntity p_179218_5_) {
        if (p_179218_3_.getDestroySpeed(p_179218_2_, p_179218_4_) != 0.0F) {
            p_179218_1_.hurtAndBreak(2, p_179218_5_, (p_220044_0_) -> {
                p_220044_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public boolean isCorrectToolForDrops(BlockState p_150897_1_) {
        return p_150897_1_.is(Blocks.COBWEB);
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType p_111205_1_) {
        return p_111205_1_ == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_111205_1_);
    }

    @Override
    public void onUseTick(World p_219972_1_, LivingEntity p_219972_2_, ItemStack p_219972_3_, int p_219972_4_) {
        /*Minecraft minecraft = Minecraft.getInstance();
        EntityRendererManager renderManager = minecraft.getEntityRenderDispatcher();
        LivingRenderer<?, ?> renderer;
        if (renderManager.getRenderer(p_219972_2_) instanceof LivingRenderer) {
            renderer = (LivingRenderer<?, ?>) renderManager.getRenderer(p_219972_2_);
            if (renderer.getModel() instanceof PlayerModel) {
                PlayerModel<PlayerEntity> model = (PlayerModel<PlayerEntity>) renderer.getModel();

                if (p_219972_2_.getUsedItemHand() == Hand.OFF_HAND) {
                    model.leftArm.xRot -= 0.62831855F;
                    model.leftArm.yRot = 0.5235988F;
                } else {
                    model.rightArm.xRot -= 0.62831855F;
                    model.rightArm.yRot = -0.5235988F;
                }
            }
        }*/
        super.onUseTick(p_219972_1_, p_219972_2_, p_219972_3_, p_219972_4_);
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
        p_77659_2_.startUsingItem(p_77659_3_);
        return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }
}
