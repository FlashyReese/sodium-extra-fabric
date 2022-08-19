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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.tag.BiomeTags;
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
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
            vanillaApplyFog(camera, fogType, viewDistance, thickFog, tickDelta);
            ci.cancel();
        }
    }

    // Vanilla copy
    private static void vanillaApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        BackgroundRenderer.FogData fogData = new BackgroundRenderer.FogData(fogType);
        BackgroundRenderer.StatusEffectFogModifier statusEffectFogModifier = BackgroundRendererAccessor.callGetFogModifier(entity, tickDelta);
        if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            if (entity.isSpectator()) {
                fogData.fogStart = -8.0f;
                fogData.fogEnd = viewDistance * 0.5f;
            } else if (entity instanceof LivingEntity && ((LivingEntity) entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                fogData.fogStart = 0.0f;
                fogData.fogEnd = 3.0f;
            } else {
                fogData.fogStart = 0.25f;
                fogData.fogEnd = 1.0f;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            if (entity.isSpectator()) {
                fogData.fogStart = -8.0f;
                fogData.fogEnd = viewDistance * 0.5f;
            } else {
                fogData.fogStart = 0.0f;
                fogData.fogEnd = 2.0f;
            }
        } else if (statusEffectFogModifier != null) {
            LivingEntity livingEntity = (LivingEntity) entity;
            StatusEffectInstance statusEffectInstance = livingEntity.getStatusEffect(statusEffectFogModifier.getStatusEffect());
            if (statusEffectInstance != null) {
                statusEffectFogModifier.applyStartEndModifier(fogData, livingEntity, statusEffectInstance, viewDistance, tickDelta);
            }
        } else if (cameraSubmersionType == CameraSubmersionType.WATER) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = 96.0f;
            if (entity instanceof ClientPlayerEntity) {
                ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity) entity;
                fogData.fogEnd *= Math.max(0.25f, clientPlayerEntity.getUnderwaterVisibility());
                RegistryEntry<Biome> registryEntry = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
                if (registryEntry.isIn(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                    fogData.fogEnd *= 0.85f;
                }
            }
            if (fogData.fogEnd > viewDistance) {
                fogData.fogEnd = viewDistance;
                fogData.fogShape = FogShape.CYLINDER;
            }
        } else if (thickFog) {
            if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                // Terrible hack to disable the fog, also breaks fog occlusion culling
                // Todo: Per dimension fog toggles and sliders perhaps
                fogData.fogStart = Short.MAX_VALUE - 1;
                fogData.fogEnd = Short.MAX_VALUE;
            } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
                fogData.fogStart = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                fogData.fogEnd = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
            } else {
                fogData.fogStart = viewDistance * 0.05f;
                fogData.fogEnd = Math.min(viewDistance, 192.0f) * 0.5f;
            }
        } else if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
            fogData.fogStart = 0.0f;
            fogData.fogEnd = viewDistance;
            fogData.fogShape = FogShape.CYLINDER;
        } else {
            float f = MathHelper.clamp(viewDistance / 10.0f, 4.0f, 64.0f);
            if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                // Terrible hack to disable the fog, also breaks fog occlusion culling
                // Todo: Per dimension fog toggles and sliders perhaps
                fogData.fogStart = Short.MAX_VALUE - 1;
                fogData.fogEnd = Short.MAX_VALUE;
            } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
                fogData.fogStart = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                fogData.fogEnd = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
            } else {
                fogData.fogStart = viewDistance - f;
                fogData.fogEnd = viewDistance;
            }
            fogData.fogShape = FogShape.CYLINDER;
        }
        RenderSystem.setShaderFogStart(fogData.fogStart);
        RenderSystem.setShaderFogEnd(fogData.fogEnd);
        RenderSystem.setShaderFogShape(fogData.fogShape);
    }
}
