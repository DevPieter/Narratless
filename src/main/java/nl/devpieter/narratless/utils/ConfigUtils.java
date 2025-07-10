package nl.devpieter.narratless.utils;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ConfigUtils {

    public static @Nullable File getConfigFolder(boolean createIfNotExists) {
        File configFolder = new File("config/narratless");

        boolean exists = configFolder.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateDirectories(configFolder) ? configFolder : null;
    }

    public static @Nullable File getSettingsFile(boolean createIfNotExists) {
        File configFolder = getConfigFolder(createIfNotExists);
        if (configFolder == null) return null;

        String fileName = "Settings.json";
        File settingsFile = new File(configFolder, fileName);

        boolean exists = settingsFile.exists();
        if (!exists && !createIfNotExists) return null;

        return FileUtils.tryCreateFile(settingsFile) ? settingsFile : null;
    }
}
