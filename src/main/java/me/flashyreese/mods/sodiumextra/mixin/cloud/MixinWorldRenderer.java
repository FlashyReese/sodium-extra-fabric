package me.flashyreese.mods.sodiumextra.mixin.cloud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Redirect(
            method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;FDDD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/SkyProperties;getCloudsHeight()F")
    )
    private float getCloudHeight(SkyProperties skyProperties) {
        if (skyProperties.getSkyType() == SkyProperties.SkyType.NORMAL) {
            return SodiumExtraClientMod.options().extraSettings.cloudHeight;
        }
        return skyProperties.getCloudsHeight();
    }
}
