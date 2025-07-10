package nl.devpieter.narratless.setting.settings;

import nl.devpieter.narratless.setting.base.SettingBase;
import nl.devpieter.narratless.setting.interfaces.IBooleanSetting;

public class BooleanSetting extends SettingBase<Boolean> implements IBooleanSetting {

    public BooleanSetting(String identifier, Boolean defaultValue) {
        super(identifier, defaultValue);
    }

    public BooleanSetting(String identifier, Boolean defaultValue, boolean allowNull) {
        super(identifier, defaultValue, allowNull);
    }

    @Override
    public void toggle() {
        setValue(!getValue());
    }

    @Override
    public void setTrue() {
        setValue(true);
    }

    @Override
    public void setFalse() {
        setValue(false);
    }
}
