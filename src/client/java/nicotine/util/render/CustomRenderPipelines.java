package nicotine.util.render;

import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
public class CustomRenderPipelines {
    public static final RenderPipeline NO_DEPTH_TEST_LINES = RenderPipelines.register(
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.LINES_SNIPPET}).withLocation("pipeline/lines").withColorTargetState(ColorTargetState.DEFAULT).withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false)).build()
    );

    public static final RenderPipeline NO_DEPTH_TEST_QUADS = RenderPipelines.register(
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET}).withLocation("pipeline/debug_quads").withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false)).withCull(false).build()
    );
}
