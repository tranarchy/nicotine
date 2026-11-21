package nicotine.util.toast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.screens.clickgui.element.misc.Texture;
import nicotine.screens.clickgui.element.window.Window;
import nicotine.util.ColorUtil;

import static nicotine.util.Common.*;

public class ToastWindow extends Window {
    public String msg;
    public AbstractClientPlayer player;
    public int expTime;
    public int expTimeOriginal;

    public ToastWindow(AbstractClientPlayer player, String msg, int expTime) {
        super(mc.getWindow().getGuiScaledWidth(), 0, mc.font.width(msg) + (player != null ? 30 : 10), mc.font.lineHeight * 3);
        this.player = player;
        this.msg = msg;
        this.expTime = expTime;
        this.expTimeOriginal = expTime;
    }

    @Override
    public void addDrawables() {
        super.addDrawables();

        int elementPosX = this.x + 5;
        int elementPosY = this.y + mc.font.lineHeight;

        if (player != null) {
            PlayerSkinRenderCache skinCache = mc.playerSkinRenderCache();
            PlayerSkinRenderCache.RenderInfo renderInfo = skinCache.getOrDefault(player.getProfile());
            Texture playerSkin = new Texture(renderInfo.playerSkin().body().id(), elementPosX, this.y + 6, 16, 16, 8.0F, 8.0F, 8, 8, 64, 64);
            this.add(playerSkin);

            elementPosX += 20;
        }

        Text msgText = new Text(this.msg, elementPosX, elementPosY);
        this.add(msgText);

        float progress = (float)expTime / (float)expTimeOriginal;
        if (progress > 0) {
            int progressLineWidth = (int)((float)this.width * progress);
            HLine progressLine = new HLine(this.x + 1, this.y + 1, progressLineWidth,  ColorUtil.ACTIVE_FOREGROUND_COLOR);
            this.add(progressLine);
        }
    }
}
