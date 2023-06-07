package me.flashyreese.mods.sodiumextra.mixin.toasts;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.toast.TutorialToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TutorialToast.class)
public class MixinTutorialToast {
    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    public void draw(DrawContext context, ToastManager manager, long startTime, CallbackInfoReturnable<Toast.Visibility> cir) {
        if (!SodiumExtraClientMod.options().extraSettings.tutorialToast) {
            cir.setReturnValue(Toast.Visibility.HIDE);
        }
    }
}
