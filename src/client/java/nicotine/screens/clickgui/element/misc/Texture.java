package nicotine.screens.clickgui.element.misc;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import nicotine.screens.clickgui.element.Element;

public class Texture extends Element {
    private final Identifier textureIdentifier;

    public Texture(Identifier textureIdentifier, int x, int y) {
        super(x, y, 16, 16);

        this.textureIdentifier = textureIdentifier;
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
        context.blit(RenderPipelines.GUI_TEXTURED, textureIdentifier, this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height, this.width, this.height);
    }
}
