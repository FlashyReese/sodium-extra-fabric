package me.flashyreese.mods.sodiumextra.mixin.features.no_overlay;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.Camera;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class MixinCamera {
    @Inject(at = {@At("HEAD")}, method = {"getSubmergedFluidState()Lnet/minecraft/fluid/FluidState;"}, cancellable = true)
    private void getSubmergedFluidState(CallbackInfoReturnable<FluidState> cir) {
        if (SodiumExtraClientMod.options().extraSettings.noOverlay)
            cir.setReturnValue(Fluids.EMPTY.getDefaultState());
    }
}