package me.flashyreese.mods.sodiumextra.mixin.features.day_time;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(World.class)
public abstract class MixinWorld implements WorldAccess {
    @Override
    public float getSkyAngle(float tickDelta) {
        if (SodiumExtraClientMod.options().extraSettings.dayLightCycle) {
            return this.getDimension().getSkyAngle(this.getLevelProperties().getTimeOfDay());
        }
        return this.getDimension().getSkyAngle(5000L);
    }
}