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
                        .setBinding((opts, value) -> opts.animationSettings.animation = value, opts -> opts.animationSettings.animation)
                        .build()
                )
                .build());

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Water")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.animateWater = value, opts -> opts.animationSettings.animateWater)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Lava")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.animateLava = value, opts -> opts.animationSettings.animateLava)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Fire")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.animateFire = value, opts -> opts.animationSettings.animateFire)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Animate Portal")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.animatePortal = value, opts -> opts.animationSettings.animatePortal)
                        .build()
                )
                .build());
        return new OptionPage("Animations", ImmutableList.copyOf(groups));
    }

    public static OptionPage particle() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Particles")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.particles = value, opts -> opts.particleSettings.particles)
                        .build()
                )
                .build());

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Rain Splash")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.rainSplash = value, opts -> opts.particleSettings.rainSplash)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Explosion")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.explosion = value, opts -> opts.particleSettings.explosion)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Water")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.water = value, opts -> opts.particleSettings.water)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Smoke")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.smoke = value, opts -> opts.particleSettings.smoke)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Potion")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.potion = value, opts -> opts.particleSettings.potion)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Portal")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.portal = value, opts -> opts.particleSettings.portal)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Redstone")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.redstone = value, opts -> opts.particleSettings.redstone)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Fluid Drip")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.fluidDrip = value, opts -> opts.particleSettings.fluidDrip)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Firework")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.firework = value, opts -> opts.particleSettings.firework)
                        .build()
                )
                .build());
        return new OptionPage("Particles", ImmutableList.copyOf(groups));
    }

    public static OptionPage detail() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Rain & Snow")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.detailSettings.rainSnow = value, opts -> opts.detailSettings.rainSnow)
                        .build()
                )
                .build());
        return new OptionPage("Details", ImmutableList.copyOf(groups));
    }

    public static OptionPage extra() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Show FPS")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.extraSettings.showFps = value, opts -> opts.extraSettings.showFps)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName("Fog")
                        .setTooltip("")
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.extraSettings.enableFog = value, opts -> opts.extraSettings.enableFog)
                        .build()
                )
                .build());
        return new OptionPage("Extras", ImmutableList.copyOf(groups));
    }
}
