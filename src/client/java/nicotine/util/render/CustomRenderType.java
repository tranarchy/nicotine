package nicotine.util.render;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.*;

public class CustomRenderType {
    public static final RenderType LINES = RenderType.create("no_depth_test_lines", RenderSetup.builder(CustomRenderPipelines.NO_DEPTH_TEST_LINES).setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).createRenderSetup());

    public static final RenderType QUADS = RenderType.create("no_depth_test_quads", RenderSetup.builder(CustomRenderPipelines.NO_DEPTH_TEST_QUADS).setOitPipelines(RenderPipelines.OIT_DEBUG_QUADS).sortOnUpload().createRenderSetup());
}
