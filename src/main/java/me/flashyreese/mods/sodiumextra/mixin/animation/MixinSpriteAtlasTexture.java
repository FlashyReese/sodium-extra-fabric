package me.flashyreese.mods.sodiumextra.mixin.animation;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(SpriteAtlasTexture.class)
public abstract class MixinSpriteAtlasTexture extends AbstractTexture {
    @Unique
    private final Map<Supplier<Boolean>, List<Identifier>> animatedSprites = Map.of(
            () -> SodiumExtraClientMod.options().animationSettings.water, List.of(
                    new Identifier("minecraft", "block/water_still"),
                    new Identifier("minecraft", "block/water_flow")
            ),
            () -> SodiumExtraClientMod.options().animationSettings.lava, List.of(
                    new Identifier("minecraft", "block/lava_still"),
                    new Identifier("minecraft", "block/lava_flow")
            ),
            () -> SodiumExtraClientMod.options().animationSettings.portal, List.of(
                    new Identifier("minecraft", "block/nether_portal")
            ),
            () -> SodiumExtraClientMod.options().animationSettings.fire, List.of(
                    new Identifier("minecraft", "block/fire_0"),
                    new Identifier("minecraft", "block/fire_1"),
                    new Identifier("minecraft", "block/soul_fire_0"),
                    new Identifier("minecraft", "block/soul_fire_1"),
                    new Identifier("minecraft", "block/campfire_fire"),
                    new Identifier("minecraft", "block/campfire_log_lit"),
                    new Identifier("minecraft", "block/soul_campfire_fire"),
                    new Identifier("minecraft", "block/soul_campfire_log_lit")
            ),
            () -> SodiumExtraClientMod.options().animationSettings.blockAnimations, List.of(
                    new Identifier("minecraft", "block/magma"),
                    new Identifier("minecraft", "block/lantern"),
                    new Identifier("minecraft", "block/sea_lantern"),
                    new Identifier("minecraft", "block/soul_lantern"),
                    new Identifier("minecraft", "block/kelp"),
                    new Identifier("minecraft", "block/kelp_plant"),
                    new Identifier("minecraft", "block/seagrass"),
                    new Identifier("minecraft", "block/tall_seagrass_top"),
                    new Identifier("minecraft", "block/tall_seagrass_bottom"),
                    new Identifier("minecraft", "block/warped_stem"),
                    new Identifier("minecraft", "block/crimson_stem"),
                    new Identifier("minecraft", "block/blast_furnace_front_on"),
                    new Identifier("minecraft", "block/smoker_front_on"),
                    new Identifier("minecraft", "block/stonecutter_saw"),
                    new Identifier("minecraft", "block/prismarine"),
                    new Identifier("minecraft", "block/respawn_anchor_top"),
                    new Identifier("minecraft", "entity/conduit/wind"),
                    new Identifier("minecraft", "entity/conduit/wind_vertical")
            ),
            () -> SodiumExtraClientMod.options().animationSettings.sculkSensor, List.of(
                    new Identifier("minecraft", "block/sculk"),
                    new Identifier("minecraft", "block/sculk_catalyst_top_bloom"),
                    new Identifier("minecraft", "block/sculk_catalyst_side_bloom"),
                    new Identifier("minecraft", "block/sculk_shrieker_inner_top"),
                    new Identifier("minecraft", "block/sculk_vein"),
                    new Identifier("minecraft", "block/sculk_shrieker_can_summon_inner_top"),
                    new Identifier("minecraft", "block/sculk_sensor_tendril_inactive"),
                    new Identifier("minecraft", "block/sculk_sensor_tendril_active"),
                    new Identifier("minecraft", "vibration")
            )
    );

    @Redirect(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/Sprite;createAnimation()Lnet/minecraft/client/texture/Sprite$TickableAnimation;"))
    public Sprite.TickableAnimation sodiumExtra$tickAnimatedSprites(Sprite instance) {
        Sprite.TickableAnimation tickableAnimation = instance.createAnimation();
        if (tickableAnimation != null && SodiumExtraClientMod.options().animationSettings.animation && this.shouldAnimate(instance.getContents().getId()))
            return tickableAnimation;
        return null;
    }

    @Unique
    private boolean shouldAnimate(Identifier identifier) {
        if (identifier != null) {
            for (Map.Entry<Supplier<Boolean>, List<Identifier>> supplierListEntry : this.animatedSprites.entrySet()) {
                if (supplierListEntry.getValue().contains(identifier)) {
                    return supplierListEntry.getKey().get();
                }
            }
        }
        return true;
    }
}
