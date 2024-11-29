package nicotine.mod.mods.hud;

import net.minecraft.item.ItemStack;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class ArmorHUD {
    public static void init() {
        Mod armorHUD = new Mod("Armor");
        ModManager.addMod(ModCategory.HUD, armorHUD);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!armorHUD.enabled)
                return true;

            int x = (mc.getWindow().getScaledWidth() / 2) + 95;
            final int y = mc.getWindow().getScaledHeight() - 59;
            for (ItemStack armor : mc.player.getAllArmorItems()) {
                x -= 19;
                event.drawContext.drawItem(armor, x, y);
                event.drawContext.drawStackOverlay(mc.textRenderer, armor, x, y);
            }

            return true;
        });
    }
}
