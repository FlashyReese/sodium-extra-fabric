package me.flashyreese.mods.sodiumextra.mixin.animation;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpriteAtlasTexture.class)
public abstract class MixinSpriteAtlasTexture extends AbstractTexture {

    @Redirect(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/Sprite;isAnimated()Z"))
    public boolean sodiumExtra$tickAnimatedSprites(Sprite instance) {
        return instance.isAnimated() && SodiumExtraClientMod.options().animationSettings.animation && this.shouldAnimate(instance);
    }

    private boolean shouldAnimate(Sprite sprite) {
        if (sprite.getId().getPath().endsWith("water_still") || sprite.getId().getPath().endsWith("water_flow")) {
            return SodiumExtraClientMod.options().animationSettings.water;
        } else if (sprite.getId().getPath().endsWith("lava_still") || sprite.getId().getPath().endsWith("lava_flow")) {
            return SodiumExtraClientMod.options().animationSettings.lava;
        } else if (sprite.getId().getPath().endsWith("nether_portal")) {
            return SodiumExtraClientMod.options().animationSettings.portal;
        } else if (sprite.getId().getPath().endsWith("fire_0") || sprite.getId().getPath().endsWith("fire_1") || sprite.getId().getPath().endsWith("soul_fire_0") ||
                sprite.getId().getPath().endsWith("soul_fire_1") || sprite.getId().getPath().endsWith("campfire_fire") || sprite.getId().getPath().endsWith("campfire_log_lit") ||
                sprite.getId().getPath().endsWith("soul_campfire_fire") || sprite.getId().getPath().endsWith("soul_campfire_log_lit")) {
            return SodiumExtraClientMod.options().animationSettings.fire;
        } else if (sprite.getId().getPath().endsWith("magma") || sprite.getId().getPath().endsWith("lantern") || sprite.getId().getPath().endsWith("sea_lantern") ||
                sprite.getId().getPath().endsWith("soul_lantern") || sprite.getId().getPath().endsWith("kelp") || sprite.getId().getPath().endsWith("kelp_plant") ||
                sprite.getId().getPath().endsWith("seagrass") || sprite.getId().getPath().endsWith("warped_stem") || sprite.getId().getPath().endsWith("crimson_stem") ||
                sprite.getId().getPath().endsWith("blast_furnace_front_on") || sprite.getId().getPath().endsWith("smoker_front_on") ||
                sprite.getId().getPath().endsWith("stonecutter_saw")) {
            return SodiumExtraClientMod.options().animationSettings.blockAnimations;
        }
        return sprite.isAnimated();
    }
}
