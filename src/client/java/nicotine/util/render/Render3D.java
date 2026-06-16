package nicotine.util.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import nicotine.mod.mods.general.Render;
import nicotine.util.ColorUtil;
import nicotine.util.math.Boxf;

import java.util.LinkedHashMap;

import static nicotine.util.Common.mc;

public class Render3D {

    public static void drawTracer(SubmitNodeStorage submitNodeStorage, Camera camera, PoseStack matrixStack, Vec3 targetPos, int color) {
        if (mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT)
            return;

        Vec3 adjustedPos = targetPos.add(camera.position().reverse());
        Vec3 crosshairPos = mc.hitResult.getLocation().add(camera.position().reverse());

        float dirX = (float) adjustedPos.x - (float) crosshairPos.x;
        float dirY = (float) adjustedPos.y - (float) crosshairPos.y;
        float dirZ = (float) adjustedPos.z - (float) crosshairPos.z;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float normX = dirX / length;
        float normY = dirY / length;
        float normZ = dirZ / length;

        submitNodeStorage.submitCustomGeometry(
                matrixStack,
                CustomRenderType.LINES,
                (entry, buffer) -> {
                    buffer.addVertex(entry, (float) crosshairPos.x, (float) crosshairPos.y, (float) crosshairPos.z).setColor(color).setNormal(entry, normX, normY, normZ).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, (float) adjustedPos.x, (float) adjustedPos.y, (float) adjustedPos.z).setColor(color).setNormal(entry, normX, normY, normZ).setLineWidth(Render.lineWidth.value);
                }
        );
    }

    public static void drawBox(SubmitNodeStorage submitNodeStorage, Camera camera, PoseStack matrixStack, Boxf boxf, int color) {
        submitNodeStorage.submitCustomGeometry(
                matrixStack,
                CustomRenderType.LINES,
                (entry, buffer) -> {
                    Boxf box = boxf.move(camera.position().reverse());

                    buffer.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(Render.lineWidth.value);

                    buffer.addVertex(entry, box.minX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(Render.lineWidth.value);

                    buffer.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color).setNormal(entry, -1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color).setNormal(entry, -1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(Render.lineWidth.value);

                    buffer.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, -1.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, -1.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);

                    buffer.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, -1.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, -1.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(Render.lineWidth.value);

                    buffer.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(Render.lineWidth.value);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(color).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(Render.lineWidth.value);

                }
        );
    }

    public static void drawFilledBox(SubmitNodeStorage submitNodeStorage, Camera camera, PoseStack matrixStack, Boxf box, int color) {
        drawFilledBox(submitNodeStorage, camera, matrixStack, box, color, false);
    }

    public static void drawFilledBox(SubmitNodeStorage submitNodeStorage, Camera camera, PoseStack matrixStack, Boxf boxf, int color, boolean fade) {
        drawBox(submitNodeStorage, camera, matrixStack, boxf, color);

        submitNodeStorage.submitCustomGeometry(
                matrixStack,
                CustomRenderType.QUADS,
                (entry, buffer) -> {
                    int dynamicColor = ColorUtil.changeAlpha(color, fade ? ColorUtil.getDynamicFadeVal() : (int)Render.alpha.value);
                    Boxf box = boxf.move(camera.position().reverse());

                    buffer.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(dynamicColor);

                    buffer.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.minY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(dynamicColor);

                    buffer.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(dynamicColor);

                    buffer.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.minY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(dynamicColor);

                    buffer.addVertex(entry, box.minX, box.maxY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.maxY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.minY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.minX, box.minY, box.maxZ).setColor(dynamicColor);

                    buffer.addVertex(entry, box.maxX, box.maxY, box.minZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.maxX, box.maxY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.maxX, box.minY, box.maxZ).setColor(dynamicColor);
                    buffer.addVertex(entry, box.maxX, box.minY, box.minZ).setColor(dynamicColor);
                }
        );
    }

    public static void drawText(SubmitNodeStorage submitNodeStorage, PoseStack matrix, Camera camera, Vec3 position, String text, int color, float scale) {
        drawText(submitNodeStorage, matrix, camera, position, text, color, scale, false);
    }

    public static void drawText(SubmitNodeStorage submitNodeStorage, PoseStack matrix, Camera camera, Vec3 position, String text, int color, float scale, boolean dynamicScaling) {
        LinkedHashMap<String, Integer> texts = new LinkedHashMap<>();
        texts.put(text, color);
        drawTexts(submitNodeStorage, matrix, camera, position, texts, scale, dynamicScaling);
    }

    public static void drawTexts(SubmitNodeStorage submitNodeStorage, PoseStack matrix, Camera camera, Vec3 position, LinkedHashMap<String, Integer> texts, float scale, boolean dynamicScaling) {
        Font textRenderer = mc.font;

        Vec3 cameraPos = camera.position();

        matrix.pushPose();
        matrix.translate((float)(position.x - cameraPos.x), (float)(position.y - cameraPos.y) + 0.50F, (float)(position.z - cameraPos.z));
        matrix.mulPose(camera.rotation());
        float size = 0.025F * scale;

        if (dynamicScaling)
            size += (float)position.distanceTo(mc.player.position()) / 1000;

        matrix.scale(size, -size, size);

        int y = (int)(size / 0.025F);

        for (String text : texts.reversed().keySet()) {
            float x = (float) textRenderer.width(text) / 2.0F;
            int color = texts.get(text);
            submitNodeStorage.submitText(matrix, -x, y, Component.literal(text).getVisualOrderText(), false, Font.DisplayMode.SEE_THROUGH, 0, color, 0x50000000, 0);

            y -= mc.font.lineHeight + 2;
        }

        matrix.popPose();
    }
}
