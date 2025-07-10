package nl.devpieter.narratless.mixins;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import nl.devpieter.narratless.Narratless;
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
        this.narratorHotkey = Narratless.getInstance().getNarratorDecoyOption();
    }

//    @Inject(at = @At("TAIL"), method = "accept")
//    private void onAccept(/*Visitor visitor*/ CallbackInfo ci) {
//        // visitor.accept("myOption", this.myOption);
//    }

//    @Inject(method = "accept", at = @At("TAIL"))
//    private void onAccept(Object visitor, CallbackInfo ci) {
//        OptionVisitorBridge bridge = (OptionVisitorBridge) visitor;
//    }
}

