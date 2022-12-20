package me.flashyreese.mods.sodiumextra.client.gui;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.mixin.gui.MinecraftClientAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;

public class HudRenderImpl implements HudRenderCallback {

    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!this.client.options.debugEnabled) {
            //Gotta love hardcoding
            if (SodiumExtraClientMod.options().extraSettings.showFps && SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderFPS(matrixStack);
                this.renderCoords(matrixStack);
            } else if (SodiumExtraClientMod.options().extraSettings.showFps) {
                this.renderFPS(matrixStack);
            } else if (SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderCoords(matrixStack);
            }
            if (!SodiumExtraClientMod.options().renderSettings.lightUpdates) {
                this.renderLightUpdatesWarning(matrixStack);
            }
        }
    }

    //Should I make this OOP or just leave as it :> I don't think I will be adding any more than these 2.
    private void renderFPS(MatrixStack matrices) {
        int currentFPS = MinecraftClientAccessor.getCurrentFPS();

        Text text = new TranslatableText("sodium-extra.overlay.fps", currentFPS);

        if (SodiumExtraClientMod.options().extraSettings.showFPSExtended)
            text = new LiteralText(String.format("%s %s", text.getString(), new TranslatableText("sodium-extra.overlay.fps_extended", SodiumExtraClientMod.getClientTickHandler().getHighestFps(), SodiumExtraClientMod.getClientTickHandler().getAverageFps(),
                    SodiumExtraClientMod.getClientTickHandler().getLowestFps()).getString()));

        int x = 0, y = 0;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT:
                x = 2;
                y = 2;
                break;
            case TOP_RIGHT:
                x = this.client.getWindow().getScaledWidth() - this.client.textRenderer.getWidth(text) - 2;
                y = 2;
                break;
            case BOTTOM_LEFT:
                x = 2;
                y = this.client.getWindow().getScaledHeight() - this.client.textRenderer.fontHeight - 2;
                break;
            case BOTTOM_RIGHT:
                x = this.client.getWindow().getScaledWidth() - this.client.textRenderer.getWidth(text) - 2;
                y = this.client.getWindow().getScaledHeight() - this.client.textRenderer.fontHeight - 2;
                break;
        }

        this.drawString(matrices, text, x, y);
    }

    private void renderCoords(MatrixStack matrices) {
        if (this.client.player == null) return;
        if (this.client.hasReducedDebugInfo()) return;
        Vec3d pos = this.client.player.getPos();

        Text text = new TranslatableText("sodium-extra.overlay.coordinates", String.format("%.2f", pos.x), String.format("%.2f", pos.y), String.format("%.2f", pos.z));

        int x = 0, y = 0;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT:
                x = 2;
                y = 12;
                break;
            case TOP_RIGHT:
                x = this.client.getWindow().getScaledWidth() - this.client.textRenderer.getWidth(text) - 2;
                y = 12;
                break;
            case BOTTOM_LEFT:
                x = 2;
                y = this.client.getWindow().getScaledHeight() - this.client.textRenderer.fontHeight - 12;
                break;
            case BOTTOM_RIGHT:
                x = this.client.getWindow().getScaledWidth() - this.client.textRenderer.getWidth(text) - 2;
                y = this.client.getWindow().getScaledHeight() - this.client.textRenderer.fontHeight - 12;
                break;
        }

        this.drawString(matrices, text, x, y);
    }

    private void renderLightUpdatesWarning(MatrixStack matrices) {
        Text text = new TranslatableText("sodium-extra.overlay.light_updates");

        int x = 0, y = 0;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT:
                x = 2;
                y = 22;
                break;
            case TOP_RIGHT:
                x = this.client.getWindow().getScaledWidth() - this.client.textRenderer.getWidth(text) - 2;
                y = 22;
                break;
            case BOTTOM_LEFT:
                x = 2;
                y = this.client.getWindow().getScaledHeight() - this.client.textRenderer.fontHeight - 22;
                break;
            case BOTTOM_RIGHT:
                x = this.client.getWindow().getScaledWidth() - this.client.textRenderer.getWidth(text) - 2;
                y = this.client.getWindow().getScaledHeight() - this.client.textRenderer.fontHeight - 22;
                break;
        }

        this.drawString(matrices, text, x, y);
    }

    private void drawString(MatrixStack matrices, Text text, int x, int y) {
        if (SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.NONE) {
            this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
        } else if (SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.BACKGROUND) {
            DrawableHelper.fill(matrices, x - 1, y - 1, x + this.client.textRenderer.getWidth(text) + 1, y + this.client.textRenderer.fontHeight, -1873784752);
            this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
        } else if (SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.SHADOW) {
            this.client.textRenderer.drawWithShadow(matrices, text, x, y, 0xffffffff);
        }
    }
}
