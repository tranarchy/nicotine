package nicotine.events;

import net.minecraft.client.network.ClientPlayerEntity;

public class IsUsingItemEvent {
    public ClientPlayerEntity entity;

    public IsUsingItemEvent(ClientPlayerEntity entity) {
        this.entity = entity;
    }
}
