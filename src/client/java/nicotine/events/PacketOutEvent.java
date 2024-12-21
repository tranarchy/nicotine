package nicotine.events;

import net.minecraft.network.packet.Packet;

public class PacketOutEvent {
    public Packet<?> packet;

    public PacketOutEvent(Packet<?> packet) {
        this.packet = packet;
    }
}
