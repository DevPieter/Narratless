package nl.devpieter.narratless.setting.interfaces;

import java.lang.reflect.Type;

public interface IBooleanSetting extends ISetting<Boolean> {

    @Override
    default Type getType() {
        return Boolean.class;
    }

    void toggle();

    void setTrue();

    void setFalse();
}
