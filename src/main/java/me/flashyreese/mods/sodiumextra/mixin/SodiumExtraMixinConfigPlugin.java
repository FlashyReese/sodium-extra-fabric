package me.flashyreese.mods.sodiumextra.mixin;

import net.caffeinemc.caffeineconfig.AbstractCaffeineConfigMixinPlugin;
import net.caffeinemc.caffeineconfig.CaffeineConfig;
import net.fabricmc.loader.api.FabricLoader;

public class SodiumExtraMixinConfigPlugin extends AbstractCaffeineConfigMixinPlugin {

    private static final String MIXIN_PACKAGE_ROOT = "me.flashyreese.mods.sodiumextra.mixin.";

    @Override
    protected CaffeineConfig createConfig() {
        return CaffeineConfig.builder("Sodium Extra").withSettingsKey("sodium-extra:options")
                .addMixinOption("adaptive_sync", true)
                .addMixinOption("animation", true)
                .addMixinOption("biome_colors", true)
                .addMixinOption("cloud", true)
                .addMixinOption("compat", true) // Should not allow users to turn this off
                .addMixinOption("fog", true)
                .addMixinOption("gui", true)
                .addMixinOption("instant_sneak", true)
                .addMixinOption("light_updates", true)
                .addMixinOption("particle", true)
                .addMixinOption("prevent_shaders", true)
                .addMixinOption("reduce_resolution_on_mac", true)
                .addMixinOption("render", true)
                .addMixinOption("render.block", true)
                .addMixinOption("render.block.entity", true)
                .addMixinOption("render.entity", true)
                .addMixinOption("sky", true)
                .addMixinOption("sky_colors", true)
                .addMixinOption("sodium", true)
                .addMixinOption("sodium.accessibility", true)
                .addMixinOption("sodium.biome_blend", true)
                .addMixinOption("sodium.fast_random", true)
                .addMixinOption("sodium.gui_scale", true)
                .addMixinOption("sodium.resolution", true)
                .addMixinOption("sodium.scrollable_page", true)
                .addMixinOption("sodium.vsync", true)
                .addMixinOption("stars", true)
                .addMixinOption("sun_moon", true)
                .addMixinOption("toasts", true)


                .withInfoUrl("https://github.com/FlashyReese/sodium-extra-fabric/wiki/Configuration-File")
                .build(FabricLoader.getInstance().getConfigDir().resolve("sodium-extra.properties"));
    }

    @Override
    protected String mixinPackageRoot() {
        return MIXIN_PACKAGE_ROOT;
    }
}
