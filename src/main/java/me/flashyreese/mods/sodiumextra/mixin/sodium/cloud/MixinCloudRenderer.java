package me.flashyreese.mods.sodiumextra.mixin.sodium.cloud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CloudRenderer.class)
public class MixinCloudRenderer {
    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/option/GameOptions;getClampedViewDistance()I"), ordinal = 0)
    public int modifyCloudRenderDistance(int original) {
        return original * SodiumExtraClientMod.options().extraSettings.cloudDistance / 100;
    }
}