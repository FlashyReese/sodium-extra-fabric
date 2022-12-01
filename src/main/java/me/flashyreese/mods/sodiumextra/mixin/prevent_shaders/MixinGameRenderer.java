package me.flashyreese.mods.sodiumextra.mixin.prevent_shaders;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "togglePostProcessorEnabled", at = @At("HEAD"), cancellable = true)
    private void preventShaders(CallbackInfo ci) {
        if (SodiumExtraClientMod.options().extraSettings.preventShaders) {
            ci.cancel();
        }
    }

    @Inject(method = "loadPostProcessor", at = @At("HEAD"), cancellable = true)
    private void dontLoadShader(Identifier identifier, CallbackInfo ci) {
        if (SodiumExtraClientMod.options().extraSettings.preventShaders) {
            ci.cancel();
        }
    }
}
