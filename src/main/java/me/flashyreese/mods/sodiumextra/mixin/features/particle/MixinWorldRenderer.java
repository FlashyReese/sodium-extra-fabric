package me.flashyreese.mods.sodiumextra.mixin.features.particle;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Inject(method = "tickRainSplashing", at = @At(value = "INVOKE"), cancellable = true)
    public void tickRainSplashing(Camera camera, CallbackInfo callbackInfo) {
        if (!(SodiumExtraClientMod.options().particleSettings.particles && SodiumExtraClientMod.options().particleSettings.rainSplash)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At(value = "INVOKE"), cancellable = true)
    private void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo callbackInfo) {
        if (!(SodiumExtraClientMod.options().particleSettings.particles && SodiumExtraClientMod.options().particleSettings.weather)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;getRandomParticleSpawnChance(Z)Lnet/minecraft/client/options/ParticlesMode;"), cancellable = true)
    private void spawnParticle(ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable callbackInfo) {
        if (SodiumExtraClientMod.options().particleSettings.particles) {
            if (!SodiumExtraClientMod.options().particleSettings.explosion) {
                if (parameters == ParticleTypes.EXPLOSION_EMITTER || parameters == ParticleTypes.EXPLOSION || parameters == ParticleTypes.POOF) {
                    callbackInfo.setReturnValue(null);
                }
            } else if (!SodiumExtraClientMod.options().particleSettings.water) {
                if (parameters == ParticleTypes.UNDERWATER) {
                    callbackInfo.setReturnValue(null);
                }
            } else if (!SodiumExtraClientMod.options().particleSettings.smoke) {
                if (parameters == ParticleTypes.SMOKE || parameters == ParticleTypes.LARGE_SMOKE || parameters == ParticleTypes.CAMPFIRE_COSY_SMOKE || parameters == ParticleTypes.CAMPFIRE_SIGNAL_SMOKE) {
                    callbackInfo.setReturnValue(null);
                }
            } else if (!SodiumExtraClientMod.options().particleSettings.potion) {
                if (parameters == ParticleTypes.ENTITY_EFFECT || parameters == ParticleTypes.AMBIENT_ENTITY_EFFECT || parameters == ParticleTypes.EFFECT || parameters == ParticleTypes.INSTANT_EFFECT || parameters == ParticleTypes.WITCH) {
                    callbackInfo.setReturnValue(null);
                }
            } else if (!SodiumExtraClientMod.options().particleSettings.portal) {
                if (parameters == ParticleTypes.PORTAL || parameters == ParticleTypes.REVERSE_PORTAL) {
                    callbackInfo.setReturnValue(null);
                }
            } else if (!SodiumExtraClientMod.options().particleSettings.fluidDrip) {
                if (parameters == ParticleTypes.DRIPPING_WATER || parameters == ParticleTypes.DRIPPING_LAVA || parameters == ParticleTypes.DRIPPING_HONEY || parameters == ParticleTypes.DRIPPING_OBSIDIAN_TEAR) {
                    callbackInfo.setReturnValue(null);
                }
            } else if (!SodiumExtraClientMod.options().particleSettings.firework) {
                if (parameters == ParticleTypes.FIREWORK) {
                    callbackInfo.setReturnValue(null);
                }
            }
        } else {
            callbackInfo.setReturnValue(null);
        }
    }
}
