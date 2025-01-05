package net.minecraft.theTitans.items;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class ItemTitanEgg extends Item {
    private final EntityType<? extends EntityTitan> titanType;

    public ItemTitanEgg(EntityType<? extends EntityTitan> titanType) {
        super(new Properties().tab(TheTitans.titansTab));
        this.titanType = titanType;
    }

    @Override
    public ActionResultType useOn(ItemUseContext p_195939_1_) {
        World level = p_195939_1_.getLevel();
        PlayerEntity player = p_195939_1_.getPlayer();
        BlockPos pos = p_195939_1_.getClickedPos();
        EntityTitan entity = null;
        if (this.titanType == RenderTheTitans.slimeTitan) {
            entity = new EntitySlimeTitan(this.titanType, level);
        } else if (this.titanType == RenderTheTitans.zombieTitan) {
            entity = new EntityZombieTitan(this.titanType, level);
        } else if (this.titanType == RenderTheTitans.skeletonTitan) {
            entity = new EntitySkeletonTitan(this.titanType, level);
        } else if (this.titanType == RenderTheTitans.witherSkeletonTitan) {
            entity = new EntitySkeletonTitan(this.titanType, level, true);
			((EntitySkeletonTitan)entity).becomeWitherSkeleton(true);
        } else if (this.titanType == RenderTheTitans.ghastTitan) {
            entity = new EntityGhastTitan(this.titanType, level);
        } else if (this.titanType == RenderTheTitans.ironGolemTitan) {
            entity = new EntityIronGolemTitan(this.titanType, level);
        } else if (this.titanType == RenderTheTitans.witherzilla) {
            entity = new EntityWitherzilla(this.titanType, level);
        }
		entity.setPos(pos.getX(), pos.getY() + 1.0, pos.getZ());
        if (!player.level.isClientSide)
            entity.finalizeSpawn(player.level.getServer().getLevel(player.level.dimension()), p_195939_1_.getLevel().getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.SPAWN_EGG, null, null);
        level.addFreshEntity(entity);
        if (entity != null) {
            entity.playAmbientSound();
            if (!player.isCreative()) {
                p_195939_1_.getItemInHand().shrink(1);
            }
            if (titanType == RenderTheTitans.slimeTitan) {
                entity.playSound(TitanSounds.titanBirth, 1000.0f, 1.5f);
            } else if (titanType == RenderTheTitans.ghastTitan) {
                entity.playSound(TitanSounds.titanBirth, 1000.0f, 0.875f);
            }
            if (entity != null && !level.isClientSide && titanType == RenderTheTitans.witherzilla) {
                player.sendMessage(new StringTextComponent("Thank you for breaking the seal..."), entity.getUUID());
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playSound(SoundEvents.ITEM_BREAK, 10000.0f, 0.5f);
                entity.playAmbientSound();
                player.inventory.removeItem(new ItemStack(TitanItems.eggWitherzilla));
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public Rarity getRarity(ItemStack p_77613_1_) {
        Rarity rarity = Rarity.UNCOMMON;
        Item choose = p_77613_1_.getItem();
        if (choose == TitanItems.eggWitherzilla)
            rarity = Rarity.EPIC;
        return rarity;
    }
}
