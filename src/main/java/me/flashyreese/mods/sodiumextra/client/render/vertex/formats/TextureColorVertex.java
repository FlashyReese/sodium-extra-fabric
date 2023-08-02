package me.flashyreese.mods.sodiumextra.client.render.vertex.formats;

import net.caffeinemc.mods.sodium.api.math.MatrixHelper;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatRegistry;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

public class TextureColorVertex {
    public static final VertexFormatDescription FORMAT = VertexFormatRegistry.instance().get(VertexFormats.POSITION_COLOR_TEXTURE);

    public static final int STRIDE = 24;

    private static final int OFFSET_POSITION = 0;
    private static final int OFFSET_COLOR = 12;
    private static final int OFFSET_TEXTURE = 16;

    public static void write(long ptr, Matrix4f matrix, float x, float y, float z, int color, float u, float v) {
        float xt = MatrixHelper.transformPositionX(matrix, x, y, z);
        float yt = MatrixHelper.transformPositionY(matrix, x, y, z);
        float zt = MatrixHelper.transformPositionZ(matrix, x, y, z);

        write(ptr, xt, yt, zt, color, u, v);
    }

    public static void write(long ptr, float x, float y, float z, int color, float u, float v) {
        MemoryUtil.memPutFloat(ptr + OFFSET_POSITION, x);
        MemoryUtil.memPutFloat(ptr + OFFSET_POSITION + 4, y);
        MemoryUtil.memPutFloat(ptr + OFFSET_POSITION + 8, z);

        MemoryUtil.memPutInt(ptr + OFFSET_COLOR, color);

        MemoryUtil.memPutFloat(ptr + OFFSET_TEXTURE, u);
        MemoryUtil.memPutFloat(ptr + OFFSET_TEXTURE + 4, v);
    }
}
