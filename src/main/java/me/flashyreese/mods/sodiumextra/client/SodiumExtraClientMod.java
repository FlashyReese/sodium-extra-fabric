package me.flashyreese.mods.sodiumextra.client;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import me.flashyreese.mods.sodiumextra.client.gui.HudRenderImpl;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SodiumExtraClientMod implements ClientModInitializer {

    private static final ClientTickHandler clientTickHandler = new ClientTickHandler();
    private static SodiumExtraGameOptions CONFIG;
    private static Logger LOGGER;

    public static Logger logger() {
        if (LOGGER == null) {
            LOGGER = LoggerFactory.getLogger("Sodium Extra");
        }

        return LOGGER;
    }

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
        return SodiumExtraGameOptions.load(FabricLoader.getInstance().getConfigDir().resolve("sodium-extra-options.json").toFile());
    }

    @Override
    public void onInitializeClient() {
        if (SodiumExtraClientMod.options().superSecretSettings.fetchSodiumExtraCrowdinTranslations) {
            CrowdinTranslate.downloadTranslations(SodiumExtraClientMod.options().superSecretSettings.sodiumExtraCrowdinProjectIdentifier, "sodium-extra");
        }
        if (SodiumExtraClientMod.options().superSecretSettings.fetchSodiumCrowdinTranslations) {
            CrowdinTranslate.downloadTranslations(SodiumExtraClientMod.options().superSecretSettings.sodiumCrowdinProjectIdentifier, "sodium");
        }

        getClientTickHandler().onClientInitialize();
        HudRenderCallback.EVENT.register(new HudRenderImpl());
    }
}
