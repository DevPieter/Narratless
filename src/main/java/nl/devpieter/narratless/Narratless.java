package nl.devpieter.narratless;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import nl.devpieter.narratless.statics.KeyBindings;
import nl.devpieter.narratless.statics.NarratlessOptions;
import nl.devpieter.narratless.statics.Settings;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Narratless implements ClientModInitializer {

    private static Narratless INSTANCE;

    private final Logger logger = LoggerFactory.getLogger("Narratless");

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        Settings.load();
        KeyBindings.init();
        NarratlessOptions.init();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            NarratlessOptions.NARRATOR_DECOY_OPTION.set(false);
            Minecraft.getInstance().options.save();

            NarratlessOptions.NARRATOR_KEY_ENABLED_OPTION.set(Settings.NARRATOR_KEY_ENABLED.getValue());
            NarratlessOptions.NARRATOR_REQUIRES_MODIFIER_OPTION.set(Settings.NARRATOR_REQUIRES_MODIFIER.getValue());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KeyBindings.DISABLE_NARRATOR_KEY.consumeClick()) this.tryDisableNarrator(client);
            if (KeyBindings.CYCLE_NARRATOR_KEY.consumeClick()) this.tryCycleNarrator(client);
        });
    }

    public static Narratless getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return this.logger;
    }

    private void tryDisableNarrator(Minecraft client) {
        if (NarratlessOptions.NARRATOR_REQUIRES_MODIFIER_OPTION.get() && !isControlPressed()) return;
        OptionInstance<NarratorStatus> narratorOption = client.options.narrator();

        narratorOption.set(NarratorStatus.OFF);
        client.options.save();

        this.refreshNarrator(client);
    }

    private void tryCycleNarrator(Minecraft client) {
        if (!NarratlessOptions.NARRATOR_KEY_ENABLED_OPTION.get()) return;
        if (NarratlessOptions.NARRATOR_REQUIRES_MODIFIER_OPTION.get() && !isControlPressed()) return;

        OptionInstance<NarratorStatus> narratorOption = client.options.narrator();
        narratorOption.set(NarratorStatus.byId(narratorOption.get().getId() + 1));
        client.options.save();

        this.refreshNarrator(client);
    }

    private void refreshNarrator(@NotNull Minecraft client) {
        OptionInstance<NarratorStatus> narratorOption = client.options.narrator();
        boolean isOff = narratorOption.get() == NarratorStatus.OFF;

        //#if MC>=262
        Screen screen = client.gui.screen();
        //#else
        //$$ Screen screen = client.screen;
        //#endif

        if (screen != null) screen.updateNarratorStatus(isOff);
    }

    private static boolean isControlPressed() {
        long handle = Minecraft.getInstance().getWindow().handle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }
}
