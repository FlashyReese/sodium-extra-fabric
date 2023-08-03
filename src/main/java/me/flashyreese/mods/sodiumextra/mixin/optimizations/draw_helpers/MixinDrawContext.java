package me.flashyreese.mods.sodiumextra.mixin.optimizations.draw_helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.render.vertex.formats.TextureColorVertex;
import me.flashyreese.mods.sodiumextra.client.render.vertex.formats.TextureVertex;
import me.flashyreese.mods.sodiumextra.common.util.ColorRGBA;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ColorVertex;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class MixinDrawContext {

    @Shadow
    @Final
    private MatrixStack matrices;

    @Shadow
    @Final
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Shadow
    @Deprecated
    protected abstract void tryDraw();

    /**
     * @author FlashyReese
     * @reason Impl Sodium's vertex writer
     */
    @Inject(method = "fillGradient(Lnet/minecraft/client/render/VertexConsumer;IIIIIII)V", at = @At(value = "HEAD"), cancellable = true)
    private void fillGradient(VertexConsumer vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd, CallbackInfo ci) {
        VertexBufferWriter writer = VertexBufferWriter.of(vertexConsumer);
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        colorStart = ColorRGBA.fromOrToABGR(ColorRGBA.fromARGB(colorStart));
        colorEnd = ColorRGBA.fromOrToABGR(ColorRGBA.fromARGB(colorEnd));
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final long buffer = stack.nmalloc(4 * ColorVertex.STRIDE);
            long ptr = buffer;

            ColorVertex.put(ptr, matrix4f, startX, startY, z, colorStart);
            ptr += ColorVertex.STRIDE;

            ColorVertex.put(ptr, matrix4f, startX, endY, z, colorEnd);
            ptr += ColorVertex.STRIDE;

            ColorVertex.put(ptr, matrix4f, endX, endY, z, colorEnd);
            ptr += ColorVertex.STRIDE;

            ColorVertex.put(ptr, matrix4f, endX, startY, z, colorStart);
            ptr += ColorVertex.STRIDE;

            writer.push(stack, buffer, 4, ColorVertex.FORMAT);
        }
        ci.cancel();
    }

    /**
     * @author FlashyReese
     * @reason Impl Sodium's vertex writer
     */
    @Inject(method = "fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V", at = @At(value = "HEAD"), cancellable = true)
    public void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int z, int color, CallbackInfo ci) {
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }
        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(layer);
        VertexBufferWriter writer = VertexBufferWriter.of(vertexConsumer);
        color = ColorRGBA.fromOrToABGR(ColorRGBA.fromARGB(color));
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final long buffer = stack.nmalloc(4 * ColorVertex.STRIDE);
            long ptr = buffer;

            ColorVertex.put(ptr, matrix4f, x1, y1, z, color);
            ptr += ColorVertex.STRIDE;

            ColorVertex.put(ptr, matrix4f, x1, y2, z, color);
            ptr += ColorVertex.STRIDE;

            ColorVertex.put(ptr, matrix4f, x2, y2, z, color);
            ptr += ColorVertex.STRIDE;

            ColorVertex.put(ptr, matrix4f, x2, y1, z, color);
            ptr += ColorVertex.STRIDE;

            writer.push(stack, buffer, 4, ColorVertex.FORMAT);
        }
        this.tryDraw();
        ci.cancel();
    }


    /**
     * @author FlashyReese
     * @reason Impl Sodium's vertex writer
     */
    @Inject(method = "drawTexturedQuad(Lnet/minecraft/util/Identifier;IIIIIFFFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void drawTexturedQuad(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, CallbackInfo ci) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final long buffer = stack.nmalloc(4 * TextureVertex.STRIDE);
            long ptr = buffer;

            TextureVertex.write(ptr, matrix4f, x1, y1, z, u1, v1);
            ptr += TextureVertex.STRIDE;

            TextureVertex.write(ptr, matrix4f, x1, y2, z, u1, v2);
            ptr += TextureVertex.STRIDE;

            TextureVertex.write(ptr, matrix4f, x2, y2, z, u2, v2);
            ptr += TextureVertex.STRIDE;

            TextureVertex.write(ptr, matrix4f, x2, y1, z, u2, v1);
            ptr += TextureVertex.STRIDE;

            writer.push(stack, buffer, 4, TextureVertex.FORMAT);
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        ci.cancel();
    }

    /**
     * @author FlashyReese
     * @reason Impl Sodium's vertex writer
     */
    @Inject(method = "drawTexturedQuad(Lnet/minecraft/util/Identifier;IIIIIFFFFFFFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void drawTexturedQuad(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha, CallbackInfo ci) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);
        int color = ColorABGR.pack(red, green, blue, alpha);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final long buffer = stack.nmalloc(4 * TextureColorVertex.STRIDE);
            long ptr = buffer;

            TextureColorVertex.write(ptr, matrix4f, x1, y1, z, color, u1, v1);
            ptr += TextureColorVertex.STRIDE;

            TextureColorVertex.write(ptr, matrix4f, x1, y2, z, color, u1, v2);
            ptr += TextureColorVertex.STRIDE;

            TextureColorVertex.write(ptr, matrix4f, x2, y2, z, color, u2, v2);
            ptr += TextureColorVertex.STRIDE;

            TextureColorVertex.write(ptr, matrix4f, x2, y1, z, color, u2, v1);
            ptr += TextureColorVertex.STRIDE;

            writer.push(stack, buffer, 4, TextureColorVertex.FORMAT);
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
        ci.cancel();
    }
}
