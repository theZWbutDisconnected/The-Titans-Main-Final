package net.minecraft.theTitans.models;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.titan.EntityTitan;
import java.lang.reflect.Field;

public abstract class ModelTitanBase<T extends EntityTitan> extends EntityModel<T>
{
	public final ObjectList<ModelRenderer> childBones = new ObjectArrayList<>();

	@Override
    public void prepareMobModel(T p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        for (ModelRenderer i : this.getChilds()) {
			childBones.add(i);
			Class c = i.getClass();
			Field f = null;
			try {
				f = c.getDeclaredField("children");
			} catch (NoSuchFieldException e) {
				try {
					f = c.getDeclaredField("field_78805_m");
				} catch (NoSuchFieldException e1) {}
			}
			try {
				for (int k = 0; k < ((ObjectList)f.get(i)).size(); k++) {
					ModelRenderer model = (ModelRenderer) ((ObjectList)f.get(i)).get(k);
					if (!this.has(this.getChilds(), model))
						childBones.add(model);
				}
			} catch (IllegalAccessException e) {} catch (IllegalArgumentException e) {}
		}
        super.prepareMobModel(p_212843_1_, p_212843_2_, p_212843_3_, p_212843_4_);
    }
	
	private boolean has(ModelRenderer[] ms, ModelRenderer m) {
		for (ModelRenderer i : ms) {
			if (i == m) {
				return true;
			}
		}
		return false;
	}
	
	public ModelRenderer[] getChilds() {
		return new ModelRenderer[] {};
	}
}
