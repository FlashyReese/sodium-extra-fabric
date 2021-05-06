package me.flashyreese.mods.sodiumextra.client.gui.frame;

import me.flashyreese.mods.sodiumextra.client.gui.frame.components.ScrollBarComponent;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.Control;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;

import java.util.ArrayList;
import java.util.List;

public class OptionPageScrollFrame extends AbstractFrame {
    private boolean canScroll;
    private ScrollBarComponent scrollBar = null;

    protected final OptionPage page;

    public OptionPageScrollFrame(Dim2i dim, boolean renderOutline, OptionPage page) {
        super(dim, renderOutline);
        this.page = page;
        this.setupFrame();
        this.buildFrame();
    }

    public void setupFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();

        int y = 0;
        OptionGroup lastGroup = this.page.getGroups().get(this.page.getGroups().size() - 1);

        for (OptionGroup group : this.page.getGroups()) {
            for (int i = 0; i < group.getOptions().size(); i++) {
                y += 18;
            }
            if (group != lastGroup) {
                y += 4;
            }
        }

        this.canScroll = this.dim.getHeight() < y;
        if (this.canScroll) {
            this.scrollBar = new ScrollBarComponent(new Dim2i(this.dim.getLimitX() - 10, this.dim.getOriginY(), 10, this.dim.getHeight()), ScrollBarComponent.Mode.VERTICAL, y, this.dim.getHeight(), this::buildFrame, this.dim);
        }
    }

    @Override
    public void buildFrame() {
        if (this.page == null) return;

        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();

        int y = 0;
        for (OptionGroup group : this.page.getGroups()) {
            // Add each option's control element
            for (Option<?> option : group.getOptions()) {
                Control<?> control = option.getControl();
                ControlElement<?> element = control.createElement(new Dim2i(this.dim.getOriginX(), this.dim.getOriginY() + y - (this.canScroll ? this.scrollBar.getOffset() : 0), this.dim.getWidth() - (this.canScroll ? 11 : 0), 18));
                this.children.add(element);

                // Move down to the next option
                y += 18;
            }

            // Add padding beneath each option group
            y += 4;
        }

        if (this.canScroll) {
            this.scrollBar.updateThumbPosition();
        }

        super.buildFrame();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ControlElement<?> hoveredElement = this.controlElements.stream()
                .filter(ControlElement::isHovered)
                .findFirst()
                .orElse(null);
        this.applyScissor(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getWidth(), this.dim.getHeight(), () -> super.render(matrices, mouseX, mouseY, delta));
        if (this.canScroll) {
            this.scrollBar.render(matrices, mouseX, mouseY, delta);
        }
        if (hoveredElement != null) {
            this.renderOptionTooltip(matrices, mouseX, mouseY, hoveredElement);
        }
    }

    private void renderOptionTooltip(MatrixStack matrixStack, int mouseX, int mouseY, ControlElement<?> element) {
        Dim2i dim = element.getDimensions();

        int textPadding = 3;
        int boxPadding = 3;

        int boxWidth = dim.getWidth();

        //Offset based on mouse position, width and height of content and width and height of the window
        int boxY = dim.getLimitY();
        int boxX = dim.getOriginX();

        Option<?> option = element.getOption();
        List<OrderedText> tooltip = new ArrayList<>(MinecraftClient.getInstance().textRenderer.wrapLines(option.getTooltip(), boxWidth - (textPadding * 2)));

        OptionImpact impact = option.getImpact();

        if (impact != null) {
            tooltip.add(Language.getInstance().reorder(new LiteralText(Formatting.GRAY + "Performance Impact: " + impact.toDisplayString())));
        }

        int boxHeight = (tooltip.size() * 12) + boxPadding;
        int boxYLimit = boxY + boxHeight;
        int boxYCutoff = this.dim.getLimitY();

        // If the box is going to be cutoff on the Y-axis, move it back up the difference
        if (boxYLimit > boxYCutoff) {
            boxY -= boxHeight + dim.getHeight();
        }

        if (boxY < 0){
            boxY = dim.getLimitY();
        }

        this.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xE0000000);
        this.drawRectOutline(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xFF94E4D3);

        for (int i = 0; i < tooltip.size(); i++) {
            MinecraftClient.getInstance().textRenderer.draw(matrixStack, tooltip.get(i), boxX + textPadding, boxY + textPadding + (i * 12), 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (this.canScroll) {
            if (this.scrollBar.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (this.canScroll) {
            if (this.scrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        if (this.canScroll) {
            if (this.scrollBar.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) {
            return true;
        }
        if (this.canScroll) {
            if (this.scrollBar.mouseScrolled(mouseX, mouseY, amount)) {
                return true;
            }
        }
        return false;
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Dim2i dim;
        private boolean renderOutline;
        private OptionPage page;

        public Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public Builder shouldRenderOutline(boolean state) {
            this.renderOutline = state;
            return this;
        }

        public Builder setOptionPage(OptionPage page) {
            this.page = page;
            return this;
        }

        public OptionPageScrollFrame build() {
            return new OptionPageScrollFrame(this.dim, this.renderOutline, this.page);
        }
    }
}