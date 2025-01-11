package net.minecraft.theTitans;

import com.zerwhit.RegistryHelper;
import com.zerwhit.tools.EnumHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.titan.*;
import net.minecraft.entity.titanminion.EntityGiantZombieBetter;
import net.minecraft.entity.titanminion.EntityZombieMinion;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.theTitans.configs.TitanConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.theTitans.world.WorldOreGeneration;

@Mod(TheTitans.modid)
public class TheTitans {
    public static final String modid = "thetitans";
    public static final ResourceLocation genericTitanWhiteTexture32x64;
    public static final ResourceLocation genericTitanWhiteTexture64x64;
    public static final ItemGroup titansTab;
    public static final Rarity godly;
    public static UseAction blocked;
    protected static final String modname = "The Titans Mod";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final RegistryHelper registry;

    static {
        registry = new RegistryHelper();
        genericTitanWhiteTexture32x64 = new ResourceLocation("thetitans", "textures/entities/32x64_disintigration.png");
        genericTitanWhiteTexture64x64 = new ResourceLocation("thetitans", "textures/entities/64x64_disintigration.png");
        titansTab = new ItemGroup("thetitans") {
            public ItemStack makeIcon() {
                return new ItemStack(TitanItems.growthSerum);
            }
        };
        godly = Rarity.create("GODLY", TextFormatting.DARK_PURPLE);
        //blocked = EnumHelper.addEnumAction("BLOCKED");
    }

    public TheTitans() {
        TitanConfig.load();
        registry.register(new RenderTheTitans());
        registry.register(new TitanItems());
        registry.register(new TitanBlocks());
        registry.register(new TitanSounds());
        registry.postInit();
		new CommonProxy();
        Attributes.MAX_HEALTH = RenderTheTitans.MAX_HEALTH;
        Attributes.ATTACK_DAMAGE = RenderTheTitans.ATTACK_DAMAGE;
    }

    public static void log(Object message) {
        System.out.println(message);
    }

    public static void error(Object message) {
        LOGGER.error(message);
    }

    public static void fatal(Throwable message) {
        LOGGER.fatal(message);
        try {
            throw message;
        } catch (Throwable ignored) {
        }
    }
}
