package me.flashyreese.mods.sodiumextra.client.gui;

import com.google.common.collect.ImmutableList;
import me.flashyreese.mods.sodiumextra.client.gui.options.storage.SodiumExtraOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;

import java.util.ArrayList;
import java.util.List;

public class SodiumExtraGameOptionPages {
    private static final SodiumExtraOptionsStorage sodiumExtraOpts = new SodiumExtraOptionsStorage();

    public static OptionPage animation() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animations")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animation.animation = value, opts -> opts.animation.animation)
                        .build()
                )
                .build());

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Water")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animation.animateWater = value, opts -> opts.animation.animateWater)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Lava")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animation.animateLava = value, opts -> opts.animation.animateLava)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Fire")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animation.animateFire = value, opts -> opts.animation.animateFire)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Portal")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animation.animatePortal = value, opts -> opts.animation.animatePortal)
                        .build()
                )
                .build());
        return new OptionPage("Animations", ImmutableList.copyOf(groups));
    }
}
