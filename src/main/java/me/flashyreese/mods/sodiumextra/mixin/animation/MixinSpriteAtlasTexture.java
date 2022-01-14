package me.flashyreese.mods.sodiumextra.mixin.animation;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.animation.SpriteAnimationExtended;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SpriteAtlasTexture.class)
public abstract class MixinSpriteAtlasTexture extends AbstractTexture {

    @Shadow
    @Final
    private List<TextureTickListener> animatedSprites;

    /**
     * @author FlashyReese
     */
    @Overwrite
    public void tickAnimatedSprites() {
        this.bindTexture();

        if (SodiumExtraClientMod.options().animationSettings.animation) {
            for (TextureTickListener textureTickListener : this.animatedSprites) {
                if (textureTickListener instanceof SpriteAnimationExtended animationExtended) {
                    if (this.shouldAnimate(animationExtended.getId()))
                        textureTickListener.tick();
                }
            }
        }
    }

    private boolean shouldAnimate(Identifier identifier) {
        if (identifier != null) {
            String path = identifier.getPath();
            if (path.endsWith("water_still") || path.endsWith("water_flow")) {
                return SodiumExtraClientMod.options().animationSettings.animateWater;
            } else if (path.endsWith("lava_still") || path.endsWith("lava_flow")) {
                return SodiumExtraClientMod.options().animationSettings.animateLava;
            } else if (path.endsWith("nether_portal")) {
                return SodiumExtraClientMod.options().animationSettings.animatePortal;
            } else if (path.endsWith("fire_0") || path.endsWith("fire_1") || path.endsWith("soul_fire_0") ||
                    path.endsWith("soul_fire_1") || path.endsWith("campfire_fire") || path.endsWith("campfire_log_lit") ||
                    path.endsWith("soul_campfire_fire") || path.endsWith("soul_campfire_log_lit")) {
                return SodiumExtraClientMod.options().animationSettings.animateFire;
            } else if (path.endsWith("magma") || path.endsWith("lantern") || path.endsWith("sea_lantern") ||
                    path.endsWith("soul_lantern") || path.endsWith("kelp") || path.endsWith("kelp_plant") ||
                    path.endsWith("seagrass") || path.endsWith("warped_stem") || path.endsWith("crimson_stem") ||
                    path.endsWith("blast_furnace_front_on") || path.endsWith("smoker_front_on") ||
                    path.endsWith("stonecutter_saw")) {
                return SodiumExtraClientMod.options().animationSettings.blockAnimations;
            }
        }
        return true;
    }
}
