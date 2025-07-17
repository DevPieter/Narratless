package nl.devpieter.narratless.mixins;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import nl.devpieter.narratless.Narratless;
import nl.devpieter.narratless.statics.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Final
    @Mutable
    @Shadow
    private SimpleOption<Boolean> narratorHotkey;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(CallbackInfo ci) {
        this.narratorHotkey = Options.NARRATOR_DECOY_OPTION;
    }
}

