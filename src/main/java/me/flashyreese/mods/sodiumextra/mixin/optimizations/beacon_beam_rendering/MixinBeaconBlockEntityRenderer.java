package me.flashyreese.mods.sodiumextra.mixin.optimizations.beacon_beam_rendering;

import me.flashyreese.mods.sodiumextra.compat.IrisCompat;
import net.caffeinemc.mods.sodium.api.math.MatrixHelper;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ModelVertex;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BeaconBlockEntityRenderer.class, priority = 1500)
public abstract class MixinBeaconBlockEntityRenderer {

    /**
     * @author FlashyReese
     * @reason Use optimized vertex writer, also avoids unnecessary allocations
     */
    @Inject(method = "renderBeam(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/util/Identifier;FFJII[FFF)V", at = @At(value = "HEAD"), cancellable = true)
    private static void optimizeRenderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, Identifier textureId, float tickDelta, float heightScale, long worldTime, int yOffset, int maxY, float[] color, float innerRadius, float outerRadius, CallbackInfo ci) {
        ci.cancel();
        if (IrisCompat.isIrisPresent()) {
            if (IrisCompat.isRenderingShadowPass()) {
                return;
            }
        }

        int height = yOffset + maxY;
        matrices.push();
        matrices.translate(0.5, 0.0, 0.5);
        float time = (float) Math.floorMod(worldTime, 40) + tickDelta;
        float negativeTime = maxY < 0 ? time : -time;
        float fractionalPart = MathHelper.fractionalPart(negativeTime * 0.2F - (float) MathHelper.floor(negativeTime * 0.1F));
        float red = color[0];
        float green = color[1];
        float blue = color[2];
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 2.25F - 45.0F));
        float innerX1;
        float innerZ2;
        float innerX3 = -innerRadius;
        float innerZ4 = -innerRadius;
        float innerV2 = -1.0F + fractionalPart;
        float innerV1 = (float) maxY * heightScale * (0.5F / innerRadius) + innerV2;

        int colorNoneTranslucent = ColorABGR.pack(red, green, blue, 1.0F);
        int colorTranslucent = ColorABGR.pack(red, green, blue, 0.125F);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            long buffer = stack.nmalloc(2 * 16 * ModelVertex.STRIDE);
            long ptr = buffer;
            ptr = writeBeamLayerVertices(ptr, matrices, colorNoneTranslucent, yOffset, height, 0.0F, innerRadius, innerRadius, 0.0F, innerX3, 0.0F, 0.0F, innerZ4, innerV1, innerV2);
            VertexBufferWriter.of(vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(textureId, false))).push(stack, buffer, 16, ModelVertex.FORMAT);

            matrices.pop();
            innerX1 = -outerRadius;
            float outerZ1 = -outerRadius;
            innerZ2 = -outerRadius;
            innerX3 = -outerRadius;
            innerV2 = -1.0F + fractionalPart;
            innerV1 = (float) maxY * heightScale + innerV2;

            buffer = ptr;
            ptr = writeBeamLayerVertices(ptr, matrices, colorTranslucent, yOffset, height, innerX1, outerZ1, outerRadius, innerZ2, innerX3, outerRadius, outerRadius, outerRadius, innerV1, innerV2);
            VertexBufferWriter.of(vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(textureId, true))).push(stack, buffer, 16, ModelVertex.FORMAT);
        }
        matrices.pop();
    }

    @Unique
    private static long writeBeamLayerVertices(long ptr, MatrixStack matrixStack, int color, int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float v1, float v2) {
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f positionMatrix = entry.getPositionMatrix();
        Matrix3f normalMatrix = entry.getNormalMatrix();

        var normal = MatrixHelper.transformNormal(normalMatrix, (float) 0.0, (float) 1.0, (float) 0.0);

        ptr = transformAndWriteVertex(ptr, positionMatrix, x1, height, z1, color, 1.0f, v1, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x1, yOffset, z1, color, 1.0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x2, yOffset, z2, color, 0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x2, height, z2, color, 0f, v1, normal);

        ptr = transformAndWriteVertex(ptr, positionMatrix, x4, height, z4, color, 1.0f, v1, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x4, yOffset, z4, color, 1.0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x3, yOffset, z3, color, 0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x3, height, z3, color, 0f, v1, normal);

        ptr = transformAndWriteVertex(ptr, positionMatrix, x2, height, z2, color, 1.0f, v1, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x2, yOffset, z2, color, 1.0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x4, yOffset, z4, color, 0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x4, height, z4, color, 0f, v1, normal);

        ptr = transformAndWriteVertex(ptr, positionMatrix, x3, height, z3, color, 1.0f, v1, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x3, yOffset, z3, color, 1.0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x1, yOffset, z1, color, 0f, v2, normal);
        ptr = transformAndWriteVertex(ptr, positionMatrix, x1, height, z1, color, 0f, v1, normal);
        return ptr;
    }

    @Unique
    private static long transformAndWriteVertex(long ptr, Matrix4f positionMatrix, float x, float y, float z, int color, float u, float v, int normal) {
        float transformedX = MatrixHelper.transformPositionX(positionMatrix, x, y, z);
        float transformedY = MatrixHelper.transformPositionY(positionMatrix, x, y, z);
        float transformedZ = MatrixHelper.transformPositionZ(positionMatrix, x, y, z);

        ModelVertex.write(ptr, transformedX, transformedY, transformedZ, color, u, v, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, normal);
        ptr += ModelVertex.STRIDE;
        return ptr;
    }

    @Inject(method = "render(Lnet/minecraft/block/entity/BeaconBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "HEAD"), cancellable = true)
    public void render(BeaconBlockEntity beaconBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        Frustum frustum = ((WorldRendererAccessor) MinecraftClient.getInstance().worldRenderer).getFrustum();
        Box box = new Box(
                beaconBlockEntity.getPos().getX() - 1.0,
                beaconBlockEntity.getPos().getY() - 1.0,
                beaconBlockEntity.getPos().getZ() - 1.0,
                beaconBlockEntity.getPos().getX() + 1.0,
                beaconBlockEntity.getPos().getY() + (beaconBlockEntity.getBeamSegments().isEmpty() ? 1.0 : 1024.0), // todo: probably want to limit this to max height vanilla overshoots as well
                beaconBlockEntity.getPos().getZ() + 1.0);

        if (!frustum.isVisible(box)) {
            ci.cancel();
        }
    }
}
