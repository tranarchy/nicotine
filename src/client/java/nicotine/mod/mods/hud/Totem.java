package nicotine.mod.mods.hud;

import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class Totem {
    public static void init() {
        Mod totem = new Mod("Totem");
        ModManager.addMod(ModCategory.HUD, totem);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!totem.enabled)
                return true;

            final int x = (mc.getWindow().getScaledWidth() / 2) - 9;
            final int y = mc.getWindow().getScaledHeight() - 59;

            int totemCount = 0;

            for (int i = 0; i <= 45; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    totemCount++;
                }
            }

            event.drawContext.drawItem(Items.TOTEM_OF_UNDYING.getDefaultStack(), x, y);
            event.drawContext.drawStackOverlay(mc.textRenderer, Items.TOTEM_OF_UNDYING.getDefaultStack(), x, y, String.valueOf(totemCount));

            return true;
        });
    }
}
