package me.flashyreese.mods.sodiumextra.mixin.features.particle;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.particle.Particle;
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

    @Inject(method = "spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;getRandomParticleSpawnChance(Z)Lnet/minecraft/client/options/ParticlesMode;"), cancellable = true)
    private void spawnParticle(ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> callbackInfo) {
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
            } else if (!SodiumExtraClientMod.options().particleSettings.drip) {
                if (parameters == ParticleTypes.DRIPPING_WATER || parameters == ParticleTypes.FALLING_WATER || parameters == ParticleTypes.DRIPPING_LAVA ||
                        parameters == ParticleTypes.FALLING_LAVA || parameters == ParticleTypes.LANDING_LAVA || parameters == ParticleTypes.DRIPPING_HONEY ||
                        parameters == ParticleTypes.FALLING_HONEY || parameters == ParticleTypes.LANDING_HONEY || parameters == ParticleTypes.FALLING_NECTAR ||
                        parameters == ParticleTypes.DRIPPING_OBSIDIAN_TEAR || parameters == ParticleTypes.FALLING_OBSIDIAN_TEAR || parameters == ParticleTypes.LANDING_OBSIDIAN_TEAR) {
                    callbackInfo.setReturnValue(null);
                }
            } else if (!SodiumExtraClientMod.options().particleSettings.firework) {
                if (parameters == ParticleTypes.FIREWORK) {
                    callbackInfo.setReturnValue(null);
                }
                //Fix Bubble columns
                if (parameters == ParticleTypes.BUBBLE || parameters == ParticleTypes.BUBBLE_POP || parameters == ParticleTypes.BUBBLE_COLUMN_UP) {
                    callbackInfo.setReturnValue(null);
                }
            }
        } else {
            callbackInfo.setReturnValue(null);
        }
    }

    /*@Inject(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;byId(I)Lnet/minecraft/util/math/Direction;", shift = At.Shift.BEFORE), cancellable = true)
    public void processWorldEvent2000(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo callbackInfo) {
        if (!SodiumExtraClientMod.options().particleSettings.particles){
            callbackInfo.cancel();
        }
    }

    @Inject(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;ofBottomCenter(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/math/Vec3d;", shift = At.Shift.BEFORE), cancellable = true)
    public void processWorldEvent2003(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo callbackInfo) {
        if (!SodiumExtraClientMod.options().particleSettings.particles){
            callbackInfo.cancel();
        }
    }

    @Inject(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;ofBottomCenter(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/math/Vec3d;", shift = At.Shift.BEFORE), cancellable = true)
    public void processWorldEvent2007(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo callbackInfo) {
        if (!SodiumExtraClientMod.options().particleSettings.particles){
            callbackInfo.cancel();
        }
    }*/
}
