package nicotine.mixin;

import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ClientSuggestionProvider.class)
public class ClientSuggestionProviderMixin {
    @Inject(method = "getCustomTabSugggestions", at = @At("RETURN"), cancellable = true)
    public void getCustomTabSugggestions(CallbackInfoReturnable<Collection<String>> infoReturnable) {
        Collection<String> suggestions =  infoReturnable.getReturnValue();

        for (Command command : CommandManager.commands) {
            suggestions.add(String.format("%s%s", CommandManager.prefix, command.name));
        }

        infoReturnable.setReturnValue(suggestions);
    }
}
