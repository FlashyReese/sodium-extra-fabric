package me.flashyreese.mods.sodiumextra.mixin.features.cloud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Redirect(
            method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FDDD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DimensionEffects;getCloudsHeight()F")
    )
    private float getCloudHeight(DimensionEffects skyProperties) {
        if (skyProperties.getSkyType() == DimensionEffects.SkyType.NORMAL) {
            return SodiumExtraClientMod.options().extraSettings.cloudHeight;
        }
        return skyProperties.getCloudsHeight();
    }
}
