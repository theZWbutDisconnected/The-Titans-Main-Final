//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thehippomaster.AnimationAPI.packet;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3f;

public class Transform {
    private final Vector3f rotation = new Vector3f();
    private final Vector3f offset = new Vector3f();

    public Transform() {
    }

    public Vector3f getRotation() {
        return this.rotation;
    }

    public Vector3f getOffset() {
        return this.offset;
    }

    public void rotate(ModelRenderer renderer, float multiplier) {
        this.rotation.mul(multiplier);
        renderer.xRot += this.rotation.x();
        renderer.yRot += this.rotation.y();
        renderer.zRot += this.rotation.z();
    }

    public void offset(ModelRenderer renderer, float multiplier) {
        this.offset.mul(multiplier);
        renderer.x += this.offset.x();
        renderer.y += this.offset.y();
        renderer.z += this.offset.z();
    }
}
