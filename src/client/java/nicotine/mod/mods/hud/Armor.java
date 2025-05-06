package nicotine.mod.mods.hud;

import net.minecraft.item.ItemStack;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.Player;

import static nicotine.util.Common.mc;

public class Armor {
    public static void init() {
        Mod armor = new Mod("Armor");
        ModManager.addMod(ModCategory.HUD, armor);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!armor.enabled)
                return true;

            int x = (mc.getWindow().getScaledWidth() / 2) + 95;
            final int y = mc.getWindow().getScaledHeight() - 59;
            for (ItemStack armorItem : Player.getArmorItems()) {
                x -= 19;
                event.drawContext.drawItem(armorItem, x, y);
                event.drawContext.drawStackOverlay(mc.textRenderer, armorItem, x, y);
           }

            return true;
        });
    }
}
