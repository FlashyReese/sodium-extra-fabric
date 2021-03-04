package me.flashyreese.mods.sodiumextra.mixin.features.gui;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
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
        if (!this.client.options.debugEnabled) {
            //Gotta love hardcoding
            if (SodiumExtraClientMod.options().extraSettings.showFps && SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderFPS(matrices, 2, 2);
                this.renderCoords(matrices, 2, 12);
            } else if (SodiumExtraClientMod.options().extraSettings.showFps) {
                this.renderFPS(matrices, 2, 2);
            } else if (SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderCoords(matrices, 2, 2);
            }
        }
    }

    //Should I make this OOP or just leave as it :> I don't think I will be adding any more than these 2.
    private void renderFPS(MatrixStack matrices, int x, int y) {
        int currentFPS = ((MinecraftClientAccessor) this.client).getCurrentFPS();
        this.client.textRenderer.draw(matrices, new LiteralText(String.format("%s fps (max. %s / avg. %s / min. %s)", currentFPS,
                SodiumExtraClientMod.getClientTickHandler().getHighestFps(), SodiumExtraClientMod.getClientTickHandler().getAverageFps(),
                SodiumExtraClientMod.getClientTickHandler().getLowestFps())), x, y, 0xffffffff);
    }

    private void renderCoords(MatrixStack matrices, int x, int y) {
        if (this.client.player == null) return;
        Vec3d pos = this.client.player.getPos();
        this.client.textRenderer.draw(matrices, new LiteralText(String.format("X: %s, Y: %s, Z: %s", (int)pos.x, (int)pos.y, (int)pos.z)), x, y, 0xffffffff);
    }
}
