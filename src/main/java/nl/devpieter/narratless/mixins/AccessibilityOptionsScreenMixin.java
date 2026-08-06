package nl.devpieter.narratless.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import nl.devpieter.narratless.statics.NarratlessOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {

    @ModifyReturnValue(at = @At("RETURN"), method = "options")
    private static OptionInstance<?>[] replaceOption(OptionInstance<?>[] original, Options options) {
        List<OptionInstance<?>> modified = new ArrayList<>(List.of(original));

        OptionInstance<Boolean> actualOption = NarratlessOptions.NARRATOR_KEY_ENABLED_OPTION;
        OptionInstance<Boolean> requiresControlOption = NarratlessOptions.NARRATOR_REQUIRES_MODIFIER_OPTION;
        OptionInstance<Boolean> decoyOption = NarratlessOptions.NARRATOR_DECOY_OPTION;

        for (int i = 0; i < modified.size(); i++) {
            if (modified.get(i) != decoyOption) continue;
            modified.set(i, actualOption);
            break;
        }

        modified.add(requiresControlOption);
        return modified.toArray(new OptionInstance[0]);
    }
}
