package nicotine.mod.mods.hud;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.RenderGUI;
import org.joml.Vector2i;

import static nicotine.util.Common.mc;

public class ECrystal {

    public static void init() {
        HUDMod eCrystal = new HUDMod("ECrystal");
        ModManager.addMod(ModCategory.HUD, eCrystal);

        final int x = (mc.getWindow().getScaledWidth() / 2) - 18;
        final int y = mc.getWindow().getScaledHeight() - 68;

        eCrystal.size = new Vector2i(16, 16);
        eCrystal.pos = RenderGUI.absPosToRelativePos(new Vector2i(x, y), eCrystal.size);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!eCrystal.enabled)
                return true;

            Vector2i pos = RenderGUI.relativePosToAbsPos(eCrystal.pos, eCrystal.size);

            int eCrystalCount = 0;

            for (int i = 0; i <= 45; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.getItem() == Items.END_CRYSTAL) {
                    eCrystalCount += stack.getCount();
                }
            }

            event.drawContext.drawItem(Items.END_CRYSTAL.getDefaultStack(), pos.x, pos.y);
            event.drawContext.drawStackOverlay(mc.textRenderer, Items.END_CRYSTAL.getDefaultStack(), pos.x, pos.y, String.valueOf(eCrystalCount));

            if (mc.currentScreen instanceof HUDEditorScreen) {
                int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

                RenderGUI.drawBorder(event.drawContext, pos.x, pos.y, eCrystal.size.x, eCrystal.size.y, dynamicColor);
            }

            return true;
        });
    }
}
