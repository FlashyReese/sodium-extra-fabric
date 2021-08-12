package me.flashyreese.mods.sodiumextra.mixin.features.static_fov;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "getFov", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;lastMovementFovMultiplier:F"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void preventFovChange(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir, double currentFov) {
        if (SodiumExtraClientMod.options().extraSettings.staticFov) {
            if (camera.getFocusedEntity() instanceof PlayerEntity && !((PlayerEntity)camera.getFocusedEntity()).isUsingSpyglass()){
                cir.setReturnValue(currentFov);
            }
        }
    }
}
