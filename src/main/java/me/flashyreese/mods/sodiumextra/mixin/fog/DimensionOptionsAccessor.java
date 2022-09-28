package me.flashyreese.mods.sodiumextra.mixin.fog;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.LinkedHashSet;

@Mixin(DimensionOptions.class)
public interface DimensionOptionsAccessor {
    @Accessor("BASE_DIMENSIONS")
    static LinkedHashSet<RegistryKey<DimensionOptions>> getBaseDimensions() {
        return Sets.newLinkedHashSet(ImmutableList.of());
    }
}
