package me.flashyreese.mods.sodiumextra.mixin.sodium.render_distance;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SodiumGameOptionPages.class)
public class MixinSodiumGameOptionsPages {
    @ModifyArg(method = "lambda$general$0", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/control/SliderControl;<init>(Lme/jellysquid/mods/sodium/client/gui/options/Option;IIILme/jellysquid/mods/sodium/client/gui/options/control/ControlValueFormatter;)V"), index = 2)
    private static int modifyMaxRenderDistance(int maxRenderDistance) {
        return 64;
    }
}
