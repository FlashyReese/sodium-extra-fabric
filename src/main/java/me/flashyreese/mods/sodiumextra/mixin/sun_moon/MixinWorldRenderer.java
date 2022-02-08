package me.flashyreese.mods.sodiumextra.mixin.sun_moon;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/DimensionEffects;getFogColorOverride(FF)[F"
            )
    )
    public float[] redirectGetFogColorOverride(DimensionEffects instance, float skyAngle, float tickDelta) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            return instance.getFogColorOverride(skyAngle, tickDelta);
        } else {
            return null;
        }
    }

    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
                    ordinal = 0
            )
    )
    public void preDrawSunPhases(int i, Identifier identifier) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            RenderSystem.setShaderTexture(i, identifier);
        } else {
            RenderSystem.setShaderTexture(i, new Identifier("sodium-extra","textures/transparent.png")); // Hack :)
        }
    }

    @Redirect(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
                    ordinal = 1
            )
    )
    public void preDrawMoonPhases(int i, Identifier identifier) {
        if (SodiumExtraClientMod.options().detailSettings.sunMoon) {
            RenderSystem.setShaderTexture(i, identifier);
        } else {
            RenderSystem.setShaderTexture(i, new Identifier("sodium-extra","textures/transparent.png")); // Hack :)
        }
    }
}
