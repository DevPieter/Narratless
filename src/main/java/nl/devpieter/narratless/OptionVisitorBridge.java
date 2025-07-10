package nl.devpieter.narratless;

import net.minecraft.client.option.SimpleOption;

@SuppressWarnings("rawtypes")
public interface OptionVisitorBridge {
    <T> void accept(String name, SimpleOption<T> option);
}
