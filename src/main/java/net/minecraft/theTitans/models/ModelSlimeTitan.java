package net.minecraft.theTitans.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.titan.EntitySlimeTitan;

public class ModelSlimeTitan extends EntityModel<EntitySlimeTitan> {
    ModelRenderer slimeBodies;
    ModelRenderer slimeRightEye;
    ModelRenderer slimeLeftEye;
    ModelRenderer slimeMouth;

    public ModelSlimeTitan(final int p_i1157_1_) {
        (this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_)).addBox(-4.0f, 16.0f, -4.0f, 8, 8, 8);
        if (p_i1157_1_ > 0) {
            (this.slimeBodies = new ModelRenderer(this, 0, p_i1157_1_)).addBox(-3.0f, 17.0f, -3.0f, 6, 6, 6);
            (this.slimeRightEye = new ModelRenderer(this, 32, 0)).addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2);
            this.slimeRightEye.setPos(-2.25f, 19.0f, -2.5f);
            (this.slimeLeftEye = new ModelRenderer(this, 32, 4)).addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2);
            this.slimeLeftEye.setPos(2.25f, 19.0f, -2.5f);
            (this.slimeMouth = new ModelRenderer(this, 32, 8)).addBox(0.0f, 21.0f, -3.5f, 1, 1, 1);
        }
    }

    @Override
    public void setupAnim(EntitySlimeTitan p_225597_1_, final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_) {
        if (this.slimeRightEye != null) {
            this.slimeRightEye.yRot = p_78087_4_ / 57.295776f;
            this.slimeRightEye.xRot = p_78087_5_ / 57.295776f;
        }
        if (this.slimeLeftEye != null) {
            this.slimeLeftEye.yRot = p_78087_4_ / 57.295776f;
            this.slimeLeftEye.xRot = p_78087_5_ / 57.295776f;
        }
    }

    @Override
    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.slimeBodies.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
            this.slimeLeftEye.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
            this.slimeMouth.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        }
    }
}
