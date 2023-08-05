package me.flashyreese.mods.sodiumextra.mixin.sodium.fog;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderSectionManager.class, remap = false)
public class MixinRenderSectionManager {

    @Shadow
    @Final
    private ClientWorld world;

    @Shadow private int centerChunkX;

    @Shadow private int centerChunkZ;

    @Inject(method = "getDistanceFromCamera", at = @At(value = "HEAD"), cancellable = true)
    public void sodiumExtra$getDistanceFromCamera(RenderSection section, CallbackInfoReturnable<Integer> cir) {
        int fogDistance = SodiumExtraClientMod.options().renderSettings.multiDimensionFogControl ?
                SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.getOrDefault(this.world.getDimension().effects(), 0) :
                SodiumExtraClientMod.options().renderSettings.fogDistance;
        if (fogDistance == 33) {
            int x = Math.abs(section.getChunkX() - this.centerChunkX);
            int z = Math.abs(section.getChunkZ() - this.centerChunkZ);
            cir.setReturnValue(Math.max(x, z));
        }
    }
}
