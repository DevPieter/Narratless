package nl.devpieter.narratless.setting;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import nl.devpieter.narratless.setting.interfaces.ISetting;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingManager {

    private static SettingManager INSTANCE;

    private final Gson gson = new Gson();
    private final HashMap<String, List<KeyedSetting<?>>> saveQueue = new HashMap<>();

    private SettingManager() {
    }

    public static SettingManager getInstance() {
        if (INSTANCE == null) INSTANCE = new SettingManager();
        return INSTANCE;
    }

    public boolean queueSave(File file, ISetting<?> setting) {
        String path = file.getAbsolutePath();
        KeyedSetting<?> keyedSetting = setting.asKeyedSetting();

        List<KeyedSetting<?>> settingsList = this.saveQueue.getOrDefault(path, new ArrayList<>());
        settingsList.removeIf(s -> s.key().equals(keyedSetting.key()));
        settingsList.add(keyedSetting);

        this.saveQueue.put(path, settingsList);
        return true;
    }

    public void forceSaveQueue() {
        if (this.saveQueue.isEmpty()) return;

        for (String path : this.saveQueue.keySet()) {
            File file = new File(path);

            if (this.saveBatchToFile(file, this.saveQueue.get(path))) continue;
        }

        this.saveQueue.clear();
    }

    public boolean loadSettings(File file, List<ISetting<?>> settings) {
        List<KeyedSetting<?>> batch = this.readBatchFromFile(file);
        for (ISetting<?> setting : settings) loadSettingFromBatch(setting, batch);

        return true;
    }

    private <T> boolean loadSettingFromBatch(ISetting<T> setting, List<KeyedSetting<?>> batch) {
        if (batch == null || batch.isEmpty()) {
            setting.setValue(setting.getDefault());
            return true;
        }

        for (KeyedSetting<?> keyedSetting : batch) {
            if (!keyedSetting.key().equals(setting.getIdentifier())) continue;

            JsonElement jsonElement = gson.toJsonTree(keyedSetting.value());
            T value = this.gson.fromJson(jsonElement, setting.getType());

            setting.setValue(setting.shouldAllowNull() ? value : value != null ? value : setting.getDefault());
            return true;
        }

        return false;
    }

    private boolean saveBatchToFile(File file, List<KeyedSetting<?>> settings) {
        List<KeyedSetting<?>> currentSettings = this.readBatchFromFile(file);
        if (currentSettings == null) currentSettings = new ArrayList<>();

        HashMap<String, KeyedSetting<?>> settingsMap = new HashMap<>();
        for (KeyedSetting<?> setting : currentSettings) {
            settingsMap.put(setting.key(), setting);
        }

        for (KeyedSetting<?> setting : settings) {
            settingsMap.put(setting.key(), setting);
        }

        currentSettings = new ArrayList<>(settingsMap.values());

        try (FileWriter writer = new FileWriter(file)) {
            this.gson.toJson(currentSettings, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private @Nullable List<KeyedSetting<?>> readBatchFromFile(File file) {
        try (Reader reader = new FileReader(file)) {
            return this.gson.fromJson(reader, new TypeToken<List<KeyedSetting<?>>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
