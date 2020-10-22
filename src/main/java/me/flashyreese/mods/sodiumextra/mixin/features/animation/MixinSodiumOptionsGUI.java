package me.flashyreese.mods.sodiumextra.mixin.features.animation;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class MixinSodiumOptionsGUI {

    @Shadow
    @Final
    private List<OptionPage> pages;

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        this.pages.add(SodiumExtraGameOptionPages.animation());
    }
}
