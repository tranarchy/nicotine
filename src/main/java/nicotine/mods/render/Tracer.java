package nicotine.mods.render;

import nicotine.util.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.util.Common;
import nicotine.util.Render;

public class Tracer {

    public static void init() {
        Module.Mod tracer = new Module.Mod();
        tracer.name = "Tracer";
        Module.modList.get("Render").add(tracer);

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (!tracer.enabled || Common.mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
                return;

            Vec3d view = context.camera().getPos();
            MatrixStack matrix = context.matrixStack();
            ShaderProgram shaderProgram = context.gameRenderer().getPositionColorProgram();

            matrix.push();
            matrix.translate(-view.x, -view.y, -view.z);

            VertexBuffer vertexBuffer = new VertexBuffer();

            Vec3d crosshairPos = Common.mc.crosshairTarget.getPos();

            for (Entity e : Common.mc.world.getEntities()) {
                if (e instanceof PlayerEntity && e != Common.mc.player) {
                    Box box = e.getBoundingBox();
                    Render.renderTracer(vertexBuffer, box.minX, box.minY, box.minZ, crosshairPos.x, crosshairPos.y, crosshairPos.z, Render.getEntityColor(e.getType()));
                    vertexBuffer.draw(matrix.peek().getPositionMatrix(), context.projectionMatrix(), shaderProgram);
                    vertexBuffer.unbind();

                }
            }


            vertexBuffer.close();
            matrix.pop();
        });
    }
}
