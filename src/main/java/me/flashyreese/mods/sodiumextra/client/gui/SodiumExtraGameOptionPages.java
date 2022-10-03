package me.flashyreese.mods.sodiumextra.client.gui;

import com.google.common.collect.ImmutableList;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.gui.options.control.SliderControlExtended;
import me.flashyreese.mods.sodiumextra.client.gui.options.storage.SodiumExtraOptionsStorage;
import me.flashyreese.mods.sodiumextra.common.util.ControlValueFormatterExtended;
import me.flashyreese.mods.sodiumextra.mixin.fog.DimensionOptionsAccessor;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.stream.Collectors;

public class SodiumExtraGameOptionPages {
    public static final SodiumExtraOptionsStorage sodiumExtraOpts = new SodiumExtraOptionsStorage();

    private static LiteralText parseVanillaString(String key) {
        return new LiteralText((new TranslatableText(key).getString()).replaceAll("§.", ""));
    }

    public static OptionPage animation() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("gui.socialInteractions.tab_all"))
                        .setTooltip(new TranslatableText("sodium-extra.option.animations_all.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.animation = value, opts -> opts.animationSettings.animation)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .build());

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("block.minecraft.water"))
                        .setTooltip(new TranslatableText("sodium-extra.option.animate_water.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.water = value, opts -> opts.animationSettings.water)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("block.minecraft.lava"))
                        .setTooltip(new TranslatableText("sodium-extra.option.animate_lava.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.lava = value, opts -> opts.animationSettings.lava)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("block.minecraft.fire"))
                        .setTooltip(new TranslatableText("sodium-extra.option.animate_fire.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.fire = value, opts -> opts.animationSettings.fire)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("block.minecraft.nether_portal"))
                        .setTooltip(new TranslatableText("sodium-extra.option.animate_portal.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.animationSettings.portal = value, opts -> opts.animationSettings.portal)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.block_animations"))
                        .setTooltip(new TranslatableText("sodium-extra.option.block_animations.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.animationSettings.blockAnimations = value, options -> options.animationSettings.blockAnimations)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("block.minecraft.sculk_sensor"))
                        .setTooltip(new TranslatableText("sodium-extra.option.animate_sculk_sensor.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.animationSettings.sculkSensor = value, options -> options.animationSettings.sculkSensor)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .build());
        return new OptionPage(new TranslatableText("sodium-extra.option.animations"), ImmutableList.copyOf(groups));
    }

    public static OptionPage particle() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("gui.socialInteractions.tab_all"))
                        .setTooltip(new TranslatableText("sodium-extra.option.particles_all.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.particles = value, opts -> opts.particleSettings.particles)
                        .build()
                )
                .build());

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("subtitles.entity.generic.splash"))
                        .setTooltip(new TranslatableText("sodium-extra.option.rain_splash.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.rainSplash = value, opts -> opts.particleSettings.rainSplash)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("subtitles.block.generic.break"))
                        .setTooltip(new TranslatableText("sodium-extra.option.block_break.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.blockBreak = value, opts -> opts.particleSettings.blockBreak)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("subtitles.block.generic.hit"))
                        .setTooltip(new TranslatableText("sodium-extra.option.block_breaking.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.particleSettings.blockBreaking = value, opts -> opts.particleSettings.blockBreaking)
                        .build()
                )
                .build());

        Map<String, List<Identifier>> otherParticles = Registry.PARTICLE_TYPE.getIds().stream()
                .collect(Collectors.groupingBy(Identifier::getNamespace));
        otherParticles.forEach((namespace, identifiers) -> groups.add(identifiers.stream()
                .map(identifier -> OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(translatableName(identifier, "particles"))
                        .setTooltip(translatableTooltip(identifier, "particles"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, val) -> opts.particleSettings.otherMap.put(identifier, val),
                                opts -> opts.particleSettings.otherMap.getOrDefault(identifier, true))
                        .build())
                .sorted(Comparator.comparing(o -> o.getName().getString()))
                .collect(
                        OptionGroup::createBuilder,
                        OptionGroup.Builder::add,
                        (b1, b2) -> {
                        }
                ).build()
        ));

        return new OptionPage(parseVanillaString("options.particles"), ImmutableList.copyOf(groups));
    }

    public static OptionPage detail() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.sky"))
                        .setTooltip(new TranslatableText("sodium-extra.option.sky.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.detailSettings.sky = value, opts -> opts.detailSettings.sky)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.stars"))
                        .setTooltip(new TranslatableText("sodium-extra.option.stars.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.detailSettings.stars = value, opts -> opts.detailSettings.stars)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.sun_moon"))
                        .setTooltip(new TranslatableText("sodium-extra.option.sun_moon.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.detailSettings.sunMoon = value, opts -> opts.detailSettings.sunMoon)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("soundCategory.weather"))
                        .setTooltip(new TranslatableText("sodium-extra.option.rain_snow.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.detailSettings.rainSnow = value, opts -> opts.detailSettings.rainSnow)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.biome_colors"))
                        .setTooltip(new TranslatableText("sodium-extra.option.biome_colors.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.detailSettings.biomeColors = value, options -> options.detailSettings.biomeColors)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.sky_colors"))
                        .setTooltip(new TranslatableText("sodium-extra.option.sky_colors.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.detailSettings.skyColors = value, options -> options.detailSettings.skyColors)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build());
        return new OptionPage(new TranslatableText("sodium-extra.option.details"), ImmutableList.copyOf(groups));
    }

    public static OptionPage render() {
        List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.multi_dimension_fog"))
                        .setTooltip(new TranslatableText("sodium-extra.option.multi_dimension_fog.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.renderSettings.multiDimensionFogControl = value, options -> options.renderSettings.multiDimensionFogControl)
                        .build()
                )
                .build());

        if (SodiumExtraClientMod.options().renderSettings.multiDimensionFogControl) {
            if (SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.size() < DimensionOptionsAccessor.getBaseDimensions().size()) {
                groups.add(DimensionOptionsAccessor.getBaseDimensions().stream()
                        .map(dimensionOptionsRegistryKey -> OptionImpl.createBuilder(int.class, sodiumExtraOpts)
                                .setName(new TranslatableText("sodium-extra.option.fog", translatableName(dimensionOptionsRegistryKey.getValue(), "dimensions").getString()))
                                .setTooltip(new TranslatableText("sodium-extra.option.fog.tooltip"))
                                .setControl(option -> new SliderControlExtended(option, 0, 33, 1, ControlValueFormatterExtended.fogDistance(), false))
                                .setBinding((opts, val) -> opts.renderSettings.dimensionFogDistanceMap.put(dimensionOptionsRegistryKey.getValue(), val),
                                        opts -> opts.renderSettings.dimensionFogDistanceMap.getOrDefault(dimensionOptionsRegistryKey.getValue(), 0))
                                .build())
                        .collect(
                                OptionGroup::createBuilder,
                                OptionGroup.Builder::add,
                                (b1, b2) -> {
                                }
                        ).build()
                );
            } else {
                groups.add(SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.keySet().stream()
                        .map(identifier -> OptionImpl.createBuilder(int.class, sodiumExtraOpts)
                                .setName(new TranslatableText("sodium-extra.option.fog", translatableName(identifier, "dimensions").getString()))
                                .setTooltip(new TranslatableText("sodium-extra.option.fog.tooltip"))
                                .setControl(option -> new SliderControlExtended(option, 0, 33, 1, ControlValueFormatterExtended.fogDistance(), false))
                                .setBinding((opts, val) -> opts.renderSettings.dimensionFogDistanceMap.put(identifier, val),
                                        opts -> opts.renderSettings.dimensionFogDistanceMap.getOrDefault(identifier, 0))
                                .build()
                        ).collect(
                                OptionGroup::createBuilder,
                                OptionGroup.Builder::add,
                                (b1, b2) -> {
                                }
                        ).build()
                );
            }
        } else {
            groups.add(OptionGroup.createBuilder()
                    .add(OptionImpl.createBuilder(int.class, sodiumExtraOpts)
                            .setName(new TranslatableText("sodium-extra.option.single_fog"))
                            .setTooltip(new TranslatableText("sodium-extra.option.single_fog.tooltip"))
                            .setControl(option -> new SliderControlExtended(option, 0, 33, 1, ControlValueFormatterExtended.fogDistance(), false))
                            .setBinding((options, value) -> options.renderSettings.fogDistance = value, options -> options.renderSettings.fogDistance)
                            .build()
                    )
                    .build());
        }

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.linear_flat_color_blender"))
                        .setTooltip(new TranslatableText("sodium-extra.option.linear_flat_color_blender.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setImpact(OptionImpact.VARIES)
                        .setBinding((options, value) -> options.renderSettings.useLinearFlatColorBlender = value, options -> options.renderSettings.useLinearFlatColorBlender)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.light_updates"))
                        .setTooltip(new TranslatableText("sodium-extra.option.light_updates.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.renderSettings.lightUpdates = value, options -> options.renderSettings.lightUpdates)
                        .build()
                )
                .build());
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("entity.minecraft.item_frame"))
                        .setTooltip(new TranslatableText("sodium-extra.option.item_frames.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.renderSettings.itemFrame = value, opts -> opts.renderSettings.itemFrame)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("entity.minecraft.armor_stand"))
                        .setTooltip(new TranslatableText("sodium-extra.option.armor_stands.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.renderSettings.armorStand = value, options -> options.renderSettings.armorStand)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("entity.minecraft.painting"))
                        .setTooltip(new TranslatableText("sodium-extra.option.paintings.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.renderSettings.painting = value, options -> options.renderSettings.painting)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build());
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.beacon_beam"))
                        .setTooltip(new TranslatableText("sodium-extra.option.beacon_beam.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.renderSettings.beaconBeam = value, opts -> opts.renderSettings.beaconBeam)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.enchanting_table_book"))
                        .setTooltip(new TranslatableText("sodium-extra.option.enchanting_table_book.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.renderSettings.enchantingTableBook = value, opts -> opts.renderSettings.enchantingTableBook)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(parseVanillaString("block.minecraft.piston"))
                        .setTooltip(new TranslatableText("sodium-extra.option.piston.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.renderSettings.piston = value, options -> options.renderSettings.piston)
                        .build()
                )
                .build());
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.item_frame_name_tag"))
                        .setTooltip(new TranslatableText("sodium-extra.option.item_frame_name_tag.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.renderSettings.itemFrameNameTag = value, opts -> opts.renderSettings.itemFrameNameTag)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.player_name_tag"))
                        .setTooltip(new TranslatableText("sodium-extra.option.player_name_tag.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.renderSettings.playerNameTag = value, options -> options.renderSettings.playerNameTag)
                        .build()
                )
                .build());
        return new OptionPage(new TranslatableText("sodium-extra.option.render"), ImmutableList.copyOf(groups));
    }

    public static OptionPage extra() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, SodiumExtraGameOptionPages.sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.use_fast_random"))
                        .setTooltip(new TranslatableText("sodium-extra.option.use_fast_random.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.useFastRandom = value, options -> options.extraSettings.useFastRandom)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.reduce_resolution_on_mac"))
                        .setTooltip(new TranslatableText("sodium-extra.option.reduce_resolution_on_mac.tooltip"))
                        .setEnabled(MinecraftClient.IS_SYSTEM_MAC)
                        .setImpact(OptionImpact.HIGH)
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.extraSettings.reduceResolutionOnMac = value, opts -> opts.extraSettings.reduceResolutionOnMac)
                        .build()
                ).build());
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(SodiumExtraGameOptions.OverlayCorner.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.overlay_corner"))
                        .setTooltip(new TranslatableText("sodium-extra.option.overlay_corner.tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SodiumExtraGameOptions.OverlayCorner.class))
                        .setBinding((opts, value) -> opts.extraSettings.overlayCorner = value, opts -> opts.extraSettings.overlayCorner)
                        .build()
                )
                .add(OptionImpl.createBuilder(SodiumExtraGameOptions.TextContrast.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.text_contrast"))
                        .setTooltip(new TranslatableText("sodium-extra.option.text_contrast.tooltip"))
                        .setControl(option -> new CyclingControl<>(option, SodiumExtraGameOptions.TextContrast.class))
                        .setBinding((opts, value) -> opts.extraSettings.textContrast = value, opts -> opts.extraSettings.textContrast)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.show_fps"))
                        .setTooltip(new TranslatableText("sodium-extra.option.show_fps.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.extraSettings.showFps = value, opts -> opts.extraSettings.showFps)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.show_fps_extended"))
                        .setTooltip(new TranslatableText("sodium-extra.option.show_fps_extended.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.extraSettings.showFPSExtended = value, opts -> opts.extraSettings.showFPSExtended)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.show_coordinates"))
                        .setTooltip(new TranslatableText("sodium-extra.option.show_coordinates.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.extraSettings.showCoords = value, opts -> opts.extraSettings.showCoords)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.cloud_height"))
                        .setTooltip(new TranslatableText("sodium-extra.option.cloud_height.tooltip"))
                        .setControl(option -> new SliderControl(option, -64, 319, 1, ControlValueFormatter.number()))
                        .setBinding((options, value) -> options.extraSettings.cloudHeight = value, options -> options.extraSettings.cloudHeight)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.toasts"))
                        .setTooltip(new TranslatableText("sodium-extra.option.toasts.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.toasts = value, options -> options.extraSettings.toasts)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.advancementToast"))
                        .setTooltip(new TranslatableText("sodium-extra.option.advancementToast.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.advancementToast = value, options -> options.extraSettings.advancementToast)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.recipeToast"))
                        .setTooltip(new TranslatableText("sodium-extra.option.recipeToast.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.recipeToast = value, options -> options.extraSettings.recipeToast)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.systemToast"))
                        .setTooltip(new TranslatableText("sodium-extra.option.systemToast.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.systemToast = value, options -> options.extraSettings.systemToast)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.tutorialToast"))
                        .setTooltip(new TranslatableText("sodium-extra.option.tutorialToast.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.tutorialToast = value, options -> options.extraSettings.tutorialToast)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.instant_sneak"))
                        .setTooltip(new TranslatableText("sodium-extra.option.instant_sneak.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.instantSneak = value, options -> options.extraSettings.instantSneak)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
                        .setName(new TranslatableText("sodium-extra.option.prevent_shaders"))
                        .setTooltip(new TranslatableText("sodium-extra.option.prevent_shaders.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.extraSettings.preventShaders = value, options -> options.extraSettings.preventShaders)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build());

        return new OptionPage(new TranslatableText("sodium-extra.option.extras"), ImmutableList.copyOf(groups));
    }

    private static Text translatableName(Identifier identifier, String category) {
        String key = "options.".concat(category).concat(".").concat(identifier.getNamespace()).concat(".").concat(identifier.getPath());

        Text translatable = new TranslatableText(key);
        if (translatable.getString().equals(key)) {
            translatable = new TranslatableText(
                    Arrays.stream(key.substring(key.lastIndexOf('.') + 1).split("_"))
                            .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                            .collect(Collectors.joining(" "))
            );
        }
        return translatable;
    }

    private static Text translatableTooltip(Identifier identifier, String category) {
        String key = "options.".concat(category).concat(".").concat(identifier.getNamespace()).concat(".").concat(identifier.getPath()).concat(".tooltip");

        Text translatable = new TranslatableText(key);
        if (translatable.getString().equals(key)) {
            translatable = new TranslatableText(
                    "sodium-extra.option.".concat(category).concat(".tooltips"),
                    translatableName(identifier, category)
            );
        }
        return translatable;
    }
}
