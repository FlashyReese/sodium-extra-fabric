package me.flashyreese.mods.sodiumextra.mixin.light_updates;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightingProvider.class)
public class MixinLightingProvider {
    @Inject(at = @At("HEAD"), method = "checkBlock", cancellable = true)
    public void checkBlock(BlockPos pos, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().renderSettings.lightUpdates)
            ci.cancel();
    }

    @Inject(at = @At("RETURN"), method = "doLightUpdates", cancellable = true)
    public void doLightUpdates(int maxUpdateCount, boolean doSkylight, boolean skipEdgeLightPropagation, CallbackInfoReturnable<Integer> cir) {
        if (!SodiumExtraClientMod.options().renderSettings.lightUpdates)
            cir.setReturnValue(0);
    }
}
