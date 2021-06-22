package me.flashyreese.mods.sodiumextra.mixin.features.fog;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    @Inject(method = "applyFog", at = @At(value = "RETURN", shift = At.Shift.AFTER), locals = LocalCapture.PRINT, cancellable = true)
    private static void applyFogModifyDensity(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
        //Todo: Fix Fog
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            ci.cancel();
        }
    }
}
