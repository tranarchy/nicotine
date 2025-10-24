package nicotine.util.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;

public class CustomRenderPipelines {
    public static final RenderPipeline NO_DEPTH_TEST_LINES = RenderPipelines.register(
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.RENDERTYPE_LINES_SNIPPET}).withLocation("pipeline/lines").withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
    );

    public static final RenderPipeline NO_DEPTH_TEST_QUADS = RenderPipelines.register(
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.POSITION_COLOR_SNIPPET}).withLocation("pipeline/debug_quads").withCull(false).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
    );

    public static final RenderPipeline NO_DEPTH_TEST_RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL = RenderPipelines.register(
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET}).withLocation("pipeline/item_entity_translucent_cull").withVertexShader("core/rendertype_item_entity_translucent_cull").withFragmentShader("core/rendertype_item_entity_translucent_cull").withSampler("Sampler0").withSampler("Sampler2").withBlend(BlendFunction.TRANSLUCENT).withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
    );
}
