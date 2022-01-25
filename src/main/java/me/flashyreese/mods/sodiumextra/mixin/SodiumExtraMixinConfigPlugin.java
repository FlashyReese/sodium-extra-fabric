package me.flashyreese.mods.sodiumextra.mixin;

import net.caffeinemc.caffeineconfig.AbstractCaffeineConfigMixinPlugin;
import net.caffeinemc.caffeineconfig.CaffeineConfig;
import net.fabricmc.loader.api.FabricLoader;

public class SodiumExtraMixinConfigPlugin extends AbstractCaffeineConfigMixinPlugin {

    private static final String MIXIN_PACKAGE_ROOT = "me.flashyreese.mods.sodiumextra.mixin.";

    @Override
    protected CaffeineConfig createConfig() {
        // Todo: Organize and repackage to allow users to disable pages? bad idea maybe?
        return CaffeineConfig.builder("Sodium Extra")

                .addMixinOption("features", true)
                .addMixinOption("features.animation", true)
                .addMixinOption("features.biome_colors", true)
                .addMixinOption("features.cloud", true)
                .addMixinOption("features.fog", true)
                .addMixinOption("features.gui", true)
                .addMixinOption("features.instant_sneak", true)
                .addMixinOption("features.light_updates", true)
                .addMixinOption("features.particle", true)
                .addMixinOption("features.prevent_shaders", true)
                .addMixinOption("features.reduce_resolution_on_mac", true)
                .addMixinOption("features.render", true)
                .addMixinOption("features.render.entity", true)
                .addMixinOption("features.sky_colors", true)
                .addMixinOption("features.toasts", true)

                .addMixinOption("reeses_sodium_options", true)

                .addMixinOption("sodium", true)

                .withInfoUrl("https://github.com/FlashyReese/sodium-extra-fabric/wiki/Configuration-File")
                .build(FabricLoader.getInstance().getConfigDir().resolve("sodium-extra.properties"));
    }

    @Override
    protected String mixinPackageRoot() {
        return MIXIN_PACKAGE_ROOT;
    }
}
