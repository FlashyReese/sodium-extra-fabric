package me.flashyreese.mods.sodiumextra.mixin.features.fog;

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

    @Inject(method = "applyFog", at = @At(value = "HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info) {
        vanillaApplyFog(camera, fogType, viewDistance, thickFog);
        info.cancel();
    }

    // Vanilla copy
    private static void vanillaApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        float y;
        if (cameraSubmersionType == CameraSubmersionType.WATER) {
            y = 192.0F;
            if (entity instanceof ClientPlayerEntity clientPlayerEntity) {
                y *= Math.max(0.25F, clientPlayerEntity.getUnderwaterVisibility());
                Biome biome = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
                if (biome.getCategory() == Biome.Category.SWAMP) {
                    y *= 0.85F;
                }
            }

            RenderSystem.setShaderFogStart(-8.0F);
            RenderSystem.setShaderFogEnd(y * 0.5F);
        } else {
            float ab;
            if (cameraSubmersionType == CameraSubmersionType.LAVA) {
                if (entity.isSpectator()) {
                    y = -8.0F;
                    ab = viewDistance * 0.5F;
                } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                    y = 0.0F;
                    ab = 3.0F;
                } else {
                    y = 0.25F;
                    ab = 1.0F;
                }
            } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.BLINDNESS)) {
                int m = ((LivingEntity)entity).getStatusEffect(StatusEffects.BLINDNESS).getDuration();
                float n = MathHelper.lerp(Math.min(1.0F, (float)m / 20.0F), viewDistance, 5.0F);
                if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
                    y = 0.0F;
                    ab = n * 0.8F;
                } else {
                    y = n * 0.25F;
                    ab = n;
                }
            } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
                if (entity.isSpectator()) {
                    y = -8.0F;
                    ab = viewDistance * 0.5F;
                } else {
                    y = 0.0F;
                    ab = 2.0F;
                }
            } else if (thickFog) {
                if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                    // Terrible hack to disable the fog, also breaks fog occlusion culling
                    // Todo: Per dimension fog toggles and sliders perhaps
                    y = Short.MAX_VALUE - 1;
                    ab = Short.MAX_VALUE;
                } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0){
                    y = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                    ab = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
                } else {
                    y = viewDistance * 0.05F;
                    ab = Math.min(viewDistance, 192.0F) * 0.5F;
                }
            } else if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
                y = 0.0F;
                ab = viewDistance;
            } else {
                if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                    // Terrible hack to disable the fog, also breaks fog occlusion culling
                    // Todo: Per dimension fog toggles and sliders perhaps
                    y = Short.MAX_VALUE - 1;
                    ab = Short.MAX_VALUE;
                } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0){
                    y = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                    ab = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
                } else {
                    y = viewDistance * 0.75F;
                    ab = viewDistance;
                }
            }

            RenderSystem.setShaderFogStart(y);
            RenderSystem.setShaderFogEnd(ab);
        }

    }
}
