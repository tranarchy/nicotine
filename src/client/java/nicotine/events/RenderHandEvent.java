package nicotine.events;

import net.minecraft.client.render.RawProjectionMatrix;

public class RenderHandEvent {
    public RawProjectionMatrix worldProjectionMatrix;

    public RenderHandEvent(RawProjectionMatrix worldProjectionMatrix){
        this.worldProjectionMatrix = worldProjectionMatrix;
    }
}
