package nicotine.mods.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.util.Common;
import nicotine.util.Module;
import nicotine.util.Render;

import java.util.ArrayList;
import java.util.List;

public class StorageTracer {

    private static List<BlockEntity> blockEntities = new ArrayList<>();

    public static void init() {
        Module.Mod storageTracer = new Module.Mod();
        storageTracer.name = "StorageTracer";
        Module.modList.get("Render").add(storageTracer);

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (!storageTracer.enabled || Common.mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
                return;

            Vec3d view = context.camera().getPos();
            MatrixStack matrix = context.matrixStack();
            ShaderProgram shaderProgram = context.gameRenderer().getPositionColorProgram();

            matrix.push();
            matrix.translate(-view.x, -view.y, -view.z);

            VertexBuffer vertexBuffer = new VertexBuffer();

            Vec3d crosshairPos = Common.mc.crosshairTarget.getPos();

            for (Entity e : Common.mc.world.getEntities()) {
                if (Render.espEntityList.contains(e.getType())) {
                    Box box = e.getBoundingBox();
                    Render.renderTracer(vertexBuffer, box.minX, box.minY, box.minZ, crosshairPos.x, crosshairPos.y, crosshairPos.z, Render.getEntityColor(e.getType()));
                    vertexBuffer.draw(matrix.peek().getPositionMatrix(), context.projectionMatrix(), shaderProgram);
                    vertexBuffer.unbind();
                }
            }

            for (BlockEntity blockEntity : blockEntities) {
                if (Render.espBlockEntityList.contains(blockEntity.getType())) {
                        Render.renderTracer(vertexBuffer, blockEntity.getPos().getX(),blockEntity.getPos().getY(), blockEntity.getPos().getZ(), crosshairPos.x, crosshairPos.y, crosshairPos.z, Render.getBlockEntityColor(blockEntity.getType()));
                        vertexBuffer.draw(matrix.peek().getPositionMatrix(), context.projectionMatrix(), shaderProgram);
                        vertexBuffer.unbind();
                }
            }

            vertexBuffer.close();
            matrix.pop();
        });

        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> {
            blockEntities.add(blockEntity);
        });

        ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> {
            blockEntities.remove(blockEntity);
        });

    }
}
