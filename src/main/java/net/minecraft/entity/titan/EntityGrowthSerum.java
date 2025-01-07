package net.minecraft.entity.titan;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.theTitans.DamageSourceExtra;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.theTitans.RenderTheTitans;

@OnlyIn(
	value = Dist.CLIENT,
	_interface = IRendersAsItem.class
)
public class EntityGrowthSerum extends ProjectileItemEntity
{
	public EntityGrowthSerum(EntityType<EntityGrowthSerum> type, World level) {
		super(type, level);
		EntityType.SNOWBALL
	}


	public EntityGrowthSerum(World p_i1774_1_, LivingEntity p_i1774_2_) {
	    super(RenderTheTitans.growthSerum, p_i1774_2_, p_i1774_1_);
	}

	public EntityGrowthSerum(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
	    super(RenderTheTitans.growthSerum, p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
	}
	
	protected Item getDefaultItem() {
		return TitanItems.growthSerum;
	}
	
	@Override
	protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
		if (!this.level.isClientSide()) {
			if (p_213868_1_.getEntity() instanceof LivingEntity) {
				((LivingEntity)p_213868_1_.getEntity()).setSecondsOnFire(20);
                ((LivingEntity)p_213868_1_.getEntity()).hurt(DamageSourceExtra.wip, 2000.0F);
                this.spawnAtLocation(TitanItems.growthSerum, 1);
                this.playSound(SoundEvents.PLAYER_HURT, 2.0F, 2.0F);
			}
            this.removeAfterChangingDimensions();
        }
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
		if (!this.level.isClientSide()) {
            this.spawnAtLocation(TitanItems.growthSerum, 1);

            this.playSound(SoundEvents.PLAYER_HURT, 2.0F, 2.0F);
            this.removeAfterChangingDimensions();
        }
	}
}
