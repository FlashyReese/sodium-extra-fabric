package me.flashyreese.mods.sodiumextra.mixin.fog;

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

    @Inject(method = "applyFog", at = @At(value = "TAIL"))
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
        if (SodiumExtraClientMod.options().renderSettings.fogDistance == 0) {
            return;
        }
        if (thickFog || fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            float fogStart = 0;
            float fogEnd = 0;
            if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                // Terrible hack to disable the fog, also breaks fog occlusion culling
                // Todo: Per dimension fog toggles and sliders perhaps
                fogStart = Short.MAX_VALUE - 1;
                fogEnd = Short.MAX_VALUE;
            } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
                fogStart = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                fogEnd = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
            }
            RenderSystem.setShaderFogStart(fogStart);
            RenderSystem.setShaderFogEnd(fogEnd);
        }
    }
}
