package me.flashyreese.mods.sodiumextra.client.gui.scrollable_page;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.Control;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;

import java.util.ArrayList;
import java.util.List;

public class OptionPageScrollFrame extends AbstractFrame {
    protected final OptionPage page;
    private boolean canScroll;
    private ScrollBarComponent scrollBar = null;

    public OptionPageScrollFrame(Dim2i dim, OptionPage page) {
        super(dim);
        this.page = page;
        this.setupFrame();
        this.buildFrame();
    }

    public void setupFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();

        int y = 0;
        if (!this.page.getGroups().isEmpty()) {
            OptionGroup lastGroup = this.page.getGroups().get(this.page.getGroups().size() - 1);

            for (OptionGroup group : this.page.getGroups()) {
                y += group.getOptions().size() * 18;
                if (group != lastGroup) {
                    y += 4;
                }
            }
        }

        this.canScroll = this.dim.height() < y;
        if (this.canScroll) {
            this.scrollBar = new ScrollBarComponent(new Dim2i(this.dim.getLimitX() - 10, this.dim.y(), 10, this.dim.height()), y, this.dim.height(), this::buildFrame, this.dim);
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
                ControlElement<?> element = control.createElement(new Dim2i(this.dim.x(), this.dim.y() + y - (this.canScroll ? this.scrollBar.getOffset() : 0), this.dim.width() - (this.canScroll ? 11 : 0), 18));
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
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        ControlElement<?> hoveredElement = this.controlElements.stream()
                .filter(ControlElement::isHovered)
                .findFirst()
                .orElse(null);
        this.applyScissor(this.dim.x(), this.dim.y(), this.dim.width(), this.dim.height(), () -> super.render(drawContext, mouseX, mouseY, delta));
        if (this.canScroll) {
            this.scrollBar.render(drawContext, mouseX, mouseY, delta);
        }
        if (this.dim.containsCursor(mouseX, mouseY) && hoveredElement != null) {
            this.renderOptionTooltip(drawContext, hoveredElement);
        }
    }

    private void renderOptionTooltip(DrawContext drawContext, ControlElement<?> element) {
        Dim2i dim = element.getDimensions();

        int textPadding = 3;
        int boxPadding = 3;

        int boxWidth = 200;

        int boxY = Math.max(dim.y(), this.dim.y());
        int boxX = this.dim.getLimitX() + boxPadding;

        Option<?> option = element.getOption();
        List<OrderedText> tooltip = new ArrayList<>(MinecraftClient.getInstance().textRenderer.wrapLines(option.getTooltip(), boxWidth - (textPadding * 2)));

        OptionImpact impact = option.getImpact();

        if (impact != null) {
            tooltip.add(Language.getInstance().reorder(Text.translatable("sodium.options.performance_impact_string", impact.getLocalizedName()).formatted(Formatting.GRAY)));
        }

        /*if (option.getFlags().contains(OptionFlag.REQUIRES_GAME_RESTART)) {
            tooltip.add(Language.getInstance().reorder(Text.translatable("sodium.option_flag.requires_game_restart.tooltip").formatted(Formatting.RED)));
        }*/

        int boxHeight = (tooltip.size() * 12) + boxPadding;
        int boxYLimit = boxY + boxHeight;
        int boxYCutoff = this.dim.getLimitY() - 25 /*- 64*/;

        // If the box is going to be cutoff on the Y-axis, move it back up the difference
        if (boxYLimit > boxYCutoff) {
            boxY -= boxYLimit - boxYCutoff;
        }

        this.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xE0000000);

        for (int i = 0; i < tooltip.size(); i++) {
            drawContext.drawText(MinecraftClient.getInstance().textRenderer, tooltip.get(i), boxX + textPadding, boxY + textPadding + (i * 12), 0xFFFFFFFF, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.dim.containsCursor(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (this.canScroll) {
            return this.scrollBar.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (this.canScroll) {
            return this.scrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        if (this.canScroll) {
            return this.scrollBar.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) {
            return true;
        }
        if (this.canScroll) {
            return this.scrollBar.mouseScrolled(mouseX, mouseY, amount);
        }
        return false;
    }
}