package me.flashyreese.mods.sodiumextra.mixin.gui;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

    @Shadow
    private int scaledWidth;
    
    @Shadow
    private int scaledHeight;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.BEFORE))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo callbackInfo) {
        if (!this.client.options.debugEnabled) {
            //Gotta love hardcoding
            if (SodiumExtraClientMod.options().extraSettings.showFps && SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderFPS(matrices);
                this.renderCoords(matrices);
            } else if (SodiumExtraClientMod.options().extraSettings.showFps) {
                this.renderFPS(matrices);
            } else if (SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderCoords(matrices);
            }
        }
    }

    //Should I make this OOP or just leave as it :> I don't think I will be adding any more than these 2.
    private void renderFPS(MatrixStack matrices) {
        int currentFPS = ((MinecraftClientAccessor) this.client).getCurrentFPS();

        Text text = new TranslatableText("sodium-extra.overlay.fps", currentFPS);

        if (SodiumExtraClientMod.options().extraSettings.showFPSExtended)
            text = new LiteralText(String.format("%s %s", text.getString(), new TranslatableText("sodium-extra.overlay.fps_extended", SodiumExtraClientMod.getClientTickHandler().getHighestFps(), SodiumExtraClientMod.getClientTickHandler().getAverageFps(),
                    SodiumExtraClientMod.getClientTickHandler().getLowestFps()).getString()));
        
        int x, y;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT:
                x = 2;
                y = 2;
                return;
            case TOP_RIGHT:
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = 2;
                return;
            case BOTTOM_LEFT:
                x = 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 2;
                return;
            case BOTTOM_RIGHT:
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 2;
                return;
            default:
                x = 2;
                y = 2;
        }

        this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
    }

    private void renderCoords(MatrixStack matrices) {
        if (this.client.player == null) return;
        Vec3d pos = this.client.player.getPos();

        Text text = new TranslatableText("sodium-extra.overlay.coordinates", String.format("%.2f", pos.x), String.format("%.2f", pos.y), String.format("%.2f", pos.z));

        int x, y;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT:
                x = 2;
                y = 12;
                return;
            case TOP_RIGHT:
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = 12;
                return;
            case BOTTOM_LEFT:
                x = 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 12;
                return;
            case BOTTOM_RIGHT:
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 12;
                return;
            default:
                x = 2;
                y = 12;
        }

        this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
    }
}
