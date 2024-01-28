package me.flashyreese.mods.sodiumextra.mixin.sodium.fog;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OcclusionCuller.class, remap = false)
public abstract class MixinOcclusionCuller {
    @Shadow
    private static int nearestToZero(int min, int max) {
        return 0;
    }

    @Inject(method = "isWithinRenderDistance", at = @At(value = "HEAD"), cancellable = true)
    private static void isWithinRenderDistance(CameraTransform camera, RenderSection section, float maxDistance, CallbackInfoReturnable<Boolean> cir) {
        int fogDistance = SodiumExtraClientMod.options().renderSettings.multiDimensionFogControl ?
                SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.getOrDefault(MinecraftClient.getInstance().world.getDimension().effects(), 0) :
                SodiumExtraClientMod.options().renderSettings.fogDistance;
        if (fogDistance == 33) {
            int ox = section.getOriginX() - camera.intX;
            int oz = section.getOriginZ() - camera.intZ;
            float dx = nearestToZero(ox, ox + 16) - camera.fracX;
            float dz = nearestToZero(oz, oz + 16) - camera.fracZ;
            boolean result = ((dx * dx) + (dz * dz)) < (maxDistance * maxDistance);
            cir.setReturnValue(result);
        }
    }
}
