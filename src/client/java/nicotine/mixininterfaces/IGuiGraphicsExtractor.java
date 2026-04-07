package nicotine.mixininterfaces;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;

public interface IGuiGraphicsExtractor {
    GuiRenderState getGuiRenderState();
    GuiGraphicsExtractor.ScissorStack getScissorStack();
}
