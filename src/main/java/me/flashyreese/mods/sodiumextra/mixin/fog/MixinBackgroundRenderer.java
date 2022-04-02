package me.flashyreese.mods.sodiumextra.mixin.fog;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    
    @Inject(method = "applyFog", at = @At(value = "HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info) {
        if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0) {
            vanillaApplyFog(camera, fogType, viewDistance, thickFog);
            info.cancel();
        }
    }

    // Vanilla copy
    private static void vanillaApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog) {
        FluidState fluidState = camera.getSubmergedFluidState();
        Entity entity = camera.getFocusedEntity();
        if (fluidState.isIn(FluidTags.WATER)) {
            float f;
            f = 0.05f;
            if (entity instanceof ClientPlayerEntity) {
                ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)entity;
                f -= clientPlayerEntity.getUnderwaterVisibility() * clientPlayerEntity.getUnderwaterVisibility() * 0.03f;
                Biome biome = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
                if (biome.getCategory() == Biome.Category.SWAMP) {
                    f += 0.005f;
                }
            }
            RenderSystem.fogDensity(f);
            RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
        } else {
            float fogEnd;
            float fogStart;
            if (fluidState.isIn(FluidTags.LAVA)) {
                if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                    fogStart = 0.0f;
                    fogEnd = 3.0f;
                } else {
                    fogStart = 0.25f;
                    fogEnd = 1.0f;
                }
            } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.BLINDNESS)) {
                int biome = Objects.requireNonNull(((LivingEntity) entity).getStatusEffect(StatusEffects.BLINDNESS)).getDuration();
                float g = MathHelper.lerp(Math.min(1.0f, (float)biome / 20.0f), viewDistance, 5.0f);
                if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
                    fogStart = 0.0f;
                    fogEnd = g * 0.8f;
                } else {
                    fogStart = g * 0.25f;
                    fogEnd = g;
                }
            } else if (thickFog) {
                if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                    // Terrible hack to disable the fog, also breaks fog occlusion culling
                    // Todo: Per dimension fog toggles and sliders perhaps
                    fogStart = Short.MAX_VALUE - 1;
                    fogEnd = Short.MAX_VALUE;
                } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0){
                    fogStart = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                    fogEnd = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
                } else {
                    fogStart = viewDistance * 0.05F;
                    fogEnd = Math.min(viewDistance, 192.0F) * 0.5F;
                }
            } else if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
                fogStart = 0.0f;
                fogEnd = viewDistance;
            } else {
                if (SodiumExtraClientMod.options().renderSettings.fogDistance == 33) {
                    // Terrible hack to disable the fog, also breaks fog occlusion culling
                    // Todo: Per dimension fog toggles and sliders perhaps
                    fogStart = Short.MAX_VALUE - 1;
                    fogEnd = Short.MAX_VALUE;
                } else if (SodiumExtraClientMod.options().renderSettings.fogDistance != 0){
                    fogStart = SodiumExtraClientMod.options().renderSettings.fogDistance * 16;
                    fogEnd = (SodiumExtraClientMod.options().renderSettings.fogDistance + 1) * 16;
                } else {
                    fogStart = viewDistance * 0.75F;
                    fogEnd = viewDistance;
                }
            }
            RenderSystem.fogStart(fogStart);
            RenderSystem.fogEnd(fogEnd);
            RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
            RenderSystem.setupNvFogDistance();
        }
    }
}
