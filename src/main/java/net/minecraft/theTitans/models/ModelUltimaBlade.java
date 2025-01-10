package net.minecraft.theTitans.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import java.util.List;
import java.util.Random;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;

public class ModelUltimaBlade implements IBakedModel {
    private final IBakedModel existingModel;

    public ModelUltimaBlade(IBakedModel existingModel) {
        this.existingModel = existingModel;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
        throw new AssertionError("IForgeBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
		List<BakedQuad> l = this.existingModel.getQuads(state, side, rand);
		for (BakedQuad q : l) {
			q.getDirection().rotate(new Matrix4f(new Quaternion(0.1f, 0.0f, 0.0f, 1.0f)), side);
		}
        return l;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.existingModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.existingModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.existingModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.existingModel.getParticleIcon();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.existingModel.getOverrides();
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        switch (cameraTransformType) {
            case GROUND:
            case THIRD_PERSON_RIGHT_HAND:
            case THIRD_PERSON_LEFT_HAND:
                return this;
        }
        return this.existingModel.handlePerspective(cameraTransformType, mat);
    }
}
