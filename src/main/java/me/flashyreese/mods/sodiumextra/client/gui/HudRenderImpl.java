package me.flashyreese.mods.sodiumextra.client.gui;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.mixin.gui.MinecraftClientAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class HudRenderImpl implements HudRenderCallback {

    private final MinecraftClient client = MinecraftClient.getInstance();
    public SodiumExtraGameOptions.OverlayCorner overlayCorner;
    int fontHeight, scaledWidth, scaledHeight, fpsTextWidth, coordsTextWidth, lightUpdatesTextWidth;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!this.client.options.debugEnabled) {
            overlayCorner = SodiumExtraClientMod.options().extraSettings.overlayCorner;
            fontHeight = this.client.textRenderer.fontHeight;
            scaledWidth = this.client.getWindow().getScaledWidth();
            scaledHeight = this.client.getWindow().getScaledHeight();

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

        Text text = Text.translatable("sodium-extra.overlay.fps", currentFPS);

        if (SodiumExtraClientMod.options().extraSettings.showFPSExtended)
            text = Text.literal(String.format("%s %s", text.getString(), Text.translatable("sodium-extra.overlay.fps_extended", SodiumExtraClientMod.getClientTickHandler().getHighestFps(), SodiumExtraClientMod.getClientTickHandler().getAverageFps(),
                    SodiumExtraClientMod.getClientTickHandler().getLowestFps()).getString()));
        fpsTextWidth = this.client.textRenderer.getWidth(text);

        int x, y;
        switch (overlayCorner) {
            case TOP_LEFT -> {
                x = 2;
                y = 2;
            }
            case TOP_RIGHT -> {
                x = scaledWidth - fpsTextWidth - 2;
                y = 2;
            }
            case BOTTOM_LEFT -> {
                x = 2;
                y = scaledHeight - fontHeight - 2;
            }
            case BOTTOM_RIGHT -> {
                x = scaledWidth - fpsTextWidth - 2;
                y = scaledHeight - fontHeight - 2;
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + overlayCorner);
        }

        this.drawString(matrices, text, x, y, fpsTextWidth);
    }

    private void renderCoords(MatrixStack matrices) {
        if (this.client.player == null) return;
        Vec3d pos = this.client.player.getPos();

        Text text = Text.translatable("sodium-extra.overlay.coordinates", String.format("%.2f", pos.x), String.format("%.2f", pos.y), String.format("%.2f", pos.z));
        coordsTextWidth =  this.client.textRenderer.getWidth(text);

        int x, y;
        switch (overlayCorner) {
            case TOP_LEFT -> {
                x = 2;
                y = 12;
            }
            case TOP_RIGHT -> {
                x = scaledWidth - coordsTextWidth - 2;
                y = 12;
            }
            case BOTTOM_LEFT -> {
                x = 2;
                y = scaledHeight - fontHeight - 12;
            }
            case BOTTOM_RIGHT -> {
                x = scaledWidth - coordsTextWidth - 2;
                y = scaledHeight - fontHeight - 12;
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + overlayCorner);
        }

        this.drawString(matrices, text, x, y, coordsTextWidth);
    }

    private void renderLightUpdatesWarning(MatrixStack matrices) {
        Text text = Text.translatable("sodium-extra.overlay.light_updates");
        lightUpdatesTextWidth = this.client.textRenderer.getWidth(text);

        int x, y;
        switch (overlayCorner) {
            case TOP_LEFT -> {
                x = 2;
                y = 22;
            }
            case TOP_RIGHT -> {
                x = scaledWidth - lightUpdatesTextWidth - 2;
                y = 22;
            }
            case BOTTOM_LEFT -> {
                x = 2;
                y = scaledHeight - fontHeight - 22;
            }
            case BOTTOM_RIGHT -> {
                x = scaledWidth - lightUpdatesTextWidth - 2;
                y = scaledHeight - fontHeight - 22;
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + overlayCorner);
        }

        this.drawString(matrices, text, x, y, lightUpdatesTextWidth);
    }

    private void drawString(MatrixStack matrices, Text text, int x, int y, int textWidth) {
        switch (SodiumExtraClientMod.options().extraSettings.textContrast) {
            case NONE -> this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
            case BACKGROUND -> {
                DrawableHelper.fill(matrices, x - 1, y - 1, x + textWidth + 1, y + fontHeight, -1873784752);
                this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
            }
            case SHADOW -> this.client.textRenderer.drawWithShadow(matrices, text, x, y, 0xffffffff);
        }
    }
}
