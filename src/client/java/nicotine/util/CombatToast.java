package nicotine.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

import static nicotine.util.Common.mc;

public class CombatToast implements Toast {

    private Toast.Visibility visibility;
    private Component component;
    private AbstractClientPlayer player;

    @Override
    public Visibility getWantedVisibility() {
        return this.visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        this.visibility = (double) time >= (double) 5000.0F * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics context, Font textRenderer, long startTime) {
        context.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.parse("toast/advancement"), 0, 0, this.width(), this.height());
        PlayerFaceRenderer.draw(context, player.getSkin().body().id(), 8, 7,  16, false, false, -1);
        context.drawString(mc.font, this.player.getName().getString(), 30, 7, ColorUtil.ACTIVE_FOREGROUND_COLOR, true);
        context.drawString(mc.font, this.component, 30, 18, CommonColors.WHITE, true);
    }

    public CombatToast(Component component, AbstractClientPlayer player) {
        this.visibility = Visibility.SHOW;
        this.component = component;
        this.player = player;
    }
}
