package me.flashyreese.mods.sodiumextra.client.gui.scrollable_page;

import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class ScrollBarComponent extends AbstractWidget {

    protected final Dim2i dim;

    private final int frameLength;
    private final int viewPortLength;
    private final int maxScrollBarOffset;
    private final Runnable onSetOffset;
    private int offset = 0;
    private boolean isDragging;

    private Dim2i scrollThumb = null;
    private int scrollThumbClickOffset;

    private Dim2i extendedScrollArea = null;

    public ScrollBarComponent(Dim2i trackArea, int frameLength, int viewPortLength, Runnable onSetOffset) {
        this.dim = trackArea;
        this.frameLength = frameLength;
        this.viewPortLength = viewPortLength;
        this.onSetOffset = onSetOffset;
        this.maxScrollBarOffset = this.frameLength - this.viewPortLength;
    }

    public ScrollBarComponent(Dim2i scrollBarArea, int frameLength, int viewPortLength, Runnable onSetOffset, Dim2i extendedTrackArea) {
        this(scrollBarArea, frameLength, viewPortLength, onSetOffset);
        this.extendedScrollArea = extendedTrackArea;
    }

    public void updateThumbPosition() {
        int scrollThumbLength = (this.viewPortLength * this.dim.getHeight()) / this.frameLength;
        int maximumScrollThumbOffset = this.viewPortLength - scrollThumbLength;
        int scrollThumbOffset = this.offset * maximumScrollThumbOffset / this.maxScrollBarOffset;
        this.scrollThumb = new Dim2i(this.dim.getOriginX() + 2, this.dim.getOriginY() + 2 + scrollThumbOffset, this.dim.getWidth() - 4, scrollThumbLength - 4);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.drawRectOutline(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getLimitX(), this.dim.getLimitY(), 0xFFAAAAAA);
        this.drawRect(this.scrollThumb.getOriginX(), this.scrollThumb.getOriginY(), this.scrollThumb.getLimitX(), this.scrollThumb.getLimitY(), 0xFFAAAAAA);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.dim.containsCursor(mouseX, mouseY)) {
            if (this.scrollThumb.containsCursor(mouseX, mouseY)) {
                this.scrollThumbClickOffset = (int) (mouseY - (this.scrollThumb.getOriginY() + this.scrollThumb.getHeight() / 2));
                this.isDragging = true;
            } else {
                int value = (int) ((mouseY - this.dim.getOriginY() - (this.scrollThumb.getHeight() / 2)) / (this.dim.getHeight() - this.scrollThumb.getHeight()) * this.maxScrollBarOffset);
                this.setOffset(value);
                this.isDragging = false;
            }
            return true;
        }
        this.isDragging = false;
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging) {
            int value = (int) ((mouseY - this.scrollThumbClickOffset - this.dim.getOriginY() - (this.scrollThumb.getHeight() / 2)) / (this.dim.getHeight() - this.scrollThumb.getHeight()) * this.maxScrollBarOffset);
            this.setOffset(value);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.dim.containsCursor(mouseX, mouseY) || this.extendedScrollArea != null && this.extendedScrollArea.containsCursor(mouseX, mouseY)) {
            if (this.offset <= this.maxScrollBarOffset && this.offset >= 0) {
                int value = (int) (this.offset - amount * 6);
                this.setOffset(value);
                return true;
            }
        }
        return false;
    }

    public int getOffset() {
        return this.offset;
    }

    private void setOffset(int value) {
        this.offset = MathHelper.clamp(value, 0, this.maxScrollBarOffset);
        this.updateThumbPosition();
        this.onSetOffset.run();
    }

    protected void drawRectOutline(double x, double y, double w, double h, int color) {
        final float a = (float) (color >> 24 & 255) / 255.0F;
        final float r = (float) (color >> 16 & 255) / 255.0F;
        final float g = (float) (color >> 8 & 255) / 255.0F;
        final float b = (float) (color & 255) / 255.0F;

        this.drawQuads(vertices -> {
            addQuad(vertices, x, y, w, y + 1, a, r, g, b);
            addQuad(vertices, x, h - 1, w, h, a, r, g, b);
            addQuad(vertices, x, y, x + 1, h, a, r, g, b);
            addQuad(vertices, w - 1, y, w, h, a, r, g, b);
        });
    }
}
