package me.flashyreese.mods.sodiumextra.mixin.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    //Todo: Re write this implementation
    @Inject(method = "applyFog", at = @At(value = "HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info) {
        if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
            vanillaApplyFog(camera, fogType, viewDistance, thickFog);
            info.cancel();
        }
    }

    // Vanilla copy
    private static void vanillaApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog) {
        float g;
        float f;
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        FogShape fogShape = FogShape.SPHERE;
        if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            if (entity.isSpectator()) {
                f = -8.0f;
                g = viewDistance * 0.5f;
            } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                f = 0.0f;
                g = 3.0f;
            } else {
                f = 0.25f;
                g = 1.0f;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            if (entity.isSpectator()) {
                f = -8.0f;
                g = viewDistance * 0.5f;
            } else {
                f = 0.0f;
                g = 2.0f;
            }
        } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.BLINDNESS)) {
            int i = ((LivingEntity)entity).getStatusEffect(StatusEffects.BLINDNESS).getDuration();
            float h = MathHelper.lerp(Math.min(1.0f, (float)i / 20.0f), viewDistance, 5.0f);
            if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
                f = 0.0f;
                g = h * 0.8f;
            } else {
                f = cameraSubmersionType == CameraSubmersionType.WATER ? -4.0f : h * 0.25f;
                g = h;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.WATER) {
            f = -8.0f;
            g = 96.0f;
            if (entity instanceof ClientPlayerEntity) {
                ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)entity;
                g *= Math.max(0.25f, clientPlayerEntity.getUnderwaterVisibility());
                RegistryEntry<Biome> registryEntry = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
                if (Biome.getCategory(registryEntry) == Biome.Category.SWAMP) {
                    g *= 0.85f;
                }
            }
            if (g > viewDistance) {
                g = viewDistance;
                fogShape = FogShape.CYLINDER;
            }
        } else if (thickFog) {
            if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                // Terrible hack to disable the fog, also breaks fog occlusion culling
                // Todo: Per dimension fog toggles and sliders perhaps
                f = Short.MAX_VALUE - 1;
                g = Short.MAX_VALUE;
            } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
                f = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                g = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
            } else {
                f = viewDistance * 0.05f;
                g = Math.min(viewDistance, 192.0f) * 0.5f;
            }
        } else if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
            f = 0.0f;
            g = viewDistance;
            fogShape = FogShape.CYLINDER;
        } else {
            float j = MathHelper.clamp(viewDistance / 10.0f, 4.0f, 64.0f);

            if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                // Terrible hack to disable the fog, also breaks fog occlusion culling
                // Todo: Per dimension fog toggles and sliders perhaps
                f = Short.MAX_VALUE - 1;
                g = Short.MAX_VALUE;
            } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
                f = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                g = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
            } else {
                f = viewDistance - j;
                g = viewDistance;
            }
            fogShape = FogShape.CYLINDER;
        }
        RenderSystem.setShaderFogStart(f);
        RenderSystem.setShaderFogEnd(g);
        RenderSystem.setShaderFogShape(fogShape);
    }
}
