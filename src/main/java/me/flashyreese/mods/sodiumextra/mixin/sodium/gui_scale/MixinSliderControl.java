package me.flashyreese.mods.sodiumextra.mixin.sodium.gui_scale;

import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SliderControl.class, priority = 999, remap = false)
public class MixinSliderControl {

    @Shadow
    @Final
    @Mutable
    private int max;

    @Shadow
    @Final
    private ControlValueFormatter mode;

    @Inject(method = "createElement", at = @At(value = "HEAD"))
    public void preCreateElement(Dim2i dim, CallbackInfoReturnable<ControlElement<Integer>> cir) {
        if (this.mode == ControlValueFormatter.guiScale()) {
            this.max = MinecraftClient.getInstance().getWindow().calculateScaleFactor(0, MinecraftClient.getInstance().forcesUnicodeFont());
        }
    }
}