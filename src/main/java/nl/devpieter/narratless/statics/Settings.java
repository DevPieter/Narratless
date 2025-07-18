package nl.devpieter.narratless.statics;

import nl.devpieter.narratless.Narratless;
import nl.devpieter.utilize.setting.SettingManager;
import nl.devpieter.utilize.setting.interfaces.ISetting;
import nl.devpieter.utilize.setting.settings.BooleanSetting;
import nl.devpieter.utilize.utils.common.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class Settings {

    private static final File CONFIG_FOLDER = new File("config/narratless");
    private static final File SETTINGS_FILE = new File(CONFIG_FOLDER, "Settings.json");

    public static final BooleanSetting NARRATOR_KEY_ENABLED = new BooleanSetting(
            "narratless.narrator_key_enabled",
            true
    );

    public static final BooleanSetting NARRATOR_REQUIRES_MODIFIER = new BooleanSetting(
            "narratless.narrator_requires_modifier",
            true
    );

    public static void load() {
        if (!FileUtils.doesFileExist(SETTINGS_FILE)) {
            Narratless.getInstance().getLogger().info("Settings file does not exist, skipping load.");
            return;
        }

        SettingManager settingManager = SettingManager.getInstance();
        settingManager.loadSettings(SETTINGS_FILE, List.of(
                NARRATOR_KEY_ENABLED,
                NARRATOR_REQUIRES_MODIFIER
        ));

        Narratless.getInstance().getLogger().info("Settings loaded successfully.");
    }

    public static void save(ISetting<?> setting) {
        if (!FileUtils.tryCreateFile(SETTINGS_FILE)) {
            Narratless.getInstance().getLogger().info("Could not create settings file.");
            return;
        }

        SettingManager settingManager = SettingManager.getInstance();
        settingManager.queueSave(SETTINGS_FILE, setting);
    }
}
