package me.flashyreese.mods.sodiumextra.mixin.cloud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.DimensionEffects;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DimensionEffects.Overworld.class)
public abstract class MixinDimensionEffectsOverworld extends DimensionEffects {
    public MixinDimensionEffectsOverworld(float cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean brightenLighting, boolean darkened) {
        super(cloudsHeight, alternateSkyColor, skyType, brightenLighting, darkened);
    }

    @Override
    public float getCloudsHeight() {
        return SodiumExtraClientMod.options().extraSettings.cloudHeight;
    }
}
