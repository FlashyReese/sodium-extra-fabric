package me.flashyreese.mods.sodiumextra.mixin.sodium.biome_blend;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.BiomeColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(value = BiomeColorBlender.class, remap = false)
public abstract class MixinBiomeColorBlender {
    @Shadow
    @Final
    private boolean useSmoothBlending;
    @Shadow
    @Final
    private int[] cachedRet;

    @Shadow
    protected abstract <T> void getColorsLinear(BlockRenderView world, BlockPos origin, ModelQuadView quad, ColorSampler<T> sampler, T state, int[] colors);

    /**
     * @author FlashyReese
     */
    @Inject(method = "getColors", at = @At(value = "HEAD"), cancellable = true)
    private <T> void getColors(BlockRenderView world, BlockPos origin, ModelQuadView quad, ColorSampler<T> sampler, T state, CallbackInfoReturnable<int[]> cir) {
        if (this.useSmoothBlending && SodiumExtraClientMod.options().renderSettings.useLinearFlatColorBlender) {
            int[] colors = this.cachedRet;
            this.getColorsLinearFlat(world, origin, quad, sampler, state, colors);
            cir.setReturnValue(colors);
        }
    }

    private <T> void getColorsLinearFlat(BlockRenderView world, BlockPos origin, ModelQuadView quad, ColorSampler<T> sampler, T state, int[] colors) {
        this.getColorsLinear(world, origin, quad, sampler, state, colors);

        int a = Arrays.stream(colors).map(ColorARGB::unpackAlpha).sum();
        int r = Arrays.stream(colors).map(ColorARGB::unpackRed).sum();
        int g = Arrays.stream(colors).map(ColorARGB::unpackGreen).sum();
        int b = Arrays.stream(colors).map(ColorARGB::unpackBlue).sum();

        int color = ColorARGB.pack(r / colors.length, g / colors.length, b / colors.length, a / colors.length);
        Arrays.fill(colors, color);
    }
}
