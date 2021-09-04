package me.flashyreese.mods.sodiumextra.mixin.reeses_sodium_options;

import me.flashyreese.mods.reeses_sodium_options.client.gui.SodiumVideoOptionsScreen;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.BasicFrame;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.tab.Tab;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.tab.TabFrame;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.jellysquid.mods.sodium.client.gui.widgets.FlatButtonWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

@Pseudo
@Mixin(SodiumVideoOptionsScreen.class)
public class MixinSodiumVideoOptionsScreen {

    @Shadow
    private FlatButtonWidget applyButton, closeButton, undoButton;

    @Shadow
    private FlatButtonWidget donateButton, hideDonateButton;

    @Shadow
    @Final
    private List<OptionPage> pages;

    @Unique
    @Inject(method = "parentFrameBuilder",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lme/flashyreese/mods/reeses_sodium_options/client/gui/frame/BasicFrame;createBuilder()Lme/flashyreese/mods/reeses_sodium_options/client/gui/frame/BasicFrame$Builder;"),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            cancellable = true
    )
    private void parentBasicFrameBuilder(CallbackInfoReturnable<BasicFrame.Builder> cir, Dim2i basicFrameDim, Dim2i tabFrameDim) {
        BasicFrame.Builder builder = BasicFrame.createBuilder()
                .setDimension(basicFrameDim)
                .addChild(parentDim -> TabFrame.createBuilder()
                        .setDimension(tabFrameDim)
                        .addTabs(tabs -> this.pages.forEach(page -> tabs.add(dim -> new Tab.Builder<>().from(page, dim))))
                        .addTab(subDim -> new Tab.Builder<>()
                                .setText(new LiteralText("Sodium Extra"))
                                .setFrame(TabFrame.createBuilder()
                                        .setDimension(subDim)
                                        .addTab(subSubDim -> new Tab.Builder<>().from(SodiumExtraGameOptionPages.animation(), subSubDim))
                                        .addTab(subSubDim -> new Tab.Builder<>().from(SodiumExtraGameOptionPages.particle(), subSubDim))
                                        .addTab(subSubDim -> new Tab.Builder<>().from(SodiumExtraGameOptionPages.detail(), subSubDim))
                                        .addTab(subSubDim -> new Tab.Builder<>().from(SodiumExtraGameOptionPages.render(), subSubDim))
                                        .addTab(subSubDim -> new Tab.Builder<>().from(SodiumExtraGameOptionPages.extra(), subSubDim))
                                        .build())
                                .build()
                        )
                        .build()
                )
                .addChild(dim -> this.undoButton)
                .addChild(dim -> this.applyButton)
                .addChild(dim -> this.closeButton)
                .addChild(dim -> this.donateButton)
                .addChild(dim -> this.hideDonateButton);
        cir.setReturnValue(builder);
    }

    /*
     * Terrible mixin code just to avoid enum reflection hacks, currently REQUIRES_GAME_RESTART is unused by Sodium.
     */
    @Inject(method = "applyChanges",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getInstance()Lnet/minecraft/client/MinecraftClient;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void applyChanges(CallbackInfo ci, HashSet<OptionStorage<?>> dirtyStorages, EnumSet<OptionFlag> flags) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (flags.contains(OptionFlag.REQUIRES_GAME_RESTART)) {
            client.getWindow().applyVideoMode();
        }
    }
}
