package nicotine.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import nicotine.events.ConnectEvent;
import nicotine.mod.mods.render.ActiveSpawner;
import nicotine.mod.mods.render.Peek;
import nicotine.util.EventBus;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nicotine.util.Common.*;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/client/gui/screens/ConnectScreen;connect(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/multiplayer/resolver/ServerAddress;Lnet/minecraft/client/multiplayer/ServerData;Lnet/minecraft/client/multiplayer/TransferState;)V")
    private void connect(final Minecraft minecraft, final ServerAddress serverAddress, final ServerData serverData, final @Nullable TransferState transferState, CallbackInfo callbackInfo) {
        EventBus.post(new ConnectEvent());

        Peek.echestWasOpened = false;
        Peek.enderChestItems.clear();

        loadedChunks.clear();
        ActiveSpawner.activeSpawners.clear();

        currentServer = serverData;
    }
}
