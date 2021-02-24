package me.flashyreese.mods.sodiumextra.mixin.features.block.entity.piston;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.PistonBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonBlockEntityRenderer.class)
public class MixinPistonBlockEntityRenderer {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(PistonBlockEntity pistonBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().animationSettings.pistonAnimations)
            ci.cancel();
    }
}
