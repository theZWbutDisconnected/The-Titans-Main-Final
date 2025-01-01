package net.minecraft.theTitans.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.titan.EntityGhastTitan;
import net.minecraft.theTitans.models.ModelGhastTitan;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGhastTitan extends MobRenderer<EntityGhastTitan, ModelGhastTitan> {
    private static final ResourceLocation ghastTextures;
    private static final ResourceLocation ghastShootingTextures;

    static {
        ghastTextures = new ResourceLocation("thetitans", "textures/entities/titans/ghast_titan.png");
        ghastShootingTextures = new ResourceLocation("thetitans", "textures/entities/titans/ghast_titan_shooting.png");
    }

    public RenderGhastTitan(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new ModelGhastTitan(), 4.0f);
    }

    @Override
    public void render(final EntityGhastTitan p_77041_1_, final float p_77041_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        GL11.glPushMatrix();
        p_225623_4_.pushPose();
        float f1 = (p_77041_1_.prevAttackCounter + (p_77041_1_.attackCounter - p_77041_1_.prevAttackCounter) * p_77041_2_) / 20.0f;
        if (f1 < 0.0f) {
            f1 = 0.0f;
        }
        f1 = 1.0f / (f1 * f1 * f1 * f1 * f1 * 2.0f + 1.0f);
        final float f2 = (8.0f + f1) / 2.0f;
        final float f3 = (8.0f + 1.0f / f1) / 2.0f;
        p_225623_4_.scale(f3, f2, f3);
        float f4 = 24.0f;
        final int i = p_77041_1_.getInvulTime();
        if (i > 0) {
            f4 -= (i - p_77041_2_) / 440.0f * 7.75f;
        }
        final int i2 = p_77041_1_.getExtraPower();
        if (i2 > 0) {
            f1 += i2 * 0.5f;
        }
        p_225623_4_.scale(f4, f4, f4);
        if (p_77041_1_.hurtTime > 0) {
            GL11.glDepthFunc(514);
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glDepthFunc(515);
        }
        super.render(p_77041_1_, p_77041_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
        p_225623_4_.popPose();
        GL11.glPopMatrix();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGhastTitan p_180576_1_) {
        return p_180576_1_.func_110182_bF() ? RenderGhastTitan.ghastShootingTextures : RenderGhastTitan.ghastTextures;
    }
}
