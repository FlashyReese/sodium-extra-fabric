package me.flashyreese.mods.sodiumextra.common.util;

import net.caffeinemc.mods.sodium.api.util.ColorU8;

public class ColorRGBA implements ColorU8 {
    public ColorRGBA() {
    }

    public static int pack(int r, int g, int b, int a) {
        return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
    }

    public static int unpackRed(int color) {
        return color >> 24 & 0xFF;
    }

    public static int unpackGreen(int color) {
        return color >> 16 & 0xFF;
    }

    public static int unpackBlue(int color) {
        return color >> 8 & 0xFF;
    }

    public static int unpackAlpha(int color) {
        return color & 0xFF;
    }

    public static int fromARGB(int color) {
        return color << 8 | (color >> 24 & 0xFF);
    }

    public static int fromOrToABGR(int color) {
        return Integer.reverseBytes(color);
    }
}
