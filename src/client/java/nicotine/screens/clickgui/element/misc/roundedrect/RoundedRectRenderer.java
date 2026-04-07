package nicotine.screens.clickgui.element.misc.roundedrect;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.joml.Matrix4f;

public class RoundedRectRenderer extends PictureInPictureRenderer<RoundedRectRenderState> {
    public RoundedRectRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    private final float[] SIN_TABLE = {
            0,0.1736482f,
            0.3420201f,
            0.5f,
            0.6427876f,
            0.7660444f,
            0.8660254f,
            0.9396926f,
            0.9848077f,
            1,
            0.9848078f,
            0.9396927f,
            0.8660255f,
            0.7660446f,
            0.6427878f,
            0.5000002f,
            0.3420205f,
            0.1736485f,
            3.894144E-07f,
            -0.1736478f,
            -0.3420197f,
            -0.4999996f,
            -0.6427872f,
            -0.7660443f,
            -0.8660252f,
            -0.9396925f,
            -0.9848077f,
            -1,
            -0.9848078f,
            -0.9396928f,
            -0.8660257f,
            -0.7660449f,
            -0.6427881f,
            -0.5000006f,
            -0.3420208f,
            -0.1736489f,
            0,
            0.1736482f,
            0.3420201f,
            0.5f,
            0.6427876f,
            0.7660444f,
            0.8660254f,
            0.9396926f,
            0.9848077f
    };

    @Override
    public Class<RoundedRectRenderState> getRenderStateClass() {
        return RoundedRectRenderState.class;
    }

    @Override
    protected String getTextureLabel() {
        return "nicotine: rounded rect pip";
    }

    @Override
    protected void renderToTexture(RoundedRectRenderState renderState, PoseStack pose) {
        VertexConsumer vertexConsumer = this.bufferSource.getBuffer(RenderTypes.debugTriangleFan());

        Matrix4f entry = pose.last().pose();

        float cx = 0f;
        float cy = 0f;
        float dx = (float) (renderState.x1() - renderState.x0());
        float dy = (float) (renderState.y1() - renderState.y0());

        float r = 4f;

        int i = 0;
        float x0,y0,x,y;
        x = 0f;

        dx-=r+r;
        dy-=r+r;

        vertexConsumer.addVertex(entry, cx, cy, 0f).setColor(renderState.color());

        x0 = cx + (0.5f * dx);
        y0 = cy + (0.5f * dy);

        for (; i < 9; i++) {
            x= x0 + (r * SIN_TABLE[i+9]);
            y= y0 + (r * SIN_TABLE[i]);
            vertexConsumer.addVertex(entry, x, y, 0f).setColor(renderState.color());
        }

        x0 -= dx;
        for (; i < 18; i++) {
            x= x0+ (r * SIN_TABLE[i+9]);
            y= y0 + (r * SIN_TABLE[i]);
            vertexConsumer.addVertex(entry, x, y, 0f).setColor(renderState.color());
        }

        y0 -= dy;
        for (; i < 27; i++) {
            x= x0 + (r * SIN_TABLE[i+9]);
            y= y0 + (r * SIN_TABLE[i]);
            vertexConsumer.addVertex(entry, x, y, 0).setColor(renderState.color());
        }

        x0 += dx;
        for (; i < 36; i++) {
            x=x0+(r * SIN_TABLE[i+9]);
            y=y0+(r * SIN_TABLE[i]);
            vertexConsumer.addVertex(entry, x, y, 0).setColor(renderState.color());
        }

        vertexConsumer.addVertex(entry, x,cy+(0.5f*dy), 0).setColor(renderState.color());
    }

    @Override
    protected float getTranslateY(int scaledHeight, int guiScale) {
        return scaledHeight / 2f;
    }
}
