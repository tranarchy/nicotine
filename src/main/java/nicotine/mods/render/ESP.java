package nicotine.mods.render;

import nicotine.util.Common;
import nicotine.util.Render;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.util.Module;

public class ESP {

    public static void init() {
        Module.Mod esp = new Module.Mod();
        esp.name = "ESP";
        Module.modList.get("Render").add(esp);

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (!esp.enabled)
                return;

            Vec3d view = context.camera().getPos();
            MatrixStack matrix = context.matrixStack();
            ShaderProgram shaderProgram = context.gameRenderer().getPositionColorProgram();

            matrix.push();
            matrix.translate(-view.x, -view.y, -view.z);

            VertexBuffer vertexBuffer = new VertexBuffer();
            for (Entity entity : Common.mc.world.getEntities()) {

                if (entity instanceof PlayerEntity && entity != Common.mc.player) {
                    Box box = entity.getBoundingBox();
                    Render.renderBox(vertexBuffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, Render.getEntityColor(entity.getType()));
                    vertexBuffer.draw(matrix.peek().getPositionMatrix(), context.projectionMatrix(), shaderProgram);
                    vertexBuffer.unbind();
                }
            }

            vertexBuffer.close();
            matrix.pop();
        });

    }
}
