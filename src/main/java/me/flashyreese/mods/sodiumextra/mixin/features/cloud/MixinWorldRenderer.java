package me.flashyreese.mods.sodiumextra.mixin.features.cloud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Shadow
    private CloudRenderMode lastCloudsRenderMode;

    // Disabled - Not working as intended
    private void renderClouds(BufferBuilder builder, double x, double y, double z, Vec3d color) {
        float k = (float) MathHelper.floor(x) * 0.00390625F;
        float l = (float) MathHelper.floor(z) * 0.00390625F;
        float m = (float) color.x;
        float n = (float) color.y;
        float o = (float) color.z;
        float p = m * 0.9F;
        float q = n * 0.9F;
        float r = o * 0.9F;
        float s = m * 0.7F;
        float t = n * 0.7F;
        float u = o * 0.7F;
        float v = m * 0.8F;
        float w = n * 0.8F;
        float aa = o * 0.8F;
        builder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
        float ab = (float) Math.floor(y / 4.0D) * 4.0F;
        if (this.lastCloudsRenderMode == CloudRenderMode.FANCY) {
            for (int ac = -3; ac <= 4; ++ac) {
                for (int ad = -3; ad <= 4; ++ad) {
                    float ae = (float) (ac * 8);
                    float af = (float) (ad * 8);
                    if (ab > -5.0F) { //Top
                        builder.vertex((ae + 0.0F), (ab + 0.0F), (af + 8.0F)).texture((ae + 0.0F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(s, t, u, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                        builder.vertex((ae + 8.0F), (ab + 0.0F), (af + 8.0F)).texture((ae + 8.0F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(s, t, u, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                        builder.vertex((ae + 8.0F), (ab + 0.0F), (af + 0.0F)).texture((ae + 8.0F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(s, t, u, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                        builder.vertex((ae + 0.0F), (ab + 0.0F), (af + 0.0F)).texture((ae + 0.0F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(s, t, u, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                    }

                    if (ab <= 5.0F) { //Bottom
                        builder.vertex((ae + 0.0F), (ab + 4.0F - 9.765625E-4F), (af + 8.0F)).texture((ae + 0.0F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, 1.0F, 0.0F).next();
                        builder.vertex((ae + 8.0F), (ab + 4.0F - 9.765625E-4F), (af + 8.0F)).texture((ae + 8.0F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, 1.0F, 0.0F).next();
                        builder.vertex((ae + 8.0F), (ab + 4.0F - 9.765625E-4F), (af + 0.0F)).texture((ae + 8.0F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, 1.0F, 0.0F).next();
                        builder.vertex((ae + 0.0F), (ab + 4.0F - 9.765625E-4F), (af + 0.0F)).texture((ae + 0.0F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, 1.0F, 0.0F).next();
                    }

                    int aj;
                    if (ac > -1) {
                        for (aj = 0; aj < 8; ++aj) {
                            builder.vertex((ae + (float) aj + 0.00000001F), (ab + 0.00000001F), (af + 8.0F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(-1.0F, 0.0F, 0.0F).next();
                            builder.vertex((ae + (float) aj + 0.00000001F), (ab + 3.99999999F), (af + 8.0F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(-1.0F, 0.0F, 0.0F).next();
                            builder.vertex((ae + (float) aj + 0.00000001F), (ab + 3.99999999F), (af + 0.0F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(-1.0F, 0.0F, 0.0F).next();
                            builder.vertex((ae + (float) aj + 0.00000001F), (ab + 0.00000001F), (af + 0.0F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(-1.0F, 0.0F, 0.0F).next();
                        }
                    }

                    if (ac <= 1) {
                        for (aj = 0; aj < 8; ++aj) {
                            builder.vertex((ae + (float) aj + 1.0F - 9.765625E-4F), (ab + 0.00000001F), (af + 7.99999999F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(1.0F, 0.0F, 0.0F).next();
                            builder.vertex((ae + (float) aj + 1.0F - 9.765625E-4F), (ab + 3.99999999F), (af + 7.99999999F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 8.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(1.0F, 0.0F, 0.0F).next();
                            builder.vertex((ae + (float) aj + 1.0F - 9.765625E-4F), (ab + 3.99999999F), (af - 0.00000001F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(1.0F, 0.0F, 0.0F).next();
                            builder.vertex((ae + (float) aj + 1.0F - 9.765625E-4F), (ab + 0.00000001F), (af - 0.00000001F)).texture((ae + (float) aj + 0.5F) * 0.00390625F + k, (af + 0.0F) * 0.00390625F + l).color(p, q, r, 0.8F).normal(1.0F, 0.0F, 0.0F).next();
                        }
                    }

                    if (ad > -1) {
                        for (aj = 0; aj < 8; ++aj) {
                            builder.vertex((ae + 0.00000001F), (ab + 3.99999999F), (af + (float) aj + 0.0F)).texture((ae + 0.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, -1.0F).next();
                            builder.vertex((ae + 8.00000001F), (ab + 3.99999999F), (af + (float) aj + 0.0F)).texture((ae + 8.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, -1.0F).next();
                            builder.vertex((ae + 8.00000001F), (ab + 0.00000001F), (af + (float) aj + 0.0F)).texture((ae + 8.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, -1.0F).next();
                            builder.vertex((ae + 0.00000001F), (ab + 0.00000001F), (af + (float) aj + 0.0F)).texture((ae + 0.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, -1.0F).next();
                        }
                    }

                    if (ad <= 1) {
                        for (aj = 0; aj < 8; ++aj) {
                            builder.vertex((ae + 0.0F), (ab + 3.99999999F), (af + (float) aj + 0.99999999F - 9.765625E-4F)).texture((ae - 0.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, 1.0F).next();
                            builder.vertex((ae + 8.0F), (ab + 3.99999999F), (af + (float) aj + 0.99999999F - 9.765625E-4F)).texture((ae + 8.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, 1.0F).next();
                            builder.vertex((ae + 8.0F), (ab + 0.00000001F), (af + (float) aj + 0.99999999F - 9.765625E-4F)).texture((ae + 8.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, 1.0F).next();
                            builder.vertex((ae + 0.0F), (ab + 0.00000001F), (af + (float) aj + 0.99999999F - 9.765625E-4F)).texture((ae + 0.0F) * 0.00390625F + k, (af + (float) aj + 0.5F) * 0.00390625F + l).color(v, w, aa, 0.8F).normal(0.0F, 0.0F, 1.0F).next();
                        }
                    }
                }
            }
        } else {

            for (int am = -32; am < 32; am += 32) {
                for (int an = -32; an < 32; an += 32) {
                    builder.vertex((am), ab, (an + 32)).texture((float) (am) * 0.00390625F + k, (float) (an + 32) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                    builder.vertex((am + 32), ab, (an + 32)).texture((float) (am + 32) * 0.00390625F + k, (float) (an + 32) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                    builder.vertex((am + 32), ab, (an)).texture((float) (am + 32) * 0.00390625F + k, (float) (an) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                    builder.vertex((am), ab, (an)).texture((float) (am) * 0.00390625F + k, (float) (an) * 0.00390625F + l).color(m, n, o, 0.8F).normal(0.0F, -1.0F, 0.0F).next();
                }
            }
        }

    }

    @Redirect(
            method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;FDDD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/SkyProperties;getCloudsHeight()F")
    )
    private float getCloudHeight(SkyProperties skyProperties) {
        return SodiumExtraClientMod.options().extraSettings.cloudHeight;
    }
}
