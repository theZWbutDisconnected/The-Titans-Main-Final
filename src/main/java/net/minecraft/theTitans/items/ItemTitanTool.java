package net.minecraft.theTitans.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemTitanTool extends TieredItem implements IVanishable {
    protected final float speed;
    private final Set<Block> blocks;
    private final float attackDamageBaseline;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemTitanTool(IItemTier p_i48512_3_, Set<Block> p_i48512_4_, Item.Properties p_i48512_5_) {
        super(p_i48512_3_, p_i48512_5_);
        this.blocks = p_i48512_4_;
        this.speed = p_i48512_3_.getSpeed();
        float at = 0.0f;
        if (this instanceof ItemTitanSpade)
            at = 1.0f;
        else if (this instanceof ItemTitanPickaxe)
            at = 2.0f;
        else if (this instanceof ItemTitanAxe)
            at = 3.0f;
        this.attackDamageBaseline = at + p_i48512_3_.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) this.attackDamageBaseline - 1.0f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) speed - 4.0f, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public float getDestroySpeed(ItemStack p_150893_1_, BlockState p_150893_2_) {
        if (getToolTypes(p_150893_1_).stream().anyMatch(e -> p_150893_2_.isToolEffective(e))) return speed;
        return this.blocks.contains(p_150893_2_.getBlock()) ? this.speed : 1.0F;
    }

    public boolean hurtEnemy(ItemStack p_77644_1_, LivingEntity p_77644_2_, LivingEntity p_77644_3_) {
        p_77644_1_.hurtAndBreak(2, p_77644_3_, (p_220039_0_) -> {
            p_220039_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack p_179218_1_, World p_179218_2_, BlockState p_179218_3_, BlockPos p_179218_4_, LivingEntity p_179218_5_) {
        if (!p_179218_2_.isClientSide && p_179218_3_.getDestroySpeed(p_179218_2_, p_179218_4_) != 0.0F) {
            p_179218_1_.hurtAndBreak(1, p_179218_5_, (p_220038_0_) -> {
                p_220038_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType p_111205_1_) {
        return p_111205_1_ == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_111205_1_);
    }

    public float getAttackDamage() {
        return this.attackDamageBaseline;
    }
}
