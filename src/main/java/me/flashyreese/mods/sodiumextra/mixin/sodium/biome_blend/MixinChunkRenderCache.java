package me.flashyreese.mods.sodiumextra.mixin.sodium.biome_blend;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.render.terrain.color.LinearFlatColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.BiomeColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.FlatBiomeColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.SmoothBiomeColorBlender;
import me.jellysquid.mods.sodium.client.render.pipeline.ChunkRenderCache;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ChunkRenderCache.class, remap = false)
public class MixinChunkRenderCache {
    /**
     * @author FlashyReese
     * @reason Includes linear flat color blender
     */
    @Overwrite
    protected BiomeColorBlender createBiomeColorBlender() {
        return MinecraftClient.getInstance().options.biomeBlendRadius <= 0 ? new FlatBiomeColorBlender() : SodiumExtraClientMod.options().renderSettings.useLinearFlatColorBlender ? new LinearFlatColorBlender() : new SmoothBiomeColorBlender();
    }
}
