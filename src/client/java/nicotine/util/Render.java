package nicotine.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import nicotine.util.math.Boxf;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static nicotine.util.Common.mc;

public class Render {
    
    public static void toggleRender(MatrixStack matrixStack, Camera camera, boolean rendering) {
        if (rendering) {
            Vec3d view = camera.getPos();
            matrixStack.push();
            matrixStack.translate(-view.x, -view.y, -view.z);

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
            matrixStack.pop();

            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();

            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL13.GL_MULTISAMPLE);
        }
    }


    public static void drawTracer(MatrixStack matrixStack, Vec3d targetPos, int color) {
        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
            return;

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        MatrixStack.Entry entry = matrixStack.peek();
        
        Vec3d crosshairPos = mc.crosshairTarget.getPos();
        
        bufferBuilder.vertex(entry, (float)crosshairPos.x, (float)crosshairPos.y, (float)crosshairPos.z).color(color);
        bufferBuilder.vertex(entry, (float)targetPos.x, (float)targetPos.y, (float) targetPos.z).color(color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawBox(MatrixStack matrixStack, Boxf box, int color) {

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        MatrixStack.Entry entry = matrixStack.peek();

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

    public static void drawFilledBox(MatrixStack matrixStack, Boxf box, int color) {
        drawFilledBox(matrixStack, box, color, false);
    }

    public static void drawFilledBox(MatrixStack matrixStack, Boxf box, int color, boolean fade) {
        drawBox(matrixStack, box, color);

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        MatrixStack.Entry entry = matrixStack.peek();


        color = ColorUtil.changeAlpha(color, fade ? ColorUtil.dynamicFadeVal : 0x32);

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
    }

    public static void drawWireframeBox(MatrixStack matrixStack, Boxf box, int color) {
        drawBox(matrixStack, box, color);

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
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

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
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
