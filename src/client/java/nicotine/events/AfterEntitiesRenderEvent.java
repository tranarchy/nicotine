package nicotine.events;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class AfterEntitiesRenderEvent {
    public Camera camera;
    public MatrixStack matrixStack;
    public VertexConsumerProvider vertexConsumerProvider;

    public AfterEntitiesRenderEvent(Camera camera, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
        this.camera = camera;
        this.matrixStack = matrixStack;
        this.vertexConsumerProvider = vertexConsumerProvider;
    }
}
