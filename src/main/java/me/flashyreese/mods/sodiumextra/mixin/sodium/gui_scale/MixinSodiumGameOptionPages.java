package me.flashyreese.mods.sodiumextra.mixin.sodium.gui_scale;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = SodiumGameOptionPages.class, priority = 999)
public class MixinSodiumGameOptionPages {
    @ModifyConstant(method = "lambda$general$9", constant = @Constant(intValue = 4))
    private static int guiScaleVanilla(int oldValue) {
        return MinecraftClient.getInstance().getWindow().calculateScaleFactor(0, MinecraftClient.getInstance().forcesUnicodeFont());
    }
}