package net.minecraft.theTitans.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.titan.EntityIronGolemTitan;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.theTitans.models.ModelIronGolemTitan;
import net.minecraft.theTitans.models.ModelZombieTitan;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUltimaIronGolemTitan extends MobRenderer<EntityIronGolemTitan, ModelIronGolemTitan> {
    private static final ResourceLocation ironGolemTextures;

    static {
        ironGolemTextures = new ResourceLocation("thetitans", "textures/entities/titans/iron_golem_titan.png");
    }

    public RenderUltimaIronGolemTitan(EntityRendererManager p_i50961_1_, ModelIronGolemTitan p_i50961_2_, float p_i50961_3_) {
        super(p_i50961_1_, p_i50961_2_, p_i50961_3_);
    }

    public RenderUltimaIronGolemTitan(EntityRendererManager p_i50961_1_) {
        this(p_i50961_1_, new ModelIronGolemTitan(), 1.0f);
    }

    @Override
    public boolean shouldRender(EntityIronGolemTitan p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return true;
    }

    @Override
    public void render(EntityIronGolemTitan p_180592_1_, float p_180592_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        float f1 = 24.0f;
        final int i = p_180592_1_.getInvulTime();
        if (i > 0) {
            f1 -= (i - p_180592_2_) / 440.0f * 7.75f;
        }
        p_225623_4_.scale(f1, f1, f1);
        super.render(p_180592_1_, p_180592_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
        p_225623_4_.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityIronGolemTitan p_180578_1_) {
        return RenderUltimaIronGolemTitan.ironGolemTextures;
    }
}
