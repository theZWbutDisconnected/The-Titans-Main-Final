//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thehippomaster.AnimationAPI.client;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thehippomaster.AnimationAPI.IAnimatedEntity;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.theTitans.models.ModelTitanBase;

@OnlyIn(Dist.CLIENT)
public class Animator {
    public static final float PI = 3.1415927F;
    private final HashMap<ModelRenderer, Transform> transformMap = new HashMap();
    private final HashMap<ModelRenderer, Transform> prevTransformMap = new HashMap();
    private int tempTick = 0;
    private int prevTempTick;
    private boolean correctAnim = false;
    private ModelTitanBase mainModel;
    private IAnimatedEntity animEntity;
    private float partialTick;

    public Animator(ModelTitanBase modelRenderer) {
		this.mainModel = modelRenderer;
    }

    public static void resetAngles(ModelRenderer... boxes) {
        ModelRenderer[] var1 = boxes;
        int var2 = boxes.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ModelRenderer box = var1[var3];
            resetAngles(box);
        }

    }

    public static void resetAngles(ModelRenderer box) {
        box.xRot = 0.0F;
        box.yRot = 0.0F;
        box.zRot = 0.0F;
    }

    public IAnimatedEntity getEntity() {
        return this.animEntity;
    }

    public void update(IAnimatedEntity entity, float partialTick) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnim = false;
        this.animEntity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
        this.partialTick = partialTick;
		for (int i = 0; i < this.mainModel.childBones.size(); i++) {
			ModelRenderer model = (ModelRenderer) this.mainModel.childBones.get(i);
			model.xRot = 0.0F;
			model.yRot = 0.0F;
			model.zRot = 0.0F;
		}
    }

    public boolean setAnim(int animID) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnim = this.animEntity.getAnimID() == animID;
        return this.correctAnim;
    }

    public void startPhase(int duration) {
        if (this.correctAnim) {
            this.prevTempTick = this.tempTick;
            this.tempTick += duration;
        }
    }

    public void setStationaryPhase(int duration) {
        this.startPhase(duration);
        this.endPhase(true);
    }

    public void resetPhase(int duration) {
        this.startPhase(duration);
        this.endPhase();
    }

    public void rotate(ModelRenderer box, float x, float y, float z) {
        if (this.correctAnim) {
            if (!this.transformMap.containsKey(box)) {
                this.transformMap.put(box, new Transform(x, y, z));
            } else {
                this.transformMap.get(box).addRot(x, y, z);
            }

        }
    }

    public void move(ModelRenderer box, float x, float y, float z) {
        if (this.correctAnim) {
            if (!this.transformMap.containsKey(box)) {
                this.transformMap.put(box, new Transform(x, y, z, 0.0F, 0.0F, 0.0F));
            } else {
                this.transformMap.get(box).addOffset(x, y, z);
            }

        }
    }

    public void endPhase() {
        this.endPhase(false);
    }

    private void endPhase(boolean stationary) {
        if (this.correctAnim) {
            int animTick = this.animEntity.getAnimTick();
            if (animTick >= this.prevTempTick && animTick < this.tempTick) {
                ModelRenderer box;
                Transform transform;
                if (stationary) {
                    for (Iterator var3 = this.prevTransformMap.keySet().iterator(); var3.hasNext(); box.z += transform.offsetZ) {
                        box = (ModelRenderer) var3.next();
                        transform = this.prevTransformMap.get(box);
                        box.xRot += transform.rotX;
                        box.yRot += transform.rotY;
                        box.zRot += transform.rotZ;
                        box.x += transform.offsetX;
                        box.y += transform.offsetY;
                    }
                } else {
                    float tick = ((float) (animTick - this.prevTempTick) + this.partialTick) / (float) (this.tempTick - this.prevTempTick);
                    float inc = MathHelper.sin(tick * 3.1415927F / 2.0F);
                    float dec = 1.0F - inc;

                    Iterator var6;
					/*ModelRenderer box;
					Transform transform;*/
                    for (var6 = this.prevTransformMap.keySet().iterator(); var6.hasNext(); box.z += dec * transform.offsetZ) {
                        box = (ModelRenderer) var6.next();
                        transform = this.prevTransformMap.get(box);
                        box.xRot += dec * transform.rotX;
                        box.yRot += dec * transform.rotY;
                        box.zRot += dec * transform.rotZ;
                        box.x += dec * transform.offsetX;
                        box.y += dec * transform.offsetY;
                    }

                    for (var6 = this.transformMap.keySet().iterator(); var6.hasNext(); box.z += inc * transform.offsetZ) {
                        box = (ModelRenderer) var6.next();
                        transform = this.transformMap.get(box);
                        box.xRot += inc * transform.rotX;
                        box.yRot += inc * transform.rotY;
                        box.zRot += inc * transform.rotZ;
                        box.x += inc * transform.offsetX;
                        box.y += inc * transform.offsetY;
                    }
                }
            }

            if (!stationary) {
                this.prevTransformMap.clear();
                this.prevTransformMap.putAll(this.transformMap);
                this.transformMap.clear();
            }

        }
    }
}
