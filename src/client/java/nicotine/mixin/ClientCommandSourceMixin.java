package nicotine.mixin;

import net.minecraft.client.network.ClientCommandSource;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ClientCommandSource.class)
public class ClientCommandSourceMixin {
    @Inject(method = "getChatSuggestions", at = @At("RETURN"), cancellable = true)
    public void getChatSuggestions(CallbackInfoReturnable<Collection<String>> infoReturnable) {
        Collection<String> suggestions =  infoReturnable.getReturnValue();

        for (Command command : CommandManager.commands) {
            suggestions.add(String.format("%s%s", CommandManager.prefix, command.name));
        }

        infoReturnable.setReturnValue(suggestions);
    }
}
