package nicotine.events;

import net.minecraft.client.renderer.PerspectiveProjectionMatrixBuffer;

public class RenderHandEvent {
    public PerspectiveProjectionMatrixBuffer levelProjectionMatrixBuffer;

    public RenderHandEvent(PerspectiveProjectionMatrixBuffer levelProjectionMatrixBuffer){
        this.levelProjectionMatrixBuffer = levelProjectionMatrixBuffer;
    }
}
