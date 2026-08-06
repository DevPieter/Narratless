package nl.devpieter.narratless.statics;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.input.InputQuirks;
import net.minecraft.network.chat.Component;

public class NarratlessOptions {

    public static final OptionInstance<Boolean> NARRATOR_KEY_ENABLED_OPTION = OptionInstance.createBoolean(
            "narratless.options.narrator_key.enabled",
            OptionInstance.cachedConstantTooltip(Component.translatable("narratless.options.narrator_key.enabled.tooltip")),
            true,
            aBoolean -> {
                Settings.NARRATOR_KEY_ENABLED.setValue(aBoolean);
                Settings.save(Settings.NARRATOR_KEY_ENABLED);
            }
    );

    public static final OptionInstance<Boolean> NARRATOR_REQUIRES_MODIFIER_OPTION = OptionInstance.createBoolean(
            "narratless.options.narrator_key.requires_modifier",
            OptionInstance.cachedConstantTooltip(Component.translatable(InputQuirks.REPLACE_CTRL_KEY_WITH_CMD_KEY ?
                    "narratless.options.narrator_key.requires_modifier.tooltip.mac" :
                    "narratless.options.narrator_key.requires_modifier.tooltip")
            ),
            true,
            aBoolean -> {
                Settings.NARRATOR_REQUIRES_MODIFIER.setValue(aBoolean);
                Settings.save(Settings.NARRATOR_REQUIRES_MODIFIER);
            }
    );

    public static final OptionInstance<Boolean> NARRATOR_DECOY_OPTION = OptionInstance.createBoolean(
            "options.accessibility.narrator_hotkey",
            OptionInstance.cachedConstantTooltip(Component.literal("This is a decoy option to prevent default narrator hotkey from being set.")),
            false
    );

    public static void init() {
        // Java magic
    }
}
