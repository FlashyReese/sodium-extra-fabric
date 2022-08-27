package me.flashyreese.mods.sodiumextra.mixin.sodium.vsync;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SodiumGameOptionPages.class)
public class MixinSodiumGameOptionsPages {
    @Shadow
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Shadow
    @Final
    private static SodiumOptionsStorage sodiumOpts;

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lme/jellysquid/mods/sodium/client/gui/options/Option;)Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 5), remap = false)
    private static OptionGroup.Builder redirectVsyncToggle(OptionGroup.Builder instance, Option<?> option) {
        if (!option.getTooltip().getString().equals(Text.translatable("sodium.options.v_sync.tooltip").getString())) {
            return instance.add(option);
        }
        return instance.add(OptionImpl.createBuilder(SodiumExtraGameOptions.VerticalSyncOption.class, sodiumOpts)
                .setName(Text.translatable("options.vsync"))
                .setTooltip(Text.literal(Text.translatable("sodium.options.v_sync.tooltip").getString() + "\n- " + Text.translatable("sodium-extra.option.use_adaptive_sync.name").getString() + ": " + Text.translatable("sodium.options.use_adaptive_sync.tooltip").getString()))
                .setControl((opt) -> new CyclingControl<>(opt, SodiumExtraGameOptions.VerticalSyncOption.class,
                        SodiumExtraGameOptions.VerticalSyncOption.getAvailableOptions()))
                .setBinding((opts, value) -> {
                    switch (value) {
                        case OFF -> {
                            opts.performance.useAdaptiveSync = false;
                            vanillaOpts.getData().getEnableVsync().setValue(false);
                        }
                        case ON -> {
                            opts.performance.useAdaptiveSync = false;
                            vanillaOpts.getData().getEnableVsync().setValue(true);
                        }
                        case ADAPTIVE -> {
                            opts.performance.useAdaptiveSync = true;
                            vanillaOpts.getData().getEnableVsync().setValue(true);
                        }
                    }
                    vanillaOpts.save();
                }, opts -> {
                    if (vanillaOpts.getData().getEnableVsync().getValue() && !opts.performance.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.ON;
                    } else if (!vanillaOpts.getData().getEnableVsync().getValue() && !opts.performance.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.OFF;
                    } else {
                        return SodiumExtraGameOptions.VerticalSyncOption.ADAPTIVE;
                    }
                })
                .setImpact(OptionImpact.VARIES)
                .build());
    }

    @Redirect(method = "performance", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lme/jellysquid/mods/sodium/client/gui/options/Option;)Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 7), remap = false)
    private static OptionGroup.Builder removeAdaptiveSyncToggle(OptionGroup.Builder instance, Option<?> option) {
        if (!option.getTooltip().getString().equals(Text.translatable("sodium.options.use_adaptive_sync.tooltip").getString())) {
            return instance.add(option);
        }
        return instance;
    }
}
