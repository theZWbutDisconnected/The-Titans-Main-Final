//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thehippomaster.AnimationAPI.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.CommonProxy;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
    private Timer mcTimer;

    public ClientProxy() {
    }

    public void initTimer() {
        this.mcTimer = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getInstance(), AnimationAPI.fTimer.toString());
    }

    public float getPartialTick() {
        return this.mcTimer.partialTick;
    }

    public World getWorldClient() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.level;
    }
}
