package nicotine.events;


import net.minecraft.client.player.AbstractClientPlayer;

public class TotemPopEvent {
    public AbstractClientPlayer player;

    public TotemPopEvent(AbstractClientPlayer player) {
        this.player = player;
    }
}
