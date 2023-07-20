package me.flashyreese.mods.sodiumextra.compat;

import me.jellysquid.mods.sodium.client.render.vertex.formats.ModelVertex;
import me.jellysquid.mods.sodium.client.render.vertex.serializers.VertexSerializer;
import org.lwjgl.system.MemoryUtil;

public class ModelVertexToTerrainSerializer implements VertexSerializer {
    @Override
    public void serialize(long src, long dst, int vertexCount) {
        /*
        var0.put("Position", VertexFormats.POSITION_ELEMENT);
        var0.put("Color", VertexFormats.COLOR_ELEMENT);
        var0.put("UV0", VertexFormats.TEXTURE_ELEMENT);
        var0.put("UV2", VertexFormats.LIGHT_ELEMENT);
        var0.put("Normal", VertexFormats.NORMAL_ELEMENT);
        var0.put("Padding", VertexFormats.PADDING_ELEMENT);
        var0.put("mc_Entity", ENTITY_ELEMENT);
        var0.put("mc_midTexCoord", MID_TEXTURE_ELEMENT);
        var0.put("at_tangent", TANGENT_ELEMENT);
        var0.put("at_midBlock", MID_BLOCK_ELEMENT);
        var0.put("Padding2", VertexFormats.PADDING_ELEMENT);

        	ImmutableMap.builder()
			.put("Position", POSITION_ELEMENT)
			.put("Color", COLOR_ELEMENT)
			.put("UV0", TEXTURE_ELEMENT)
			.put("UV1", OVERLAY_ELEMENT)
			.put("UV2", LIGHT_ELEMENT)
			.put("Normal", NORMAL_ELEMENT)
			.put("Padding", PADDING_ELEMENT)
			.build()
         */
        for(int i = 0; i < vertexCount; ++i) {
            // Position
            MemoryUtil.memPutFloat(dst + 0L, MemoryUtil.memGetFloat(src + 0L));
            MemoryUtil.memPutFloat(dst + 4L, MemoryUtil.memGetFloat(src + 4L));
            MemoryUtil.memPutFloat(dst + 8L, MemoryUtil.memGetFloat(src + 8L));
            // Color
            MemoryUtil.memPutByte(dst + 12L, MemoryUtil.memGetByte(src + 12L));
            MemoryUtil.memPutByte(dst + 13L, MemoryUtil.memGetByte(src + 13L));
            MemoryUtil.memPutByte(dst + 14L, MemoryUtil.memGetByte(src + 14L));
            MemoryUtil.memPutByte(dst + 15L, MemoryUtil.memGetByte(src + 15L));
            // Texture
            MemoryUtil.memPutFloat(dst + 16L, MemoryUtil.memGetFloat(src + 16L));
            MemoryUtil.memPutFloat(dst + 20L, MemoryUtil.memGetFloat(src + 20L));
            // Light
            MemoryUtil.memPutShort(dst + 24L, MemoryUtil.memGetShort(src + 28L));
            MemoryUtil.memPutShort(dst + 26L, MemoryUtil.memGetShort(src + 30L));
            // Normal
            MemoryUtil.memPutByte(dst + 28L, MemoryUtil.memGetByte(src + 32L));
            MemoryUtil.memPutByte(dst + 29L, MemoryUtil.memGetByte(src + 33L));
            MemoryUtil.memPutByte(dst + 30L, MemoryUtil.memGetByte(src + 34L));
            MemoryUtil.memPutByte(dst + 31L, MemoryUtil.memGetByte(src + 35L));
            // Padding
            MemoryUtil.memPutByte(dst + 32L, MemoryUtil.memGetByte(dst + 32L));
            // mc_Entity
            MemoryUtil.memPutShort(dst + 33L, MemoryUtil.memGetShort(dst + 33L));
            MemoryUtil.memPutShort(dst + 35L, MemoryUtil.memGetShort(dst + 35L));
            // mc_midTexCoord
            MemoryUtil.memPutFloat(dst + 37L, MemoryUtil.memGetFloat(dst + 37L));
            MemoryUtil.memPutFloat(dst + 41L, MemoryUtil.memGetFloat(dst + 41L));
            // at_tangent
            MemoryUtil.memPutByte(dst + 45L, MemoryUtil.memGetByte(dst + 45L));
            MemoryUtil.memPutByte(dst + 46L, MemoryUtil.memGetByte(dst + 46L));
            MemoryUtil.memPutByte(dst + 47L, MemoryUtil.memGetByte(dst + 47L));
            MemoryUtil.memPutByte(dst + 48L, MemoryUtil.memGetByte(dst + 48L));
            // at_midBlock
            MemoryUtil.memPutByte(dst + 49L, MemoryUtil.memGetByte(dst + 49L));
            MemoryUtil.memPutByte(dst + 50L, MemoryUtil.memGetByte(dst + 50L));
            MemoryUtil.memPutByte(dst + 51L, MemoryUtil.memGetByte(dst + 51L));
            // Padding
            MemoryUtil.memPutByte(dst + 52L, MemoryUtil.memGetByte(dst + 52L));

            src += ModelVertex.STRIDE;
            dst += IrisCompat.getTerrainFormat().getVertexSizeByte();
        }
    }
}
