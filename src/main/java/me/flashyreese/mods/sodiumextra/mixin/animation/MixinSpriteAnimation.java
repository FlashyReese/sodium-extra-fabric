package me.flashyreese.mods.sodiumextra.mixin.animation;

import me.flashyreese.mods.sodiumextra.client.animation.SpriteAnimationExtended;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Sprite.Animation.class)
public class MixinSpriteAnimation implements SpriteAnimationExtended {

    private Identifier identifier;

    @Override
    public void setId(Identifier id) {
        this.identifier = id;
    }

    @Override
    public Identifier getId() {
        return this.identifier;
    }
}
