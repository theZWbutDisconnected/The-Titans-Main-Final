package net.minecraft.theTitans.items;
import net.minecraft.item.Item;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import java.util.Collection;
import net.minecraft.item.ItemGroup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.client.gui.FontRenderer;
import java.util.function.Consumer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ActionResultType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.IWorldReader;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.SoundEvents;
import net.minecraft.entity.titan.EntityGrowthSerum;

public class ItemGrowthSerum extends Item
{
	public ItemGrowthSerum() {
		super(new Properties().tab(TheTitans.titansTab));
	}

	@Override
	public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
		ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
		if (!p_77659_2_.abilities.invulnerable)
		    itemstack.grow(-1);
		p_77659_2_.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (p_77659_2_.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!p_77659_1_.isClientSide) {
            p_77659_1_.addFreshEntity(new EntityGrowthSerum(var2, p_77659_1_));
        }
		return ActionResult.fail(itemstack);
	}
}
