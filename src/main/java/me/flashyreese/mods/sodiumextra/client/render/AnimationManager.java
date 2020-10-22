package me.flashyreese.mods.sodiumextra.client.render;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.texture.Sprite;

public class AnimationManager {

    public boolean shouldAnimate(Sprite sprite) {
        if (!SodiumExtraClientMod.options().animation.animation) {
            return false;
        } else {
            if (sprite.getId().getPath().endsWith("water_still") || sprite.getId().getPath().endsWith("water_flow")) {
                return SodiumExtraClientMod.options().animation.animateWater;
            } else if (sprite.getId().getPath().endsWith("lava_still") || sprite.getId().getPath().endsWith("lava_flow")) {
                return SodiumExtraClientMod.options().animation.animateLava;
            } else if (sprite.getId().getPath().endsWith("nether_portal")) {
                return SodiumExtraClientMod.options().animation.animatePortal;
            } else if (sprite.getId().getPath().endsWith("fire_0") || sprite.getId().getPath().endsWith("fire_1") || sprite.getId().getPath().endsWith("soul_fire_0") || sprite.getId().getPath().endsWith("soul_fire_1")) {
                return SodiumExtraClientMod.options().animation.animateFire;
            } else if (sprite.getId().getPath().contains("command_block")) {
                return SodiumExtraClientMod.options().animation.animateCommandBlock;
            }
            return true;
        }
    }
}
