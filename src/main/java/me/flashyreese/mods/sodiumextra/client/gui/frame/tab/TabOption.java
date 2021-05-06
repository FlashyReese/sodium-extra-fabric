package me.flashyreese.mods.sodiumextra.client.gui.frame.tab;

import me.flashyreese.mods.sodiumextra.client.gui.frame.AbstractFrame;

public interface TabOption<T extends AbstractFrame> {
    T getFrame();
}
