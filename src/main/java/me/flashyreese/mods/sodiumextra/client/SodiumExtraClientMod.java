package me.flashyreese.mods.sodiumextra.client;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;

@Environment(EnvType.CLIENT)
public class SodiumExtraClientMod implements ClientModInitializer {

    private static SodiumExtraGameOptions CONFIG;

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
}
