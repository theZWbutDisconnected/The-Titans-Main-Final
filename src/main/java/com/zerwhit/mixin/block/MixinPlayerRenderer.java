package com.zerwhit.mixin.block;


import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public MixinPlayerRenderer(EntityRendererManager manager, boolean p_i46103_2_) {
        super(manager, new PlayerModel<>(0.0F, p_i46103_2_), 0.5F);
    }

    @Inject(method = "getArmPose",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getUseAnimation()Lnet/minecraft/item/UseAction;"),
            locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void renderSwordMain(AbstractClientPlayerEntity p_241741_0_, Hand p_241741_1_, CallbackInfoReturnable<BipedModel.ArmPose> info, ItemStack itemstack){
        UseAction useanim = itemstack.getUseAnimation();
        if (useanim == UseAction.valueOf("BLOCKED") && useanim == itemstack.getUseAnimation()) {
            info.setReturnValue(BipedModel.ArmPose.BLOCK);
        }
    }
}
