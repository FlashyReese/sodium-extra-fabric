package me.flashyreese.mods.sodiumextra.client.gui;

import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.minecraft.text.TranslatableText;

public interface ControlValueFormatterExtended extends ControlValueFormatter {

    static ControlValueFormatter fogDistance() {
        return (v) -> {
            if (v == 0) {
                return new TranslatableText("generator.default").getString();
            } else if (v == 33) {
                return new TranslatableText("options.off").getString();
            } else {
                return new TranslatableText("options.chunks", v).getString();
            }
        };
    }
}
