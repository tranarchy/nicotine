package nicotine.screens;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import nicotine.mod.HUDMod;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.mods.hud.*;
import nicotine.util.ColorUtil;
import nicotine.util.Settings;
import nicotine.util.render.GUI;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class HUDEditorScreen extends Screen {

    private static final Vector2i dragOffset = new Vector2i(-1, -1);
    private static HUDMod.Anchor prevAnchor;
    private static HUDMod selectedHudMod = null;

    private static class AnchorArea {
        public Vector2i pos;
        public Vector2i size;
        public HUDMod.Anchor anchor;

        public AnchorArea(Vector2i pos, Vector2i size, HUDMod.Anchor anchor) {
            this.pos = pos;
            this.size = size;
            this.anchor = anchor;
        }
    }

    private static List<AnchorArea> anchorAreas = new ArrayList<>();

    public HUDEditorScreen() {
        super(Component.literal("nicotine HUD Editor"));
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        if (dragOffset.x != -1 && dragOffset.y != -1) {
            dragOffset.x = -1;
            dragOffset.y = -1;

           for (AnchorArea anchorArea : anchorAreas) {
               if (GUI.mouseOver(anchorArea.pos.x, anchorArea.pos.y, anchorArea.size.x, anchorArea.size.y, mouseX, mouseY)) {
                   selectedHudMod.anchor = anchorArea.anchor;
                   break;
               }
           }

           if (selectedHudMod.anchor == HUDMod.Anchor.None)
               selectedHudMod.anchor = prevAnchor;

           selectedHudMod = null;
        }

        return true;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double offsetX, double offsetY) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        if (dragOffset.x != -1 && dragOffset.y != -1) {

            Vector2i size = selectedHudMod.size;
            Vector2i dragPos = GUI.mouseDragInBounds(mouseX, mouseY, dragOffset, size);

            selectedHudMod.pos = new Vector2i(dragPos.x, dragPos.y);
            return true;
        }

        for (Mod mod : ModManager.modules.get(ModCategory.HUD)) {
            if (!(mod instanceof HUDMod hudMod) || !hudMod.enabled)
                continue;

            Vector2i size = hudMod.size;

            if (GUI.mouseOver(hudMod.pos.x, hudMod.pos.y, size.x, size.y, mouseX, mouseY)) {
                selectedHudMod = hudMod;
                prevAnchor = selectedHudMod.anchor;
                selectedHudMod.anchor = HUDMod.Anchor.None;

                dragOffset.x = hudMod.pos.x - (int) mouseX;
                dragOffset.y = hudMod.pos.y - (int) mouseY;
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE) {
            Settings.save();
            this.onClose();
        }

        return true;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        final int windowWidth = mc.getWindow().getGuiScaledWidth();
        final int windowHeight = mc.getWindow().getGuiScaledHeight();

        int windowAreaX = windowWidth / 4;
        int windowAreaY = windowHeight / 4;

        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        anchorAreas = Arrays.asList(
                new AnchorArea(new Vector2i(0, 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopLeft),
                new AnchorArea(new Vector2i(windowWidth - windowAreaX - 1, 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopRight),
                new AnchorArea(new Vector2i(0, windowHeight - windowAreaY - 1), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.BottomLeft),
                new AnchorArea(new Vector2i(windowWidth - windowAreaX - 1, windowHeight - windowAreaY - 1), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.BottomRight)
        );

        for (AnchorArea anchorArea : anchorAreas) {
            context.fill(anchorArea.pos.x, anchorArea.pos.y, anchorArea.pos.x + anchorArea.size.x, anchorArea.pos.y + anchorArea.size.y, ColorUtil.BACKGROUND_COLOR);
            GUI.drawBorder(context, anchorArea.pos.x, anchorArea.pos.y, anchorArea.size.x, anchorArea.size.y, dynamicColor);
        }

        HUD.drawHUD(context);

       super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {}
}
