package me.flashyreese.mods.sodiumextra.mixin.compat;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.gui.SuggestRSOScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "RETURN"))
    private void postInit(CallbackInfo ci) {
        if (!FabricLoader.getInstance().isModLoaded("reeses-sodium-options") && !SodiumExtraClientMod.options().notificationSettings.hideRSORecommendation && !SodiumExtraClientMod.options().hasSuggestedRSO()) {
            this.client.openScreen(new SuggestRSOScreen(this));
            SodiumExtraClientMod.options().setSuggestedRSO(true);
        }
    }
}
