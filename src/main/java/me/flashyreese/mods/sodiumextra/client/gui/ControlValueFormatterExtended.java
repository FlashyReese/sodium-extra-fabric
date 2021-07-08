package me.flashyreese.mods.sodiumextra.client.gui;

import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;

public interface ControlValueFormatterExtended extends ControlValueFormatter {

    static ControlValueFormatter fogDistance() {
        return (v) -> {
            if (v == 0) {
                return "Vanilla";
            } else if (v == 33) {
                return "Off";
            } else if (v == 1) {
                return v + " Chunk";
            } else {
                return v + " Chunks";
            }
        };
    }
}
