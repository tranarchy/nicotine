package nicotine.util.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mixininterfaces.IItemRenderState;
import nicotine.mixininterfaces.ILayerRenderState;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.math.Boxf;

import static nicotine.util.Common.mc;

public class Render {

    private static BufferBuilder bufferBuilderLines;
    private static BufferBuilder bufferBuilderQuads;
    private static BufferBuilder bufferBuilderItems;

    public static void init() {
        Tessellator tessellatorLines = new Tessellator();
        Tessellator tessellatorQuads = new Tessellator();
        Tessellator tessellatorItems = new Tessellator();
        bufferBuilderLines = tessellatorLines.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);
        bufferBuilderQuads = tessellatorQuads.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilderItems = tessellatorItems.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);

        EventBus.register(RenderEvent.class, event -> {
            Vec3d view = event.camera.getPos();
            event.matrixStack.push();
            event.matrixStack.translate(-view.x, -view.y, -view.z);

            BuiltBuffer builtBufferLines = bufferBuilderLines.endNullable();
            BuiltBuffer builtBufferQuads = bufferBuilderQuads.endNullable();
            BuiltBuffer builtBufferItems = bufferBuilderItems.endNullable();

            if (builtBufferLines != null)
                CustomRenderLayer.LINES.draw(builtBufferLines);

            if (builtBufferQuads != null)
                CustomRenderLayer.QUADS.draw(builtBufferQuads);

            if (builtBufferItems != null)
                CustomRenderLayer.ITEM_ENTITY_TRANSLUCENT_CULL.apply(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).draw(builtBufferItems);

            tessellatorLines.clear();
            tessellatorQuads.clear();
            tessellatorItems.clear();

            bufferBuilderLines = tessellatorLines.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);
            bufferBuilderQuads = tessellatorQuads.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferBuilderItems = tessellatorItems.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);

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

    private static int getTint(int[] tints, int index) {
        return index >= 0 && index < tints.length ? tints[index] : -1;
    }

    public static void drawItem(MatrixStack matrixStack, Camera camera, Vec3d position, IItemRenderState itemRenderState, float scale) {
        Vec3d cameraPos = camera.getPos();

        matrixStack.push();
        matrixStack.translate((float)(position.x - cameraPos.x), (float)(position.y - cameraPos.y) + 0.50F, (float)(position.z - cameraPos.z));
        matrixStack.multiply(camera.getRotation());
        float size = (0.6F * scale) + (float)position.distanceTo(mc.player.getEntityPos()) / 1000;
        matrixStack.scale(size, size, size);

        MatrixStack.Entry entry = matrixStack.peek();

        for (int i = 0; i < itemRenderState.getLayerCount(); i++) {

            ItemRenderState.LayerRenderState layerRenderState =  itemRenderState.getLayers()[i];
            ILayerRenderState iLayerRenderState = (ILayerRenderState) layerRenderState;

            for (BakedQuad bakedQuad :layerRenderState.getQuads()) {
                float alpha;
                float red;
                float green;
                float blue;
                if (bakedQuad.hasTint()) {
                    int tint = getTint(iLayerRenderState.getTints(), bakedQuad.tintIndex());
                    alpha = (float) ColorHelper.getAlpha(tint) / 255.0F;
                    red = (float) ColorHelper.getRed(tint) / 255.0F;
                    green = (float) ColorHelper.getGreen(tint) / 255.0F;
                    blue = (float) ColorHelper.getBlue(tint) / 255.0F;
                } else {
                    alpha = 1.0F;
                    red = 1.0F;
                    green = 1.0F;
                    blue = 1.0F;
                }

                bufferBuilderItems.quad(entry, bakedQuad, red, green, blue, alpha, 15728640, OverlayTexture.DEFAULT_UV);
            }
        }

        matrixStack.pop();
    }

    public static void drawText(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, Camera camera, Vec3d position, String text, int color, float scale) {
        TextRenderer textRenderer = mc.textRenderer;

        Vec3d cameraPos = camera.getPos();

        matrix.push();
        matrix.translate((float)(position.x - cameraPos.x), (float)(position.y - cameraPos.y) + 0.50F, (float)(position.z - cameraPos.z));
        matrix.multiply(camera.getRotation());
        float size = (0.025F * scale) + (float)position.distanceTo(mc.player.getEntityPos()) / 1000;
        matrix.scale(size, -size, size);
        float x = (float) textRenderer.getWidth(text) / 2.0F;

        textRenderer.draw(text, -x, 0.0F, color, true, matrix.peek().getPositionMatrix(), vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH,  0x50000000, 0);
        matrix.pop();
    }
}
