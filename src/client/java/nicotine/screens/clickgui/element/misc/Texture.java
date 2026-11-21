package nicotine.screens.clickgui.element.misc;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import nicotine.screens.clickgui.element.Element;

public class Texture extends Element {
    private final Identifier textureIdentifier;
    public float u;
    public float v;
    public int srcWidth;
    public int srcHeight;
    public int textureWidth;
    public int textureHeight;


    public Texture(Identifier textureIdentifier, int x, int y) {
        super(x, y, 16, 16);

        this.textureIdentifier = textureIdentifier;
        this.u = 0.0F;
        this.v = 0.0F;
        this.srcWidth = this.width;
        this.srcHeight = this.height;
        this.textureWidth = this.width;
        this.textureHeight = this.height;
    }

    public Texture(Identifier textureIdentifier, int x, int y, int width, int height, float u, float v, int srcWidth, int srcHeight, int textureWidth, int textureHeight) {
        super(x, y, width, height);

        this.textureIdentifier = textureIdentifier;
        this.u = u;
        this.v = v;
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        context.blit(RenderPipelines.GUI_TEXTURED, textureIdentifier, this.x, this.y, u, v, this.width, this.height, this.srcWidth, this.srcHeight, this.textureWidth, this.textureHeight);
    }
}
