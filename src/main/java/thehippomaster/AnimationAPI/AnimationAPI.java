//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thehippomaster.AnimationAPI;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import thehippomaster.AnimationAPI.client.ClientProxy;
import thehippomaster.AnimationAPI.packet.PacketAnim;

import java.util.Iterator;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("titansanimationapi")
public class AnimationAPI {
    public static final String[] fTimer = new String[]{"field_71428_T", "S", "timer"};
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel wrapper;
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public AnimationAPI() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean isEffectiveClient() {
        return EffectiveSide.get().isClient();
    }

    public static void sendAnimPacket(IAnimatedEntity entity, int animID) {
        if (!isEffectiveClient()) {
            entity.setAnimID(animID);
            sendToAll(new PacketAnim((byte) animID, ((Entity) entity).getId()));
        }
    }

    public static <MSG> void sendToAll(MSG message) {
        Iterator var1 = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().iterator();

        while (var1.hasNext()) {
            ServerPlayerEntity player = (ServerPlayerEntity) var1.next();
            wrapper.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        }

    }

    public void init(FMLCommonSetupEvent evt) {
        wrapper = NetworkRegistry.newSimpleChannel(new ResourceLocation("titansanimationapi"), () -> {
            return "1";
        }, "1"::equals, "1"::equals);
        wrapper.registerMessage(0, PacketAnim.class, PacketAnim::write, PacketAnim::read, PacketAnim.Handler::onMessage);
    }

    public void postInit(FMLLoadCompleteEvent event) {

    }
}
