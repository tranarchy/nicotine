package nicotine.screens.clickgui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import nicotine.events.GuiRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.screens.clickgui.element.window.Window;
import nicotine.screens.clickgui.element.button.HUDButton;
import nicotine.util.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class HUDScreen extends BaseScreen {

    private static class AnchorArea {
        public Window window;
        public HUDMod.Anchor anchor;

        public AnchorArea(Window window, HUDMod.Anchor anchor) {
            this.window = window;
            this.anchor = anchor;
        }
    }

    private final List<AnchorArea> anchorAreas = new ArrayList<>();
    private final HashMap<HUDMod.Anchor, HUDButton> hudButtons = new HashMap<>();
    private HUDMod.Anchor prevAnchor = HUDMod.Anchor.None;

    public HUDScreen() {
        super("nicotine HUD", new Window(0, 0, 0, 0));
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        if (dragOffset.x != -1 && dragOffset.y != -1) {
            HUDButton hudButton = ((HUDButton)this.elementForDragging);

            for (AnchorArea anchorArea : anchorAreas) {
                if (anchorArea.window.mouseOverElement(mouseX, mouseY)) {
                    hudButton.hudMod.anchor = anchorArea.anchor;
                    break;
                }
            }

            if (hudButton.hudMod.anchor == HUDMod.Anchor.None) {
                hudButton.hudMod.anchor = prevAnchor;
            }

            prevAnchor = HUDMod.Anchor.None;
        }

        super.mouseReleased(mouseButtonEvent);

        return true;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double offsetX, double offsetY) {
        super.mouseDragged(mouseButtonEvent, offsetX, offsetY);

        if (this.elementForDragging != null) {
            HUDButton hudButton = ((HUDButton)this.elementForDragging);

            if (prevAnchor == HUDMod.Anchor.None)
                prevAnchor = hudButton.hudMod.anchor;

            hudButton.hudMod.anchor = HUDMod.Anchor.None;
        }

        return true;
    }

    @Override
    protected void addDrawables() {
        final int windowWidth = mc.getWindow().getGuiScaledWidth();
        final int windowHeight = mc.getWindow().getGuiScaledHeight();

        int windowAreaX = windowWidth / 4;
        int windowAreaY = windowHeight / 4;

        if (mc.screen instanceof HUDScreen) {
            anchorAreas.add(new AnchorArea(new Window(0, 0, windowAreaX, windowAreaY), HUDMod.Anchor.TopLeft));
            anchorAreas.add(new AnchorArea(new Window(windowWidth - windowAreaX - 1, 0, windowAreaX, windowAreaY), HUDMod.Anchor.TopRight));
            anchorAreas.add(new AnchorArea(new Window(0, windowHeight - windowAreaY - 1, windowAreaX, windowAreaY), HUDMod.Anchor.BottomLeft));
            anchorAreas.add(new AnchorArea(new Window(windowWidth - windowAreaX - 1, windowHeight - windowAreaY - 1, windowAreaX, windowAreaY), HUDMod.Anchor.BottomRight));
        }

        for (AnchorArea anchorArea : anchorAreas) {
            this.window.add(anchorArea.window);
        }

        for (Mod mod : ModManager.modules.get(ModCategory.HUD)) {
            if (mod instanceof HUDMod hudMod && hudMod.enabled && !hudMod.texts.isEmpty()) {
                addHUDButton(hudMod);
            }
        }
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        this.hudButtons.clear();
        this.anchorAreas.clear();
        super.extractRenderState(context, mouseX, mouseY, delta);
    }

    public void addHUDButton(HUDMod hudMod) {
        final int windowWidth = mc.getWindow().getGuiScaledWidth();
        final int windowHeight = mc.getWindow().getGuiScaledHeight();

        HUDButton lastButton = hudButtons.getOrDefault(hudMod.anchor, new HUDButton(null, 0, 0, 0, 0));

        if ((hudMod.anchor == HUDMod.Anchor.BottomLeft || hudMod.anchor == HUDMod.Anchor.BottomRight) && lastButton.hudMod == null)
            lastButton.y = windowHeight;

        int posX = 0;
        int posY = 0;

        final int padding = 4;

        int width = mc.font.width(hudMod.texts.stream().max(Comparator.comparingInt(mc.font::width)).get());
        int height = mc.font.lineHeight * hudMod.texts.size();

        height += padding * (hudMod.texts.size() - 1);

        switch (hudMod.anchor) {
            case TopLeft:
                posX = padding;
                posY = padding + lastButton.y + lastButton.height;
                break;
            case TopRight:
                posX = windowWidth - padding - width;
                posY = padding + lastButton.y + lastButton.height;
                break;
            case BottomLeft:
                posX = padding;
                posY = windowHeight - (windowHeight - lastButton.y) - padding - height;
                break;
            case BottomRight:
                posX = windowWidth - padding - width;
                posY = windowHeight - (windowHeight - lastButton.y) - padding - height;
                break;
            case None:
                posX = elementForDragging != null ? elementForDragging.x : 0;
                posY = elementForDragging != null ? elementForDragging.y : 0;
            }

            HUDButton hudButton = new HUDButton(hudMod, posX, posY, width, height);

            this.window.add(hudButton);

        hudButtons.put(hudMod.anchor, hudButton);
    }

    public void init() {
        EventBus.register(GuiRenderAfterEvent.class, event -> {
            if (mc.screen instanceof HUDScreen)
               return true;

            extractRenderState(event.drawContext, 0, 0, 0);

            return true;
        });
    }
}
