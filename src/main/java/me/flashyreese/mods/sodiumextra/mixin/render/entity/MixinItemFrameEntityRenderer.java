package me.flashyreese.mods.sodiumextra.mixin.render.entity;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntityRenderer.class)
public class MixinItemFrameEntityRenderer {

    @Inject(method = "render(Lnet/minecraft/entity/decoration/ItemFrameEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.AFTER), cancellable = true)
    public void render(ItemFrameEntity itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().renderSettings.itemFrame) {
            ci.cancel();
        }
    }

    @Inject(method = "hasLabel(Lnet/minecraft/entity/decoration/ItemFrameEntity;)Z", at = @At(value = "HEAD"), cancellable = true)
    private <T extends ItemFrameEntity> void hasLabel(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (!SodiumExtraClientMod.options().renderSettings.itemFrameNameTag) {
            cir.setReturnValue(false);
        }
    }
}
