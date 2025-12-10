package nicotine.events;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public class RenderPlayerEvent {
    public AvatarRenderState avatarRenderState;

    public RenderPlayerEvent(AvatarRenderState avatarRenderState) {
        this.avatarRenderState = avatarRenderState;
    }
}
