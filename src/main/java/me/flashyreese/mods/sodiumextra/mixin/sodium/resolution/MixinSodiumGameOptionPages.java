package me.flashyreese.mods.sodiumextra.mixin.sodium.resolution;

import me.flashyreese.mods.sodiumextra.client.gui.options.control.SliderControlExtended;
import me.flashyreese.mods.sodiumextra.common.util.ControlValueFormatterExtended;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.VideoMode;
import net.minecraft.client.util.Window;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(SodiumGameOptionPages.class)
public class MixinSodiumGameOptionPages {

    @Shadow
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Inject(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 1, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private static void general(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups){
        Window window = MinecraftClient.getInstance().getWindow();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                        .setName(new TranslatableText("options.fullscreen.resolution"))
                        .setTooltip(new TranslatableText("sodium-extra.option.resolution.tooltip"))
                        .setControl(option -> new SliderControlExtended(option, 0, window.getMonitor() != null ? window.getMonitor().getVideoModeCount() : 0, 1, ControlValueFormatterExtended.resolution(), false))
                        .setBinding((options, value) -> {
                            if (window.getMonitor() != null) {
                                if (value == 0) {
                                    window.setVideoMode(Optional.empty());
                                } else {
                                    window.setVideoMode(Optional.of(window.getMonitor().getVideoMode(value - 1)));
                                }
                            }
                        }, options -> {
                            if (window.getMonitor() == null) {
                                return 0;
                            } else {
                                Optional<VideoMode> optional = window.getVideoMode();
                                return optional.map((videoMode) -> window.getMonitor().findClosestVideoModeIndex(videoMode) + 1).orElse(0);
                            }
                        })
                        .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                        .setImpact(OptionImpact.HIGH)
                        .build())
                .build());
    }

    @Inject(method = "quality", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 2, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private static void quality(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups){
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                        .setName(new TranslatableText("options.screenEffectScale"))
                        .setTooltip(new TranslatableText("options.screenEffectScale.tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                        .setBinding((opts, value) -> opts.distortionEffectScale = (float) value / 100.0F, (opts) -> Math.round(opts.distortionEffectScale * 100.0F))
                        .setImpact(OptionImpact.LOW)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                        .setName(new TranslatableText("options.fovEffectScale"))
                        .setTooltip(new TranslatableText("options.fovEffectScale.tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                        .setBinding((opts, value) -> opts.fovEffectScale = (float) Math.sqrt(value / 100.0F), (opts) -> (int) Math.round(Math.pow(opts.fovEffectScale , 2.0D) * 100.0F))
                        .setImpact(OptionImpact.LOW)
                        .build()
                )
                .build());
    }
}
