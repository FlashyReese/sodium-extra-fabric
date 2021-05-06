package me.flashyreese.mods.sodiumextra.client.gui.frame.tab;

import me.flashyreese.mods.sodiumextra.client.gui.frame.AbstractFrame;
import me.flashyreese.mods.sodiumextra.client.gui.frame.OptionPageScrollFrame;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class Tab<T extends AbstractFrame> implements TabOption<T> {
    private final Text text;
    private final T frame;

    public Tab(Text text, T frame) {
        this.text = text;
        this.frame = frame;
    }

    public Text getText() {
        return text;
    }

    @Override
    public T getFrame() {
        return this.frame;
    }

    public static class Builder<T extends AbstractFrame> {
        private Text text = null;
        private T frame = null;

        public Builder<T> setText(Text text) {
            this.text = text;
            return this;
        }

        public Builder<T> setFrame(T frame) {
            this.frame = frame;
            return this;
        }

        public Tab<T> build() {
            return new Tab<T>(this.text, this.frame);
        }

        public Tab<OptionPageScrollFrame> from(OptionPage page, Dim2i dim) {
            this.text = new LiteralText(page.getName());
            return new Tab<>(this.text, OptionPageScrollFrame.createBuilder().setDimension(dim).setOptionPage(page).build());
        }
    }
}