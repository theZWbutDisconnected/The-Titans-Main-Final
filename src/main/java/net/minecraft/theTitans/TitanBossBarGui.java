package net.minecraft.theTitans;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.titan.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TitanBossBarGui extends AbstractGui {
    private static ResourceLocation texture;

    static {
        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/witherzilla.png");
    }

    private final Minecraft mc;
    private MatrixStack matrixStack;

    public TitanBossBarGui(final Minecraft mc) {
        this.mc = mc;
    }

    @SubscribeEvent
    public void onRenderOverlay(final RenderGameOverlayEvent event) {
        this.matrixStack = event.getMatrixStack();
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
            return;
        }
        final int u = 0;
        final int v = 0;
        String outstring = null;
        int color = 16382457;
        final FontRenderer fr = this.mc.font;
        int barWidth = 182;
        int namey = 10;
        int barHeight = 5;
        int y = 0;
        float fade = 1.0f;
        float gfHealth = 0.0f;
        boolean flag = true;
        Entity entity = null;
        PlayerEntity player = null;
        if (Minecraft.getInstance().options.hideGui) {
            return;
        }
        player = this.mc.player;
        if (player == null) {
            return;
        }
        final List list = player.level.getLoadedEntitiesOfClass(EntityTitan.class, this.mc.player.getBoundingBox().inflate(2000));
        if (list != null && !list.isEmpty()) {
            for (int j = 0; j < list.size(); ++j) {
                entity = (Entity) list.get(j);
                if (entity != null && !entity.removed && entity instanceof EntityTitan e) {
                    outstring = e.getName().getString();
                    gfHealth = e.getHealth() / e.getMaxHealth();
                    flag = (!e.getWaiting() && !e.isInvisible());
                    if (!e.isAlive()) {
                        fade = 0.25f;
                    } else {
                        fade = 1.0f;
                    }
                    if (e instanceof EntitySnowGolemTitan) {
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/snow_golem_titan.png");
                        barWidth = 185;
                        barHeight = 27;
                        color = 12369084;
                        y = 0;
                        namey = y + 20;
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntitySlimeTitan) {
                        if (e instanceof EntityMagmaCubeTitan) {
                            barWidth = 193;
                            barHeight = 19;
                            y = 17;
                            namey = y + 12;
                            color = 16579584;
                            TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/magma_cube_titan.png");
                        } else {
                            barWidth = 189;
                            barHeight = 22;
                            y = 14;
                            namey = y + 9;
                            color = 5349438;
                            TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/slime_titan.png");
                        }
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntityZombieTitan) {
                        barWidth = 185;
                        barHeight = 22;
                        color = 7969893;
                        y = 40;
                        namey = y + 17;
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/zombie_titan.png");
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntitySkeletonTitan && ((EntitySkeletonTitan) e).getSkeletonType() != 1) {
                        barWidth = 185;
                        barHeight = 24;
                        color = 12698049;
                        y = 31;
                        namey = y + 17;
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/skeleton_titan.png");
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntitySkeletonTitan && ((EntitySkeletonTitan) e).getSkeletonType() == 1) {
                        barWidth = 185;
                        barHeight = 24;
                        color = 4802889;
                        y = 48;
                        namey = y + 17;
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/wither_skeleton_titan.png");
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntityGhastTitan) {
                        barWidth = 185;
                        barHeight = 32;
                        color = 12369084;
                        y = 46;
                        namey = y + 43;
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/ghast_titan.png");
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntityIronGolemTitan) {
                        barWidth = 191;
                        barHeight = 26;
                        color = 7237230;
                        y = 55;
                        namey = y + 40;
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/ultima_iron_golem_titan.png");
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntityGargoyleTitan) {
                        barWidth = 191;
                        barHeight = 26;
                        color = 7829367;
                        y = 71;
                        namey = y + 44;
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/gargoyle_king.png");
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, 16382457);
                        }
                    }
                    if (e instanceof EntityWitherzilla) {
                        barWidth = 185;
                        barHeight = 32;
                        color = 15728880 - (int) (MathHelper.cos(e.tickCount * 0.05f) * 1.572888E7f);
                        y = 46;
                        namey = y + 61;
                        fade = 1.0f + MathHelper.cos(e.tickCount * 0.2f) * 0.3f;
                        TitanBossBarGui.texture = new ResourceLocation("thetitans", "textures/entities/titans/bossbars/witherzilla.png");
                        final int width = event.getWindow().getGuiScaledWidth();
                        final int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        final int x = width / 2 - barWidth / 2;
                        if (flag) {
                            fr.drawShadow(this.matrixStack, (int) e.getHealth() + "/" + (int) e.getMaxHealth(), width / 2 - fr.width((int) e.getHealth() + "/" + (int) e.getMaxHealth()) / 2, namey + 10, color);
                        }
                    }
                    if (outstring == null) {
                        return;
                    }
                    if (flag) {
                        final Minecraft minecraft = Minecraft.getInstance();
                        final int width = minecraft.getWindow().getGuiScaledWidth();
                        int barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        int x = width / 2 - barWidth / 2;
                        this.mc.getTextureManager().bind(TitanBossBarGui.texture);
                        RenderSystem.color4f(fade, fade, fade, 1.0f);
                        if (e.getTitanStatus() == EnumTitanStatus.GREATER) {
                            RenderSystem.scalef(1.25f, 1.25f, 1.25f);
                            x = width / 3 - barWidth / 3 - 6;
                            barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        }
                        if (e.getTitanStatus() == EnumTitanStatus.GOD) {
                            RenderSystem.scalef(1.5f, 1.5f, 1.5f);
                            x = width / 4 - barWidth / 4 - 12;
                            barWidthFilled = (int) (gfHealth * (barWidth + 1));
                        }
                        GL11.glEnable(2977);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        this.blit(this.matrixStack, x, y, u, v, barWidth, barHeight);
                        if (barWidthFilled > 0) {
                            this.blit(this.matrixStack, x, y, u, v + barHeight, barWidthFilled, barHeight);
                        }
                        GL11.glDisable(3042);
                        if (e.getTitanStatus() == EnumTitanStatus.GREATER) {
                            RenderSystem.scalef(0.8f, 0.8f, 0.8f);
                        }
                        if (e.getTitanStatus() == EnumTitanStatus.GOD) {
                            RenderSystem.scalef(0.6666667f, 0.6666667f, 0.6666667f);
                        }
                        fr.drawShadow(this.matrixStack, outstring, (float) width / 2 - (float) fr.width(outstring) / 2, namey, color);
                    }
                }
            }
        }
    }
}
