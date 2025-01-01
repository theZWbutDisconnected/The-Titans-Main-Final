package net.minecraft.theTitans.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.titan.EntityWitherzilla;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.models.ModelWitherzilla;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class RenderWitherzilla extends MobRenderer<EntityWitherzilla, ModelWitherzilla> {
    private static final ResourceLocation enderDragonExplodingTextures;
    private static final ResourceLocation witherzillaSheild;
    private static final ResourceLocation witherzillaOmegaTextures;
    private static final ResourceLocation witherzillaTextures;

    static {
        enderDragonExplodingTextures = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
        witherzillaSheild = new ResourceLocation("thetitans", "textures/entities/wither_aura.png");
        witherzillaOmegaTextures = new ResourceLocation("thetitans", "textures/entities/titans/witherzilla_omega.png");
        witherzillaTextures = new ResourceLocation("thetitans", "textures/entities/titans/witherzilla.png");
    }

    public RenderWitherzilla(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new ModelWitherzilla(), 1.0f);
        this.addLayer(new WitherzillaAuraLayer(this));
    }

    @Override
    public void render(EntityWitherzilla entity, float partialTicks, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int p_225623_6_) {
        matrixStack.pushPose();
        func_180592_a(entity, partialTicks, matrixStack);
        super.render(entity, partialTicks, p_225623_3_, matrixStack, bufferIn, p_225623_6_);
        matrixStack.popPose();
    }

    protected void func_180592_a(final EntityWitherzilla p_180592_1_, final float p_180592_2_, MatrixStack p_225623_4_) {
        float f1 = p_180592_1_.isInOmegaForm() ? 256.0f : 128.0f;
        final int i = p_180592_1_.getInvulTime();
        if (i > 0) {
            f1 -= (i - p_180592_2_) / 440.0f * 7.75f;
        }
        final int i2 = p_180592_1_.getExtraPower();
        if (i2 > 0) {
            f1 += i2 * 0.5f;
        }
        p_225623_4_.scale(f1, f1, f1);
        if (p_180592_1_.isArmored()) {
            p_225623_4_.translate(0.0f, 2.0f, 0.0f);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWitherzilla entity) {
        final int i = entity.getInvulTime();
        return (i > 0 && (i > 300 || i / 10 % 2 != 1)) ? TheTitans.genericTitanWhiteTexture64x64 : ((entity.isInvulnerable() || entity.isInOmegaForm()) ? RenderWitherzilla.witherzillaOmegaTextures : RenderWitherzilla.witherzillaTextures);
    }

    @OnlyIn(Dist.CLIENT)
    class WitherzillaAuraLayer extends LayerRenderer<EntityWitherzilla, ModelWitherzilla> {
        private final ModelWitherzilla model = new ModelWitherzilla();

        public WitherzillaAuraLayer(IEntityRenderer<EntityWitherzilla, ModelWitherzilla> p_i50915_1_) {
            super(p_i50915_1_);
        }

        public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, EntityWitherzilla p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
            if (p_225628_4_.isInOmegaForm() && p_225628_4_.isAlive()) {
                GL11.glPushMatrix();
                GL11.glDepthMask(!p_225628_4_.isInvisible());
                RenderSystem.pushLightingAttributes();
                RenderSystem.disableLighting();
                float f = (float) p_225628_4_.tickCount + p_225628_7_;
                EntityModel<EntityWitherzilla> entitymodel = this.model;
                entitymodel.prepareMobModel(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_);
                this.getParentModel().copyPropertiesTo(entitymodel);
                IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(RenderType.energySwirl(this.getTextureLocation(), 1.0f / (f * 0.02f), f * 0.01F));
                entitymodel.setupAnim(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
                entitymodel.renderToBuffer(p_225628_1_, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
                RenderSystem.enableLighting();
                RenderSystem.popAttributes();
                GL11.glPopMatrix();
            }
        }

        protected ResourceLocation getTextureLocation() {
            return witherzillaSheild;
        }
    }
}
