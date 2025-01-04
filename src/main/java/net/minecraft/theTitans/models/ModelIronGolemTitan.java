package net.minecraft.theTitans.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.titan.EntityIronGolemTitan;
import net.minecraft.util.math.MathHelper;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import thehippomaster.AnimationAPI.client.Animator;

public class ModelIronGolemTitan extends ModelTitanBase<EntityIronGolemTitan> {
    private final Animator animator;
    public ModelRenderer LeftLeg1;
    public ModelRenderer RightLeg1;
    public ModelRenderer Torso;
    public ModelRenderer LeftLeg2;
    public ModelRenderer RightLeg2;
    public ModelRenderer Body;
    public ModelRenderer Head;
    public ModelRenderer LeftArm1;
    public ModelRenderer RightArm1;
    public ModelRenderer Nose;
    public ModelRenderer LeftArm2;
    public ModelRenderer LeftArm3;
    public ModelRenderer LeftArm4;
    public ModelRenderer RightArm2;
    public ModelRenderer RightArm3;
    public ModelRenderer RightArm4;
    private float partialTick;

    public ModelIronGolemTitan() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.LeftArm4 = new ModelRenderer(this, 58, 29);
        this.LeftArm4.mirror = true;
        this.LeftArm4.setPos(0.0f, 7.0f, 0.0f);
        this.LeftArm4.addBox(-2.0f, 0.0f, -3.0f, 4, 10, 6, 0.0f);
        (this.Torso = new ModelRenderer(this, 0, 41)).setPos(0.0f, 8.0f, 0.0f);
        this.Torso.addBox(-4.5f, -5.0f, -3.0f, 9, 5, 6, 0.5f);
        (this.RightLeg2 = new ModelRenderer(this, 98, 0)).setPos(0.0f, 8.0f, 0.0f);
        this.RightLeg2.addBox(-3.0f, 0.0f, -2.5f, 6, 8, 5, 0.0f);
        (this.RightArm1 = new ModelRenderer(this, 58, 13)).setPos(-9.0f, -9.0f, 0.0f);
        this.RightArm1.addBox(-4.0f, -3.0f, -3.0f, 4, 6, 6, 0.0f);
        (this.RightArm2 = new ModelRenderer(this, 78, 13)).setPos(-2.0f, 2.0f, 0.0f);
        this.RightArm2.addBox(-2.0f, 0.0f, -3.0f, 4, 10, 6, 0.0f);
        (this.RightArm3 = new ModelRenderer(this, 98, 13)).setPos(0.0f, 8.0f, 0.0f);
        this.RightArm3.addBox(-2.0f, 0.0f, -3.0f, 4, 10, 6, 0.0f);
        this.LeftArm3 = new ModelRenderer(this, 98, 13);
        this.LeftArm3.mirror = true;
        this.LeftArm3.setPos(0.0f, 8.0f, 0.0f);
        this.LeftArm3.addBox(-2.0f, 0.0f, -3.0f, 4, 10, 6, 0.0f);
        (this.Head = new ModelRenderer(this, 0, 0)).setPos(0.0f, -12.0f, -3.0f);
        this.Head.addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, 0.0f);
        (this.Body = new ModelRenderer(this, 0, 18)).setPos(0.0f, -5.0f, 0.0f);
        this.Body.addBox(-9.0f, -12.0f, -6.0f, 18, 12, 11, 0.0f);
        (this.RightLeg1 = new ModelRenderer(this, 76, 0)).setPos(-4.0f, 8.0f, 0.0f);
        this.RightLeg1.addBox(-3.0f, 0.0f, -2.5f, 6, 8, 5, 0.0f);
        this.LeftArm2 = new ModelRenderer(this, 78, 13);
        this.LeftArm2.mirror = true;
        this.LeftArm2.setPos(2.0f, 2.0f, 0.0f);
        this.LeftArm2.addBox(-2.0f, 0.0f, -3.0f, 4, 10, 6, 0.0f);
        this.LeftArm1 = new ModelRenderer(this, 58, 13);
        this.LeftArm1.mirror = true;
        this.LeftArm1.setPos(9.0f, -9.0f, 0.0f);
        this.LeftArm1.addBox(0.0f, -3.0f, -3.0f, 4, 6, 6, 0.0f);
        (this.Nose = new ModelRenderer(this, 0, 0)).setPos(0.0f, 0.0f, -5.0f);
        this.Nose.addBox(-1.0f, -3.0f, -1.0f, 2, 4, 2, 0.0f);
        (this.RightArm4 = new ModelRenderer(this, 58, 29)).setPos(0.0f, 7.0f, 0.0f);
        this.RightArm4.addBox(-2.0f, 0.0f, -3.0f, 4, 10, 6, 0.0f);
        this.LeftLeg1 = new ModelRenderer(this, 32, 0);
        this.LeftLeg1.mirror = true;
        this.LeftLeg1.setPos(4.0f, 8.0f, 0.0f);
        this.LeftLeg1.addBox(-3.0f, 0.0f, -2.5f, 6, 8, 5, 0.0f);
        this.LeftLeg2 = new ModelRenderer(this, 54, 0);
        this.LeftLeg2.mirror = true;
        this.LeftLeg2.setPos(0.0f, 8.0f, 0.0f);
        this.LeftLeg2.addBox(-3.0f, 0.0f, -2.5f, 6, 8, 5, 0.0f);
        this.LeftArm3.addChild(this.LeftArm4);
        this.RightLeg1.addChild(this.RightLeg2);
        this.Body.addChild(this.RightArm1);
        this.RightArm1.addChild(this.RightArm2);
        this.RightArm2.addChild(this.RightArm3);
        this.LeftArm2.addChild(this.LeftArm3);
        this.Body.addChild(this.Head);
        this.Torso.addChild(this.Body);
        this.LeftArm1.addChild(this.LeftArm2);
        this.Body.addChild(this.LeftArm1);
        this.Head.addChild(this.Nose);
        this.RightArm3.addChild(this.RightArm4);
        this.LeftLeg1.addChild(this.LeftLeg2);
        this.animator = new Animator(this);
    }

    @Override
    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.Torso.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.RightLeg1.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.LeftLeg1.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }

    public void setRotateAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void setAngles() {
        this.setRotateAngle(this.Head, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.Body, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.Torso, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.LeftLeg1, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.RightLeg1, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.LeftLeg2, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.RightLeg2, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.LeftArm1, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.RightArm1, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.LeftArm2, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.RightArm2, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.LeftArm3, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.RightArm3, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.LeftArm4, 0.0f, 0.0f, 0.0f);
        this.setRotateAngle(this.RightArm4, 0.0f, 0.0f, 0.0f);
        this.RightArm1.setPos(-9.0f, -9.0f, 0.0f);
        this.RightArm2.setPos(-2.0f, 1.0f, 0.0f);
        this.RightArm3.setPos(0.0f, 8.0f, 0.0f);
        this.RightArm4.setPos(0.0f, 8.0f, 0.0f);
        this.LeftArm1.setPos(9.0f, -9.0f, 0.0f);
        this.LeftArm2.setPos(2.0f, 1.0f, 0.0f);
        this.LeftArm3.setPos(0.0f, 8.0f, 0.0f);
        this.LeftArm4.setPos(0.0f, 8.0f, 0.0f);
        this.Nose.setPos(0.0f, 0.0f, -5.0f);
        this.Head.setPos(0.0f, -12.0f, -3.0f);
        this.Body.setPos(0.0f, -5.0f, 0.0f);
        this.Torso.setPos(0.0f, 8.0f, 0.0f);
        this.RightLeg1.setPos(-4.0f, 8.0f, 0.0f);
        this.RightLeg2.setPos(0.0f, 8.0f, 0.0f);
        this.LeftLeg1.setPos(4.0f, 8.0f, 0.0f);
        this.LeftLeg2.setPos(0.0f, 8.0f, 0.0f);
    }

    @Override
    public void prepareMobModel(EntityIronGolemTitan p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        this.partialTick = p_212843_4_;
        super.prepareMobModel(p_212843_1_, p_212843_2_, p_212843_3_, p_212843_4_);
    }

    @Override
    public void setupAnim(EntityIronGolemTitan entity, final float f, final float f1, final float f2, final float f3, final float f4) {
        this.animate((IAnimatedEntity) entity, f, f1, f2, f3, f4);
    }

    public void animate(final IAnimatedEntity entity, final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_) {
        final EntityIronGolemTitan entitytitan = (EntityIronGolemTitan) entity;
        this.animator.update(entitytitan, this.partialTick);
        this.setAngles();
        final float fo = 0.22206666f;
        if (entitytitan.deathTicks <= 0) {
            this.Head.yRot = p_78087_4_ / 57.295776f;
            this.Head.xRot = p_78087_5_ / 57.295776f;
            this.Torso.zRot = MathHelper.cos(p_78087_1_ * fo) * 0.2f * p_78087_2_;
            this.Body.zRot = MathHelper.cos(p_78087_1_ * fo - 1.0f) * 0.2f * p_78087_2_;
            if (entitytitan.animID == 0) {
                this.RightLeg1.xRot = MathHelper.cos(p_78087_1_ * fo - 0.5f) * 0.75f * p_78087_2_;
                this.LeftLeg1.xRot = MathHelper.cos(p_78087_1_ * fo + 2.6415927f) * 0.75f * p_78087_2_;
                this.RightLeg2.xRot = MathHelper.cos(p_78087_1_ * fo + 3.1415927f) * 0.75f * p_78087_2_;
                this.LeftLeg2.xRot = MathHelper.cos(p_78087_1_ * fo) * 0.75f * p_78087_2_;
                this.RightArm1.xRot = MathHelper.cos(p_78087_1_ * fo + 3.1415927f) * 0.5f * p_78087_2_;
                this.RightArm2.xRot = MathHelper.cos(p_78087_1_ * fo + 2.1415927f) * 0.5f * p_78087_2_;
                this.RightArm3.xRot = MathHelper.cos(p_78087_1_ * fo + 1.6415927f) * 0.5f * p_78087_2_;
                this.RightArm4.xRot = MathHelper.cos(p_78087_1_ * fo + 1.1415927f) * 0.5f * p_78087_2_;
                this.LeftArm1.xRot = MathHelper.cos(p_78087_1_ * fo) * 0.5f * p_78087_2_;
                this.LeftArm2.xRot = MathHelper.cos(p_78087_1_ * fo - 1.0f) * 0.5f * p_78087_2_;
                this.LeftArm3.xRot = MathHelper.cos(p_78087_1_ * fo - 1.5f) * 0.5f * p_78087_2_;
                this.LeftArm4.xRot = MathHelper.cos(p_78087_1_ * fo - 2.0f) * 0.5f * p_78087_2_;
            }
            if (this.RightLeg2.xRot < 0.0f) {
                this.RightLeg2.xRot = 0.0f;
            }
            if (this.LeftLeg2.xRot < 0.0f) {
                this.LeftLeg2.xRot = 0.0f;
            }
            if (this.RightArm2.xRot > 0.0f) {
                this.RightArm2.xRot = 0.0f;
            }
            if (this.LeftArm2.xRot > 0.0f) {
                this.LeftArm2.xRot = 0.0f;
            }
            if (this.RightArm3.xRot > 0.0f) {
                this.RightArm3.xRot = 0.0f;
            }
            if (this.LeftArm3.xRot > 0.0f) {
                this.LeftArm3.xRot = 0.0f;
            }
            if (this.RightArm4.xRot > 0.0f) {
                this.RightArm4.xRot = 0.0f;
            }
            if (this.LeftArm4.xRot > 0.0f) {
                this.LeftArm4.xRot = 0.0f;
            }
            this.animateAntiTitanAttack();
            this.animateThrow();
            this.animateSlam();
            this.animateStomp();
            this.animateSwat();
            this.animatePunch();
            this.RightLeg1.setPos(-4.0f, this.Torso.y, 0.0f);
            this.LeftLeg1.setPos(4.0f, this.Torso.y, 0.0f);
        } else {
            this.animateDeath();
        }
    }

    private void animateAntiTitanAttack() {
        this.animator.setAnim(1);
        this.animator.startPhase(10);
        this.animator.rotate(this.Head, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, 2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm1, 2.0f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(10);
        this.animator.rotate(this.Head, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -3.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm1, -3.0f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.resetPhase(10);
    }

    private void animateThrow() {
        this.animator.setAnim(5);
        this.animator.startPhase(10);
        this.animator.rotate(this.Head, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.6f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(20);
        this.animator.rotate(this.RightArm1, -2.0f, 0.0f, -0.5f);
        this.animator.rotate(this.RightArm2, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.5f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(20);
        this.animator.rotate(this.Head, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -0.5f, 0.0f, -0.5f);
        this.animator.endPhase();
        this.animator.resetPhase(30);
    }

    private void animateSlam() {
        this.animator.setAnim(6);
        this.animator.startPhase(15);
        this.animator.move(this.Torso, 0.0f, 1.0f, 0.0f);
        this.animator.rotate(this.RightLeg1, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, 0.8f, 0.5f, 0.5f);
        this.animator.rotate(this.LeftArm1, 0.8f, -0.5f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.6f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(15);
        this.animator.move(this.Torso, 0.0f, -1.0f, 0.0f);
        this.animator.rotate(this.RightLeg1, -1.6f, 0.2f, 0.0f);
        this.animator.rotate(this.LeftLeg1, 0.0f, 0.2f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.9f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.1f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -3.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm1, -3.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm2, -2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -1.0f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(10);
        this.animator.move(this.Torso, 0.0f, 8.0f, -1.0f);
        this.animator.rotate(this.RightLeg1, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightLeg2, 2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, -2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Torso, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -2.0f, 0.0f, 0.5f);
        this.animator.rotate(this.LeftArm1, -2.0f, 0.0f, -0.5f);
        this.animator.rotate(this.RightArm2, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.4f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.4f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.4f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.4f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.resetPhase(30);
    }

    private void animateStomp() {
        this.animator.setAnim(7);
        this.animator.startPhase(25);
        this.animator.rotate(this.RightLeg1, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, 0.8f, 0.5f, 0.5f);
        this.animator.rotate(this.LeftArm1, 0.8f, -0.5f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.6f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(25);
        this.animator.rotate(this.RightLeg1, -2.0f, 0.2f, 0.75f);
        this.animator.rotate(this.LeftLeg1, 0.0f, 0.2f, 0.0f);
        this.animator.rotate(this.RightLeg2, 1.5f, 0.0f, -0.5f);
        this.animator.rotate(this.LeftLeg2, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, -1.0f, 0.0f, 0.25f);
        this.animator.rotate(this.RightArm1, -0.75f, 0.0f, 0.5f);
        this.animator.rotate(this.LeftArm1, -0.75f, 0.0f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.2f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(20);
        this.animator.move(this.Torso, 0.0f, 2.0f, 0.0f);
        this.animator.rotate(this.RightLeg1, -0.5f, 0.5f, 0.0f);
        this.animator.rotate(this.LeftLeg1, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.1f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.5f, 0.0f, -0.4f);
        this.animator.rotate(this.RightArm1, 0.9f, 0.0f, 0.5f);
        this.animator.rotate(this.LeftArm1, 0.9f, 0.0f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.2f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.setStationaryPhase(5);
        this.animator.startPhase(20);
        this.animator.rotate(this.RightLeg1, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, -2.0f, 0.0f, -0.75f);
        this.animator.rotate(this.RightLeg2, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 1.5f, 0.0f, 0.5f);
        this.animator.rotate(this.Body, -1.0f, 0.0f, -0.25f);
        this.animator.rotate(this.RightArm1, 0.9f, 0.0f, 0.5f);
        this.animator.rotate(this.LeftArm1, 0.9f, 0.0f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.3f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.3f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.3f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.3f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.3f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.3f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(20);
        this.animator.move(this.Torso, 0.0f, 2.0f, 0.0f);
        this.animator.rotate(this.RightLeg1, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, -0.5f, -0.5f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.5f, 0.0f, 0.4f);
        this.animator.rotate(this.RightArm1, 0.9f, 0.0f, 0.5f);
        this.animator.rotate(this.LeftArm1, 0.9f, 0.0f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.2f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.setStationaryPhase(20);
        this.animator.resetPhase(20);
    }

    private void animateSwat() {
        this.animator.setAnim(8);
        this.animator.startPhase(20);
        this.animator.rotate(this.LeftArm1, 2.5f, 0.0f, -1.5f);
        this.animator.rotate(this.Body, 1.5f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(20);
        this.animator.rotate(this.LeftArm1, -1.5f, 0.0f, -1.5f);
        this.animator.rotate(this.LeftArm2, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 1.5f, 1.0f, 0.0f);
        this.animator.endPhase();
        this.animator.resetPhase(20);
    }

    private void animatePunch() {
        this.animator.setAnim(9);
        this.animator.startPhase(20);
        this.animator.rotate(this.Head, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.6f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(20);
        this.animator.rotate(this.Body, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm2, -1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.5f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(10);
        this.animator.rotate(this.Body, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Torso, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -1.25f, 0.0f, 0.5f);
        this.animator.rotate(this.RightArm2, -0.75f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.75f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.75f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.setStationaryPhase(20);
        this.animator.resetPhase(20);
    }

    private void animateDeath() {
        this.animator.setAnim(10);
        this.animator.startPhase(40);
        this.animator.move(this.Torso, 0.0f, 2.0f, 4.0f);
        this.animator.move(this.RightLeg1, 0.0f, 2.0f, 4.0f);
        this.animator.move(this.LeftLeg1, 0.0f, 2.0f, 4.0f);
        this.animator.rotate(this.RightLeg1, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, -0.65f, 0.0f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.65f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.75f, 0.0f, 0.0f);
        this.animator.rotate(this.Torso, 0.75f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -0.5f, 0.5f, 0.5f);
        this.animator.rotate(this.LeftArm1, -0.5f, -0.5f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.25f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(40);
        this.animator.move(this.Torso, 0.0f, 2.0f, 8.0f);
        this.animator.move(this.RightLeg1, 0.0f, 2.0f, 8.0f);
        this.animator.move(this.LeftLeg1, 0.0f, 2.0f, 8.0f);
        this.animator.rotate(this.RightLeg1, -0.65f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.65f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, 0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.Torso, 0.6f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -0.5f, 0.5f, 0.5f);
        this.animator.rotate(this.LeftArm1, -0.5f, -0.5f, -0.5f);
        this.animator.rotate(this.RightArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.25f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(40);
        this.animator.move(this.Torso, 0.0f, 2.0f, 9.0f);
        this.animator.move(this.RightLeg1, 0.0f, 2.0f, 9.0f);
        this.animator.move(this.LeftLeg1, 0.0f, 2.0f, 9.0f);
        this.animator.rotate(this.RightLeg1, -0.65f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg1, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.65f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Body, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Torso, 0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -1.25f, 0.4f, 0.0f);
        this.animator.rotate(this.LeftArm1, -1.25f, -0.4f, 0.0f);
        this.animator.rotate(this.RightArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.25f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(80);
        this.animator.move(this.Torso, 0.0f, 10.0f, 1.0f);
        this.animator.move(this.RightLeg1, 0.0f, 10.0f, 0.0f);
        this.animator.move(this.LeftLeg1, 0.0f, 10.0f, 0.0f);
        this.animator.rotate(this.RightLeg1, 1.4f, -0.5f, 0.0f);
        this.animator.rotate(this.LeftLeg1, 1.4f, 0.5f, 0.0f);
        this.animator.rotate(this.RightLeg2, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 1.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, 0.0f, 0.9f, 0.0f);
        this.animator.rotate(this.Body, -0.5f, 0.0f, 0.0f);
        this.animator.rotate(this.Torso, 2.0f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -2.8f, 0.0f, -0.5f);
        this.animator.rotate(this.LeftArm1, -2.8f, 0.0f, 0.5f);
        this.animator.rotate(this.RightArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm2, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm3, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm4, -0.25f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftArm4, -0.25f, 0.0f, 0.0f);
        this.animator.endPhase();
        this.animator.startPhase(100);
        this.animator.move(this.Torso, 0.0f, 10.0f, 1.0f);
        this.animator.move(this.RightLeg1, 0.0f, 10.0f, 0.0f);
        this.animator.move(this.LeftLeg1, 0.0f, 10.0f, 0.0f);
        this.animator.rotate(this.RightLeg1, 1.4f, -0.5f, 0.0f);
        this.animator.rotate(this.LeftLeg1, 1.4f, 0.5f, 0.0f);
        this.animator.rotate(this.RightLeg2, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.LeftLeg2, 0.0f, 0.0f, 0.0f);
        this.animator.rotate(this.Head, 0.0f, 0.9f, 0.0f);
        this.animator.rotate(this.Body, -0.2f, 0.0f, 0.0f);
        this.animator.rotate(this.Torso, 1.75f, 0.0f, 0.0f);
        this.animator.rotate(this.RightArm1, -3.0f, 0.0f, -0.5f);
        this.animator.rotate(this.LeftArm1, -3.0f, 0.0f, 0.5f);
        this.animator.endPhase();
        this.animator.setStationaryPhase(1700);
        this.animator.resetPhase(0);
    }
}
