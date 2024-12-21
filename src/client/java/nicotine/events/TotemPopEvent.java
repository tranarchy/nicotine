package nicotine.events;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public class TotemPopEvent {
    public AbstractClientPlayerEntity player;

    public TotemPopEvent(AbstractClientPlayerEntity player) {
        this.player = player;
    }
}
