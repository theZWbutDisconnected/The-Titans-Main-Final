package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class LanServerPingThread extends Thread {
   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
   private static final Logger LOGGER = LogManager.getLogger();
   private final String motd;
   private final DatagramSocket socket;
   private boolean isRunning = true;
   private final String serverAddress;

   public LanServerPingThread(String p_i1321_1_, String p_i1321_2_) throws IOException {
      super("LanServerPinger #" + UNIQUE_THREAD_ID.incrementAndGet());
      this.motd = p_i1321_1_;
      this.serverAddress = p_i1321_2_;
      this.setDaemon(true);
      this.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
      this.socket = new DatagramSocket();
   }

   public void run() {
      String s = createPingString(this.motd, this.serverAddress);
      byte[] abyte = s.getBytes(StandardCharsets.UTF_8);

      while(!this.isInterrupted() && this.isRunning) {
         try {
            InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
            DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
            this.socket.send(datagrampacket);
         } catch (IOException ioexception) {
            LOGGER.warn("LanServerPinger: {}", (Object)ioexception.getMessage());
            break;
         }

         try {
            sleep(1500L);
         } catch (InterruptedException interruptedexception) {
         }
      }

   }

   public void interrupt() {
      super.interrupt();
      this.isRunning = false;
   }

   public static String createPingString(String p_77525_0_, String p_77525_1_) {
      return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
   }

   public static String parseMotd(String p_77524_0_) {
      int i = p_77524_0_.indexOf("[MOTD]");
      if (i < 0) {
         return "missing no";
      } else {
         int j = p_77524_0_.indexOf("[/MOTD]", i + "[MOTD]".length());
         return j < i ? "missing no" : p_77524_0_.substring(i + "[MOTD]".length(), j);
      }
   }

   public static String parseAddress(String p_77523_0_) {
      int i = p_77523_0_.indexOf("[/MOTD]");
      if (i < 0) {
         return null;
      } else {
         int j = p_77523_0_.indexOf("[/MOTD]", i + "[/MOTD]".length());
         if (j >= 0) {
            return null;
         } else {
            int k = p_77523_0_.indexOf("[AD]", i + "[/MOTD]".length());
            if (k < 0) {
               return null;
            } else {
               int l = p_77523_0_.indexOf("[/AD]", k + "[AD]".length());
               return l < k ? null : p_77523_0_.substring(k + "[AD]".length(), l);
            }
         }
      }
   }
}