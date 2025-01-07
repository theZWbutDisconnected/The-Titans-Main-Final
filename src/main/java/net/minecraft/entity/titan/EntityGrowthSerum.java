package net.minecraft.entity.titan;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.theTitans.TitanItems;
import net.minecraft.theTitans.TheTitans;

public class EntityGrowthSerum extends ProjectileItemEntity
{
	public EntityGrowthSerum(EntityType<EntityGrowthSerum> type, World level) {
		super(type, level);
	}

	protected Item getDefaultItem() {
		return TitanItems.growthSerum;
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
		
	}
}
