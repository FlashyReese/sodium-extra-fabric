package me.flashyreese.mods.sodiumextra.mixin.sodium.vsync;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptionPages;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
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

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lme/jellysquid/mods/sodium/client/gui/options/Option;)Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 5), remap = false)
    private static OptionGroup.Builder redirectVsyncToggle(OptionGroup.Builder instance, Option<?> option) {
        if (!option.getTooltip().getString().equals(new TranslatableText("sodium.options.v_sync.tooltip").getString())) {
            return instance.add(option);
        }
        return instance.add(OptionImpl.createBuilder(SodiumExtraGameOptions.VerticalSyncOption.class, SodiumExtraGameOptionPages.sodiumExtraOpts)
                .setName(new TranslatableText("options.vsync").getString())
                .setTooltip(new LiteralText(new TranslatableText("sodium.options.v_sync.tooltip").getString() + "\n- " + new TranslatableText("sodium-extra.option.use_adaptive_sync.name").getString() + ": " + new TranslatableText("sodium-extra.option.use_adaptive_sync.tooltip").getString()).getString())
                .setControl((opt) -> new CyclingControl<>(opt, SodiumExtraGameOptions.VerticalSyncOption.class,
                        SodiumExtraGameOptions.VerticalSyncOption.getAvailableOptions()))
                .setBinding((opts, value) -> {
                    switch (value) {
                        case OFF:
                            opts.extraSettings.useAdaptiveSync = false;
                            vanillaOpts.getData().enableVsync = false;
                            break;
                        case ON:
                            opts.extraSettings.useAdaptiveSync = false;
                            vanillaOpts.getData().enableVsync = true;
                            break;
                        case ADAPTIVE:
                            opts.extraSettings.useAdaptiveSync = true;
                            vanillaOpts.getData().enableVsync = false;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + value);
                    }
                    vanillaOpts.save();
                }, opts -> {
                    if (vanillaOpts.getData().enableVsync && !opts.extraSettings.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.ON;
                    } else if (!vanillaOpts.getData().enableVsync && !opts.extraSettings.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.OFF;
                    } else {
                        return SodiumExtraGameOptions.VerticalSyncOption.ADAPTIVE;
                    }
                })
                .setImpact(OptionImpact.VARIES)
                .build());
    }
}
