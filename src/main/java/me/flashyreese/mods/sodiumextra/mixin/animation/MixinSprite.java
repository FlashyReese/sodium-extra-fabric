package me.flashyreese.mods.sodiumextra.mixin.animation;

import me.flashyreese.mods.sodiumextra.client.animation.SpriteAnimationExtended;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sprite.class)
public class MixinSprite {

    @Shadow
    @Final
    private Identifier id;

    @Inject(method = "createAnimation", at = @At(value = "RETURN"), cancellable = true)
    public void createAnimation(Sprite.Info info, int nativeImageWidth, int nativeImageHeight, int maxLevel, CallbackInfoReturnable<Sprite.Animation> cir) {
        Sprite.Animation value = cir.getReturnValue();
        if (value != null)
            ((SpriteAnimationExtended)value).setId(this.id);
    }
}
