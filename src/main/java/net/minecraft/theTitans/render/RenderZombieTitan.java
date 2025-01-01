package net.minecraft.theTitans.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.titan.EntityWitherzilla;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.theTitans.models.ModelWitherzilla;
import net.minecraft.theTitans.models.ModelZombieTitan;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public class RenderZombieTitan extends MobRenderer<EntityZombieTitan, ModelZombieTitan> {
    private static final ResourceLocation zombieTitanTextures;
    private static final ResourceLocation zombieTitanArmedTextures;
    private static final ResourceLocation zombieVillagerTitanTextures;
    private static final ResourceLocation zombieVillagerTitanArmedTextures;

    static {
        zombieTitanTextures = new ResourceLocation("thetitans", "textures/entities/titans/zombie_titan.png");
        zombieTitanArmedTextures = new ResourceLocation("thetitans", "textures/entities/titans/zombie_titan_armed.png");
        zombieVillagerTitanTextures = new ResourceLocation("thetitans", "textures/entities/titans/zombie_villager_titan.png");
        zombieVillagerTitanArmedTextures = new ResourceLocation("thetitans", "textures/entities/titans/zombie_villager_titan_armed.png");
    }

    public RenderZombieTitan(EntityRendererManager p_i50961_1_, ModelZombieTitan p_i50961_2_, float p_i50961_3_) {
        super(p_i50961_1_, p_i50961_2_, p_i50961_3_);
        this.addLayer(new ZombieAuraLayer(this));
    }

    public RenderZombieTitan(EntityRendererManager p_i50961_1_) {
        this(p_i50961_1_, new ModelZombieTitan(), 8.0f);
    }

    @Override
    public boolean shouldRender(EntityZombieTitan p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return true;
    }

    @Override
    public void render(EntityZombieTitan p_180592_1_, float p_180592_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        float f1 = p_180592_1_.getRenderSizeModifier();
        final int i = p_180592_1_.getInvulTime();
        if (i > 0) {
            f1 -= (i - p_225623_3_) / 440.0f * 7.75f;
        }
        final int i2 = p_180592_1_.getExtraPower();
        if (i2 > 0) {
            f1 += i2 * 0.5f;
        }
        p_225623_4_.scale(f1, f1, f1);
        p_225623_4_.translate(0.0f, 0.01f, 0.0f);
        super.render(p_180592_1_, p_180592_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
        p_225623_4_.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityZombieTitan p_180578_1_) {
        if (p_180578_1_.isArmed()) {
            return p_180578_1_.isVillager() ? RenderZombieTitan.zombieVillagerTitanArmedTextures : RenderZombieTitan.zombieTitanArmedTextures;
        }
        return p_180578_1_.isVillager() ? RenderZombieTitan.zombieVillagerTitanTextures : RenderZombieTitan.zombieTitanTextures;
    }


    @OnlyIn(Dist.CLIENT)
    class ZombieAuraLayer extends LayerRenderer<EntityZombieTitan, ModelZombieTitan> {
        private final ModelZombieTitan model = new ModelZombieTitan(0.1F);

        public ZombieAuraLayer(IEntityRenderer<EntityZombieTitan, ModelZombieTitan> p_i50915_1_) {
            super(p_i50915_1_);
        }

        public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, EntityZombieTitan p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
            if (p_225628_4_.isArmored() && p_225628_4_.isAlive()) {
                p_225628_1_.pushPose();
                GL11.glPushMatrix();
                GL11.glDepthMask(!p_225628_4_.isInvisible());
                RenderSystem.pushLightingAttributes();
                RenderSystem.disableLighting();
                final float f1 = p_225628_4_.tickCount + p_225628_7_;
                final float f2 = MathHelper.cos(f1 * 0.02f) * 5.0f;
                final float f3 = f1 * 0.01f;
                //p_225628_1_.translate(MathHelper.cos(f1 * 0.2f), f3, f2);
                EntityModel<EntityZombieTitan> entitymodel = this.model;
                entitymodel.prepareMobModel(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_);
                this.getParentModel().copyPropertiesTo(entitymodel);
                IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(RenderType.energySwirl(this.getTextureLocation(), MathHelper.cos(f1 * 0.2f), f3));
                entitymodel.setupAnim(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
                ((ModelZombieTitan) entitymodel).HeldItem.visible = false;
                entitymodel.renderToBuffer(p_225628_1_, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, 0.0f, 0.6f + MathHelper.cos(f1 * 0.05f) * 0.3f, 0.0f, 1.0f);
                RenderSystem.enableLighting();
                RenderSystem.popAttributes();
                GL11.glPopMatrix();
                p_225628_1_.popPose();
            }
        }

        protected ResourceLocation getTextureLocation() {
            return TheTitans.genericTitanWhiteTexture64x64;
        }
    }
}
