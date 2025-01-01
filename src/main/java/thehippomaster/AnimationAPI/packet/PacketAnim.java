//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package thehippomaster.AnimationAPI.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

import java.util.function.Supplier;

public class PacketAnim {
    private byte animID;
    private int entityID;

    public PacketAnim() {
    }

    public PacketAnim(byte anim, int entity) {
        this.animID = anim;
        this.entityID = entity;
    }

    public static void write(PacketAnim anim, ByteBuf buffer) {
        buffer.writeByte(anim.animID);
        buffer.writeInt(anim.entityID);
    }

    public static PacketAnim read(ByteBuf buffer) {
        return new PacketAnim(buffer.readByte(), buffer.readInt());
    }

    public static class Handler {
        public Handler() {
        }

        public static void onMessage(PacketAnim packet, Supplier<NetworkEvent.Context> ctx) {
            World world = AnimationAPI.proxy.getWorldClient();
            IAnimatedEntity entity = (IAnimatedEntity) world.getEntity(packet.entityID);
            if (entity != null && packet.animID != -1) {
                entity.setAnimID(packet.animID);
                if (packet.animID == 0) {
                    entity.setAnimTick(0);
                }
            }

            ctx.get().setPacketHandled(true);
        }
    }
}
