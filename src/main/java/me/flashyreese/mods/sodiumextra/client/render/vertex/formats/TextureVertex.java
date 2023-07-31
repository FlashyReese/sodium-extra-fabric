package me.flashyreese.mods.sodiumextra.client.render.vertex.formats;

import me.jellysquid.mods.sodium.client.render.vertex.VertexFormatDescription;
import me.jellysquid.mods.sodium.client.render.vertex.VertexFormatRegistry;
import me.jellysquid.mods.sodium.common.util.MatrixHelper;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

public class TextureVertex {
    public static final VertexFormatDescription FORMAT = VertexFormatRegistry.get(VertexFormats.POSITION_TEXTURE);

    public static final int STRIDE = 20;

    private static final int OFFSET_POSITION = 0;
    private static final int OFFSET_TEXTURE = 12;

    public static void write(long ptr, Matrix4f matrix, float x, float y, float z, float u, float v) {
        float xt = MatrixHelper.transformPositionX(matrix, x, y, z);
        float yt = MatrixHelper.transformPositionY(matrix, x, y, z);
        float zt = MatrixHelper.transformPositionZ(matrix, x, y, z);

        write(ptr, xt, yt, zt, u, v);
    }

    public static void write(long ptr, float x, float y, float z, float u, float v) {
        MemoryUtil.memPutFloat(ptr + OFFSET_POSITION + 0, x);
        MemoryUtil.memPutFloat(ptr + OFFSET_POSITION + 4, y);
        MemoryUtil.memPutFloat(ptr + OFFSET_POSITION + 8, z);

        MemoryUtil.memPutFloat(ptr + OFFSET_TEXTURE + 0, u);
        MemoryUtil.memPutFloat(ptr + OFFSET_TEXTURE + 4, v);
    }
}
