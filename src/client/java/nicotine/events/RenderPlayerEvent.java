package nicotine.events;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

public class RenderPlayerEvent {
    public PlayerEntityRenderState playerEntityRenderState;

    public RenderPlayerEvent(PlayerEntityRenderState playerEntityRenderState) {
        this.playerEntityRenderState = playerEntityRenderState;
    }
}
