package net.minecraft.theTitans.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.titan.EntityProtoBall;
import net.minecraft.util.ResourceLocation;

public class RenderProtoBall extends EntityRenderer<EntityProtoBall> {
    public RenderProtoBall(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityProtoBall p_110775_1_) {
        return null;
    }

    @Override
    public void render(EntityProtoBall p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
    }
}
