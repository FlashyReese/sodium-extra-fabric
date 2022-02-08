package me.flashyreese.mods.sodiumextra.mixin.sky;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/VertexBuffer;bind()V",
                    ordinal = 0
            )
    )
    public void redirectVertexBufferBind(VertexBuffer instance) {
        if (SodiumExtraClientMod.options().detailSettings.sky) {
            instance.bind();
        }
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexFormat;startDrawing(J)V",
                    ordinal = 0
            )
    )
    public void redirectVertexFormatStartDrawing(VertexFormat instance, long pointer) {
        if (SodiumExtraClientMod.options().detailSettings.sky) {
            instance.startDrawing(pointer);
        }
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/VertexBuffer;draw(Lnet/minecraft/util/math/Matrix4f;I)V",
                    ordinal = 0
            )
    )
    public void redirectVertexBufferDraw(VertexBuffer instance, Matrix4f matrix, int mode) {
        if (SodiumExtraClientMod.options().detailSettings.sky) {
            instance.draw(matrix, mode);
        }
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/VertexBuffer;unbind()V",
                    ordinal = 0
            )
    )
    public void redirectVertexBufferUnbind() {
        if (SodiumExtraClientMod.options().detailSettings.sky) {
            VertexBuffer.unbind();
        }
    }

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexFormat;endDrawing()V",
                    ordinal = 0
            )
    )
    public void redirectVertexFormatEndDrawing(VertexFormat instance) {
        if (SodiumExtraClientMod.options().detailSettings.sky) {
            instance.endDrawing();
        }
    }

    @Inject(method = "renderEndSky", at = @At(value = "HEAD"), cancellable = true)
    public void preRenderEndSky(MatrixStack matrices, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().detailSettings.sky) {
            ci.cancel();
        }
    }
}
