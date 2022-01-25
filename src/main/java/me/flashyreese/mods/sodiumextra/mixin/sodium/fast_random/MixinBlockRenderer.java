package me.flashyreese.mods.sodiumextra.mixin.sodium.fast_random;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.model.light.LightPipelineProvider;
import me.jellysquid.mods.sodium.client.model.quad.blender.BiomeColorBlender;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BlockRenderer.class)
public class MixinBlockRenderer {
    @Shadow
    @Final
    @Mutable
    private Random random;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(MinecraftClient client, LightPipelineProvider lighters, BiomeColorBlender biomeColorBlender, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().extraSettings.useFastRandom) {
            this.random = new Random();
        }
    }
}
