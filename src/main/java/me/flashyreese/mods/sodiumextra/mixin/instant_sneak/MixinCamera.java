package me.flashyreese.mods.sodiumextra.mixin.instant_sneak;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class MixinCamera {

    @Shadow
    private float cameraY;

    @Shadow
    private Entity focusedEntity;

    @Inject(at = @At("HEAD"), method = "updateEyeHeight")
    public void noLerp(CallbackInfo ci) {
        if (SodiumExtraClientMod.options().extraSettings.instantSneak && this.focusedEntity != null) {
            this.cameraY = this.focusedEntity.getStandingEyeHeight();
        }
    }
}