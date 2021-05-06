package me.flashyreese.mods.sodiumextra.mixin.sodium;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.gui.SliderControlElement;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SliderControl.class)
public class MixinSliderControl {
    @Shadow
    @Final
    private Option<Integer> option;

    @Shadow
    @Final
    @Mutable
    private int max;

    @Shadow
    @Final
    private int min;

    @Shadow
    @Final
    private int interval;

    @Shadow
    @Final
    private ControlValueFormatter mode;

    /*
     * Completely sane
     */
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(Option<Integer> option, int min, int max, int interval, ControlValueFormatter mode, CallbackInfo ci){
        if (SodiumExtraClientMod.options().extraSettings.highMaxBrightness && mode == ControlValueFormatter.brightness()){
            this.max = 1000;
        }
    }

    @Inject(method = "createElement", at = @At(value = "RETURN"), cancellable = true, remap = false)
    public void createElement(Dim2i dim, CallbackInfoReturnable<ControlElement<Integer>> cir) {
        cir.setReturnValue(new SliderControlElement(this.option, dim, this.min, this.max, this.interval, this.mode));
    }
}
