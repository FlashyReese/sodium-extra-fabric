package me.flashyreese.mods.sodiumextra.mixin.fog_falloff;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    @ModifyArg(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), index = 0)
    private static float modifySetShaderFogStart(float original) {
        float fogStart = (float) SodiumExtraClientMod.options().renderSettings.fogStart / 100;
        return original * fogStart;
    }
}
