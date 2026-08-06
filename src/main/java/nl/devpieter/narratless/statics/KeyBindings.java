package nl.devpieter.narratless.statics;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final KeyMapping DISABLE_NARRATOR_KEY = new KeyMapping(
            "narratless.key.narrator.disable",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            KeyMapping.Category.MISC
    );

    public static final KeyMapping CYCLE_NARRATOR_KEY = new KeyMapping(
            "narratless.key.narrator.cycle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KeyMapping.Category.MISC
    );

    public static void init() {
        KeyMappingHelper.registerKeyMapping(DISABLE_NARRATOR_KEY);
        KeyMappingHelper.registerKeyMapping(CYCLE_NARRATOR_KEY);
    }
}
