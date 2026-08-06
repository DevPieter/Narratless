package nl.devpieter.narratless.mixins;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import nl.devpieter.narratless.statics.NarratlessOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class GameOptionsMixin {

    @Shadow
    @Final
    @Mutable
    private OptionInstance<Boolean> narratorHotkey;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(CallbackInfo ci) {
        this.narratorHotkey = NarratlessOptions.NARRATOR_DECOY_OPTION;
    }
}

