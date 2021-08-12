package me.flashyreese.mods.sodiumextra.mixin.features.reduce_resolution_on_mac;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Approach is based on that used by RetiNo, by Julian Dunskus
 * https://github.com/juliand665/retiNO
 * Original is licensed under MIT
 *
 * Code directly pulled from Canvas by grondag
 * https://github.com/grondag/canvas/blob/7e01cf333388bbeb7f31de55266e83c2d3252cae/src/main/java/grondag/canvas/mixin/MixinWindow.java
 * Licensed under Apache-2.0
 */
@Mixin(Window.class)
public class MixinWindow {
    @Shadow
    private int framebufferWidth;

    @Shadow
    private int framebufferHeight;

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwDefaultWindowHints()V"), method = "<init>", remap = false)
    private void onDefaultWindowHints() {
        GLFW.glfwDefaultWindowHints();

        if (MinecraftClient.IS_SYSTEM_MAC && SodiumExtraClientMod.options().extraSettings.reduceResolutionOnMac) {
            GLFW.glfwWindowHint(GLFW.GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW.GLFW_FALSE);
        }
    }

    @Inject(at = @At(value = "RETURN"), method = "updateFramebufferSize")
    private void afterUpdateFrameBufferSize(CallbackInfo ci) {
        // prevents mis-scaled startup screen
        if (MinecraftClient.IS_SYSTEM_MAC && SodiumExtraClientMod.options().extraSettings.reduceResolutionOnMac) {
            framebufferWidth /= 2;
            framebufferHeight /= 2;
        }
    }
}