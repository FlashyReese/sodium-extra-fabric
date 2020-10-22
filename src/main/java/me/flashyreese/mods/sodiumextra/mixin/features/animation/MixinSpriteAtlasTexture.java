package me.flashyreese.mods.sodiumextra.mixin.features.animation;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SpriteAtlasTexture.class)
public abstract class MixinSpriteAtlasTexture extends AbstractTexture {

    @Shadow
    @Final
    private List<Sprite> animatedSprites;

    /**
     * @author FlashyReese
     */
    @Overwrite
    public void tickAnimatedSprites() {
        this.bindTexture();
        for (Sprite sprite : this.animatedSprites) {
            if (SodiumExtraClientMod.getAnimationManager().shouldAnimate(sprite)) {
                sprite.tickAnimation();
            }
        }
    }
}
