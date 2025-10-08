package nicotine.screens;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.mod.HUDMod;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.mods.hud.*;
import nicotine.util.ColorUtil;
import nicotine.util.GUIUtil;
import nicotine.util.Settings;
import nicotine.util.render.RenderGUI;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class HUDEditorScreen extends Screen {

    private static final Vector2i dragOffset = new Vector2i(-1, -1);
    private static HUDMod selectedHudMod = null;

    private final boolean showPosition;

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

    public HUDEditorScreen(boolean showPosition) {
        super(Text.literal("nicotine HUD Editor"));
        this.showPosition = showPosition;
    }



    @Override
    public boolean mouseReleased(Click click) {
        double mouseX = click.x();
        double mouseY = click.y();

        if (dragOffset.x != -1 && dragOffset.y != -1) {
            dragOffset.x = -1;
            dragOffset.y = -1;

           for (AnchorArea anchorArea : anchorAreas) {
               if (GUIUtil.mouseOver(anchorArea.pos.x, anchorArea.pos.y, anchorArea.size.x, anchorArea.size.y, mouseX, mouseY)) {
                   selectedHudMod.anchor = anchorArea.anchor;
                   break;
               }
           }

           selectedHudMod = null;
        }

        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        double mouseX = click.x();
        double mouseY = click.y();

        if (dragOffset.x != -1 && dragOffset.y != -1) {

            Vector2i size = selectedHudMod.size;
            Vector2i dragPos = GUIUtil.mouseDragInBounds(mouseX, mouseY, dragOffset, size);

            selectedHudMod.pos = RenderGUI.absPosToRelativePos(new Vector2i(dragPos.x, dragPos.y), size);
            return true;
        }

        for (Mod mod : ModManager.modules.get(ModCategory.HUD)) {
            if (!(mod instanceof HUDMod hudMod) || !hudMod.enabled)
                continue;

            Vector2i size = hudMod.size;
            Vector2i pos = RenderGUI.relativePosToAbsPos(hudMod.pos, size);

            if (GUIUtil.mouseOver(pos.x, pos.y, size.x, size.y, mouseX, mouseY)) {
                selectedHudMod = hudMod;
                selectedHudMod.anchor = HUDMod.Anchor.None;

                dragOffset.x = pos.x - (int) mouseX;
                dragOffset.y = pos.y - (int) mouseY;
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == InputUtil.GLFW_KEY_ESCAPE) {
            Settings.save();
            this.close();
        }

        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        final int windowWidth = mc.getWindow().getScaledWidth();
        final int windowHeight = mc.getWindow().getScaledHeight();

        int windowAreaX = windowWidth / 4;
        int windowAreaY = windowHeight / 4;

        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        anchorAreas = Arrays.asList(
                new AnchorArea(new Vector2i(0, 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopLeft),
                new AnchorArea(new Vector2i(windowWidth - windowAreaX - 1, 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopRight),
                new AnchorArea(new Vector2i(0, windowHeight - windowAreaY - 1), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.BottomLeft),
                new AnchorArea(new Vector2i(windowWidth - windowAreaX - 1, windowHeight - windowAreaY - 1), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.BottomRight),
                new AnchorArea(new Vector2i(windowWidth / 2 - (windowAreaX / 2), 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopCenter)
        );

        for (AnchorArea anchorArea : anchorAreas) {
            context.fill(anchorArea.pos.x, anchorArea.pos.y, anchorArea.pos.x + anchorArea.size.x, anchorArea.pos.y + anchorArea.size.y, ColorUtil.BACKGROUND_COLOR);
            RenderGUI.drawBorder(context, anchorArea.pos.x, anchorArea.pos.y, anchorArea.size.x, anchorArea.size.y, dynamicColor);
        }

        if (selectedHudMod != null && showPosition) {
            Vector2f pos = selectedHudMod.pos;
            Vector2i absPos =  RenderGUI.relativePosToAbsPos(selectedHudMod.pos, selectedHudMod.size);

            Text posText = Text.of(String.format("X: %d Y: %d (%s%d%% %d%%%s)", absPos.x, absPos.y, Formatting.AQUA, (int)(pos.x * 100), (int)(pos.y * 100), Formatting.RESET));

            context.drawTooltip(posText, mouseX, mouseY - 10);

            context.drawVerticalLine(absPos.x + selectedHudMod.size.x / 2, 0, absPos.y, ColorUtil.ACTIVE_FOREGROUND_COLOR);
            context.drawVerticalLine(absPos.x + selectedHudMod.size.x / 2, absPos.y + selectedHudMod.size.y, windowHeight, ColorUtil.ACTIVE_FOREGROUND_COLOR);

            context.drawHorizontalLine(0, absPos.x, absPos.y + selectedHudMod.size.y / 2, ColorUtil.ACTIVE_FOREGROUND_COLOR);
            context.drawHorizontalLine(absPos.x + selectedHudMod.size.x, windowWidth, absPos.y + selectedHudMod.size.y / 2, ColorUtil.ACTIVE_FOREGROUND_COLOR);
        }

        HUD.drawHUD(context);
        Combat.drawHUD(context);
        Totem.drawHUD(context);
        ECrystal.drawHUD(context);
        Armor.drawHUD(context);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}
}
