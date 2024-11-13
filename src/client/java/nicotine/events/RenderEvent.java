package nicotine.events;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent {
    public final Camera camera;
    public final MatrixStack matrixStack;
    public final VertexConsumerProvider vertexConsumerProvider;

    public RenderEvent(Camera camera, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
        this.camera = camera;
        this.matrixStack = matrixStack;
        this.vertexConsumerProvider = vertexConsumerProvider;
    }
}
