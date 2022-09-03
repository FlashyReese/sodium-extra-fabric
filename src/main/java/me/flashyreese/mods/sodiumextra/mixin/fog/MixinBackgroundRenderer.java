package me.flashyreese.mods.sodiumextra.mixin.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {
    @Inject(method = "applyFog", at = @At(value = "TAIL"))
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        int fogDistance = SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.getOrDefault(camera.getFocusedEntity().world.getRegistryKey().getValue(), 0);
        if (fogDistance == 0) {
            return;
        }
        if (camera.getSubmersionType() == CameraSubmersionType.NONE && (thickFog || fogType == BackgroundRenderer.FogType.FOG_TERRAIN)) {
            if (fogDistance == 33) {
                RenderSystem.setShaderFogStart(Short.MAX_VALUE - 1);
                RenderSystem.setShaderFogEnd(Short.MAX_VALUE);
            } else {
                RenderSystem.setShaderFogStart(fogDistance * 16);
                RenderSystem.setShaderFogEnd((fogDistance + 1) * 16);
            }
        }
    }
}
