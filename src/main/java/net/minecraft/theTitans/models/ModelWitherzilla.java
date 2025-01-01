package net.minecraft.theTitans.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.titan.EntityWitherzilla;
import net.minecraft.util.math.MathHelper;

public class ModelWitherzilla extends EntityModel<EntityWitherzilla> {
    private final ModelRenderer[] spine;
    public ModelRenderer[] heads;

    public ModelWitherzilla() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.spine = new ModelRenderer[3];
        (this.spine[0] = new ModelRenderer(this, 0, 16)).addBox(-10.0f, 3.9f, -0.5f, 20, 3, 3);
        (this.spine[1] = new ModelRenderer(this).setTexSize(this.texWidth, this.texHeight)).setPos(-2.0f, 6.9f, -0.5f);
        this.spine[1].texOffs(0, 22).addBox(0.0f, 0.0f, 0.0f, 3, 10, 3);
        this.spine[1].texOffs(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11, 2, 2);
        this.spine[1].texOffs(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11, 2, 2);
        this.spine[1].texOffs(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11, 2, 2);
        (this.spine[2] = new ModelRenderer(this, 12, 22)).addBox(0.0f, 0.0f, 0.0f, 3, 6, 3);
        this.heads = new ModelRenderer[3];
        (this.heads[0] = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        (this.heads[1] = new ModelRenderer(this, 32, 0)).addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6);
        (this.heads[2] = new ModelRenderer(this, 32, 0)).addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6);
    }

    public int func_82903_a() {
        return 32;
    }

    @Override
    public void setupAnim(EntityWitherzilla p_78087_7_, final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_) {
        final float f6 = MathHelper.cos(p_78087_3_ * 0.025f);
        this.spine[1].xRot = (0.025f + 0.05f * f6) * 3.1415927f;
        this.spine[2].setPos(-2.0f, 6.9f + MathHelper.cos(this.spine[1].xRot) * 10.0f, -0.5f + MathHelper.sin(this.spine[1].xRot) * 10.0f);
        this.spine[2].xRot = (0.265f + 0.1f * f6) * 3.1415927f;
        this.heads[0].yRot = p_78087_4_ / 57.295776f;
        this.heads[0].xRot = p_78087_5_ / 57.295776f;
        if (p_78087_7_.getInvulTime() < 1000) {
            this.heads[0].setPos(0.0f, 0.0f, 0.0f);
            this.heads[1].setPos(-10.0f, 4.0f, 0.0f);
            this.heads[2].setPos(10.0f, 4.0f, 0.0f);
            this.spine[0].setPos(0.0f, 0.0f, 0.0f);
            this.spine[1].setPos(-2.0f, 6.9f, -0.5f);
        } else {
            this.heads[0].setPos((float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0));
            this.heads[1].setPos((float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0));
            this.heads[2].setPos((float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0));
            this.spine[0].setPos((float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0));
            this.spine[1].setPos((float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0));
            this.spine[2].setPos((float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0), (float) (p_78087_7_.getRandom().nextGaussian() * 2.0));
        }
    }

    @Override
    public void prepareMobModel(final EntityWitherzilla p_78086_1_, final float p_78086_2_, final float p_78086_3_, final float p_78086_4_) {
        for (int i = 1; i < 3; ++i) {
            this.heads[i].yRot = (p_78086_1_.getHeadYRot(i - 1) - p_78086_1_.yBodyRot) / 57.295776f;
            this.heads[i].xRot = p_78086_1_.getHeadXRot(i - 1) / 57.295776f;
        }
    }

    @Override
    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        for (final ModelRenderer modelrenderer : this.heads) {
            modelrenderer.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        }
        for (final ModelRenderer modelrenderer : this.spine) {
            modelrenderer.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        }
    }
}
