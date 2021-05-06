package me.flashyreese.mods.sodiumextra.mixin.sodium;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptionPages;
import me.flashyreese.mods.sodiumextra.common.util.ControlValueFormatterExtended;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.VideoMode;
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

    @Inject(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 0, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private static void general(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups){
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                        .setName("Resolution")
                        .setTooltip("Sets resolution of the game.")
                        .setControl(option -> new SliderControl(option, 0, MinecraftClient.getInstance().getWindow().getMonitor() != null ? MinecraftClient.getInstance().getWindow().getMonitor().getVideoModeCount() : 0, 1, ControlValueFormatterExtended.resolution()))
                        .setBinding((options, value) -> {
                            if (MinecraftClient.getInstance().getWindow().getMonitor() != null) {
                                if (value == 0) {
                                    MinecraftClient.getInstance().getWindow().setVideoMode(Optional.empty());
                                } else {
                                    MinecraftClient.getInstance().getWindow().setVideoMode(Optional.of(MinecraftClient.getInstance().getWindow().getMonitor().getVideoMode(value - 1)));
                                }
                            }
                        }, options -> {
                            if (MinecraftClient.getInstance().getWindow().getMonitor() == null) {
                                return 0;
                            } else {
                                Optional<VideoMode> optional = MinecraftClient.getInstance().getWindow().getVideoMode();
                                return optional.map((videoMode) -> MinecraftClient.getInstance().getWindow().getMonitor().findClosestVideoModeIndex(videoMode) + 1).orElse(0);
                            }
                        })
                        .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                        .setImpact(OptionImpact.HIGH)
                        .build())
                .build());
    }

    @Inject(method = "advanced", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 2), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private static void advanced(CallbackInfoReturnable<OptionPage> cir, boolean disableBlacklist, List<OptionGroup> groups){
        OptionGroup original = groups.get(1);
        OptionGroup.Builder modifiedBuilder = OptionGroup.createBuilder();
        original.getOptions().forEach(modifiedBuilder::add);
        modifiedBuilder.add(OptionImpl.createBuilder(boolean.class, SodiumExtraGameOptionPages.sodiumExtraOpts)
                .setName("Use Fast Random")
                .setTooltip("If enabled, a fast random function will be used for block rendering. This can affect the rotation of randomly rotated textures when compared to vanilla.")
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> options.extraSettings.useFastRandom = value, options -> options.extraSettings.useFastRandom)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build());
        groups.set(1, modifiedBuilder.build());
    }
}
