package me.flashyreese.mods.sodiumextra.mixin.fog;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BackgroundRenderer.class)
public interface BackgroundRendererAccessor {
    @Invoker
    static BackgroundRenderer.StatusEffectFogModifier callGetFogModifier(Entity entity, float tickDelta) {
        throw new UnsupportedOperationException();
    }
}
