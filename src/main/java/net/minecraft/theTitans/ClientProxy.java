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
import net.minecraftforge.api.distmarker.OnlyIn;
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
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
	public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(new TitanBossBarGui(Minecraft.getInstance()));
	}
	
    public void renderEventHandler(FMLClientSetupEvent event) {
		EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.growthSerum, (m) -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.titanFireball, RenderTitanFireball::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.protoBall, RenderProtoBall::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.titanFallingBlock, FallingBlockRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.gammaLightingBolt, RenderGammaLightning::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.titanSpirit, RenderTitanSpirit::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.slimeTitan, RenderSlimeTitan::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.zombieTitan, RenderZombieTitan::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.skeletonTitan, RenderSkeletonTitan::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.witherSkeletonTitan, RenderSkeletonTitan::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.ghastTitan, RenderGhastTitan::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.ironGolemTitan, RenderUltimaIronGolemTitan::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.witherzilla, RenderWitherzilla::new);
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.giantZombie, (m) -> new GiantZombieRenderer(m, 6.0F));
        RenderingRegistry.registerEntityRenderingHandler(RenderTheTitans.zombieMinion, RenderZombieMinion::new);
    }

    public void bakedModel(ModelBakeEvent event) {
        Label_UltimaBlade:
        {
            Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
            ModelResourceLocation location = new ModelResourceLocation(Objects.requireNonNull(TitanItems.ultimaBlade.getRegistryName()), "inventory");
            IBakedModel existingModel = modelRegistry.get(location);
            ModelUltimaBlade modelBlade = new ModelUltimaBlade(existingModel);
            event.getModelRegistry().put(location, modelBlade);
            break Label_UltimaBlade;
        }

        Label_OptimaAxe:
        {
            Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
            ModelResourceLocation location = new ModelResourceLocation(Objects.requireNonNull(TitanItems.optimaAxe.getRegistryName()), "inventory");
            IBakedModel existingModel = modelRegistry.get(location);
            ModelOptimaAxe modelAxe = new ModelOptimaAxe(existingModel);
            event.getModelRegistry().put(location, modelAxe);
            break Label_OptimaAxe;
        }
    }
}
