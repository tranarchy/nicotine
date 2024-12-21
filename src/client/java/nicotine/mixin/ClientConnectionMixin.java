package nicotine.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import nicotine.events.PacketInEvent;
import nicotine.events.PacketOutEvent;
import nicotine.util.EventBus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/network/ClientConnection;channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        boolean result = EventBus.post(new PacketInEvent(packet));

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V", cancellable = true)
    public void send(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush, CallbackInfo info) {
        boolean result = EventBus.post(new PacketOutEvent(packet));

        if (!result) {
            info.cancel();
        }
    }
}
