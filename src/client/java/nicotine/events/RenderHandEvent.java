package nicotine.events;

import net.minecraft.client.renderer.ProjectionMatrixBuffer;

public class RenderHandEvent {
    public ProjectionMatrixBuffer levelProjectionMatrixBuffer;

    public RenderHandEvent(ProjectionMatrixBuffer levelProjectionMatrixBuffer){
        this.levelProjectionMatrixBuffer = levelProjectionMatrixBuffer;
    }
}
