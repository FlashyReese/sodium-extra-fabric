package me.flashyreese.mods.sodiumextra.mixin.animation;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpriteAtlasTexture.class)
public abstract class MixinSpriteAtlasTexture extends AbstractTexture {
    
    @Redirect(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/Sprite;getAnimation()Lnet/minecraft/client/texture/TextureTickListener;"))
    public TextureTickListener sodiumExtra$tickAnimatedSprites(Sprite instance) {
        if (instance.getAnimation() != null && SodiumExtraClientMod.options().animationSettings.animation && this.shouldAnimate(instance.getId()))
            return instance.getAnimation();
        return null;
    }

    private boolean shouldAnimate(Identifier identifier) {
        if (identifier != null) {
            String path = identifier.getPath();
            if (path.endsWith("water_still") || path.endsWith("water_flow")) {
                return SodiumExtraClientMod.options().animationSettings.water;
            } else if (path.endsWith("lava_still") || path.endsWith("lava_flow")) {
                return SodiumExtraClientMod.options().animationSettings.lava;
            } else if (path.endsWith("nether_portal")) {
                return SodiumExtraClientMod.options().animationSettings.portal;
            } else if (path.endsWith("fire_0") || path.endsWith("fire_1") || path.endsWith("soul_fire_0") ||
                    path.endsWith("soul_fire_1") || path.endsWith("campfire_fire") || path.endsWith("campfire_log_lit") ||
                    path.endsWith("soul_campfire_fire") || path.endsWith("soul_campfire_log_lit")) {
                return SodiumExtraClientMod.options().animationSettings.fire;
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
