package me.flashyreese.mods.sodiumextra.mixin.steady_debug_hud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public abstract class MixinDebugHud {

    @Unique
    private final List<String> leftTextCache = new ArrayList<>();
    @Unique
    private final List<String> rightTextCache = new ArrayList<>();
    @Unique
    private long nextTime = 0L;
    @Unique
    private boolean rebuild = true;

    @Shadow
    protected abstract void drawText(DrawContext context, List<String> text, boolean left);

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void preRender(DrawContext context, CallbackInfo ci) {
        if (SodiumExtraClientMod.options().extraSettings.steadyDebugHud) {
            final long currentTime = Util.getMeasuringTimeMs();
            if (currentTime > this.nextTime) {
                this.rebuild = true;
                this.nextTime = currentTime + (SodiumExtraClientMod.options().extraSettings.steadyDebugHudRefreshRate * 50L);
            } else {
                this.rebuild = false;
            }
        } else {
            this.rebuild = true;
        }
    }


    @Redirect(method = "drawLeftText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V"))
    public void sodiumExtra$redirectDrawLeftText(DebugHud instance, DrawContext context, List<String> text, boolean left) {
        if (this.rebuild) {
            this.leftTextCache.clear();
            this.leftTextCache.addAll(text);
        }
        this.drawText(context, this.leftTextCache, left);
    }

    @Redirect(method = "drawRightText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V"))
    public void sodiumExtra$redirectDrawRightText(DebugHud instance, DrawContext context, List<String> text, boolean left) {
        if (this.rebuild) {
            this.rightTextCache.clear();
            this.rightTextCache.addAll(text);
        }
        this.drawText(context, this.rightTextCache, left);
    }
}
