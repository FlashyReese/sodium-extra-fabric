package me.flashyreese.mods.sodiumextra.mixin.optimizations.beacon_beam_rendering;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import me.flashyreese.mods.sodiumextra.compat.IrisCompat;
import me.flashyreese.mods.sodiumextra.compat.ModelVertexToTerrainSerializer;
import me.jellysquid.mods.sodium.client.render.vertex.serializers.VertexSerializerRegistryImpl;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatRegistry;
import net.caffeinemc.mods.sodium.api.vertex.serializer.VertexSerializer;
import net.minecraft.client.render.VertexFormats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VertexSerializerRegistryImpl.class, remap = false)
public class MixinVertexSerializerRegistryImpl {

    @Shadow
    @Final
    private Long2ReferenceMap<VertexSerializer> cache;

    @Shadow
    private static long createKey(VertexFormatDescription a, VertexFormatDescription b) {
        return 0;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void putSerializerIris(CallbackInfo ci) {
        if (IrisCompat.isIrisPresent()) {
            this.cache.put(createKey(VertexFormatRegistry.instance().get(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), VertexFormatRegistry.instance().get(IrisCompat.getTerrainFormat())), new ModelVertexToTerrainSerializer());
        }
    }
}
