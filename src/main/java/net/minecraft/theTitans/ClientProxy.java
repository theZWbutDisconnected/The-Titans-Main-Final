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
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

@Mod.EventBusSubscriber(modid = TheTitans.modid, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy {
    @SubscribeEvent
    public static void renderEventHandler(FMLClientSetupEvent event) {
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

    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent event) {
        Label_UltimaBlade:
        {
            Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
            ModelResourceLocation location = new ModelResourceLocation(Objects.requireNonNull(TitanItems.ultimaBlade.getRegistryName()), "inventory");
            IBakedModel existingModel = modelRegistry.get(location);
			List l = existingModel.getQuads(null, null, null);
			for (BakedQuad q : l) {
				q.getDirection().rotate(new Matrix4f(new Quaternion(0.05f, 0.0f, 0.0f, 1.0f)), Direction.NORTH);
			}
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
