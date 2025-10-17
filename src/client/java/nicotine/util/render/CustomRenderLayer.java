package nicotine.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import org.joml.Matrix4fStack;

import java.util.OptionalDouble;
import java.util.function.Supplier;

public class CustomRenderLayer {
    public static final RenderLayer.MultiPhase LINES = RenderLayer.of(
            "no_depth_test_lines",
            1536,
            CustomRenderPipelines.NO_DEPTH_TEST_LINES,
            RenderLayer.MultiPhaseParameters.builder().lineWidth(new RenderPhase.LineWidth(OptionalDouble.empty())).layering(RenderLayer.VIEW_OFFSET_Z_LAYERING).target(RenderLayer.ITEM_ENTITY_TARGET).build(false)
    );

    public static final RenderLayer.MultiPhase QUADS = RenderLayer.of(
            "no_depth_test_quads",
            1536,
            false,
            true,
            CustomRenderPipelines.NO_DEPTH_TEST_QUADS,
            RenderLayer.MultiPhaseParameters.builder().build(false)
    );
}
