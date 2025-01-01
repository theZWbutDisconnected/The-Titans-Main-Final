package net.minecraft.theTitans.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

public class ItemHarcadiumHelmet extends ItemTitanArmor {
    public ItemHarcadiumHelmet(IArmorMaterial p_i48534_1_) {
        super(p_i48534_1_, EquipmentSlotType.HEAD);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> multimap = ImmutableMultimap.builder();
        if (slot == EquipmentSlotType.HEAD)
            multimap.put(Attributes.MAX_HEALTH, this.modifierHarcadiumHelmet);
        return multimap.build();
    }
}
