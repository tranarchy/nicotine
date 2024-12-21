package nicotine.events;

import net.minecraft.client.render.OutlineVertexConsumerProvider;

public class RenderEntityOutlineEvent {
    public OutlineVertexConsumerProvider outlineVertexConsumerProvider;
    public RenderEntityOutlineEvent(OutlineVertexConsumerProvider outlineVertexConsumerProvider) {
        this.outlineVertexConsumerProvider = outlineVertexConsumerProvider;
    }
}
