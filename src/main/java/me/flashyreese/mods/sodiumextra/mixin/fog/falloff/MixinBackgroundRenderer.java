package me.flashyreese.mods.sodiumextra.mixin.fog.falloff;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    @Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"))
    private static void redirectSetShaderFogStart(float shaderFogStart) {
        float fogStart = (float) SodiumExtraClientMod.options().renderSettings.fogStart / 100;
        RenderSystem.setShaderFogStart(shaderFogStart * fogStart);
    }
}
