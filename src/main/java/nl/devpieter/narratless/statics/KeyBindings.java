package nl.devpieter.narratless.statics;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final KeyBinding DISABLE_NARRATOR_KEY = new KeyBinding(
            "narratless.key.narrator.disable",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            KeyBinding.MISC_CATEGORY
    );

    public static final KeyBinding CYCLE_NARRATOR_KEY = new KeyBinding(
            "narratless.key.narrator.cycle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KeyBinding.MISC_CATEGORY
    );

    public static void init() {
        KeyBindingHelper.registerKeyBinding(DISABLE_NARRATOR_KEY);
        KeyBindingHelper.registerKeyBinding(CYCLE_NARRATOR_KEY);
    }
}
