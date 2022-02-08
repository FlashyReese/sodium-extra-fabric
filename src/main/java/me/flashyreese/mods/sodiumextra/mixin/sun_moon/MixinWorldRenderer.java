package me.flashyreese.mods.sodiumextra.mixin.sun_moon;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/SkyProperties;getFogColorOverride(FF)[F"
            )
    )
    public float[] redirectGetFogColorOverride(SkyProperties instance, float skyAngle, float tickDelta) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            return instance.getFogColorOverride(skyAngle, tickDelta);
        } else {
            return null;
        }
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V",
                    ordinal = 0
            )
    )
    public void redirectSunTextureBind(TextureManager instance, Identifier id) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            instance.bindTexture(id);
        } else {
            instance.bindTexture(new Identifier("sodium-extra","textures/transparent.png")); // Hack :)
        }
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V",
                    ordinal = 1
            )
    )
    public void redirectMoonPhasesTextureBind(TextureManager instance, Identifier id) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            instance.bindTexture(id);
        } else {
            instance.bindTexture(new Identifier("sodium-extra","textures/transparent.png")); // Hack :)
        }
    }
}
