package me.flashyreese.mods.sodiumextra.mixin.features.gui;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.BEFORE))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo callbackInfo) {
        if (SodiumExtraClientMod.options().extraSettings.showFps && !this.client.options.debugEnabled) {
            int currentFPS = ((MinecraftClientAccessor) this.client).getCurrentFPS();
            this.client.textRenderer.draw(matrices, new LiteralText(String.format("%s fps (max. %s / avg. %s / min. %s)", currentFPS,
                    SodiumExtraClientMod.getClientTickHandler().getHighestFps(), SodiumExtraClientMod.getClientTickHandler().getAverageFps(),
                    SodiumExtraClientMod.getClientTickHandler().getLowestFps())), 2, 2, 0xffffffff);
        }
    }
}
