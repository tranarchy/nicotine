package nicotine.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import nicotine.events.PacketInEvent;
import nicotine.events.PacketOutEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(at = @At("HEAD"), method = "channelRead0", cancellable = true)
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        boolean result = EventBus.post(new PacketInEvent(packet));

        if (!result) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "send", cancellable = true)
    public void send(Packet<?> packet, CallbackInfo info) {
        boolean result = EventBus.post(new PacketOutEvent(packet));

        if (!result) {
            info.cancel();
        }
    }
}
