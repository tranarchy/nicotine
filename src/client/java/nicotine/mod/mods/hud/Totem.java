package nicotine.mod.mods.hud;

import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class Totem {

    public static HUDMod totem = new HUDMod("Totem");;

    public static void init() {
        ModManager.addMod(ModCategory.HUD, totem);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!totem.enabled)
                return true;

            totem.pos.x = (mc.getWindow().getScaledWidth() / 2) - 8;

            if (ECrystal.eCrystal.enabled)
                totem.pos.x = mc.getWindow().getScaledWidth() / 2;

            totem.pos.y = mc.getWindow().getScaledHeight() - 80;

            int totemCount = 0;

            for (int i = 0; i <= 45; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    totemCount++;
                }
            }

            event.drawContext.drawItem(Items.TOTEM_OF_UNDYING.getDefaultStack(), totem.pos.x, totem.pos.y);
            event.drawContext.drawStackOverlay(mc.textRenderer, Items.TOTEM_OF_UNDYING.getDefaultStack(), totem.pos.x, totem.pos.y, String.valueOf(totemCount));

            return true;
        });
    }
}
