package me.flashyreese.mods.sodiumextra.mixin.entity;

import net.minecraft.client.render.entity.GuardianEntityRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuardianEntityRenderer.class)
public class MixinGuardianEntityRenderer {
    /**
     * @reason Use the time of day instead of the time of the world in guardian beam rendering
     * @author AMereBagatelle
     */
    @Redirect(method = "render(Lnet/minecraft/entity/mob/GuardianEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTime()J"))
    private long useTimeOfDay(World world) {
        return world.getTimeOfDay();
    }
}
