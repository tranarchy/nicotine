package nicotine.util.render;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.*;

public class CustomRenderType {
    public static final RenderType LINES = RenderType.create("no_depth_test_lines", RenderSetup.builder(CustomRenderPipelines.NO_DEPTH_TEST_LINES).setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).createRenderSetup());

    public static final RenderType QUADS = RenderType.create("no_depth_test_quads", RenderSetup.builder(CustomRenderPipelines.NO_DEPTH_TEST_QUADS).sortOnUpload().createRenderSetup());
}
