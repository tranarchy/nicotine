package nicotine.mods.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.util.Common;
import nicotine.util.Module;
import nicotine.util.Render;

import java.util.ArrayList;
import java.util.List;

public class StorageESP {

    private static List<BlockEntity> blockEntities = new ArrayList<>();

    public static void init() {
        Module.Mod storageESP = new Module.Mod();
        storageESP.name = "StorageESP";
        Module.modList.get("Render").add(storageESP);

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (!storageESP.enabled)
                return;

            Vec3d view = context.camera().getPos();
            MatrixStack matrix = context.matrixStack();
            ShaderProgram shaderProgram = context.gameRenderer().getPositionColorProgram();

            matrix.push();
            matrix.translate(-view.x, -view.y, -view.z);

            VertexBuffer vertexBuffer = new VertexBuffer();

            for (Entity e : Common.mc.world.getEntities()) {
                if (Render.espEntityList.contains(e.getType())) {
                    Box box = e.getBoundingBox();
                    Render.renderBox(vertexBuffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, Render.getEntityColor(e.getType()));
                    vertexBuffer.draw(matrix.peek().getPositionMatrix(), context.projectionMatrix(), shaderProgram);
                    vertexBuffer.unbind();
                }
            }

            for (BlockEntity blockEntity : blockEntities) {
                    if (Render.espBlockEntityList.contains(blockEntity.getType())) {
                        Render.renderBox(vertexBuffer, blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ(), blockEntity.getPos().getX() + 1, blockEntity.getPos().getY() + 1, blockEntity.getPos().getZ() + 1, Render.getBlockEntityColor(blockEntity.getType()));
                        vertexBuffer.draw(matrix.peek().getPositionMatrix(), context.projectionMatrix(), shaderProgram);
                        vertexBuffer.unbind();

                    }
            }

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
