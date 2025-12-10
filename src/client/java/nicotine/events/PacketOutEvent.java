package nicotine.events;


import net.minecraft.network.protocol.Packet;

public class PacketOutEvent {
    public Packet<?> packet;

    public PacketOutEvent(Packet<?> packet) {
        this.packet = packet;
    }
}
