package me.flashyreese.mods.sodiumextra.mixin.sodium.biome_blend;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.render.terrain.color.LinearFlatColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.FlatColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.LinearColorBlender;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlockRenderCache.class, remap = false)
public class MixinChunkRenderCache {
    /**
     * @author FlashyReese
     * @reason Includes linear flat color blender
     */
    @Overwrite
    protected static ColorBlender createBiomeColorBlender() {
        return MinecraftClient.getInstance().options.getBiomeBlendRadius().getValue() <= 0 ? new FlatColorBlender() : SodiumExtraClientMod.options().renderSettings.useLinearFlatColorBlender ? new LinearFlatColorBlender() : new LinearColorBlender();
    }
}
