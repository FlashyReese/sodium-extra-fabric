package me.flashyreese.mods.sodiumextra.client.gui;

import me.flashyreese.mods.sodiumextra.client.gui.frame.AbstractFrame;
import me.flashyreese.mods.sodiumextra.client.gui.frame.BasicFrame;
import me.flashyreese.mods.sodiumextra.client.gui.frame.tab.Tab;
import me.flashyreese.mods.sodiumextra.client.gui.frame.tab.TabFrame;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.jellysquid.mods.sodium.client.gui.widgets.FlatButtonWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class SodiumVideoOptionsScreen extends Screen {

    private AbstractFrame frame;

    private final Screen prevScreen;
    private final List<OptionPage> pages = new ArrayList<>();

    private FlatButtonWidget applyButton, closeButton, undoButton;
    private boolean hasPendingChanges;

    public SodiumVideoOptionsScreen(Screen prev) {
        super(new LiteralText("Reese's Sodium Menu"));

        this.prevScreen = prev;

        this.pages.add(SodiumGameOptionPages.general());
        this.pages.add(SodiumGameOptionPages.quality());
        this.pages.add(SodiumGameOptionPages.advanced());
        this.pages.add(SodiumExtraGameOptionPages.animation());
        this.pages.add(SodiumExtraGameOptionPages.particle());
        this.pages.add(SodiumExtraGameOptionPages.detail());
        this.pages.add(SodiumExtraGameOptionPages.extra());
    }

    @Override
    protected void init() {
        Dim2i basicFrameDim = new Dim2i(0, 0, this.width, this.height);
        Dim2i tabFrameDim = new Dim2i(basicFrameDim.getWidth() / 4 / 2, basicFrameDim.getHeight() / 4 / 2, basicFrameDim.getWidth() / 4 * 3, basicFrameDim.getHeight() / 4 * 3);

        Dim2i undoButtonDim = new Dim2i(tabFrameDim.getLimitX() - 203, tabFrameDim.getLimitY() + 5, 65, 20);
        Dim2i applyButtonDim = new Dim2i(tabFrameDim.getLimitX() - 134, tabFrameDim.getLimitY() + 5, 65, 20);
        Dim2i closeButtonDim = new Dim2i(tabFrameDim.getLimitX() - 65, tabFrameDim.getLimitY() + 5, 65, 20);

        this.undoButton = new FlatButtonWidget(undoButtonDim, "Undo", this::undoChanges);
        this.applyButton = new FlatButtonWidget(applyButtonDim, "Apply", this::applyChanges);
        this.closeButton = new FlatButtonWidget(closeButtonDim, "Close", this::onClose);
        this.frame =
                BasicFrame.createBuilder()
                        .setDimension(basicFrameDim)
                        .addChild(parentDim -> TabFrame.createBuilder()
                                .setDimension(tabFrameDim)
                                .addTabs(tabs -> this.pages.forEach(page -> tabs.add(dim -> new Tab.Builder<>().from(page, dim))))
                                .build()
                        )
                        .addChild(dim -> this.undoButton)
                        .addChild(dim -> this.applyButton)
                        .addChild(dim -> this.closeButton)
                        .build();
        this.children.add(this.frame);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        this.updateControls();
        this.frame.render(matrices, mouseX, mouseY, delta);
    }

    private void updateControls() {
        boolean hasChanges = this.getAllOptions()
                .anyMatch(Option::hasChanged);

        for (OptionPage page : this.pages) {
            for (Option<?> option : page.getOptions()) {
                if (option.hasChanged()) {
                    hasChanges = true;
                }
            }
        }

        this.applyButton.setEnabled(hasChanges);
        this.undoButton.setVisible(hasChanges);
        this.closeButton.setEnabled(!hasChanges);

        this.hasPendingChanges = hasChanges;
    }

    private Stream<Option<?>> getAllOptions() {
        return this.pages.stream()
                .flatMap(s -> s.getOptions().stream());
    }

    private void applyChanges() {
        final HashSet<OptionStorage<?>> dirtyStorages = new HashSet<>();
        final EnumSet<OptionFlag> flags = EnumSet.noneOf(OptionFlag.class);

        this.getAllOptions().forEach((option -> {
            if (!option.hasChanged()) {
                return;
            }

            option.applyChanges();

            flags.addAll(option.getFlags());
            dirtyStorages.add(option.getStorage());
        }));

        MinecraftClient client = MinecraftClient.getInstance();

        if (flags.contains(OptionFlag.REQUIRES_RENDERER_RELOAD)) {
            client.worldRenderer.reload();
        }

        if (flags.contains(OptionFlag.REQUIRES_ASSET_RELOAD)) {
            client.resetMipmapLevels(client.options.mipmapLevels);
            client.reloadResourcesConcurrently();
        }

        for (OptionStorage<?> storage : dirtyStorages) {
            storage.save();
        }
    }

    private void undoChanges() {
        this.getAllOptions()
                .forEach(Option::reset);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_P && (modifiers & GLFW.GLFW_MOD_SHIFT) != 0) {
            MinecraftClient.getInstance().openScreen(new VideoOptionsScreen(this.prevScreen, MinecraftClient.getInstance().options));

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !this.hasPendingChanges;
    }

    @Override
    public void onClose() {
        this.client.openScreen(this.prevScreen);
    }
}