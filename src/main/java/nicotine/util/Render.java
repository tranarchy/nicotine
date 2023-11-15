package nicotine.util;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Render {

    private static class Colors {
        static float[] GOLD = new float[]{ 1.0F, 0.666F, 0.0F, 1.0F };
        static float[] GREY = new float[]{0.666F, 0.666F, 0.666F, 1.0F};
        static float[] WHITE = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
        static float[] PURPLE = new float[]{0.666F, 0.0F, 0.666F, 1.0F};
        static float[] PINK = new float[]{1.0F, 0.333F, 1.0F, 1.0F};
        static float[] RED = new float[]{1.0F, 0.333F, 0.333F, 1.0F};

    }

    public static final List<EntityType> espEntityList = new ArrayList<>() {
        {
            add(EntityType.ITEM_FRAME);
            add(EntityType.GLOW_ITEM_FRAME);
            add(EntityType.CHEST_BOAT);
            add(EntityType.CHEST_MINECART);
            add(EntityType.HOPPER_MINECART);
            add(EntityType.FURNACE_MINECART);
        }
    };

    public static final List<BlockEntityType> espBlockEntityList = new ArrayList<>() {
        {
            add(BlockEntityType.CHEST);
            add(BlockEntityType.TRAPPED_CHEST);
            add(BlockEntityType.ENDER_CHEST);
            add(BlockEntityType.BARREL);
            add(BlockEntityType.FURNACE);
            add(BlockEntityType.BLAST_FURNACE);
            add(BlockEntityType.SMOKER);
            add(BlockEntityType.HOPPER);
            add(BlockEntityType.DROPPER);
            add(BlockEntityType.DISPENSER);
            add(BlockEntityType.SHULKER_BOX);
        }
    };

    public static float[] getEntityColor(EntityType e) {
        if (e == EntityType.PLAYER)
            return Colors.RED;
        else if (e == EntityType.CHEST_BOAT || e == EntityType.CHEST_MINECART || e == EntityType.ITEM_FRAME || e == EntityType.GLOW_ITEM_FRAME)
            return Colors.GOLD;
        else if (e == EntityType.FURNACE_MINECART)
            return Colors.GREY;
        else if (e == EntityType.HOPPER_MINECART)
            return Colors.WHITE;

        return null;
    }

    public static float[] getBlockEntityColor(BlockEntityType e) {
        if (e == BlockEntityType.CHEST || e == BlockEntityType.TRAPPED_CHEST || e == BlockEntityType.BARREL)
            return Colors.GOLD;
        else if (e == BlockEntityType.FURNACE || e == BlockEntityType.BLAST_FURNACE || e == BlockEntityType.SMOKER)
            return Colors.GREY;
        else if (e == BlockEntityType.DISPENSER || e == BlockEntityType.HOPPER || e == BlockEntityType.DROPPER)
            return Colors.WHITE;
        else if(e == BlockEntityType.SHULKER_BOX)
            return Colors.PINK;
        else if(e == BlockEntityType.ENDER_CHEST)
            return Colors.PURPLE;

        return null;
    }

    public static void renderFilledBox(VertexBuffer vertexBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float[] color)
    {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        // top
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();

        // bottom
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();

        // north
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();

        // south
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();

        // west
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();

        // east
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        
        vertexBuffer.bind();
        vertexBuffer.upload(bufferBuilder.end());
    }

    public static void renderBox(VertexBuffer vertexBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float[] color)
    {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();

        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();

        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();

        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();

        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();

        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]).next();

        vertexBuffer.bind();
        vertexBuffer.upload(bufferBuilder.end());
    }

    public static void renderTracer(VertexBuffer vertexBuffer, double targetX, double targetY, double targetZ, double startX, double startY, double startZ, float[] color)
    {
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        bufferBuilder.color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(targetX, targetY, targetZ).color(color[0], color[1], color[2], color[3]).next();
        bufferBuilder.vertex(startX, startY, startZ).color(color[0], color[1], color[2], color[3]).next();

        vertexBuffer.bind();
        vertexBuffer.upload(bufferBuilder.end());
    }
}
