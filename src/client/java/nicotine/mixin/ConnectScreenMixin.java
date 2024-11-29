package nicotine.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import nicotine.events.ConnectEvent;
import nicotine.util.EventBus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nicotine.util.Common.currentServer;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/client/gui/screen/multiplayer/ConnectScreen;connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;Lnet/minecraft/client/network/CookieStorage;)V")
    private void connect(MinecraftClient client, ServerAddress address, ServerInfo info, @Nullable CookieStorage cookieStorage, CallbackInfo callbackInfo) {
        EventBus.post(new ConnectEvent());
        currentServer = info;
    }
}
