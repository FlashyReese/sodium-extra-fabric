package me.flashyreese.mods.sodiumextra.client.render.terrain.color;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.SmoothBiomeColorBlender;
import me.jellysquid.mods.sodium.client.util.color.ColorARGB;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.Arrays;

public class LinearFlatColorBlender extends SmoothBiomeColorBlender {
    @Override
    public int[] getColors(BlockColorProvider colorizer, BlockRenderView world, BlockState state, BlockPos origin, ModelQuadView quad) {
        int[] colors = super.getColors(colorizer, world, state, origin, quad);
        Arrays.fill(colors, this.getAverageColor(colors));
        return colors;
    }

    private int getAverageColor(int[] colors) {
        int a = Arrays.stream(colors).map(ColorARGB::unpackAlpha).sum();
        int r = Arrays.stream(colors).map(ColorARGB::unpackRed).sum();
        int g = Arrays.stream(colors).map(ColorARGB::unpackGreen).sum();
        int b = Arrays.stream(colors).map(ColorARGB::unpackBlue).sum();

        return ColorARGB.pack(r / colors.length, g / colors.length, b / colors.length, a / colors.length);
    }
}
