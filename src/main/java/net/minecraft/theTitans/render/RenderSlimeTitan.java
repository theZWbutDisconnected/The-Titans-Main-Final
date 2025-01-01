package net.minecraft.theTitans.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeGelLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.titan.EntityGhastTitan;
import net.minecraft.entity.titan.EntitySlimeTitan;
import net.minecraft.theTitans.models.ModelGhastTitan;
import net.minecraft.theTitans.models.ModelSlimeTitan;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class RenderSlimeTitan extends MobRenderer<EntitySlimeTitan, ModelSlimeTitan> {
    private static final ResourceLocation slimeTextures;

    static {
        slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");
    }

    public RenderSlimeTitan(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new ModelSlimeTitan(16), 4.0f);
        this.addLayer(new SlimeTitanGelLayer(this));
    }

    @Override
    public void render(final EntitySlimeTitan p_77041_1_, final float p_77041_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        //p_225623_4_.mulPose(new Quaternion(180.0f - p_77041_2_, 0.0f, 1.0f, 0.0f));
        if (p_77041_1_.deathTime > 0) {
            float f3 = (p_77041_1_.deathTime + p_225623_3_ - 1.0f) / 20.0f * 1.6f;
            f3 = MathHelper.sqrt(f3);
            if (f3 > 1.0f) {
                f3 = 1.0f;
            }
            p_225623_4_.scale(1.0f + f3 * 2.0f, 1.0f - f3 * 0.99f, 1.0f + f3 * 2.0f);
        }
        final float f1 = (float) p_77041_1_.getSlimeSize();
        final float f2 = (p_77041_1_.prevSquishFactor + (p_77041_1_.squishFactor - p_77041_1_.prevSquishFactor) * p_77041_2_) / (f1 * 0.5f + 1.0f);
        final float f3 = 1.0f / (f2 + 1.0f);
        p_225623_4_.scale(f3 * f1, 1.0f / f3 * f1, f3 * f1);
        float fl = 16.0f;
        final int i = p_77041_1_.getInvulTime();
        if (i > 0) {
            fl -= (i - p_77041_2_) / 10.0f;
        }
        p_225623_4_.scale(fl, fl, fl);
        final float f4 = 0.025f + (p_77041_1_.getEntityData().get(EntitySlimeTitan.t6) - p_77041_1_.getMaxHealth()) / p_77041_1_.getMaxHealth() * 0.5f;
        final float f5 = 1.0f + MathHelper.cos((p_77041_1_.tickCount + p_77041_1_.getId() + p_77041_2_) * f4) * f4;
        final float f6 = 1.0f + MathHelper.sin((p_77041_1_.tickCount + p_77041_1_.getId() + p_77041_2_) * f4 + 0.785f) * f4;
        if (p_77041_1_.deathTime <= 0) {
            p_225623_4_.scale(f5, f6, f5);
        }
        p_225623_4_.translate(0.0f, 0.0075f, 0.0f);
        super.render(p_77041_1_, p_77041_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
        p_225623_4_.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySlimeTitan p_180576_1_) {
        return RenderSlimeTitan.slimeTextures;
    }

    @OnlyIn(Dist.CLIENT)
    public class SlimeTitanGelLayer extends LayerRenderer<EntitySlimeTitan, ModelSlimeTitan> {
        private final ModelSlimeTitan model = new ModelSlimeTitan(0);

        public SlimeTitanGelLayer(IEntityRenderer p_i50923_1_) {
            super(p_i50923_1_);
        }

        public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, EntitySlimeTitan p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
            if (!p_225628_4_.isInvisible()) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_);
                this.model.setupAnim(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
                IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(p_225628_4_)));
                this.model.renderToBuffer(p_225628_1_, ivertexbuilder, p_225628_3_, LivingRenderer.getOverlayCoords(p_225628_4_, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
