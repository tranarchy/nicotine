package nicotine.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.mod.HUDMod;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.ColorUtil;
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

    private boolean mouseOver(int posX, int posY, int width, int height, double mouseX, double mouseY) {
        return (posX <= mouseX && mouseX <= posX + width && posY <= mouseY && mouseY <= posY + height);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragOffset.x != -1 && dragOffset.y != -1) {
            dragOffset.x = -1;
            dragOffset.y = -1;

           for (AnchorArea anchorArea : anchorAreas) {
               if (mouseOver(anchorArea.pos.x, anchorArea.pos.y, anchorArea.size.x, anchorArea.size.y, mouseX, mouseY)) {
                   selectedHudMod.anchor = anchorArea.anchor;
                   break;
               }
           }

           selectedHudMod = null;
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {

        if (dragOffset.x != -1 && dragOffset.y != -1) {

            final int windowWidth = mc.getWindow().getScaledWidth();
            final int windowHeight = mc.getWindow().getScaledHeight();

            Vector2i size = selectedHudMod.size;

            int posX = (int) mouseX + dragOffset.x;
            int posY = (int) mouseY + dragOffset.y;

            if (posX < 0) {
                posX = 0;
            } else if (posX + size.x >= windowWidth) {
                posX = windowWidth - size.x - 1;
            }

            if (posY < 0) {
                posY = 0;
            } else if (posY + size.y  >= windowHeight) {
                posY = windowHeight - size.y - 1;
            }

            selectedHudMod.pos = RenderGUI.absPosToRelativePos(new Vector2i(posX, posY), size);
            return true;
        }

        for (Mod mod : ModManager.modules.get(ModCategory.HUD)) {
            if (!(mod instanceof HUDMod hudMod) || !hudMod.enabled)
                continue;

            Vector2i size = hudMod.size;
            Vector2i pos = RenderGUI.relativePosToAbsPos(hudMod.pos, size);

            if (mouseOver(pos.x, pos.y, size.x, size.y, mouseX, mouseY)) {
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
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

        anchorAreas = Arrays.asList(
                new AnchorArea(new Vector2i(0, 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopLeft),
                new AnchorArea(new Vector2i(windowWidth - windowAreaX - 1, 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopRight),
                new AnchorArea(new Vector2i(0, windowHeight - windowAreaY - 1), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.BottomLeft),
                new AnchorArea(new Vector2i(windowWidth - windowAreaX - 1, windowHeight - windowAreaY - 1), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.BottomRight),
                new AnchorArea(new Vector2i(windowWidth / 2 - (windowAreaX / 2), 0), new Vector2i(windowAreaX, windowAreaY), HUDMod.Anchor.TopCenter)
        );

        for (AnchorArea anchorArea : anchorAreas) {
            switch (anchorArea.anchor) {
                case TopLeft:
                case BottomLeft:
                    context.drawVerticalLine(anchorArea.pos.x, anchorArea.pos.y, anchorArea.pos.y + anchorArea.size.y, ColorUtil.getRainbowColor());
                    break;
                case TopRight:
                case BottomRight:
                    context.drawVerticalLine(anchorArea.pos.x + anchorArea.size.x - 1, anchorArea.pos.y, anchorArea.pos.y + anchorArea.size.y, ColorUtil.getRainbowColor());
                    break;
                case TopCenter:
                    context.drawHorizontalLine(anchorArea.pos.x, anchorArea.pos.x + anchorArea.size.x, anchorArea.pos.y, ColorUtil.getRainbowColor());
                    break;
            }
        }

        if (selectedHudMod != null && showPosition) {
            Vector2f pos = selectedHudMod.pos;
            Vector2i absPos =  RenderGUI.relativePosToAbsPos(selectedHudMod.pos, selectedHudMod.size);

            Text posText = Text.of(String.format("X: %d Y: %d (%s%d%% %d%%%s)", absPos.x, absPos.y, Formatting.AQUA, (int)(pos.x * 100), (int)(pos.y * 100), Formatting.RESET));

            context.drawTooltip(posText, mouseX, mouseY - 10);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}
}
