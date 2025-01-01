package net.minecraft.entity.titan;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.theTitans.RenderTheTitans;
import net.minecraft.theTitans.TitanSounds;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;

public class EntityFallingBlockTitan extends FallingBlockEntity {
    private final int field_145815_h;
    private final float field_145816_i;
    public int field_145812_b;
    public boolean field_145813_c;
    public CompoundNBT field_145810_d;
    public BlockState field_145811_e = Blocks.SAND.defaultBlockState();
    private boolean field_145808_f;
    private boolean field_145809_g;

    public EntityFallingBlockTitan(final World p_i45319_1_, final double p_i45319_2_, final double p_i45319_4_, final double p_i45319_6_, final BlockState p_i45319_8_) {
        super(RenderTheTitans.titanFallingBlock, p_i45319_1_);
        this.field_145811_e = p_i45319_8_;
        this.field_145813_c = true;
        this.field_145815_h = 40;
        this.field_145816_i = 2.0f;
        this.blocksBuilding = true;
        this.setSize(0.98f, 0.98f);
        this.setPos(p_i45319_2_, p_i45319_4_, p_i45319_6_);
        this.xo = p_i45319_2_;
        this.yo = p_i45319_4_;
        this.zo = p_i45319_6_;
    }

    public EntityFallingBlockTitan(EntityType<? extends FallingBlockEntity> p_i50218_1_, World p_i50218_2_) {
        super(p_i50218_1_, p_i50218_2_);
        this.field_145813_c = true;
        this.field_145815_h = 40;
        this.field_145816_i = 2.0f;
    }    private EntitySize entitySize = this.setSize(0.5f, 0.5f);

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return entitySize;
    }

    public EntitySize setSize(float width, float height) {
        entitySize = EntitySize.fixed(width, height);
        this.refreshDimensions();
        return entitySize;
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void tick() {
        this.field_145809_g = true;
        this.noCulling = false;
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        ++this.field_145812_b;
        this.push(0.0, -0.03999999910593033, 0.0);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().x * 0.9800000190734863, this.getDeltaMovement().y * 0.9800000190734863, this.getDeltaMovement().z * 0.9800000190734863);
        if (this.field_145812_b > 40) {
            final int i = MathHelper.floor(this.getX());
            final int j = MathHelper.floor(this.getY());
            final int k = MathHelper.floor(this.getZ());
            if (this.onGround) {
                this.setDeltaMovement(this.getDeltaMovement().x * 0.699999988079071, this.getDeltaMovement().y * -0.5, this.getDeltaMovement().z * 0.699999988079071);
                if (this.level.getBlockState(new BlockPos(i, j, k)) != Blocks.MOVING_PISTON.defaultBlockState()) {
                    this.removeAfterChangingDimensions();
                    if (!this.field_145808_f && this.field_145811_e.isValidSpawn(this.level, new BlockPos(i, j, k), RenderTheTitans.titanFallingBlock) && !FallingBlock.isFree(this.level.getBlockState(new BlockPos(i, j - 1, k))) && this.level.setBlock(new BlockPos(i, j, k), this.field_145811_e, 0, 3)) {
                        this.level.addParticle(ParticleTypes.POOF, i, j, k, 0.0, 0.0, 0.0);
                        this.field_145811_e.addDestroyEffects(level, new BlockPos(i, j, k), Minecraft.getInstance().particleEngine);
                        if (this.level.getNearestPlayer(this, 16.0) != null) {
                            this.playSound(TitanSounds.titanPress, 1.0f, 1.0f + this.random.nextFloat() * 0.25f);
                        }
                        if (this.field_145810_d != null && this.field_145811_e.hasTileEntity()) {
                            TileEntity tileentity = this.level.getBlockEntity(this.blockPosition());
                            if (tileentity != null) {
                                CompoundNBT compoundnbt = tileentity.save(new CompoundNBT());

                                for (String s : this.field_145810_d.getAllKeys()) {
                                    INBT inbt = this.field_145810_d.get(s);
                                    if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                        compoundnbt.put(s, inbt.copy());
                                    }
                                }

                                tileentity.load(this.field_145811_e, compoundnbt);
                                tileentity.setChanged();
                            }
                        }
                    } else if (this.field_145813_c && this.field_145808_f) {
                        this.spawnAtLocation(this.field_145811_e.getBlock());
                    }
                }
            } else if ((!this.level.isClientSide && (j < 1 || j > 256)) || this.field_145812_b > 1000) {
                this.spawnAtLocation(this.field_145811_e.getBlock());
                this.level.addParticle(ParticleTypes.POOF, i, j, k, 0.0, 0.0, 0.0);
                this.field_145811_e.addDestroyEffects(level, new BlockPos(i, j, k), Minecraft.getInstance().particleEngine);
                this.causeFallDamage((float) this.field_145812_b, 0);
                if (this.level.getNearestPlayer(this, 16.0) != null) {
                    this.playSound(TitanSounds.titanPress, 1.0f, 1.25f + this.random.nextFloat() * 0.25f);
                }
                this.removeAfterChangingDimensions();
            }
        }
    }

    @Override
    public boolean causeFallDamage(float p_70069_1_, float p_225503_2_) {
        if (this.field_145809_g) {
            final int i = MathHelper.ceil(p_70069_1_ - 1.0f);
            if (i > 0) {
                final ArrayList arraylist = new ArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(1.0, 1.0, 1.0)));
                final boolean flag = this.field_145811_e.getBlock() == Blocks.ANVIL;
                final DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                for (final Object object : arraylist) {
                    final Entity entity = (Entity) object;
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).hurtDuration = 0;
                    }
                    entity.hurt(damagesource, Math.min((float) MathHelper.floor(i * this.field_145816_i), this.field_145815_h * 10.0f));
                    if (this.field_145811_e.getBlock() == Blocks.FIRE) {
                        entity.setRemainingFireTicks(20);
                    }
                }
                if (flag && this.random.nextFloat() < 0.05000000074505806 + i * 0.05) {
                    BlockState blockstate = AnvilBlock.damage(this.field_145811_e);
                    if (blockstate == null) {
                        this.field_145808_f = true;
                    } else {
                        this.field_145811_e = blockstate;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public BlockState getBlockState() {
        return field_145811_e;
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }




}
