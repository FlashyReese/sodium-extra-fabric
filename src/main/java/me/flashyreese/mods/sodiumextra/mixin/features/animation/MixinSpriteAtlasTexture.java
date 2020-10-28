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
            if (this.shouldAnimate(sprite)) {
                sprite.tickAnimation();
            }
        }
    }

    private boolean shouldAnimate(Sprite sprite) {
        if (!SodiumExtraClientMod.options().animationSettings.animation) {
            return false;
        } else {
            if (sprite.getId().getPath().endsWith("water_still") || sprite.getId().getPath().endsWith("water_flow")) {
                return SodiumExtraClientMod.options().animationSettings.animateWater;
            } else if (sprite.getId().getPath().endsWith("lava_still") || sprite.getId().getPath().endsWith("lava_flow")) {
                return SodiumExtraClientMod.options().animationSettings.animateLava;
            } else if (sprite.getId().getPath().endsWith("nether_portal")) {
                return SodiumExtraClientMod.options().animationSettings.animatePortal;
            } else if (sprite.getId().getPath().endsWith("fire_0") || sprite.getId().getPath().endsWith("fire_1") || sprite.getId().getPath().endsWith("soul_fire_0") || sprite.getId().getPath().endsWith("soul_fire_1")) {
                return SodiumExtraClientMod.options().animationSettings.animateFire;
            }
            return true;
        }
    }
}
