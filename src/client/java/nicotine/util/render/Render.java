package nicotine.util.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.math.Boxf;

import static nicotine.util.Common.mc;

public class Render {

    private static Tessellator tessellator;
    private static BufferBuilder bufferBuilderLines;
    private static BufferBuilder bufferBuilderQuads;

    public static void init() {
        Tessellator tessellatorLines = new Tessellator();
        Tessellator tessellatorQuads = new Tessellator();
        bufferBuilderLines = tessellatorLines.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);
        bufferBuilderQuads = tessellatorQuads.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        EventBus.register(RenderEvent.class, event -> {
            Vec3d view = event.camera.getPos();
            event.matrixStack.push();
            event.matrixStack.translate(-view.x, -view.y, -view.z);

            BuiltBuffer builtBufferLines = bufferBuilderLines.endNullable();
            BuiltBuffer builtBufferQuads = bufferBuilderQuads.endNullable();

            if (builtBufferLines != null)
                CustomRenderLayer.LINES.draw(builtBufferLines);

            if (builtBufferQuads != null)
                CustomRenderLayer.QUADS.draw(builtBufferQuads);

            tessellatorLines.clear();
            tessellatorQuads.clear();

            bufferBuilderLines = tessellatorLines.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);
            bufferBuilderQuads = tessellatorQuads.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);


            event.matrixStack.pop();

            return true;
        });
    }

    public static void drawTracer(Camera camera, MatrixStack matrixStack, Vec3d targetPos, int color) {
        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
            return;

        Vec3d view = camera.getPos();
        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);

        MatrixStack.Entry entry = matrixStack.peek();
        
        Vec3d crosshairPos = mc.crosshairTarget.getPos();

        float dirX = (float) targetPos.x - (float) crosshairPos.x;
        float dirY = (float) targetPos.y - (float) crosshairPos.y;
        float dirZ = (float) targetPos.z - (float) crosshairPos.z;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float normX = dirX / length;
        float normY = dirY / length;
        float normZ = dirZ / length;

        bufferBuilderLines.vertex(entry, (float) crosshairPos.x, (float) crosshairPos.y, (float) crosshairPos.z).color(color).normal(entry, normX, normY, normZ);
        bufferBuilderLines.vertex(entry, (float) targetPos.x, (float) targetPos.y, (float) targetPos.z).color(color).normal(entry, normX, normY, normZ);

        matrixStack.pop();
    }

    public static void drawBox(Camera camera, MatrixStack matrixStack, Boxf box, int color) {
        Vec3d view = camera.getPos();
        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);
        MatrixStack.Entry entry = matrixStack.peek();

        bufferBuilderLines.vertex(entry, box.minX, box.minY, box.minZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.minY, box.minZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);

        bufferBuilderLines.vertex(entry, box.minX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);

        bufferBuilderLines.vertex(entry, box.maxX, box.maxY, box.minZ).color(color).normal(entry, -1.0F, 0.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.maxY, box.minZ).color(color).normal(entry, -1.0F, 0.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);

        bufferBuilderLines.vertex(entry, box.minX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, -1.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, -1.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.minY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.minY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);

        bufferBuilderLines.vertex(entry, box.maxX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, -1.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.minY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, -1.0F);
        bufferBuilderLines.vertex(entry, box.minX, box.maxY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color).normal(entry, 1.0F, 0.0F, 0.0F);

        bufferBuilderLines.vertex(entry, box.maxX, box.minY, box.maxZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, 1.0F, 0.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.maxY, box.minZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);
        bufferBuilderLines.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color).normal(entry, 0.0F, 0.0F, 1.0F);

        matrixStack.pop();
    }

    public static void drawFilledBox(Camera camera, MatrixStack matrixStack, Boxf box, int color) {
        drawFilledBox(camera, matrixStack, box, color, false);
    }

    public static void drawFilledBox(Camera camera, MatrixStack matrixStack, Boxf box, int color, boolean fade) {
        drawBox(camera, matrixStack, box, color);

        Vec3d view = camera.getPos();
        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);
        MatrixStack.Entry entry = matrixStack.peek();

        color = ColorUtil.changeAlpha(color, fade ? ColorUtil.getDynamicFadeVal() : 0x32);

        bufferBuilderQuads.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);

        bufferBuilderQuads.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.minY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.maxX, box.minY, box.minZ).color(color);

        bufferBuilderQuads.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.minY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);

        bufferBuilderQuads.vertex(entry, box.maxX, box.minY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);

        bufferBuilderQuads.vertex(entry, box.minX, box.maxY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.maxY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.minY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.minX, box.minY, box.maxZ).color(color);

        bufferBuilderQuads.vertex(entry, box.maxX, box.maxY, box.minZ).color(color);
        bufferBuilderQuads.vertex(entry, box.maxX, box.maxY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.maxX, box.minY, box.maxZ).color(color);
        bufferBuilderQuads.vertex(entry, box.maxX, box.minY, box.minZ).color(color);

        matrixStack.pop();
    }

    public static void drawText(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, Camera camera, Vec3d position, String text, int color, float scale) {
        TextRenderer textRenderer = mc.textRenderer;

        double d = camera.getPos().x;
        double e = camera.getPos().y;
        double f = camera.getPos().z;

        matrix.push();
        matrix.translate((float)(position.x - d), (float)(position.y - e) + 0.50F, (float)(position.z - f));
        matrix.multiply(camera.getRotation());
        float size = (0.025F * scale) + (float)position.distanceTo(mc.player.getEntityPos()) / 1000;
        matrix.scale(size, -size, size);
        float x = (float) textRenderer.getWidth(text) / 2.0F;

        textRenderer.draw(text, -x, 0.0F, color, true, matrix.peek().getPositionMatrix(), vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH,  0x50000000, 0);
        matrix.pop();
    }
}
