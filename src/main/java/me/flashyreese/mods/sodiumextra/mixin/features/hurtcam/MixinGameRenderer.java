package me.flashyreese.mods.sodiumextra.mixin.features.hurtcam;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method="bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void cancelHurtCam(MatrixStack ms, float f, CallbackInfo ci) {
        if(!SodiumExtraClientMod.options().extraSettings.hurtCam) {
            ci.cancel();
        }
    }
}