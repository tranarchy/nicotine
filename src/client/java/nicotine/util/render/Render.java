package nicotine.util.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import nicotine.util.ColorUtil;
import nicotine.util.math.Boxf;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static nicotine.util.Common.mc;

public class Render {

    private static boolean rendering = false;
    
    public static void toggleRender() {
        rendering = !rendering;

        if (rendering) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);

            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

            GL11.glEnable(GL13.GL_MULTISAMPLE);
        }
        else {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_CULL_FACE);

            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL13.GL_MULTISAMPLE);
        }
    }


    public static void drawTracer(Camera camera, MatrixStack matrixStack, Vec3d targetPos, int color) {
        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
            return;

        toggleRender();

        Vec3d view = camera.getPos();
        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);
        MatrixStack.Entry entry = matrixStack.peek();
        
        Vec3d crosshairPos = mc.crosshairTarget.getPos();

        float dirX = (float) targetPos.x - (float) crosshairPos.x;
        float dirY = (float) targetPos.y - (float) crosshairPos.y;
        float dirZ = (float) targetPos.z - (float) crosshairPos.z;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float normX = dirX / length;
        float normY = dirY / length;
        float normZ = dirZ / length;

        bufferBuilder.vertex(entry, (float) crosshairPos.x, (float) crosshairPos.y, (float) crosshairPos.z).color(color).normal(entry, normX, normY, normZ);
        bufferBuilder.vertex(entry, (float) targetPos.x, (float) targetPos.y, (float) targetPos.z).color(color).normal(entry, normX, normY, normZ);

        RenderLayer.getLines().draw(bufferBuilder.end());

        matrixStack.pop();

        toggleRender();
    }

    public static void drawBox(Camera camera, MatrixStack matrixStack, Boxf box, int color) {
        toggleRender();

        Vec3d view = camera.getPos();
        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);
        MatrixStack.Entry entry = matrixStack.peek();

        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color).normal(entry, -1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color).normal(entry, -1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, -1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, -1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.minX, box.minY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, -1.0F);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, -1.0F);
        bufferBuilder.vertex(entry, box.minX, box.maxY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilder.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);

        RenderLayer.getLines().draw(bufferBuilder.end());

        matrixStack.pop();

        toggleRender();
    }

    public static void drawFilledBox(Camera camera, MatrixStack matrixStack, Boxf box, int color) {
        drawFilledBox(camera, matrixStack, box, color, false);
    }

    public static void drawFilledBox(Camera camera, MatrixStack matrixStack, Boxf box, int color, boolean fade) {
        drawBox(camera, matrixStack, box, color);

        toggleRender();

        Vec3d view = camera.getPos();
        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        MatrixStack.Entry entry = matrixStack.peek();

        color = ColorUtil.changeAlpha(color, fade ? ColorUtil.getDynamicFadeVal() : 0x32);

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

        RenderLayer.getDebugQuads().draw(bufferBuilder.end());

        matrixStack.pop();

        toggleRender();
    }

    public static void drawWireframeBox(Camera camera, MatrixStack matrixStack, Boxf box, int color) {
        drawBox(camera, matrixStack, box, color);

        toggleRender();

        Vec3d view = camera.getPos();
        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        MatrixStack.Entry entry = matrixStack.peek();

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

        RenderLayer.getDebugLineStrip(1d).draw(bufferBuilder.end());

        matrixStack.pop();

        toggleRender();
    }

    public static void drawText(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, Camera camera, Vec3d position, String text, int color, float scale) {
        TextRenderer textRenderer = mc.textRenderer;

        double d = camera.getPos().x;
        double e = camera.getPos().y;
        double f = camera.getPos().z;

        matrix.push();
        matrix.translate((float)(position.x - d), (float)(position.y - e) + 0.50F, (float)(position.z - f));
        matrix.multiply(camera.getRotation());
        float size = (0.025F * scale) + (float)position.distanceTo(mc.player.getPos()) / 1000;
        matrix.scale(size, -size, size);
        float x = (float) textRenderer.getWidth(text) / 2.0F;

        textRenderer.draw(text, -x, 0.0F, color, true, matrix.peek().getPositionMatrix(), vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH,  0x50000000, 0);
        matrix.pop();
    }
}
