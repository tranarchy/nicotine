package nicotine.util;

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
import nicotine.util.math.Boxf;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static nicotine.util.Common.mc;

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


    public static void drawTracer(MatrixStack.Entry entry, Vec3d targetPos, int color) {
        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        
        Vec3d crosshairPos = mc.crosshairTarget.getPos();
        
        bufferBuilder.vertex(entry, (float)crosshairPos.x, (float)crosshairPos.y, (float)crosshairPos.z).color(color);
        bufferBuilder.vertex(entry, (float)targetPos.x, (float)targetPos.y, (float) targetPos.z).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawBox(MatrixStack.Entry entry, Boxf box, int color) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);

        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color);

        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);

        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);

        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);

        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

    }

    public static void drawFilledBox(MatrixStack.Entry entry, Boxf box, int color) {
        drawFilledBox(entry, box, color, false);
    }

    public static void drawFilledBox(MatrixStack.Entry entry, Boxf box, int color, boolean fade) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        
        color = color &0x00FFFFFF;
        int alphaValue = fade ? Colors.fadeVal : 0x64;
        color = (alphaValue << 24) | color;
        
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);

        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color);

        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);

        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);

        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color);

        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        drawBox(entry, box, color);
    }

    public static void drawWireframeBox(MatrixStack.Entry entry, Boxf box, int color) {
        drawBox(entry, box, color);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        //a >
        bufferBuilder.vertex(entry, box.minX,box.minY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX,box.minY,box.maxZ).color(color);//c
        bufferBuilder.vertex(entry, box.minX,box.minY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX,box.maxY,box.minZ).color(color);//f
        bufferBuilder.vertex(entry, box.minX,box.minY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX,box.maxY,box.maxZ).color(color);//h
        //b >
        bufferBuilder.vertex(entry, box.maxX,box.minY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX,box.minY,box.maxZ).color(color);//d
        bufferBuilder.vertex(entry, box.maxX,box.minY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX,box.maxY,box.minZ).color(color);//e
        bufferBuilder.vertex(entry, box.maxX,box.minY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX,box.maxY,box.maxZ).color(color);//g
        //c >
        bufferBuilder.vertex(entry, box.maxX,box.minY,box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX,box.maxY,box.minZ).color(color);//f
        bufferBuilder.vertex(entry, box.maxX,box.minY,box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX,box.maxY,box.maxZ).color(color);//h
        //d >
        bufferBuilder.vertex(entry, box.minX,box.minY,box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.maxX,box.maxY,box.maxZ).color(color);//g
        bufferBuilder.vertex(entry, box.minX,box.minY,box.maxZ).color(color);
        bufferBuilder.vertex(entry, box.minX,box.maxY,box.minZ).color(color);//e
        //e > g
        bufferBuilder.vertex(entry, box.minX,box.maxY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.maxX,box.maxY,box.maxZ).color(color);
        //f > h
        bufferBuilder.vertex(entry, box.maxX,box.maxY,box.minZ).color(color);
        bufferBuilder.vertex(entry, box.minX,box.maxY,box.maxZ).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void draw2D(MatrixStack.Entry entry, Boxf box, int color) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        float rotation = mc.player.getMovementDirection().asRotation();

        if (rotation == 180.0 || rotation == 0.0) {
            float centerZ = box.minZ + ((box.maxZ - box.minZ) / 2);

            bufferBuilder.vertex(entry, box.minX, box.maxY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.maxX, box.maxY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.minX, box.minY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.maxX, box.minY, centerZ).color(color);

            bufferBuilder.vertex(entry, box.minX, box.minY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.minX, box.maxY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.maxX, box.minY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.maxX, box.maxY, centerZ).color(color);
        } else {
            float centerX = box.minX + ((box.maxX - box.minX) / 2);

            bufferBuilder.vertex(entry, centerX, box.maxY, box.minZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.maxY, box.maxZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.minY, box.minZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.minY, box.maxZ).color(color);

            bufferBuilder.vertex(entry, centerX, box.minY, box.minZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.maxY, box.minZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.minY, box.maxZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.maxY, box.maxZ).color(color);
        }


        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawFilled2D(MatrixStack.Entry entry, Boxf box, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        color = color &0x00FFFFFF;
        color = (0x64 << 24) | color;

        float rotation = mc.player.getMovementDirection().asRotation();

        if (rotation == 180.0 || rotation == 0.0) {
            float centerZ = box.minZ + ((box.maxZ - box.minZ) / 2);

            bufferBuilder.vertex(entry, box.maxX, box.minY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.minX, box.minY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.minX, box.maxY, centerZ).color(color);
            bufferBuilder.vertex(entry, box.maxX, box.maxY, centerZ).color(color);
        } else {
            float centerX = box.minX + ((box.maxX - box.minX) / 2);

            bufferBuilder.vertex(entry, centerX, box.maxY, box.maxZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.maxY, box.minZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.minY, box.minZ).color(color);
            bufferBuilder.vertex(entry, centerX, box.minY, box.maxZ).color(color);
        }

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
