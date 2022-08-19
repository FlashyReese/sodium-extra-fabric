package me.flashyreese.mods.sodiumextra.mixin.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
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
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        float f;
        if (cameraSubmersionType == CameraSubmersionType.WATER) {
            f = 192.0F;
            if (entity instanceof ClientPlayerEntity) {
                ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)entity;
                f *= Math.max(0.25F, clientPlayerEntity.getUnderwaterVisibility());
                Biome biome = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
                if (biome.getCategory() == Biome.Category.SWAMP) {
                    f *= 0.85F;
                }
            }

            RenderSystem.setShaderFogStart(-8.0F);
            RenderSystem.setShaderFogEnd(f * 0.5F);
        } else {
            float g;
            if (cameraSubmersionType == CameraSubmersionType.LAVA) {
                if (entity.isSpectator()) {
                    f = -8.0F;
                    g = viewDistance * 0.5F;
                } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                    f = 0.0F;
                    g = 3.0F;
                } else {
                    f = 0.25F;
                    g = 1.0F;
                }
            } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.BLINDNESS)) {
                int i = ((LivingEntity)entity).getStatusEffect(StatusEffects.BLINDNESS).getDuration();
                float h = MathHelper.lerp(Math.min(1.0F, (float)i / 20.0F), viewDistance, 5.0F);
                if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
                    f = 0.0F;
                    g = h * 0.8F;
                } else {
                    f = h * 0.25F;
                    g = h;
                }
            } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
                if (entity.isSpectator()) {
                    f = -8.0F;
                    g = viewDistance * 0.5F;
                } else {
                    f = 0.0F;
                    g = 2.0F;
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
                f = 0.0F;
                g = viewDistance;
            } else {
                float j = MathHelper.clamp(viewDistance / 10.0F, 4.0F, 64.0F);
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
            }

            RenderSystem.setShaderFogStart(f);
            RenderSystem.setShaderFogEnd(g);
        }

    }
}
