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
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraft.entity.titan.EntityTitanSpirit;
import net.minecraft.entity.titan.EntitySlimeTitan;
import net.minecraft.entity.titan.EntityZombieTitan;
import net.minecraft.entity.titan.EntitySkeletonTitan;
import net.minecraft.entity.titan.EntityGhastTitan;
import net.minecraft.entity.titan.EntityIronGolemTitan;
import net.minecraft.entity.titan.EntityWitherzilla;
import net.minecraft.entity.titanminion.EntityGiantZombieBetter;
import net.minecraft.entity.titanminion.EntityZombieMinion;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;

@Mod.EventBusSubscriber(modid = TheTitans.modid, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CommonProxy
{
	public static ClientProxy client;
	
	public CommonProxy() {
		client = new ClientProxy();
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::attributeRegistry);
	}
	
    @SubscribeEvent
    public static void onSetup(FMLClientSetupEvent event) {
		client.renderEventHandler(event);
	}
	
    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent event) {
		client.bakedModel(event);
    }

    @SubscribeEvent
    public void onBiomeLoading(BiomeLoadingEvent event) {
		client.biomeGenerate(event);
    }
	
    public void renderEventHandler(FMLClientSetupEvent event) {}
	
    public void bakedModel(ModelBakeEvent event) {}
	
	public void biomeGenerate(BiomeLoadingEvent event) {
		new WorldOreGeneration(event).biomeGenerate();
	}
	
    public void attributeRegistry(EntityAttributeCreationEvent event) {
        event.put(RenderTheTitans.titanSpirit, EntityTitanSpirit.applyEntityAttributes().build());
        event.put(RenderTheTitans.slimeTitan, EntitySlimeTitan.applyEntityAttributes().build());
        event.put(RenderTheTitans.zombieTitan, EntityZombieTitan.applyEntityAttributes().build());
        event.put(RenderTheTitans.skeletonTitan, EntitySkeletonTitan.applyEntityAttributes().build());
        event.put(RenderTheTitans.witherSkeletonTitan, EntitySkeletonTitan.applyEntityAttributes().build());
        event.put(RenderTheTitans.ghastTitan, EntityGhastTitan.applyEntityAttributes().build());
        event.put(RenderTheTitans.ironGolemTitan, EntityIronGolemTitan.applyEntityAttributes().build());
        event.put(RenderTheTitans.witherzilla, EntityWitherzilla.applyEntityAttributes().build());
        event.put(RenderTheTitans.giantZombie, EntityGiantZombieBetter.applyEntityAttributes().build());
        event.put(RenderTheTitans.zombieMinion, EntityZombieMinion.applyEntityAttributes().build());
    }
}
