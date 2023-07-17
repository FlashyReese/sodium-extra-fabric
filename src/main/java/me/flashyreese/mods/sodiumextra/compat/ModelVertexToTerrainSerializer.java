package me.flashyreese.mods.sodiumextra.compat;

import me.jellysquid.mods.sodium.client.render.vertex.formats.ModelVertex;
import me.jellysquid.mods.sodium.client.render.vertex.serializers.VertexSerializer;
import org.lwjgl.system.MemoryUtil;

public class ModelVertexToTerrainSerializer implements VertexSerializer {
    @Override
    public void serialize(long src, long dst, int vertexCount) {
        for(int i = 0; i < vertexCount; ++i) {
            MemoryUtil.memCopy(src, dst, 32);
            MemoryUtil.memPutInt(dst + 32L, 0);
            MemoryUtil.memPutInt(dst + 36L, MemoryUtil.memGetInt(dst + 36L));
            MemoryUtil.memPutInt(dst + 40L, MemoryUtil.memGetInt(dst + 40L));
            MemoryUtil.memPutInt(dst + 44L, MemoryUtil.memGetInt(dst + 44L));
            src += ModelVertex.STRIDE;
            dst += IrisCompat.getTerrainFormat().getVertexSizeByte();
        }
    }
}
