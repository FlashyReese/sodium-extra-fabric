package me.flashyreese.mods.sodiumextra.client.render.terrain.color;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import me.jellysquid.mods.sodium.client.model.quad.blender.LinearColorBlender;
import me.jellysquid.mods.sodium.client.util.color.ColorARGB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.Arrays;

public class LinearFlatColorBlender extends LinearColorBlender {
    @Override
    public <T> int[] getColors(BlockRenderView world, BlockPos origin, ModelQuadView quad, ColorSampler<T> sampler, T state) {
        int[] colors = super.getColors(world, origin, quad, sampler, state);
        Arrays.fill(colors, this.getAverageColor(colors));
        return colors;
    }

    private <T> int getAverageColor(int[] colors) {
        int a = Arrays.stream(colors).map(ColorARGB::unpackAlpha).sum();
        int r = Arrays.stream(colors).map(ColorARGB::unpackRed).sum();
        int g = Arrays.stream(colors).map(ColorARGB::unpackGreen).sum();
        int b = Arrays.stream(colors).map(ColorARGB::unpackBlue).sum();

        return ColorARGB.pack(r / colors.length, g / colors.length, b / colors.length, a / colors.length);
    }
}
