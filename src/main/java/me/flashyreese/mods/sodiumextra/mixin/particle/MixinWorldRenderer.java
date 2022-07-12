package me.flashyreese.mods.sodiumextra.mixin.particle;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Inject(method = "tickRainSplashing", at = @At(value = "HEAD"), cancellable = true)
    public void tickRainSplashing(Camera camera, CallbackInfo callbackInfo) {
        if (!(SodiumExtraClientMod.options().particleSettings.particles && SodiumExtraClientMod.options().particleSettings.rainSplash)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At(value = "HEAD"), cancellable = true)
    private void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo callbackInfo) {
        if (!(SodiumExtraClientMod.options().detailSettings.rainSnow)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;getRandomParticleSpawnChance(Z)Lnet/minecraft/client/option/ParticlesMode;"), cancellable = true)
    private void spawnParticle(ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> callbackInfo) {
        if (SodiumExtraClientMod.options().particleSettings.particles) {
            Identifier particleTypeId = Registry.PARTICLE_TYPE.getId(parameters.getType());
            if (!SodiumExtraClientMod.options().particleSettings.otherMap.getOrDefault(particleTypeId, true)) {
                callbackInfo.setReturnValue(null);
            }
        } else {
            callbackInfo.setReturnValue(null);
        }
    }
}
