package me.flashyreese.mods.sodiumextra.client;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import me.flashyreese.mods.sodiumextra.client.render.AnimationManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;

@Environment(EnvType.CLIENT)
public class SodiumExtraClientMod implements ClientModInitializer {

    private static SodiumExtraGameOptions CONFIG;
    private static AnimationManager animationManager;

    @Override
    public void onInitializeClient() {

    }

    public static SodiumExtraGameOptions options() {
        if (CONFIG == null) {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    private static SodiumExtraGameOptions loadConfig() {
        SodiumExtraGameOptions config = SodiumExtraGameOptions.load(new File("config/sodium-extra-options.json"));
        return config;
    }

    public static AnimationManager getAnimationManager() {
        if (animationManager == null) animationManager = new AnimationManager();
        return animationManager;
    }
}
