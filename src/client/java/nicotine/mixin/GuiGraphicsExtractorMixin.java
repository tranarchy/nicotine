package nicotine.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import nicotine.mixininterfaces.IGuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin implements IGuiGraphicsExtractor {

    @Final
    @Shadow
    private GuiRenderState guiRenderState;

    @Final
    @Shadow
    private GuiGraphicsExtractor.ScissorStack scissorStack;

    @Override
    public GuiRenderState getGuiRenderState() {
        return guiRenderState;
    }

    @Override
    public GuiGraphicsExtractor.ScissorStack getScissorStack() {
        return scissorStack;
    }
}
