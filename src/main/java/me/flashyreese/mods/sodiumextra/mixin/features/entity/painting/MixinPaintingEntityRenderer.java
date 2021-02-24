package me.flashyreese.mods.sodiumextra.mixin.features.entity.painting;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaintingEntityRenderer.class)
public class MixinPaintingEntityRenderer {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(PaintingEntity paintingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().detailSettings.renderPaintings)
            ci.cancel();
    }
}
