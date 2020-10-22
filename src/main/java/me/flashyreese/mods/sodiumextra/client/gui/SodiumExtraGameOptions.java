package me.flashyreese.mods.sodiumextra.client.gui;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;

public class SodiumExtraGameOptions {
    public final AnimationSettings animationSettings = new AnimationSettings();
    public final ParticleSettings particleSettings = new ParticleSettings();
    private File file;

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

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

    public static class AnimationSettings {
        public boolean animation;
        public boolean animateWater;
        public boolean animateLava;
        public boolean animateFire;
        public boolean animatePortal;

        public AnimationSettings() {
            this.animation = true;
            this.animateWater = true;
            this.animateLava = true;
            this.animateFire = true;
            this.animatePortal = true;
        }
    }

    public static class ParticleSettings {
        public boolean particles;
        public boolean weather;
        public boolean rainSplash;
        public boolean explosion;
        public boolean water;
        public boolean smoke;
        public boolean potion;
        public boolean portal;
        public boolean redstone;
        public boolean fluidDrip;
        public boolean firework;

        public ParticleSettings() {
            this.particles = true;
            this.weather = true;
            this.rainSplash = true;
            this.explosion = true;
            this.water = true;
            this.smoke = true;
            this.potion = true;
            this.portal = true;
            this.redstone = true;
            this.fluidDrip = true;
            this.firework = true;
        }
    }

}
