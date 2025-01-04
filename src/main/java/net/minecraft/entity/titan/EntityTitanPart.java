package net.minecraft.entity.titan;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3d;
import java.util.HashMap;
import java.util.UUID;
import java.lang.reflect.Field;

public class EntityTitanPart extends PartEntity<EntityTitan> {
    public IEntityMultiPartTitan entityDragonObj;
    public String field_146032_b;
    public int numberOfTimesHit;

    public float width;
    public float height;
    private EntitySize entitySize;
	
	private ModelRenderer bone;
	private ModelRenderer parent;

    public EntityTitanPart(EntityTitan titan) {
        super(titan);
        this.blocksBuilding = true;
        this.noCulling = true;
    }

    protected EntityTitanPart(World level, IEntityMultiPartTitan p_i1697_1_, String p_i1697_2_, float p_i1697_3_, float p_i1697_4_) {
        this(((EntityTitan) p_i1697_1_));
        this.width = p_i1697_3_;
        this.height = p_i1697_4_;
        this.entitySize = EntitySize.fixed(this.width, this.height);
        this.entityDragonObj = p_i1697_1_;
        this.field_146032_b = p_i1697_2_;
        this.refreshDimensions();
        if (p_i1697_1_ instanceof Entity) {
            this.setPos(((Entity) p_i1697_1_).getX(), ((Entity) p_i1697_1_).getY(), ((Entity) p_i1697_1_).getZ());
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        setSize(width, height);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return entitySize;
    }

    public void setSize(float width, float height) {
        entitySize = EntitySize.fixed(width, height);
        this.refreshDimensions();
    }

    @Override
    public boolean isColliding(BlockPos p_242278_1_, BlockState p_242278_2_) {
        return false;
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    public float getRenderSizeModifier() {
        return this.getBbWidth();
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadowSize() {
        return (this.entityDragonObj != null && this.entityDragonObj instanceof LivingEntity && this.getY() > ((LivingEntity) this.entityDragonObj).getY()) ? ((float) (((LivingEntity) this.entityDragonObj).getY() - this.getY())) : 0.0f;
    }

    public boolean isOnFire() {
        return this.entityDragonObj != null && this.entityDragonObj instanceof LivingEntity && ((LivingEntity) this.entityDragonObj).isOnFire();
    }

    public boolean isPickable() {
        return this.entityDragonObj != null;
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return !this.isInvulnerableTo(p_70097_1_) && (this.entityDragonObj != null && this.entityDragonObj.attackEntityFromPart(this, p_70097_1_, p_70097_2_));
    }

    public void moveTo(double p_70012_1_, double p_70012_3_, double p_70012_5_, float p_70012_7_, float p_70012_8_) {
        if (this.entityDragonObj != null && this.entityDragonObj instanceof LivingEntity) {
            p_70012_1_ += ((LivingEntity) this.entityDragonObj).getDeltaMovement().x();
            p_70012_5_ += ((LivingEntity) this.entityDragonObj).getDeltaMovement().z();
        }
        super.moveTo(p_70012_1_, p_70012_3_, p_70012_5_, p_70012_7_, p_70012_8_);
    }

    public void tick() {
        super.tick();
        if (this.entityDragonObj != null && this.entityDragonObj instanceof LivingEntity) {
            this.yRot = ((LivingEntity) this.entityDragonObj).yBodyRot;
            this.setInvisible(((LivingEntity) this.entityDragonObj).isInvisible());
            this.setDeltaMovement(((LivingEntity) this.entityDragonObj).getDeltaMovement().x(), ((LivingEntity) this.entityDragonObj).getDeltaMovement().y(), ((LivingEntity) this.entityDragonObj).getDeltaMovement().z());
        }
        if (this.entityDragonObj == null || this.level == null || (this.entityDragonObj != null && this.entityDragonObj instanceof LivingEntity && !((LivingEntity) this.entityDragonObj).isAlive())) {
            for (int i = 0; i < 50; ++i) {
                this.level.addParticle(ParticleTypes.POOF, this.getX() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(), this.getY() + this.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(), 0.0, 0.0, 0.0);
                this.level.addParticle(ParticleTypes.POOF, this.getX() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(), this.getY() + this.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5) * (double) this.getBbWidth(), 0.0, 0.0, 0.0);
            }
            this.removeAfterChangingDimensions();
        }
    }
	
	public void boneRegistry(ModelRenderer part, ModelRenderer parent) {
		this.bone = part;
		this.parent = parent;
	}
	
	public void update() {
		Vector3d temp = this.position();
		if (this.bone != null) {
		    Vector3d pos = new Vector3d(this.bone.x, this.bone.y, this.bone.z);
		    Vector3d rot = new Vector3d(this.bone.xRot, this.bone.yRot, this.bone.zRot);
		    this.setPos(temp.x + pos.x, temp.y + pos.y, temp.z + pos.z);
		}
	}

    protected void defineSynchedData() {
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
