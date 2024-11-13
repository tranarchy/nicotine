package nicotine.util;

import static nicotine.util.Common.*;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Render {

    public static void toggleRender(boolean rendering) {
        if (rendering) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        }
        else {
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
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



}
