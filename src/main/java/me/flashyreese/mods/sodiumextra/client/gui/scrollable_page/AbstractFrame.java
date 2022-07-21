package me.flashyreese.mods.sodiumextra.client.gui.scrollable_page;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFrame extends AbstractWidget implements ParentElement {
    protected final Dim2i dim;
    protected final List<AbstractWidget> children = new ArrayList<>();
    protected final List<Drawable> drawable = new ArrayList<>();
    protected final List<ControlElement<?>> controlElements = new ArrayList<>();
    private Element focused;
    private boolean dragging;

    public AbstractFrame(Dim2i dim) {
        this.dim = dim;
    }

    public void buildFrame() {
        for (Element element : this.children) {
            if (element instanceof AbstractFrame) {
                this.controlElements.addAll(((AbstractFrame) element).controlElements);
            }
            if (element instanceof ControlElement<?>) {
                this.controlElements.add((ControlElement<?>) element);
            }
            if (element instanceof Drawable) {
                this.drawable.add((Drawable) element);
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (Drawable drawable : this.drawable) {
            drawable.render(matrices, mouseX, mouseY, delta);
        }
    }

    public void applyScissor(int x, int y, int width, int height, Runnable action) {
        double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        RenderSystem.enableScissor((int) (x * scale), (int) (MinecraftClient.getInstance().getWindow().getFramebufferHeight() - (y + height) * scale),
                (int) (width * scale), (int) (height * scale));
        action.run();
        RenderSystem.disableScissor();
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Nullable
    @Override
    public Element getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        this.focused = focused;
    }

    @Override
    public List<? extends Element> children() {
        return this.children;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.dim.containsCursor(mouseX, mouseY);
    }
}