package nicotine.util.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderEvent;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.math.Boxf;

import static nicotine.util.Common.mc;

public class Render {

    private static BufferBuilder bufferBuilderLines;
    private static BufferBuilder bufferBuilderQuads;

    public static void init() {
        Tesselator tessellatorLines = new Tesselator();
        Tesselator tessellatorQuads = new Tesselator();
        bufferBuilderLines = tessellatorLines.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH);
        bufferBuilderQuads = tessellatorQuads.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        EventBus.register(RenderEvent.class, event -> {
            MeshData builtBufferLines = bufferBuilderLines.build();
            MeshData builtBufferQuads = bufferBuilderQuads.build();

            if (builtBufferLines != null)
                CustomRenderType.LINES.draw(builtBufferLines);

            if (builtBufferQuads != null)
                CustomRenderType.QUADS.draw(builtBufferQuads);

            tessellatorLines.clear();
            tessellatorQuads.clear();

            bufferBuilderLines = tessellatorLines.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH);
            bufferBuilderQuads = tessellatorQuads.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

            return true;
        });
    }

    public static void drawTracer(Camera camera, PoseStack matrixStack, Vec3 targetPos, int color) {
        if (mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT)
            return;

        PoseStack.Pose entry = matrixStack.last();

        targetPos = targetPos.add(camera.position().reverse());
        Vec3 crosshairPos = mc.hitResult.getLocation().add(camera.position().reverse());

        float dirX = (float) targetPos.x - (float) crosshairPos.x;
        float dirY = (float) targetPos.y - (float) crosshairPos.y;
        float dirZ = (float) targetPos.z - (float) crosshairPos.z;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float normX = dirX / length;
        float normY = dirY / length;
        float normZ = dirZ / length;

        bufferBuilderLines.addVertex(entry, (float) crosshairPos.x, (float) crosshairPos.y, (float) crosshairPos.z).setColor(color).setNormal(entry, normX, normY, normZ).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, (float) targetPos.x, (float) targetPos.y, (float) targetPos.z).setColor(color).setNormal(entry, normX, normY, normZ).setLineWidth(2.0F);
    }

    public static void drawBox(Camera camera, PoseStack matrixStack, Boxf box, int color) {

        box = box.move(camera.position().reverse());

        PoseStack.Pose entry = matrixStack.last();

        bufferBuilderLines.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(2.0F);

        bufferBuilderLines.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(2.0F);

        bufferBuilderLines.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color).setNormal(entry, -1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color).setNormal(entry, -1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(2.0F);

        bufferBuilderLines.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, -1.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, -1.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(2.0F);

        bufferBuilderLines.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, -1.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, -1.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(2.0F);

        bufferBuilderLines.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
        bufferBuilderLines.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(2.0F);
    }

    public static void drawFilledBox(Camera camera, PoseStack matrixStack, Boxf box, int color) {
        drawFilledBox(camera, matrixStack, box, color, false);
    }

    public static void drawFilledBox(Camera camera, PoseStack matrixStack, Boxf box, int color, boolean fade) {
        drawBox(camera, matrixStack, box, color);

        box = box.move(camera.position().reverse());

        PoseStack.Pose entry = matrixStack.last();

        color = ColorUtil.changeAlpha(color, fade ? ColorUtil.getDynamicFadeVal() : 0x32);

        bufferBuilderQuads.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color);

        bufferBuilderQuads.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color);

        bufferBuilderQuads.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color);

        bufferBuilderQuads.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color);

        bufferBuilderQuads.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color);

        bufferBuilderQuads.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color);
        bufferBuilderQuads.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color);
    }

    public static void drawText(PoseStack matrix, MultiBufferSource multiBufferSource, Camera camera, Vec3 position, String text, int color, float scale) {
        Font textRenderer = mc.font;

        Vec3 cameraPos = camera.position();

        matrix.pushPose();
        matrix.translate((float)(position.x - cameraPos.x), (float)(position.y - cameraPos.y) + 0.50F, (float)(position.z - cameraPos.z));
        matrix.mulPose(camera.rotation());
        float size = (0.025F * scale) + (float)position.distanceTo(mc.player.position()) / 1000;
        matrix.scale(size, -size, size);
        float x = (float) textRenderer.width(text) / 2.0F;

        textRenderer.drawInBatch(Component.literal(text), -x, 0.0F, color, true, matrix.last().pose(), multiBufferSource, Font.DisplayMode.SEE_THROUGH,  0x50000000, 0);
        matrix.popPose();
    }
}
