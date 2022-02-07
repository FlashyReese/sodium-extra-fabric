package me.flashyreese.mods.sodiumextra.client.gui;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;

public class SodiumExtraGameOptions {
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();
    public final AnimationSettings animationSettings = new AnimationSettings();
    public final ParticleSettings particleSettings = new ParticleSettings();
    public final DetailSettings detailSettings = new DetailSettings();
    public final RenderSettings renderSettings = new RenderSettings();
    public final ExtraSettings extraSettings = new ExtraSettings();
    private File file;

    public static SodiumExtraGameOptions load(File file) {
        SodiumExtraGameOptions config;

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                config = gson.fromJson(reader, SodiumExtraGameOptions.class);
            } catch (IOException e) {
                throw new RuntimeException("Could not parse config", e);
            }
        } else {
            config = new SodiumExtraGameOptions();
        }

        config.file = file;
        config.writeChanges();

        return config;
    }

    public void writeChanges() {
        File dir = this.file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new RuntimeException("The parent file is not a directory");
        }

        try (FileWriter writer = new FileWriter(this.file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException("Could not save configuration file", e);
        }
    }

    public enum OverlayCorner implements TextProvider {
        TOP_LEFT("sodium-extra.option.overlay_corner.top_left"),
        TOP_RIGHT("sodium-extra.option.overlay_corner.top_right"),
        BOTTOM_LEFT("sodium-extra.option.overlay_corner.bottom_left"),
        BOTTOM_RIGHT("sodium-extra.option.overlay_corner.bottom_right");

        private final Text text;

        OverlayCorner(String text) {
            this.text = new TranslatableText(text);
        }

        @Override
        public Text getLocalizedName() {
            return this.text;
        }
    }

    public static class AnimationSettings {
        public boolean animation;
        public boolean animateWater;
        public boolean animateLava;
        public boolean animateFire;
        public boolean animatePortal;
        public boolean blockAnimations;

        public AnimationSettings() {
            this.animation = true;
            this.animateWater = true;
            this.animateLava = true;
            this.animateFire = true;
            this.animatePortal = true;
            this.blockAnimations = true;
        }
    }

    public static class ParticleSettings {
        public boolean particles;
        public boolean rainSplash;
        public boolean explosion;
        public boolean water;
        public boolean smoke;
        public boolean potion;
        public boolean portal;
        public boolean redstone;
        public boolean drip;
        public boolean firework;
        public boolean bubble;
        public boolean environment;
        public boolean villagers;
        public boolean composter;
        public boolean blockBreak;
        public boolean blockBreaking;

        public ParticleSettings() {
            this.particles = true;
            this.rainSplash = true;
            this.explosion = true;
            this.water = true;
            this.smoke = true;
            this.potion = true;
            this.portal = true;
            this.redstone = true;
            this.drip = true;
            this.firework = true;
            this.bubble = true;
            this.environment = true;
            this.villagers = true;
            this.composter = true;
            this.blockBreak = true;
            this.blockBreaking = true;
        }
    }

    public static class DetailSettings {
        public boolean rainSnow;
        public boolean biomeColors;
        public boolean skyColors;

        public DetailSettings() {
            this.rainSnow = true;
            this.biomeColors = true;
            this.skyColors = true;
        }
    }

    public static class RenderSettings {
        public int fogDistance;
        public boolean lightUpdates;
        public boolean itemFrame;
        public boolean armorStand;
        public boolean painting;
        public boolean piston;

        public RenderSettings() {
            this.fogDistance = 0;
            this.lightUpdates = true;
            this.itemFrame = true;
            this.armorStand = true;
            this.painting = true;
            this.piston = true;
        }
    }

    public static class ExtraSettings {
        public OverlayCorner overlayCorner;
        public boolean showFps;
        public boolean showFPSExtended;
        public boolean showCoords;
        public boolean reduceResolutionOnMac;
        public int cloudHeight;
        public boolean toasts;
        public boolean instantSneak;
        public boolean preventShaders;
        public boolean useFastRandom;

        public ExtraSettings() {
            this.overlayCorner = OverlayCorner.TOP_LEFT;
            this.showFps = false;
            this.showFPSExtended = true;
            this.showCoords = false;
            this.reduceResolutionOnMac = true;
            this.cloudHeight = 192;
            this.toasts = true;
            this.instantSneak = false;
            this.preventShaders = false;
            this.useFastRandom = true;
        }
    }

}
