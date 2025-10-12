package nicotine.mod.mods.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.GUI;
import org.joml.Vector2i;

import static nicotine.util.Common.mc;

public class ECrystal {

    private static HUDMod eCrystal = new HUDMod("ECrystal");;

    public static void drawHUD(DrawContext context) {
        if (!eCrystal.enabled)
            return;

        Vector2i pos = GUI.relativePosToAbsPos(eCrystal.pos, eCrystal.size);

        int eCrystalCount = 0;

        for (int i = 0; i <= 45; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.END_CRYSTAL) {
                eCrystalCount += stack.getCount();
            }
        }

        context.drawItem(Items.END_CRYSTAL.getDefaultStack(), pos.x, pos.y);
        context.drawStackOverlay(mc.textRenderer, Items.END_CRYSTAL.getDefaultStack(), pos.x, pos.y, String.valueOf(eCrystalCount));

        if (mc.currentScreen instanceof HUDEditorScreen) {
            int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

            GUI.drawBorder(context, pos.x, pos.y, eCrystal.size.x, eCrystal.size.y, dynamicColor);
        }
    }

    public static void init() {
        ModManager.addMod(ModCategory.HUD, eCrystal);

        final int x = (mc.getWindow().getScaledWidth() / 2) - 18;
        final int y = mc.getWindow().getScaledHeight() - 68;

        eCrystal.size = new Vector2i(16, 16);
        eCrystal.pos = GUI.absPosToRelativePos(new Vector2i(x, y), eCrystal.size);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!eCrystal.enabled || mc.currentScreen instanceof HUDEditorScreen)
                return true;

            drawHUD(event.drawContext);

            return true;
        });
    }
}
