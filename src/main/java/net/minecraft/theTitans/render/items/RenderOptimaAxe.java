package net.minecraft.theTitans.render.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class RenderOptimaAxe extends ItemStackTileEntityRenderer {
    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel ibakedmodel = itemRenderer.getModel(stack, null, null);
        matrixStack.pushPose();

        switch (transformType) {
            case GROUND -> {
                matrixStack.scale(8.0F, 8.0F, 8.0F);
                matrixStack.translate(0.0525F, 1.53F, 0.0525F);
            }
            case THIRD_PERSON_RIGHT_HAND -> {
                matrixStack.scale(8.0F, 8.0F, 8.0F);
                matrixStack.mulPose(Vector3f.XP.rotation(0.5F));
                matrixStack.mulPose(Vector3f.ZP.rotation(-0.22F));
                matrixStack.mulPose(Vector3f.YP.rotation(-0.36F));
                matrixStack.translate(0.08F, 1.53F, 0.008F);
            }
            case THIRD_PERSON_LEFT_HAND -> {
                matrixStack.scale(8.0F, 8.0F, 8.0F);
                matrixStack.mulPose(Vector3f.XP.rotation(0.5F));
                matrixStack.mulPose(Vector3f.ZP.rotation(0.22F));
                matrixStack.mulPose(Vector3f.YP.rotation(0.36F));
                matrixStack.translate(0.08F, 1.53F, 0.008F);
            }
        }

        itemRenderer.render(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel.getBakedModel());
        matrixStack.popPose();
    }
}
