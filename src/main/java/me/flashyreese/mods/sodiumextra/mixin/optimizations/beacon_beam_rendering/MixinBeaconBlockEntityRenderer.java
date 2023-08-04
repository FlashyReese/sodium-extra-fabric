package me.flashyreese.mods.sodiumextra.mixin.optimizations.beacon_beam_rendering;

import me.flashyreese.mods.sodiumextra.compat.IrisCompat;
import me.jellysquid.mods.sodium.client.render.vertex.VertexBufferWriter;
import me.jellysquid.mods.sodium.client.render.vertex.formats.ModelVertex;
import me.jellysquid.mods.sodium.client.util.color.ColorABGR;
import me.jellysquid.mods.sodium.common.util.MatrixHelper;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BeaconBlockEntityRenderer.class)
public class MixinBeaconBlockEntityRenderer {

    @Shadow
    @Final
    public static Identifier BEAM_TEXTURE;

    @Inject(method = "render(Lnet/minecraft/block/entity/BeaconBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "HEAD"), cancellable = true)
    public void render(BeaconBlockEntity beaconBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        ci.cancel();
        if (IrisCompat.isIrisPresent()) {
            if (IrisCompat.isRenderingShadowPass()) {
                return;
            }
        }

        int quads = 16;
        long worldTime = beaconBlockEntity.getWorld().getTime();
        List<BeaconBlockEntity.BeamSegment> list = beaconBlockEntity.getBeamSegments();
        int yOffsetNoneTranslucent = 0;
        int yOffsetTranslucent = 0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long buffer = stack.nmalloc(beaconBlockEntity.getBeamSegments().size() * 2 * quads * ModelVertex.STRIDE);
            long ptr = buffer;
            float innerRadius = 0.2F;
            float outerRadius = 0.25F;
            float heightScale = 1.0F;

            float time = (float) Math.floorMod(worldTime, 40) + tickDelta;

            matrices.push();
            matrices.translate(0.5, 0.0, 0.5);
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 2.25F - 45.0F));
            MatrixStack.Entry entry = matrices.peek();
            Matrix4f positionMatrix = entry.getPositionMatrix();
            Matrix3f normalMatrix = entry.getNormalMatrix();

            var normal = MatrixHelper.transformNormal(normalMatrix, (float) 0.0, (float) 1.0, (float) 0.0);
            for (int m = 0; m < list.size(); ++m) {
                BeaconBlockEntity.BeamSegment beamSegment = list.get(m);

                int maxY = m == list.size() - 1 ? 1024 : beamSegment.getHeight();
                int height = yOffsetNoneTranslucent + maxY;
                float red = beamSegment.getColor()[0];
                float green = beamSegment.getColor()[1];
                float blue = beamSegment.getColor()[2];
                float negativeTime = maxY < 0 ? time : -time;
                float fractionalPart = MathHelper.fractionalPart(negativeTime * 0.2F - (float) MathHelper.floor(negativeTime * 0.1F));
                float innerV2 = -1.0F + fractionalPart;
                float innerV1 = (float) maxY * heightScale * (0.5F / innerRadius) + innerV2;

                int colorNoneTranslucent = ColorABGR.pack(red, green, blue, 1.0F);

                buffer = ptr;
                ptr = writeBeamLayerVertices(ptr, positionMatrix, colorNoneTranslucent, yOffsetNoneTranslucent, height, 0.0F, innerRadius, innerRadius, 0.0F, -innerRadius, 0.0F, 0.0F, -innerRadius, innerV1, innerV2, normal);
                VertexBufferWriter.of(vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, false))).push(stack, buffer, 16, ModelVertex.FORMAT);
                yOffsetNoneTranslucent += beamSegment.getHeight();
            }
            matrices.pop();
            entry = matrices.peek();
            positionMatrix = entry.getPositionMatrix();
            normalMatrix = entry.getNormalMatrix();

            normal = MatrixHelper.transformNormal(normalMatrix, (float) 0.0, (float) 1.0, (float) 0.0);

            for (int m = 0; m < list.size(); ++m) {
                BeaconBlockEntity.BeamSegment beamSegment = list.get(m);

                int maxY = m == list.size() - 1 ? 1024 : beamSegment.getHeight();
                int height = yOffsetTranslucent + maxY;
                float red = beamSegment.getColor()[0];
                float green = beamSegment.getColor()[1];
                float blue = beamSegment.getColor()[2];
                float negativeTime = maxY < 0 ? time : -time;
                float fractionalPart = MathHelper.fractionalPart(negativeTime * 0.2F - (float) MathHelper.floor(negativeTime * 0.1F));
                float innerV2 = -1.0F + fractionalPart;
                float innerV1 = (float) maxY * heightScale + innerV2;

                int colorTranslucent = ColorABGR.pack(red, green, blue, 0.125F);

                buffer = ptr;
                ptr = writeBeamLayerVertices(ptr, positionMatrix, colorTranslucent, yOffsetTranslucent, height, -outerRadius, -outerRadius, outerRadius, -outerRadius, -outerRadius, outerRadius, outerRadius, outerRadius, innerV1, innerV2, normal);
                VertexBufferWriter.of(vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true))).push(stack, buffer, 16, ModelVertex.FORMAT);
                yOffsetTranslucent += beamSegment.getHeight();
            }
            matrices.pop();
        }
    }

    @Unique
    private static long writeBeamLayerVertices(long ptr, Matrix4f positionMatrix, int color, int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float v1, float v2, int normal) {
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
}
