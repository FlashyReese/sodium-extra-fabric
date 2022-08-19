package me.flashyreese.mods.sodiumextra.mixin.sodium.scrollable_page;

import me.flashyreese.mods.sodiumextra.client.gui.scrollable_page.OptionPageScrollFrame;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SodiumOptionsGUI.class, remap = false)
public abstract class MixinSodiumOptionsGUI extends Screen {

    @Shadow
    private OptionPage currentPage;

    protected MixinSodiumOptionsGUI(Text title) {
        super(title);
    }

    @Inject(method = "rebuildGUIOptions", at = @At(value = "HEAD"), cancellable = true)
    private void rebuildGUIOptions(CallbackInfo ci) {
        int x = 6;
        int y = 28;

        OptionPageScrollFrame optionPageScrollFrame = new OptionPageScrollFrame(new Dim2i(x, y, /*this.width - x * 2*/200, this.height - y - 10 /*- 64*/), this.currentPage);
        this.addDrawableChild(optionPageScrollFrame);
        ci.cancel();
    }

    @Inject(method = "renderOptionTooltip", at = @At(value = "HEAD"), cancellable = true)
    private void renderOptionTooltip(MatrixStack matrixStack, ControlElement<?> element, CallbackInfo ci) {
        ci.cancel();
    }
}
