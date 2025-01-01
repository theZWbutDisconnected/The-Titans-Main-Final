package com.zerwhit.mixin.block;


import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedModel.class)
public abstract class MixinBipedModel<T extends LivingEntity> extends AgeableModel<T> implements IHasArm, IHasHead {

    @Final
    @Shadow
    public ModelRenderer rightArm;

    @Final
    @Shadow
    public ModelRenderer leftArm;

    @Shadow
    public BipedModel.ArmPose rightArmPose;

    @Shadow
    public BipedModel.ArmPose leftArmPose;

    @Inject(method = "poseRightArm", at = @At(value = "HEAD"), cancellable = true)
    private void renderRight(T entity, CallbackInfo info){
        if (this.rightArmPose == BipedModel.ArmPose.BLOCK) {
            nOBTG$renderArm(entity.getMainArm(), this.rightArm);
            info.cancel();
        }
    }

    @Inject(method = "poseLeftArm", at = @At(value = "HEAD"), cancellable = true)
    private void renderLeft(T entity, CallbackInfo info){
        if (this.leftArmPose == BipedModel.ArmPose.BLOCK) {
            nOBTG$renderArm(entity.getMainArm(), this.leftArm);
            info.cancel();
        }
    }

    @Inject(method = "setupAttackAnimation", at = @At(value = "HEAD"), cancellable = true)
    private void renderCancel(T entity, float ageInTicks, CallbackInfo info){
        if (entity.getMainArm() == HandSide.RIGHT && this.rightArmPose == BipedModel.ArmPose.BLOCK) {
            info.cancel();
        } else if (entity.getMainArm() == HandSide.LEFT && this.leftArmPose == BipedModel.ArmPose.BLOCK) {
            info.cancel();
        }
    }

    @Unique
    private static void nOBTG$renderArm(HandSide type, ModelRenderer arm) {
        arm.xRot = arm.xRot * 0.5F - 0.9424778F;
        arm.yRot = (type == HandSide.RIGHT ? 1 : -1) * -0.5235988F;
    }
}
