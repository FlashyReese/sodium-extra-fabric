package me.flashyreese.mods.sodiumextra.mixin.render.block.entity;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BeaconBlockEntityRenderer.class, priority = 999)
public class MixinBeaconBlockEntityRenderer {
    @Inject(method = "render(Lnet/minecraft/block/entity/BeaconBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "HEAD"), cancellable = true)
    public void render(BeaconBlockEntity beaconBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().renderSettings.beaconBeam)
            ci.cancel();
    }
}
