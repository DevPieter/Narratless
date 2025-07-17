package nl.devpieter.narratless.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import nl.devpieter.narratless.Narratless;
import nl.devpieter.narratless.statics.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {

    @ModifyReturnValue(at = @At("RETURN"), method = "getOptions(Lnet/minecraft/client/option/GameOptions;)[Lnet/minecraft/client/option/SimpleOption;")
    private static SimpleOption<?>[] replaceOption(SimpleOption<?>[] original, GameOptions gameOptions) {
        List<SimpleOption<?>> modified = new ArrayList<>(List.of(original));

        SimpleOption<Boolean> actualOption = Options.NARRATOR_KEY_ENABLED_OPTION;
        SimpleOption<Boolean> requiresControlOption = Options.NARRATOR_REQUIRES_MODIFIER_OPTION;
        SimpleOption<Boolean> decoyOption = Options.NARRATOR_DECOY_OPTION;

        for (int i = 0; i < modified.size(); i++) {
            if (modified.get(i) != decoyOption) continue;
            modified.set(i, actualOption);
            break;
        }

        modified.add(requiresControlOption);
        return modified.toArray(new SimpleOption[0]);
    }
}
