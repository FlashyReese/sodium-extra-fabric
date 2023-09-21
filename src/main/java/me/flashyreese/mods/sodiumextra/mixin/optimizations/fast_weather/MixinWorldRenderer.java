package me.flashyreese.mods.sodiumextra.mixin.optimizations.fast_weather;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import me.flashyreese.mods.sodiumextra.client.render.vertex.formats.WeatherVertex;
import me.flashyreese.mods.sodiumextra.common.util.Utils;
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
import org.spongepowered.asm.mixin.Unique;
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
    @Unique
    private final Long2ReferenceMap<Biome> biomeLong2ReferenceMap = new Long2ReferenceOpenHashMap<>();
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
            int abX = MathHelper.floor(cameraX);
            int abY = MathHelper.floor(cameraY);
            int abZ = MathHelper.floor(cameraZ);
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

            for (int z = abZ - l; z <= abZ + l; ++z) {
                for (int x = abX - l; x <= abX + l; ++x) {
                    int p = (z - abZ + 16) * 32 + x - abX + 16;
                    double d = (double) this.NORMAL_LINE_DX[p] * 0.5;
                    double e = (double) this.NORMAL_LINE_DZ[p] * 0.5;
                    mutable.set(x, cameraY, z);
                    long biomePacked = Utils.packPosition(x, z);
                    Biome biome = this.biomeLong2ReferenceMap.computeIfAbsent(biomePacked, key -> world.getBiome(mutable).value());
                    if (biome.hasPrecipitation()) {
                        int q = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                        int r = abY - l;
                        int s = abY + l;
                        if (r < q) {
                            r = q;
                        }

                        if (s < q) {
                            s = q;
                        }

                        int t = Math.max(q, abY);

                        if (r != s) {
                            Random random = Random.create((long) x * x * 3121 + x * 45238971L ^ (long) z * z * 418711 + z * 13761L);
                            mutable.set(x, r, z);
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

                                int u = this.ticks + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31;
                                float rainOffset = -((float) u + tickDelta) / 32.0F * (3.0F + random.nextFloat());
                                double v = (double) x + 0.5 - cameraX;
                                double w = (double) z + 0.5 - cameraZ;
                                float distance = (float) Math.sqrt(v * v + w * w) / (float) l;
                                float y = ((1.0F - distance * distance) * 0.5F + 0.5F) * f;
                                mutable.set(x, t, z);
                                int light = getLightmapCoordinates(world, mutable);

                                VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);

                                int color = ColorABGR.pack(1.0F, 1.0F, 1.0F, y);

                                writeVertices(cameraX, cameraY, cameraZ, z, x, d, e, r, s, rainOffset, light, writer, color);
                            } else if (precipitation == Biome.Precipitation.SNOW) {
                                if (m != 1) {
                                    if (m >= 0) {
                                        tessellator.draw();
                                    }

                                    m = 1;
                                    RenderSystem.setShaderTexture(0, SNOW);
                                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                                }

                                float snowOffset = (float) (random.nextDouble() + (double) g * 0.01 * (double) ((float) random.nextGaussian()));
                                double ac = (double) x + 0.5 - cameraX;
                                double ad = (double) z + 0.5 - cameraZ;
                                float y = (float) Math.sqrt(ac * ac + ad * ad) / (float) l;
                                float ae = ((1.0F - y * y) * 0.3F + 0.5F) * f;
                                mutable.set(x, t, z);
                                int af = getLightmapCoordinates(world, mutable);
                                int ag = af >> 16 & 65535;
                                int ah = af & 65535;
                                int ai = (ag * 3 + 240) / 4;
                                int aj = (ah * 3 + 240) / 4;


                                VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);
                                int color = ColorABGR.pack(1.0F, 1.0F, 1.0F, ae);
                                int light = Utils.packLight(aj, ai);
                                writeVertices(cameraX, cameraY, cameraZ, z, x, d, e, r, s, snowOffset, light, writer, color);
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

    @Inject(method = "reload()V", at = @At(value = "TAIL"))
    private void postReload(CallbackInfo ci) {
        this.biomeLong2ReferenceMap.clear();
    }

    @Unique
    private void writeVertices(double cameraX, double cameraY, double cameraZ, int n, int o, double d, double e, int r, int s, float h, int light, VertexBufferWriter writer, int color) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long buffer = stack.nmalloc(4 * WeatherVertex.STRIDE);
            long ptr = buffer;

            WeatherVertex.put(ptr, (float) (o - cameraX - d + 0.5), (float) (s - cameraY), (float) (n - cameraZ - e + 0.5), color, 0.0F, (float) r * 0.25F + h, light);
            ptr += WeatherVertex.STRIDE;

            WeatherVertex.put(ptr, (float) (o - cameraX + d + 0.5), (float) (s - cameraY), (float) (n - cameraZ + e + 0.5), color, 1.0F, (float) r * 0.25F + h, light);
            ptr += WeatherVertex.STRIDE;

            WeatherVertex.put(ptr, (float) (o - cameraX + d + 0.5), (float) (r - cameraY), (float) (n - cameraZ + e + 0.5), color, 1.0F, (float) s * 0.25F + h, light);
            ptr += WeatherVertex.STRIDE;

            WeatherVertex.put(ptr, (float) (o - cameraX - d + 0.5), (float) (r - cameraY), (float) (n - cameraZ - e + 0.5), color, 0.0F, (float) s * 0.25F + h, light);
            ptr += WeatherVertex.STRIDE;

            writer.push(stack, buffer, 4, WeatherVertex.FORMAT);
        }
    }
}
