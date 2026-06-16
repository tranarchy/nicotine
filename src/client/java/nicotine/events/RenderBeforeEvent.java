package nicotine.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.SubmitNodeStorage;

public class RenderBeforeEvent {
    public final Camera camera;
    public final PoseStack matrixStack;
    public final SubmitNodeStorage submitNodeStorage;

    public RenderBeforeEvent(Camera camera, PoseStack matrixStack, SubmitNodeStorage submitNodeStorage) {
        this.camera = camera;
        this.matrixStack = matrixStack;
        this.submitNodeStorage = submitNodeStorage;
    }
}
