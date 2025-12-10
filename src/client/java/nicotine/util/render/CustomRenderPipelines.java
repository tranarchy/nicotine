package nicotine.util.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.renderer.RenderPipelines;
public class CustomRenderPipelines {
    public static final RenderPipeline NO_DEPTH_TEST_LINES = RenderPipelines.register(
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.LINES_SNIPPET}).withLocation("pipeline/lines").withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
    );

    public static final RenderPipeline NO_DEPTH_TEST_QUADS = RenderPipelines.register(
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET}).withLocation("pipeline/debug_quads").withCull(false).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
    );
}
