package net.minecraft.theTitans.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.titan.EntityProtoBall;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.titan.EntityGrowthSerum;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

@OnlyIn(Dist.CLIENT)
public class RenderGrowthSerum extends EntityRenderer<EntityGrowthSerum> {
    private static RenderType RENDER_TYPE;
	private static ItemRenderer itemRenderer;
	private static float scale;
	
    public RenderGrowthSerum(EntityRendererManager manager, ItemRenderer renderer, float s) {
        super(manager);
		this.itemRenderer = renderer;
		this.scale = s;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGrowthSerum p_110775_1_) {
        return new ResourceLocation(TheTitans.modid, "textures/items/growth_serum.png");
    }
	
	public void render(final EntityGrowthSerum fireball, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        if (fireball.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(fireball) < 12.25D)) {
			p_225623_4_.pushPose();
			p_225623_4_.scale(this.scale, this.scale, this.scale);
			p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
			p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
			this.itemRenderer.renderStatic(fireball.getItem(), ItemCameraTransforms.TransformType.GROUND, p_225623_6_, OverlayTexture.NO_OVERLAY, p_225623_4_, p_225623_5_);
			p_225623_4_.popPose();
			super.render(fireball, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
		}
        super.render(fireball, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }
}
