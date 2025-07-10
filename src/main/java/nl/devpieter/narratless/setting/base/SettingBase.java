package nl.devpieter.narratless.setting.base;

import nl.devpieter.narratless.setting.interfaces.ISetting;

public abstract class SettingBase<T> implements ISetting<T> {

    private final String identifier;
    private final T defaultValue;

    private final boolean allowNull;

    private T value;

    public SettingBase(String identifier, T defaultValue) {
        this(identifier, defaultValue, false);
    }

    public SettingBase(String identifier, T defaultValue, boolean allowNull) {
        this.identifier = identifier;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.allowNull = allowNull;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean shouldAllowNull() {
        return allowNull;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
