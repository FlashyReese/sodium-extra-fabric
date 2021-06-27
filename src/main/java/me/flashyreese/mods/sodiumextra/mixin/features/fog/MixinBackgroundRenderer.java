package me.flashyreese.mods.sodiumextra.mixin.features.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    @Inject(method = "applyFog", at = @At(value = "HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info) {
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            if (!SodiumExtraClientMod.options().renderSettings.fog) {
                // Terrible hack, also breaks fog occlusion culling
                RenderSystem.setShaderFogStart(viewDistance * viewDistance);
                RenderSystem.setShaderFogEnd(viewDistance * viewDistance + 1);
                info.cancel();
            }
        }
    }
}
