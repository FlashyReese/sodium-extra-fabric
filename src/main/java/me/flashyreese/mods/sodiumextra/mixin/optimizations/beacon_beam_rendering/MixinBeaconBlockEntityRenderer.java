package me.flashyreese.mods.sodiumextra.mixin.optimizations.beacon_beam_rendering;

import me.jellysquid.mods.sodium.client.render.RenderGlobal;
import me.jellysquid.mods.sodium.client.render.vertex.VertexBufferWriter;
import me.jellysquid.mods.sodium.client.render.vertex.formats.ModelVertex;
import me.jellysquid.mods.sodium.client.util.color.ColorABGR;
import me.jellysquid.mods.sodium.common.util.MatrixHelper;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer.BEAM_TEXTURE;

@Mixin(BeaconBlockEntityRenderer.class)
public class MixinBeaconBlockEntityRenderer {

    @Inject(method = "render(Lnet/minecraft/block/entity/BeaconBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "HEAD"), cancellable = true)
    public void sodiumExtra$render(BeaconBlockEntity beaconBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci){
        long l = beaconBlockEntity.getWorld().getTime();
        List<BeaconBlockEntity.BeamSegment> list = beaconBlockEntity.getBeamSegments();
        int k = 0;

        VertexConsumer noneTranslucent = vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, false));
        VertexBufferWriter writer = VertexBufferWriter.of(noneTranslucent);
        VertexConsumer translucent = vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true));
        VertexBufferWriter writer2 = VertexBufferWriter.of(translucent);

        for(int m = 0; m < list.size(); ++m) {
            BeaconBlockEntity.BeamSegment beamSegment = list.get(m);
            renderBeam(matrixStack, writer, writer2, f, 1.0F, l, k, m == list.size() - 1 ? 1024 : beamSegment.getHeight(), beamSegment.getColor(), 0.2F, 0.25F);
            k += beamSegment.getHeight();
        }
        ci.cancel();
    }

    private static void renderBeam(MatrixStack matrices, VertexBufferWriter noneTranslucent, VertexBufferWriter translucent, float tickDelta, float heightScale, long worldTime, int yOffset, int maxY, float[] color, float innerRadius, float outerRadius) {
        int height = yOffset + maxY;
        matrices.push();
        matrices.translate(0.5, 0.0, 0.5);
        float f = (float) Math.floorMod(worldTime, 40) + tickDelta;
        float g = maxY < 0 ? f : -f;
        float h = MathHelper.fractionalPart(g * 0.2F - (float) MathHelper.floor(g * 0.1F));
        float red = color[0];
        float green = color[1];
        float blue = color[2];
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * 2.25F - 45.0F));
        float x1;
        float z2;
        float x3 = -innerRadius;
        float z4 = -innerRadius;
        float v2 = -1.0F + h;
        float v1 = (float) maxY * heightScale * (0.5F / innerRadius) + v2;

        int color1 = ColorABGR.pack(red, green, blue, 1.0F);
        int color2 = ColorABGR.pack(red, green, blue, 0.125F);


        try (MemoryStack stack = RenderGlobal.VERTEX_DATA.push()) {
            long buffer = stack.nmalloc(2 * 16 * ModelVertex.STRIDE);
            long ptr = buffer;
            ptr = writeBeamLayerVertices(ptr, matrices, color1, yOffset, height, 0.0F, innerRadius, innerRadius, 0.0F, x3, 0.0F, 0.0F, z4, v1, v2);
            noneTranslucent.push(stack, buffer, 16, ModelVertex.FORMAT);

            matrices.pop();
            x1 = -outerRadius;
            float z1 = -outerRadius;
            z2 = -outerRadius;
            x3 = -outerRadius;
            v2 = -1.0F + h;
            v1 = (float) maxY * heightScale + v2;

            ptr = writeBeamLayerVertices(ptr, matrices, color2, yOffset, height, x1, z1, outerRadius, z2, x3, outerRadius, outerRadius, outerRadius, v1, v2);
            translucent.push(stack, buffer, 16, ModelVertex.FORMAT);
        }
        matrices.pop();
    }

    private static long writeBeamLayerVertices(
            long ptr,
            MatrixStack matrices,
            int color,
            int yOffset,
            int height,
            float x1, float z1,
            float x2, float z2,
            float x3, float z3,
            float x4, float z4,
            float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        // The packed transformed normal vector
        var normal = MatrixHelper.transformNormal(matrix3f, (float) 0.0, (float) 1.0, (float) 0.0);

        // section
        transformWrite(ptr, matrix4f, x1, height, z1, color, 1.0f, v1, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x1, yOffset, z1, color, 1.0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x2, yOffset, z2, color, 0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x2, height, z2, color, 0f, v1, normal);
        ptr += ModelVertex.STRIDE;

        // section
        transformWrite(ptr, matrix4f, x4, height, z4, color, 1.0f, v1, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x4, yOffset, z4, color, 1.0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x3, yOffset, z3, color, 0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x3, height, z3, color, 0f, v1, normal);
        ptr += ModelVertex.STRIDE;

        // section
        transformWrite(ptr, matrix4f, x2, height, z2, color, 1.0f, v1, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x2, yOffset, z2, color, 1.0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x4, yOffset, z4, color, 0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x4, height, z4, color, 0f, v1, normal);
        ptr += ModelVertex.STRIDE;

        // section
        transformWrite(ptr, matrix4f, x3, height, z3, color, 1.0f, v1, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x3, yOffset, z3, color, 1.0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x1, yOffset, z1, color, 0f, v2, normal);
        ptr += ModelVertex.STRIDE;

        transformWrite(ptr, matrix4f, x1, height, z1, color, 0f, v1, normal);
        ptr += ModelVertex.STRIDE;
        return ptr;
    }

    private static void transformWrite(long ptr, Matrix4f matPosition, float x, float y, float z, int color, float u, float v, int normal) {
        // The transformed position vector
        float xt = MatrixHelper.transformPositionX(matPosition, x, y, z);
        float yt = MatrixHelper.transformPositionY(matPosition, x, y, z);
        float zt = MatrixHelper.transformPositionZ(matPosition, x, y, z);

        ModelVertex.write(ptr, xt, yt, zt, color, u, v, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, normal);
    }

}
