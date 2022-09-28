package me.flashyreese.mods.sodiumextra.mixin.fog;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(DimensionOptions.class)
public interface DimensionOptionsAccessor {
    @Accessor("BASE_DIMENSIONS")
    static Set<RegistryKey<DimensionOptions>> getBaseDimensions() {
        return ImmutableSet.of();
    }
}
