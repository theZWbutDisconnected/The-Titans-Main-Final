/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.RenderType;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRenderCache;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.textures.ITextureAtlasSpriteLoader;

public class MinecraftForgeClient
{
    public static RenderType getRenderLayer()
    {
        return ForgeHooksClient.renderLayer.get();
    }

    /**
     * returns the Locale set by the player in Minecraft.
     * Useful for creating string and number formatters.
     */
    public static Locale getLocale()
    {
        return Minecraft.getInstance().getLanguageManager().getSelected().getJavaLocale();
    }

    private static BitSet stencilBits = new BitSet(8);
    static
    {
        stencilBits.set(0,8);
    }

    /**
     * Reserve a stencil bit for use in rendering
     *
     * Note: you must check the Framebuffer you are working with to
     * determine if stencil bits are enabled on it before use.
     *
     * @return A bit or -1 if no further stencil bits are available
     */
    public static int reserveStencilBit()
    {
        int bit = stencilBits.nextSetBit(0);
        if (bit >= 0)
        {
            stencilBits.clear(bit);
        }
        return bit;
    }

    /**
     * Release the stencil bit for other use
     *
     * @param bit The bit from {@link #reserveStencilBit()}
     */
    public static void releaseStencilBit(int bit)
    {
        if (bit >= 0 && bit < stencilBits.length())
        {
            stencilBits.set(bit);
        }
    }

    private static final LoadingCache<Pair<World, BlockPos>, Optional<ChunkRenderCache>> regionCache = CacheBuilder.newBuilder()
        .maximumSize(500)
        .concurrencyLevel(5)
        .expireAfterAccess(1, TimeUnit.SECONDS)
        .build(new CacheLoader<Pair<World, BlockPos>, Optional<ChunkRenderCache>>()
        {
            @Override
            public Optional<ChunkRenderCache> load(Pair<World, BlockPos> key)
            {
                return Optional.ofNullable(ChunkRenderCache.createIfNotEmpty(key.getLeft(), key.getRight().offset(-1, -1, -1), key.getRight().offset(16, 16, 16), 1));
            }
        });

    public static void onRebuildChunk(World world, BlockPos position, ChunkRenderCache cache)
    {
        if (cache == null)
            regionCache.invalidate(Pair.of(world, position));
        else
            regionCache.put(Pair.of(world, position), Optional.of(cache));
    }

    @Nullable
    public static ChunkRenderCache getRegionRenderCache(World world, BlockPos pos)
    {
        return getRegionRenderCacheOptional(world, pos).orElse(null);
    }

    public static Optional<ChunkRenderCache> getRegionRenderCacheOptional(World world, BlockPos pos)
    {
        int x = pos.getX() & ~0xF;
        int y = pos.getY() & ~0xF;
        int z = pos.getZ() & ~0xF;
        return regionCache.getUnchecked(Pair.of(world, new BlockPos(x, y, z)));
    }

    public static void clearRenderCache()
    {
        regionCache.invalidateAll();
        regionCache.cleanUp();
    }

    private static HashMap<ResourceLocation, Supplier<NativeImage>> bufferedImageSuppliers = new HashMap<ResourceLocation, Supplier<NativeImage>>();
    public static void registerImageLayerSupplier(ResourceLocation resourceLocation, Supplier<NativeImage> supplier)
    {
        bufferedImageSuppliers.put(resourceLocation, supplier);
    }

    @Nonnull
    public static NativeImage getImageLayer(ResourceLocation resourceLocation, IResourceManager resourceManager) throws IOException
    {
        Supplier<NativeImage> supplier = bufferedImageSuppliers.get(resourceLocation);
        if (supplier != null)
            return supplier.get();

        IResource iresource1 = resourceManager.getResource(resourceLocation);
        return NativeImage.read(iresource1.getInputStream());
    }

    private static final Map<ResourceLocation, ITextureAtlasSpriteLoader> textureAtlasSpriteLoaders = new ConcurrentHashMap<>();

    /**
     * Register a custom ITextureAtlasSprite loader. Call this method during {@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}.
     */
    public static void registerTextureAtlasSpriteLoader(ResourceLocation name, ITextureAtlasSpriteLoader loader)
    {
        textureAtlasSpriteLoaders.put(name, loader);
    }

    @Nullable
    public static ITextureAtlasSpriteLoader getTextureAtlasSpriteLoader(ResourceLocation name)
    {
        return textureAtlasSpriteLoaders.get(name);
    }

}
