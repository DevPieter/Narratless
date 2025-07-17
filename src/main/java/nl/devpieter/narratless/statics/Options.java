package nl.devpieter.narratless.statics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class Options {

    public static final SimpleOption<Boolean> NARRATOR_KEY_ENABLED_OPTION = SimpleOption.ofBoolean(
            "narratless.options.narrator_key.enabled",
            SimpleOption.constantTooltip(Text.translatable("narratless.options.narrator_key.enabled.tooltip")),
            true,
            aBoolean -> {
                Settings.NARRATOR_KEY_ENABLED.setValue(aBoolean);
                Settings.save(Settings.NARRATOR_KEY_ENABLED);
            }
    );

    public static final SimpleOption<Boolean> NARRATOR_REQUIRES_MODIFIER_OPTION = SimpleOption.ofBoolean(
            "narratless.options.narrator_key.requires_modifier",
            SimpleOption.constantTooltip(MinecraftClient.IS_SYSTEM_MAC ?
                    Text.translatable("narratless.options.narrator_key.requires_modifier.tooltip.mac") :
                    Text.translatable("narratless.options.narrator_key.requires_modifier.tooltip")),
            true,
            aBoolean -> {
                Settings.NARRATOR_REQUIRES_MODIFIER.setValue(aBoolean);
                Settings.save(Settings.NARRATOR_REQUIRES_MODIFIER);
            }
    );

    public static final SimpleOption<Boolean> NARRATOR_DECOY_OPTION = SimpleOption.ofBoolean(
            "options.accessibility.narrator_hotkey",
            SimpleOption.constantTooltip(Text.of("This is a decoy option to prevent default narrator hotkey from being set.")),
            false
    );

    public static void init() {
        // Java magic
    }
}
