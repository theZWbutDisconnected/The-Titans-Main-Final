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
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.IServerWorld;
import net.minecraft.entity.SpawnReason;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.entity.monster.ZombieVillagerEntity;

@OnlyIn(
	value = Dist.CLIENT,
	_interface = IRendersAsItem.class
)
public class EntityGrowthSerum extends ProjectileItemEntity
{
	public static final Map<Class<? extends LivingEntity>, Class<? extends EntityTitan>> titanMapping = new HashMap<>();
	
	public EntityGrowthSerum(EntityType<EntityGrowthSerum> type, World level) {
		super(type, level);
		
		titanMapping.put(ZombieEntity.class, EntityZombieTitan.class);
		titanMapping.put(SkeletonEntity.class, EntitySkeletonTitan.class);
		titanMapping.put(GhastEntity.class, EntityGhastTitan.class);
		titanMapping.put(IronGolemEntity.class, EntityIronGolemTitan.class);
		titanMapping.put(SlimeEntity.class, EntitySlimeTitan.class);
		// titanMapping.put(WitherEntity.class, EntityWitherzilla.class);
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
				TheTitans.log(titanMapping.containsKey(p_213868_1_.getEntity().getClass()));
				if (titanMapping.containsKey(p_213868_1_.getEntity().getClass())) {
					EntityTitan willBeTitan = null;
					try {
					    willBeTitan = titanMapping.get(p_213868_1_.getClass()).newInstance();
					    willBeTitan.level = this.level;
						willBeTitan.moveTo(p_213868_1_.getEntity().getX(), p_213868_1_.getEntity().getY(), p_213868_1_.getEntity().getZ(), p_213868_1_.getEntity().yRot, 0.0F);
					    this.level.addFreshEntity(willBeTitan);
						willBeTitan.playSound(SoundEvents.PORTAL_TRAVEL, 10000.0F, 1.0F);
						willBeTitan.finalizeSpawn(this.level.getServer().getLevel(this.level.dimension()), level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.SPAWNER, null, null);
						willBeTitan.playSound(TitanSounds.titanBirth, 1000.0F, 1.0F);
						willBeTitan.func_82206_m();
						if (p_213868_1_.getEntity() instanceof ZombieEntity) {
						    ((EntityZombieTitan)willBeTitan).setBaby(((ZombieEntity)p_213868_1_.getEntity()).isBaby());
							if (p_213868_1_.getEntity() instanceof ZombieVillagerEntity)
						        ((EntityZombieTitan)willBeTitan).setVillager(true);
						}
						p_213868_1_.getEntity().remove(false);
					} catch (Exception e){
					    TheTitans.log(e.getMessage());
						e.printStackTrace();
					}
				} else {
				    ((LivingEntity)p_213868_1_.getEntity()).setSecondsOnFire(20);
                    ((LivingEntity)p_213868_1_.getEntity()).hurt(DamageSourceExtra.wip, 2000.0F);
				}
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
