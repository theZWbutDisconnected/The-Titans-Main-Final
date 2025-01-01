package net.minecraft.theTitans.render.minions;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.titanminion.EntityZombieMinion;
import net.minecraft.util.ResourceLocation;

public class RenderZombieMinion extends BipedRenderer<EntityZombieMinion, ZombieModel<EntityZombieMinion>> {
    private static final ResourceLocation zombieTemplarTextures;
    private static final ResourceLocation zombieVillagerTemplarTextures;
    private static final ResourceLocation zombieBishopTextures;
    private static final ResourceLocation zombieVillagerBishopTextures;
    private static final ResourceLocation zombieZealotTextures;
    private static final ResourceLocation zombieVillagerZealotTextures;
    private static final ResourceLocation zombiePriestTextures;
    private static final ResourceLocation zombieVillagerPriestTextures;
    private static final ResourceLocation zombieTextures;
    private static final ResourceLocation zombieVillagerTextures;

    static {
        zombieTemplarTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_templar.png");
        zombieVillagerTemplarTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_villager_templar.png");
        zombieBishopTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_bishop.png");
        zombieVillagerBishopTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_villager_bishop.png");
        zombieZealotTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_zealot.png");
        zombieVillagerZealotTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_villager_zealot.png");
        zombiePriestTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_priest.png");
        zombieVillagerPriestTextures = new ResourceLocation("thetitans", "textures/entities/minions/zombies/zombie_villager_priest.png");
        zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
        zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    }

    public RenderZombieMinion(EntityRendererManager p_i46168_1_) {
        super(p_i46168_1_, new ZombieModel<>(0.0F, false), 0.5f);
        this.addLayer(new BipedArmorLayer<>(this, new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true)));
    }

    @Override
    public boolean shouldRender(EntityZombieMinion p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return true;
    }

    public ResourceLocation getTextureLocation(final EntityZombieMinion p_110775_1_) {
        switch (p_110775_1_.getMinionTypeInt()) {
            case 1 -> {
                return p_110775_1_.isVillager() ? RenderZombieMinion.zombieVillagerPriestTextures : RenderZombieMinion.zombiePriestTextures;
            }
            case 2 -> {
                return p_110775_1_.isVillager() ? RenderZombieMinion.zombieVillagerZealotTextures : RenderZombieMinion.zombieZealotTextures;
            }
            case 3 -> {
                return p_110775_1_.isVillager() ? RenderZombieMinion.zombieVillagerBishopTextures : RenderZombieMinion.zombieBishopTextures;
            }
            case 4 -> {
                return p_110775_1_.isVillager() ? RenderZombieMinion.zombieVillagerTemplarTextures : RenderZombieMinion.zombieTemplarTextures;
            }
            default -> {
                return p_110775_1_.isVillager() ? RenderZombieMinion.zombieVillagerTextures : RenderZombieMinion.zombieTextures;
            }
        }
    }
}
