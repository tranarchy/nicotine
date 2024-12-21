package nicotine.events;

import net.minecraft.network.packet.Packet;

public class PacketInEvent {
    public Packet<?> packet;

    public PacketInEvent(Packet<?> packet) {
        this.packet = packet;
    }
}
