package nicotine.screens.clickgui.element.button;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.CommonColors;
import nicotine.mod.HUDMod;
import nicotine.mod.mods.hud.HUD;
import nicotine.screens.clickgui.element.Draggable;
import nicotine.util.ColorUtil;
import nicotine.util.render.Render2D;

import static nicotine.util.Common.mc;

public class HUDButton extends GUIButton implements Draggable {
    public HUDMod hudMod;

    public HUDButton(HUDMod hudMod, int x, int y, int width, int height) {
        super("", x, y);

        this.hudMod = hudMod;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isDraggableArea(double mouseX, double mouseY) {
        return this.mouseOverElement(mouseX, mouseY);
    }

    @Override
    public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {

        final int padding = 4;

        for (int i = 0; i < hudMod.texts.size(); i++) {
           this.text = hudMod.texts.get(i);

            if (HUD.lowercase.enabled)
                text = text.toLowerCase();

            if (HUD.bold.enabled && !text.isBlank())
                text = ChatFormatting.BOLD + text;

            if (HUD.italic.enabled && !text.isBlank())
                text = ChatFormatting.ITALIC + text;

           int posX = this.x;

           if (hudMod.anchor == HUDMod.Anchor.BottomRight || hudMod.anchor == HUDMod.Anchor.TopRight)
               posX += this.width - mc.font.width(this.text);


           int posY = this.y + ((mc.font.lineHeight + padding) * i);

           Render2D.drawBorderAroundText(context, text, posX, posY,2, ColorUtil.getPulsatingColor());

           context.text(mc.font, text, posX, posY, ColorUtil.ACTIVE_FOREGROUND_COLOR);
        }
    }
}
