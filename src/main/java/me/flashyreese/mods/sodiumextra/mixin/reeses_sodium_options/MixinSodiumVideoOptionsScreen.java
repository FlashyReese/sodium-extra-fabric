package me.flashyreese.mods.sodiumextra.mixin.reeses_sodium_options;

import me.flashyreese.mods.reeses_sodium_options.client.gui.SodiumVideoOptionsScreen;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptionPages;
import me.jellysquid.mods.sodium.config.render.OptionFlag;
import me.jellysquid.mods.sodium.config.render.OptionPage;
import me.jellysquid.mods.sodium.config.render.storage.OptionStorage;
import me.jellysquid.mods.sodium.gui.widgets.FlatButtonWidget;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

@Pseudo
@Mixin(SodiumVideoOptionsScreen.class)
public abstract class MixinSodiumVideoOptionsScreen {

    @Shadow
    private FlatButtonWidget applyButton, closeButton, undoButton;

    @Shadow
    private FlatButtonWidget donateButton, hideDonateButton;

    @Shadow
    @Final
    private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(CallbackInfo ci) {
        this.pages.add(SodiumExtraGameOptionPages.animation());
        this.pages.add(SodiumExtraGameOptionPages.particle());
        this.pages.add(SodiumExtraGameOptionPages.detail());
        this.pages.add(SodiumExtraGameOptionPages.render());
        this.pages.add(SodiumExtraGameOptionPages.extra());
    }

    /*
    Let's not do this :)
    @Unique
    @Inject(method = "parentBasicFrameBuilder",
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
                        .addTabs(tabs -> this.pages.forEach(page -> {
                            if (page.getName() instanceof TranslatableText translatableText) {
                                if (!(translatableText.getKey().startsWith("sodium-extra") || translatableText.getKey().startsWith("options.particles"))) {
                                    tabs.add(dim -> new Tab.Builder<>().from(page, dim));
                                }
                            }
                        }))
                        .addTab(subDim -> new Tab.Builder<>()
                                .setText(new LiteralText("Sodium Extra"))
                                .setFrame(TabFrame.createBuilder()
                                        .setDimension(subDim)
                                        .addTabs(subTabs -> this.pages.forEach(page -> {
                                            if (page.getName() instanceof TranslatableText translatableText) {
                                                if (translatableText.getKey().startsWith("sodium-extra") || translatableText.getKey().startsWith("options.particles")) {
                                                    subTabs.add(subSubDim -> new Tab.Builder<>().from(page, subSubDim));
                                                }
                                            }
                                        }))
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
    }*/

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