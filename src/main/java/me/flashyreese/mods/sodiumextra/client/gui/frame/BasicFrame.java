package me.flashyreese.mods.sodiumextra.client.gui.frame;

import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BasicFrame extends AbstractFrame {

    protected List<Function<Dim2i, AbstractWidget>> functions;

    public BasicFrame(Dim2i dim, boolean renderOutline) {
        super(dim, renderOutline);
        this.buildFrame();
    }

    public BasicFrame(Dim2i dim, List<Function<Dim2i, AbstractWidget>> functions, boolean renderOutline) {
        super(dim, renderOutline);
        this.functions = functions;
        this.buildFrame();
    }

    @Override
    public void buildFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();

        this.functions.forEach(function -> this.children.add(function.apply(dim)));

        super.buildFrame();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private boolean renderOutline = false;
        private Dim2i dim = null;
        private final List<Function<Dim2i, AbstractWidget>> functions = new ArrayList<>();

        public Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public Builder shouldRenderOutline(boolean state) {
            this.renderOutline = state;
            return this;
        }

        public Builder addChild(Function<Dim2i, AbstractWidget> function) {
            this.functions.add(function);
            return this;
        }

        public Builder addChildren(List<Function<Dim2i, AbstractWidget>> functions) {
            this.functions.addAll(functions);
            return this;
        }

        public BasicFrame build() {
            return new BasicFrame(this.dim, this.functions, this.renderOutline);
        }
    }
}