package nicotine.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

import static nicotine.util.Common.mc;

public class CombatToast implements Toast {

    private Toast.Visibility visibility;
    private Text text;
    private AbstractClientPlayerEntity player;

    @Override
    public Visibility getVisibility() {
        return this.visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        this.visibility = (double) time >= (double) 5000.0F * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long startTime) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("toast/advancement"), 0, 0, this.getWidth(), this.getHeight());
        PlayerSkinDrawer.draw(context, player.getSkin().body().id(), 8, 7,  16, false, false, -1);
        context.drawText(mc.textRenderer, this.player.getName().getString(), 30, 7, ColorUtil.ACTIVE_FOREGROUND_COLOR, true);
        context.drawText(mc.textRenderer, this.text, 30, 18, Colors.WHITE, true);
    }

    public CombatToast(Text text, AbstractClientPlayerEntity player) {
        this.visibility = Visibility.SHOW;
        this.text = text;
        this.player = player;
    }
}
