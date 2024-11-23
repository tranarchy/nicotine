package nicotine.util;

import static nicotine.util.Common.*;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Render {

    public static void toggleRender(boolean rendering) {
        if (rendering) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();

            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

            GL11.glEnable(GL13.GL_MULTISAMPLE);

        }
        else {
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();

            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL13.GL_MULTISAMPLE);
        }
    }


    public static void drawTracer(Vec3d view, Vec3d targetPos, int color) {
        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);


        Vec3d crosshairPos = mc.crosshairTarget.getPos();
        float startX = (float) crosshairPos.x - (float) view.x;
        float startY = (float) crosshairPos.y - (float) view.y;
        float startZ = (float) crosshairPos.z - (float) view.z;
        float endX = (float) targetPos.x - (float) view.x;
        float endY = (float) targetPos.y - (float) view.y;
        float endZ = (float) targetPos.z - (float) view.z;

        bufferBuilder.vertex(startX, startY, startZ).color(color);
        bufferBuilder.vertex(endX, endY, endZ).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawBox(Vec3d view, Box targetPos, int color) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        float maxX = (float) targetPos.maxX - (float) view.x;
        float maxY = (float) targetPos.maxY - (float) view.y;
        float maxZ = (float) targetPos.maxZ - (float) view.z;
        float minX = (float) targetPos.minX - (float) view.x;
        float minY = (float) targetPos.minY - (float) view.y;
        float minZ = (float) targetPos.minZ - (float) view.z;

        bufferBuilder.vertex(maxX, maxY, minZ).color(color);
        bufferBuilder.vertex(minX, maxY, minZ).color(color);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color);
        bufferBuilder.vertex(minX, minY, maxZ).color(color);
        bufferBuilder.vertex(minX, minY, minZ).color(color);
        bufferBuilder.vertex(maxX, minY, minZ).color(color);

        bufferBuilder.vertex(minX, maxY, minZ).color(color);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color);
        bufferBuilder.vertex(maxX, maxY, minZ).color(color);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color);

        bufferBuilder.vertex(minX, minY, minZ).color(color);
        bufferBuilder.vertex(minX, minY, maxZ).color(color);
        bufferBuilder.vertex(maxX, minY, minZ).color(color);
        bufferBuilder.vertex(maxX, minY, maxZ).color(color);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color);
        bufferBuilder.vertex(maxX, minY, minZ).color(color);
        bufferBuilder.vertex(maxX, maxY, minZ).color(color);

        bufferBuilder.vertex(minX, minY, maxZ).color(color);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color);
        bufferBuilder.vertex(minX, minY, minZ).color(color);
        bufferBuilder.vertex(minX, maxY, minZ).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

    }

    public static void drawFilledBox(Vec3d view, Box targetPos, int color, boolean fade) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        color = color &0x00FFFFFF;

        int alphaValue = 0x80;
        if (fade)
            alphaValue = Colors.fadeVal;
        color = (alphaValue << 24) | color;

        float maxX = (float) targetPos.maxX - (float) view.x;
        float maxY = (float) targetPos.maxY - (float) view.y;
        float maxZ = (float) targetPos.maxZ - (float) view.z;
        float minX = (float) targetPos.minX - (float) view.x;
        float minY = (float) targetPos.minY - (float) view.y;
        float minZ = (float) targetPos.minZ - (float) view.z;

        bufferBuilder.vertex(maxX, maxY, minZ).color(color);
        bufferBuilder.vertex(minX, maxY, minZ).color(color);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color);
        bufferBuilder.vertex(minX, minY, maxZ).color(color);
        bufferBuilder.vertex(minX, minY, minZ).color(color);
        bufferBuilder.vertex(maxX, minY, minZ).color(color);

        bufferBuilder.vertex(maxX, maxY, maxZ).color(color);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color);
        bufferBuilder.vertex(minX, minY, maxZ).color(color);
        bufferBuilder.vertex(maxX, minY, maxZ).color(color);

        bufferBuilder.vertex(maxX, minY, minZ).color(color);
        bufferBuilder.vertex(minX, minY, minZ).color(color);
        bufferBuilder.vertex(minX, maxY, minZ).color(color);
        bufferBuilder.vertex(maxX, maxY, minZ).color(color);

        bufferBuilder.vertex(minX, maxY, maxZ).color(color);
        bufferBuilder.vertex(minX, maxY, minZ).color(color);
        bufferBuilder.vertex(minX, minY, minZ).color(color);
        bufferBuilder.vertex(minX, minY, maxZ).color(color);

        bufferBuilder.vertex(maxX, maxY, minZ).color(color);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color);
        bufferBuilder.vertex(maxX, minY, maxZ).color(color);
        bufferBuilder.vertex(maxX, minY, minZ).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawWireframeBox(Vec3d view, Box targetPos, int color) {
        drawBox(view,targetPos,color);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        float maxX = (float) targetPos.maxX - (float) view.x;
        float maxY = (float) targetPos.maxY - (float) view.y;
        float maxZ = (float) targetPos.maxZ - (float) view.z;
        float minX = (float) targetPos.minX - (float) view.x;
        float minY = (float) targetPos.minY - (float) view.y;
        float minZ = (float) targetPos.minZ - (float) view.z;

        //a >
        bufferBuilder.vertex(minX,minY,minZ).color(color);
        bufferBuilder.vertex(maxX,minY,maxZ).color(color);//c
        bufferBuilder.vertex(minX,minY,minZ).color(color);
        bufferBuilder.vertex(maxX,maxY,minZ).color(color);//f
        bufferBuilder.vertex(minX,minY,minZ).color(color);
        bufferBuilder.vertex(minX,maxY,maxZ).color(color);//h
        //b >
        bufferBuilder.vertex(maxX,minY,minZ).color(color);
        bufferBuilder.vertex(minX,minY,maxZ).color(color);//d
        bufferBuilder.vertex(maxX,minY,minZ).color(color);
        bufferBuilder.vertex(minX,maxY,minZ).color(color);//e
        bufferBuilder.vertex(maxX,minY,minZ).color(color);
        bufferBuilder.vertex(maxX,maxY,maxZ).color(color);//g
        //c >
        bufferBuilder.vertex(maxX,minY,maxZ).color(color);
        bufferBuilder.vertex(maxX,maxY,minZ).color(color);//f
        bufferBuilder.vertex(maxX,minY,maxZ).color(color);
        bufferBuilder.vertex(minX,maxY,maxZ).color(color);//h
        //d >
        bufferBuilder.vertex(minX,minY,maxZ).color(color);
        bufferBuilder.vertex(maxX,maxY,maxZ).color(color);//g
        bufferBuilder.vertex(minX,minY,maxZ).color(color);
        bufferBuilder.vertex(minX,maxY,minZ).color(color);//e
        //e > g
        bufferBuilder.vertex(minX,maxY,minZ).color(color);
        bufferBuilder.vertex(maxX,maxY,maxZ).color(color);
        //f > h
        bufferBuilder.vertex(maxX,maxY,minZ).color(color);
        bufferBuilder.vertex(minX,maxY,maxZ).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawText(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, Camera camera, Vec3d position, String text, int color) {
        TextRenderer textRenderer = mc.textRenderer;

        double d = camera.getPos().x;
        double e = camera.getPos().y;
        double f = camera.getPos().z;

        matrix.push();
        matrix.translate((float)(position.x - d), (float)(position.y - e) + 0.50F, (float)(position.z - f));
        matrix.multiply(camera.getRotation());
        float size = 0.025F + (float)mc.player.squaredDistanceTo(position) / 70000.0F;
        matrix.scale(size, -size, size);
        float x = (float) textRenderer.getWidth(text) / 2.0F;

        textRenderer.draw(text, -x, 0.0F, color, true, matrix.peek().getPositionMatrix(), vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH,  0x50000000, 0);
        matrix.pop();
    }

    public static Box getBlockBoundingBox(BlockEntity blockEntity) {
        BlockPos pos = blockEntity.getPos();
        Box offset = blockEntity.getCachedState().getOutlineShape(mc.world, pos).getBoundingBox();
        Box boundingBox = new Box(
                pos.getX() + offset.minX,
                pos.getY() + offset.minY,
                pos.getZ() + offset.minZ,
                pos.getX() + offset.maxX,
                pos.getY() + offset.maxY,
                pos.getZ() + offset.maxZ
        );

        return boundingBox;
    }

    public static Box getBlockBoundingBox(BlockPos pos) {
        VoxelShape voxelShape = mc.world.getBlockState(pos).getOutlineShape(mc.world, pos);
        if (voxelShape.isEmpty())
            return null;

        Box offset = voxelShape.getBoundingBox();
        Box boundingBox = new Box(
                pos.getX() + offset.minX,
                pos.getY() + offset.minY,
                pos.getZ() + offset.minZ,
                pos.getX() + offset.maxX,
                pos.getY() + offset.maxY,
                pos.getZ() + offset.maxZ
        );

        return boundingBox;
    }

    public static void drawItem(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, Camera camera, Vec3d position, ItemStack itemStack) {;
        ItemRenderer itemRenderer = mc.getItemRenderer();

        double d = camera.getPos().x;
        double e = camera.getPos().y;
        double f = camera.getPos().z;

        matrix.push();
        matrix.translate((float)(position.x - d), (float)(position.y - e) + 0.50F, (float)(position.z - f));
        matrix.multiply(camera.getRotation());
        float size = 1.0F + ((float)mc.player.squaredDistanceTo(position) / 70000.0F);
        matrix.scale(size, size, size);

        mc.getEntityRenderDispatcher().getHeldItemRenderer().renderItem(mc.player, itemStack, ModelTransformationMode.FIXED, false, matrix, vertexConsumerProvider, 0);

        //BakedModel bakedModel = itemRenderer.getModel(itemStack.copy(), mc.player, ModelTransformationMode.NONE);
        //itemRenderer.renderItem(itemStack.copy(), ModelTransformationMode.NONE, false, matrix, vertexConsumerProvider, 0, 0, bakedModel);
        matrix.pop();
    }
}
