package me.flashyreese.mods.sodiumextra.mixin.optimizations.beacon_beam_rendering;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import me.flashyreese.mods.sodiumextra.compat.IrisCompat;
import me.flashyreese.mods.sodiumextra.compat.ModelVertexToTerrainSerializer;
import me.jellysquid.mods.sodium.client.render.vertex.VertexFormatDescription;
import me.jellysquid.mods.sodium.client.render.vertex.VertexFormatRegistry;
import me.jellysquid.mods.sodium.client.render.vertex.serializers.VertexSerializer;
import me.jellysquid.mods.sodium.client.render.vertex.serializers.VertexSerializerCache;
import net.minecraft.client.render.VertexFormats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VertexSerializerCache.class, remap = false)
public class MixinVertexSerializerCache {
    @Shadow
    @Final
    private static Long2ReferenceMap<VertexSerializer> CACHE;

    @Shadow
    private static long getSerializerKey(VertexFormatDescription a, VertexFormatDescription b) {
        return 0;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void putSerializerIris(CallbackInfo ci) {
        if (IrisCompat.isIrisPresent()) {
            CACHE.put(getSerializerKey(VertexFormatRegistry.get(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), VertexFormatRegistry.get(IrisCompat.getTerrainFormat())), new ModelVertexToTerrainSerializer());
        }
    }
}
