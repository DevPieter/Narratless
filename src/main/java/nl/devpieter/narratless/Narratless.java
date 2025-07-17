package nl.devpieter.narratless;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.option.SimpleOption;
import nl.devpieter.narratless.statics.KeyBindings;
import nl.devpieter.narratless.statics.Options;
import nl.devpieter.narratless.statics.Settings;
import org.jetbrains.annotations.NotNull;
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
        Options.init();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Options.NARRATOR_DECOY_OPTION.setValue(false);
            MinecraftClient.getInstance().options.write();

            Options.NARRATOR_KEY_ENABLED_OPTION.setValue(Settings.NARRATOR_KEY_ENABLED.getValue());
            Options.NARRATOR_REQUIRES_MODIFIER_OPTION.setValue(Settings.NARRATOR_REQUIRES_MODIFIER.getValue());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KeyBindings.DISABLE_NARRATOR_KEY.wasPressed()) this.tryDisableNarrator(client);
            if (KeyBindings.CYCLE_NARRATOR_KEY.wasPressed()) this.tryCycleNarrator(client);
        });
    }

    public static Narratless getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return this.logger;
    }

    private void tryDisableNarrator(MinecraftClient client) {
        if (Options.NARRATOR_REQUIRES_MODIFIER_OPTION.getValue() && !Screen.hasControlDown()) return;
        SimpleOption<NarratorMode> narratorOption = client.options.getNarrator();

        narratorOption.setValue(NarratorMode.OFF);
        client.options.write();

        this.refreshNarrator(client);
    }

    private void tryCycleNarrator(MinecraftClient client) {
        if (Options.NARRATOR_KEY_ENABLED_OPTION.getValue() == false) return;
        if (Options.NARRATOR_REQUIRES_MODIFIER_OPTION.getValue() && !Screen.hasControlDown()) return;

        SimpleOption<NarratorMode> narratorOption = client.options.getNarrator();
        narratorOption.setValue(NarratorMode.byId(narratorOption.getValue().getId() + 1));
        client.options.write();

        this.refreshNarrator(client);
    }

    private void refreshNarrator(@NotNull MinecraftClient client) {
        SimpleOption<NarratorMode> narratorOption = client.options.getNarrator();
        boolean isOff = narratorOption.getValue() == NarratorMode.OFF;

        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen != null) screen.refreshNarrator(isOff);
    }
}
