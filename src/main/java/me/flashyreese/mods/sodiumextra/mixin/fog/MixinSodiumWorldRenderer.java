package me.flashyreese.mods.sodiumextra.mixin.fog;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SodiumWorldRenderer.class)
public class MixinSodiumWorldRenderer {

    /**
     * Sodium's 1.16.x/dev fog, added because removed from 1.16.x/next.
     *
     * @author jellysquid3
     */
    @Inject(method = "drawChunkLayer", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/passes/BlockRenderPass;startDrawing()V", shift = At.Shift.AFTER), remap = false)
    private void drawChunkLayer(RenderLayer renderLayer, MatrixStack matrixStack, double x, double y, double z, CallbackInfo info) {
        // We don't have a great way to check if underwater fog is being used, so assume that terrain will only ever
        // use linear fog. This will not disable fog in the Nether.
        if (!SodiumExtraClientMod.options().renderSettings.fog && this.isFogLinear()) {
            RenderSystem.disableFog();
        }
    }

    private boolean isFogLinear() {
        return GL11.glGetInteger(GL11.GL_FOG_MODE) == GL11.GL_LINEAR;
    }
}
