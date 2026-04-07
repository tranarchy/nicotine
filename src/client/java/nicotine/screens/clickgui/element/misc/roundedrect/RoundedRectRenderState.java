package nicotine.screens.clickgui.element.misc.roundedrect;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jspecify.annotations.Nullable;

public record RoundedRectRenderState(
        int x0,
        int x1,
        int y0,
        int y1,
        float scale,
        int color,
        @Nullable ScreenRectangle scissorArea,
        @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {
    public RoundedRectRenderState(final int x0, final int y0, final int x1, final int y1, final int color, final @Nullable ScreenRectangle scissorArea) {
        this(x0, x1, y0, y1, 1.0f, color, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }
}