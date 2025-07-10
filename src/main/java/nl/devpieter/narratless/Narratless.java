package nl.devpieter.narratless;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import nl.devpieter.narratless.setting.SettingManager;
import nl.devpieter.narratless.setting.settings.BooleanSetting;
import nl.devpieter.narratless.utils.ConfigUtils;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.List;

public class Narratless implements ClientModInitializer {

    // TODO - Move the settings handling to Utilize

    private static Narratless INSTANCE;

    private final BooleanSetting narratorHotkeyEnabled = new BooleanSetting(
            "narratless.options.narrator_hotkey",
            true
    );

    private final BooleanSetting requiresModifier = new BooleanSetting(
            "narratless.options.narrator_hotkey.requires_modifier",
            true
    );

    private final KeyBinding disableHotkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "narratless.key.narrator.disable",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            KeyBinding.MISC_CATEGORY
    ));

    private final KeyBinding cycleHotkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "narratless.key.narrator.cycle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KeyBinding.MISC_CATEGORY
    ));

    private final SimpleOption<Boolean> hotkeyEnabledOption = SimpleOption.ofBoolean(
            "narratless.options.narrator_hotkey",
            SimpleOption.constantTooltip(Text.translatable("narratless.options.narrator_hotkey.tooltip")),
            true,
            aBoolean -> {
                File settingsFile = ConfigUtils.getSettingsFile(true);
                if (settingsFile == null) return;

                SettingManager settingManager = SettingManager.getInstance();

                this.narratorHotkeyEnabled.setValue(aBoolean);
                settingManager.queueSave(settingsFile, this.narratorHotkeyEnabled);
                settingManager.forceSaveQueue();
            }
    );

    private final SimpleOption<Boolean> requiresModifierOption = SimpleOption.ofBoolean(
            "narratless.options.narrator_hotkey.requires_modifier",
            SimpleOption.constantTooltip(MinecraftClient.IS_SYSTEM_MAC ?
                    Text.translatable("narratless.options.narrator_hotkey.requires_modifier.tooltip.mac") :
                    Text.translatable("narratless.options.narrator_hotkey.requires_modifier.tooltip")),
            true,
            aBoolean -> {
                File settingsFile = ConfigUtils.getSettingsFile(true);
                if (settingsFile == null) return;

                SettingManager settingManager = SettingManager.getInstance();

                this.requiresModifier.setValue(aBoolean);
                settingManager.queueSave(settingsFile, this.requiresModifier);
                settingManager.forceSaveQueue();
            }
    );

    private final SimpleOption<Boolean> narratorDecoyOption = SimpleOption.ofBoolean(
            "options.accessibility.narrator_hotkey",
            SimpleOption.constantTooltip(Text.of("This is a decoy option to prevent default narrator hotkey from being set.")),
            false
    );

    public static Narratless getInstance() {
        if (INSTANCE == null) throw new IllegalStateException("Narratless is not initialized yet.");
        return INSTANCE;
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        File settingsFile = ConfigUtils.getSettingsFile(false);

        if (settingsFile != null) {
            SettingManager settingManager = SettingManager.getInstance();
            settingManager.loadSettings(settingsFile, List.of(this.narratorHotkeyEnabled, this.requiresModifier));
        }

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            this.narratorDecoyOption.setValue(false);
            MinecraftClient.getInstance().options.write();

            hotkeyEnabledOption.setValue(this.narratorHotkeyEnabled.getValue());
            requiresModifierOption.setValue(this.requiresModifier.getValue());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (disableHotkey.wasPressed()) this.tryDisableNarrator(client);
            if (cycleHotkey.wasPressed()) this.tryCycleNarrator(client);
        });
    }

    public SimpleOption<Boolean> getHotkeyEnabledOption() {
        return hotkeyEnabledOption;
    }

    public SimpleOption<Boolean> getRequiresModifierOption() {
        return requiresModifierOption;
    }

    public SimpleOption<Boolean> getNarratorDecoyOption() {
        return narratorDecoyOption;
    }

    private void tryDisableNarrator(MinecraftClient client) {
        if (this.requiresModifierOption.getValue() && !Screen.hasControlDown()) return;
        SimpleOption<NarratorMode> narratorOption = client.options.getNarrator();

        narratorOption.setValue(NarratorMode.OFF);
        client.options.write();

        this.refreshNarrator(client);
    }

    private void tryCycleNarrator(MinecraftClient client) {
        if (this.hotkeyEnabledOption.getValue() == false) return;
        if (this.requiresModifierOption.getValue() && !Screen.hasControlDown()) return;

        SimpleOption<NarratorMode> narratorOption = client.options.getNarrator();
        narratorOption.setValue(NarratorMode.byId(narratorOption.getValue().getId() + 1));
        client.options.write();

        this.refreshNarrator(client);
    }

    private void refreshNarrator(MinecraftClient client) {
        SimpleOption<NarratorMode> narratorOption = client.options.getNarrator();
        boolean isOff = narratorOption.getValue() == NarratorMode.OFF;

        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen != null) screen.refreshNarrator(isOff);
    }
}
