package me.flashyreese.mods.sodiumextra.client;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;

@Environment(EnvType.CLIENT)
public class SodiumExtraClientMod implements ClientModInitializer {

    private static final ClientTickHandler clientTickHandler = new ClientTickHandler();
    private static SodiumExtraGameOptions CONFIG;

    public static SodiumExtraGameOptions options() {
        if (CONFIG == null) {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    public static ClientTickHandler getClientTickHandler() {
        return clientTickHandler;
    }

    private static SodiumExtraGameOptions loadConfig() {
        return SodiumExtraGameOptions.load(new File("config/sodium-extra-options.json"));
    }

    @Override
    public void onInitializeClient() {
        getClientTickHandler().onClientInitialize();
    }
}
