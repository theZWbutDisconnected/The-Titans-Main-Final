package net.minecraft.theTitans;

import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.GiantZombieRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.theTitans.models.ModelOptimaAxe;
import net.minecraft.theTitans.models.ModelUltimaBlade;
import net.minecraft.theTitans.render.*;
import net.minecraft.theTitans.render.minions.RenderZombieMinion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import java.util.List;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraft.theTitans.world.WorldOreGeneration;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TheTitans.modid, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CommonProxy
{
    @SubscribeEvent
    public static void onSetup(FMLClientSetupEvent event) {
		new ClientProxy().renderEventHandler(event);
	}
	
    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent event) {
		new ClientProxy().bakedModel(event);
    }
	
    @SubscribeEvent
    public static void onBiomeGenerate(BiomeLoadingEvent event) {
		new CommonProxy().biomeGenerate(event);
	}
	
    public void renderEventHandler(FMLClientSetupEvent event) {}
    public void bakedModel(ModelBakeEvent event) {}
	public void biomeGenerate(BiomeLoadingEvent event) {
		new WorldOreGeneration(event).biomeGenerate();
	}
}
