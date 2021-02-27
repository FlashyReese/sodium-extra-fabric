package me.flashyreese.mods.sodiumextra.mixin.sodium;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SliderControl.class)
public class MixinSliderControl {
    @Shadow
    @Final
    @Mutable
    private int max;

    /*
     * Completely sane
     */
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(Option<Integer> option, int min, int max, int interval, ControlValueFormatter mode, CallbackInfo ci){
        if (SodiumExtraClientMod.options().extraSettings.highMaxBrightness && mode == ControlValueFormatter.brightness()){
            this.max = 1000;
        }
    }
}
