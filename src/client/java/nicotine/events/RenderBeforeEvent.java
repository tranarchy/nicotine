package nicotine.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;

public class RenderBeforeEvent {
    public final Camera camera;
    public final PoseStack matrixStack;
    public final MultiBufferSource.BufferSource multiBufferSource;

    public RenderBeforeEvent(Camera camera, PoseStack matrixStack, MultiBufferSource.BufferSource multiBufferSource) {
        this.camera = camera;
        this.matrixStack = matrixStack;
        this.multiBufferSource = multiBufferSource;
    }
}
