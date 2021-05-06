package me.flashyreese.mods.sodiumextra.client.gui;


import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.util.Rect2i;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class SliderControlElement extends ControlElement<Integer> {
    private static final int THUMB_WIDTH = 2, TRACK_HEIGHT = 1;

    private final Rect2i sliderBounds;
    private final ControlValueFormatter formatter;

    private final int min;
    private final int max;
    private final int range;
    private final int interval;

    private double thumbPosition;

    public SliderControlElement(Option<Integer> option, Dim2i dim, int min, int max, int interval, ControlValueFormatter formatter) {
        super(option, dim);

        this.min = min;
        this.max = max;
        this.range = max - min;
        this.interval = interval;
        this.thumbPosition = this.getThumbPositionForValue(option.getValue());
        this.formatter = formatter;

        this.sliderBounds = new Rect2i(dim.getLimitX() - 96, dim.getCenterY() - 5, 90, 10);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.render(matrixStack, mouseX, mouseY, delta);

        if (this.option.isAvailable() && this.hovered) {
            this.renderSlider(matrixStack);
        } else {
            this.renderStandaloneValue(matrixStack);
        }
    }

    private void renderStandaloneValue(MatrixStack matrixStack) {
        int sliderX = this.sliderBounds.getX();
        int sliderY = this.sliderBounds.getY();
        int sliderWidth = this.sliderBounds.getWidth();
        int sliderHeight = this.sliderBounds.getHeight();

        String label = this.formatter.format(this.option.getValue());
        int labelWidth = this.font.getWidth(label);

        this.drawString(matrixStack, label, sliderX + sliderWidth - labelWidth, sliderY + (sliderHeight / 2) - 4, 0xFFFFFFFF);
    }

    private void renderSlider(MatrixStack matrixStack) {
        int sliderX = this.sliderBounds.getX();
        int sliderY = this.sliderBounds.getY();
        int sliderWidth = this.sliderBounds.getWidth();
        int sliderHeight = this.sliderBounds.getHeight();

        int thumbOffset = (int) Math.floor((double) (this.getIntValue() - this.min) / this.range * sliderWidth);

        int thumbX = sliderX + thumbOffset - THUMB_WIDTH;
        int trackY = sliderY + (sliderHeight / 2) - TRACK_HEIGHT;

        this.drawRect(thumbX, sliderY, thumbX + (THUMB_WIDTH * 2), sliderY + sliderHeight, 0xFFFFFFFF);
        this.drawRect(sliderX, trackY, sliderX + sliderWidth, trackY + TRACK_HEIGHT, 0xFFFFFFFF);

        String label = String.valueOf(this.getIntValue());

        int labelWidth = this.font.getWidth(label);

        this.drawString(matrixStack, label, sliderX - labelWidth - 6, sliderY + (sliderHeight / 2) - 4, 0xFFFFFFFF);
    }

    public int getIntValue() {
        return this.min + (this.interval * (int) Math.round(this.getSnappedThumbPosition() / this.interval));
    }

    public double getSnappedThumbPosition() {
        return this.thumbPosition / (1.0D / this.range);
    }

    public double getThumbPositionForValue(int value) {
        return (value - this.min) * (1.0D / this.range);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.option.isAvailable() && button == 0 && this.sliderBounds.contains((int) mouseX, (int) mouseY)) {
            this.setValueFromMouse(mouseX);

            return true;
        }

        return false;
    }

    private void setValueFromMouse(double d) {
        this.setValue((d - (double) (this.sliderBounds.getX() + 4)) / (double) (this.sliderBounds.getWidth() - 8));
    }

    private void setValueFromMouseScroll(double amount) {
        if (this.option.getValue() + this.interval * (int) amount <= this.max && this.option.getValue() + this.interval * (int) amount >= this.min) {
            this.option.setValue(this.option.getValue() + this.interval * (int) amount);
            this.thumbPosition = this.getThumbPositionForValue(this.option.getValue());
        }
    }


    private void setValue(double d) {
        this.thumbPosition = MathHelper.clamp(d, 0.0D, 1.0D);

        int value = this.getIntValue();

        if (this.option.getValue() != value) {
            this.option.setValue(value);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.option.isAvailable() && button == 0) {
            this.setValueFromMouse(mouseX);

            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.option.isAvailable() && this.sliderBounds.contains((int) mouseX, (int) mouseY)) {
            this.setValueFromMouseScroll(amount);

            return true;
        }

        return false;
    }
}