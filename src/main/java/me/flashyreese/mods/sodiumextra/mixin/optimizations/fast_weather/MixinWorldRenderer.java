package me.flashyreese.mods.sodiumextra.mixin.optimizations.fast_weather;

import com.mojang.blaze3d.systems.RenderSystem;
import me.flashyreese.mods.sodiumextra.client.render.vertex.formats.WeatherVertex;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.render.WorldRenderer.getLightmapCoordinates;

@Mixin(value = WorldRenderer.class, priority = 1500)
public class MixinWorldRenderer {
    @Shadow
    @Final
    private static Identifier RAIN;
    @Shadow
    @Final
    private static Identifier SNOW;
    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int ticks;
    @Shadow
    @Final
    private float[] NORMAL_LINE_DX;
    @Shadow
    @Final
    private float[] NORMAL_LINE_DZ;

    @Inject(method = "renderWeather", at = @At(value = "HEAD"), cancellable = true)
    public void sodiumExtra$renderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        assert this.client.world != null;
        float f = this.client.world.getRainGradient(tickDelta);
        if (!(f <= 0.0F)) {
            manager.enable();
            World world = this.client.world;
            int i = MathHelper.floor(cameraX);
            int j = MathHelper.floor(cameraY);
            int k = MathHelper.floor(cameraZ);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            int l = 5;
            if (MinecraftClient.isFancyGraphicsOrBetter()) {
                l = 10;
            }

            RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
            int m = -1;
            float g = (float) this.ticks + tickDelta;
            RenderSystem.setShader(GameRenderer::getParticleProgram);
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int n = k - l; n <= k + l; ++n) {
                for (int o = i - l; o <= i + l; ++o) {
                    int p = (n - k + 16) * 32 + o - i + 16;
                    double d = (double) this.NORMAL_LINE_DX[p] * 0.5;
                    double e = (double) this.NORMAL_LINE_DZ[p] * 0.5;
                    mutable.set(o, cameraY, n);
                    Biome biome = world.getBiome(mutable).value();
                    if (biome.hasPrecipitation()) {
                        int q = world.getTopY(Heightmap.Type.MOTION_BLOCKING, o, n);
                        int r = j - l;
                        int s = j + l;
                        if (r < q) {
                            r = q;
                        }

                        if (s < q) {
                            s = q;
                        }

                        int t = Math.max(q, j);

                        if (r != s) {
                            Random random = Random.create((long) o * o * 3121 + o * 45238971L ^ (long) n * n * 418711 + n * 13761L);
                            mutable.set(o, r, n);
                            Biome.Precipitation precipitation = biome.getPrecipitation(mutable);
                            if (precipitation == Biome.Precipitation.RAIN) {
                                if (m != 0) {
                                    if (m >= 0) {
                                        tessellator.draw();
                                    }

                                    m = 0;
                                    RenderSystem.setShaderTexture(0, RAIN);
                                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                                }

                                int u = this.ticks + o * o * 3121 + o * 45238971 + n * n * 418711 + n * 13761 & 31;
                                float h = -((float) u + tickDelta) / 32.0F * (3.0F + random.nextFloat());
                                double v = (double) o + 0.5 - cameraX;
                                double w = (double) n + 0.5 - cameraZ;
                                float x = (float) Math.sqrt(v * v + w * w) / (float) l;
                                float y = ((1.0F - x * x) * 0.5F + 0.5F) * f;
                                mutable.set(o, t, n);
                                int z = getLightmapCoordinates(world, mutable);

                                VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);

                                int color = ColorABGR.pack(1.0F, 1.0F, 1.0F, y);

                                try (MemoryStack stack = MemoryStack.stackPush()) {
                                    long buffer = stack.nmalloc(4 * WeatherVertex.STRIDE);
                                    long ptr = buffer;

                                    WeatherVertex.put(ptr, (float) (o - cameraX - d + 0.5), (float) (s - cameraY), (float) (n - cameraZ - e + 0.5), color, 0.0F, (float) r * 0.25F + h, z);
                                    ptr += WeatherVertex.STRIDE;

                                    WeatherVertex.put(ptr, (float) (o - cameraX + d + 0.5), (float) (s - cameraY), (float) (n - cameraZ + e + 0.5), color, 1.0F, (float) r * 0.25F + h, z);
                                    ptr += WeatherVertex.STRIDE;

                                    WeatherVertex.put(ptr, (float) (o - cameraX + d + 0.5), (float) (r - cameraY), (float) (n - cameraZ + e + 0.5), color, 1.0F, (float) s * 0.25F + h, z);
                                    ptr += WeatherVertex.STRIDE;

                                    WeatherVertex.put(ptr, (float) (o - cameraX - d + 0.5), (float) (r - cameraY), (float) (n - cameraZ - e + 0.5), color, 0.0F, (float) s * 0.25F + h, z);
                                    ptr += WeatherVertex.STRIDE;

                                    writer.push(stack, buffer, 4, WeatherVertex.FORMAT);
                                }
                            } else if (precipitation == Biome.Precipitation.SNOW) {
                                if (m != 1) {
                                    if (m >= 0) {
                                        tessellator.draw();
                                    }

                                    m = 1;
                                    RenderSystem.setShaderTexture(0, SNOW);
                                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                                }

                                float h = (float) (random.nextDouble() + (double) g * 0.01 * (double) ((float) random.nextGaussian()));
                                double ac = (double) o + 0.5 - cameraX;
                                double ad = (double) n + 0.5 - cameraZ;
                                float y = (float) Math.sqrt(ac * ac + ad * ad) / (float) l;
                                float ae = ((1.0F - y * y) * 0.3F + 0.5F) * f;
                                mutable.set(o, t, n);
                                int af = getLightmapCoordinates(world, mutable);
                                int ag = af >> 16 & 65535;
                                int ah = af & 65535;
                                int ai = (ag * 3 + 240) / 4;
                                int aj = (ah * 3 + 240) / 4;


                                VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);
                                int color = ColorABGR.pack(1.0F, 1.0F, 1.0F, ae);
                                try (MemoryStack stack = MemoryStack.stackPush()) {
                                    long buffer = stack.nmalloc(4 * WeatherVertex.STRIDE);
                                    long ptr = buffer;

                                    WeatherVertex.put(ptr, (float) (o - cameraX - d + 0.5), (float) (s - cameraY), (float) (n - cameraZ - e + 0.5), color, 0.0F, (float) r * 0.25F + h, aj, ai);
                                    ptr += WeatherVertex.STRIDE;

                                    WeatherVertex.put(ptr, (float) (o - cameraX + d + 0.5), (float) (s - cameraY), (float) (n - cameraZ + e + 0.5), color, 1.0F, (float) r * 0.25F + h, aj, ai);
                                    ptr += WeatherVertex.STRIDE;

                                    WeatherVertex.put(ptr, (float) (o - cameraX + d + 0.5), (float) (r - cameraY), (float) (n - cameraZ + e + 0.5), color, 1.0F, (float) s * 0.25F + h, aj, ai);
                                    ptr += WeatherVertex.STRIDE;

                                    WeatherVertex.put(ptr, (float) (o - cameraX - d + 0.5), (float) (r - cameraY), (float) (n - cameraZ - e + 0.5), color, 0.0F, (float) s * 0.25F + h, aj, ai);
                                    ptr += WeatherVertex.STRIDE;

                                    writer.push(stack, buffer, 4, WeatherVertex.FORMAT);
                                }
                            }
                        }
                    }
                }
            }

            if (m >= 0) {
                tessellator.draw();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            manager.disable();
        }
        ci.cancel();
    }
}
