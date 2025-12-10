package nicotine.mod.mods.hud;

import net.minecraft.world.item.Items;
import nicotine.events.GuiRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class Totem {

    public static HUDMod totem = new HUDMod("Totem");;

    public static void init() {
        ModManager.addMod(ModCategory.HUD, totem);

        EventBus.register(GuiRenderAfterEvent.class, event -> {
            if (!totem.enabled)
                return true;

            totem.pos.x = (mc.getWindow().getGuiScaledWidth() / 2) - 8;

            if (ECrystal.eCrystal.enabled)
                totem.pos.x = mc.getWindow().getGuiScaledWidth() / 2;

            totem.pos.y = mc.getWindow().getGuiScaledHeight() - 80;

            int totemCount = 0;

            for (int i = 0; i <= 45; i++) {
                if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    totemCount++;
                }
            }

            event.drawContext.renderItem(Items.TOTEM_OF_UNDYING.getDefaultInstance(), totem.pos.x, totem.pos.y);
            event.drawContext.renderItemDecorations(mc.font, Items.TOTEM_OF_UNDYING.getDefaultInstance(), totem.pos.x, totem.pos.y, String.valueOf(totemCount));

            return true;
        });
    }
}
