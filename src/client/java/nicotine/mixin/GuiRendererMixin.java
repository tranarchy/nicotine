package nicotine.mixin;

import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import nicotine.screens.clickgui.element.misc.roundedrect.RoundedRectRenderState;
import nicotine.screens.clickgui.element.misc.roundedrect.RoundedRectRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public class GuiRendererMixin {

    @Unique
    private RoundedRectRenderer roundedRectRenderer = null;

    @Shadow
    @Final
    private MultiBufferSource.BufferSource bufferSource;

    @Shadow
    @Final
    private GuiRenderState renderState;


    @Inject(method = "preparePictureInPictureState", at = @At("HEAD"))
    private <T extends PictureInPictureRenderState> void preparePictureInPictureState(final T picturesInPictureState, final int guiScale, CallbackInfo info) {
        if (picturesInPictureState instanceof RoundedRectRenderState roundedRectRenderState) {
            if (roundedRectRenderer == null)
                roundedRectRenderer = new RoundedRectRenderer(this.bufferSource);

            roundedRectRenderer.prepare(roundedRectRenderState, this.renderState, guiScale);
        }
    }
}
